import ast
import os
import warnings

import joblib
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

warnings.filterwarnings("ignore")

SAVE_DIRECTORY = "../saved_models"
PROCESSED_FILE = os.path.join(SAVE_DIRECTORY, "processed_movies.csv")

if not os.path.exists(SAVE_DIRECTORY):
    os.makedirs(SAVE_DIRECTORY)


#############################################
# Loading and Preparing Data #
#############################################

def load_movies_metadata():
    """Loads and cleans the movies_metadata.csv file."""
    movies = pd.read_csv('../data/movies_metadata.csv', low_memory=False)
    movies = movies[pd.to_numeric(movies['id'], errors='coerce').notnull()]
    movies['id'] = movies['id'].astype(int)
    movies['overview'] = movies['overview'].fillna('')
    movies.rename(columns={'title': 'movieName'}, inplace=True)
    return movies[['id', 'movieName', 'overview', 'vote_average']]


def load_links():
    """Loads links_small.csv to map MovieLens movieId to TMDB id."""
    links = pd.read_csv('../data/links_small.csv', usecols=['movieId', 'tmdbId'])
    links['tmdbId'] = pd.to_numeric(links['tmdbId'], errors='coerce').dropna().astype(int)
    return links


def load_ratings():
    """Loads ratings_small.csv and maps MovieLens movieId to TMDB id."""
    ratings = pd.read_csv('../data/ratings_small.csv')
    links = load_links()
    ratings = ratings.merge(links, on='movieId', how='left').dropna(subset=['tmdbId'])
    ratings['tmdbId'] = ratings['tmdbId'].astype(int)
    ratings.rename(columns={'tmdbId': 'id'}, inplace=True)
    return ratings


def compute_movie_views(ratings):
    """Counts the number of times each movie was rated."""
    return ratings.groupby('id')['rating'].count().reset_index().rename(columns={'rating': 'views'})


############################################
# Processing Credits Information #
############################################

def load_credits():
    """Loads credits.csv containing cast and crew information."""
    credits = pd.read_csv('../data/credits.csv', usecols=['id', 'cast', 'crew'])
    credits['id'] = pd.to_numeric(credits['id'], errors='coerce').dropna().astype(int)
    return credits


def extract_director(crew_str):
    """Extracts the director's name from the 'crew' JSON field."""
    try:
        crew = ast.literal_eval(crew_str)
        for member in crew:
            if member.get('job') == 'Director':
                return member.get('name', '')
    except:
        return ''
    return ''


def extract_actors(cast_str):
    """Extracts the top three actors from the 'cast' JSON field and ensures they are separated by commas."""
    try:
        cast = ast.literal_eval(cast_str)
        actors = [member.get('name', '') for member in cast[:3]]
        return ", ".join(actors)
    except:
        return ''


def process_movie_features(retrain=False):
    """
    Loads or processes movie data and saves the processed dataset.
    - If processed data exists, load it to avoid recomputation.
    - Otherwise, process and save it for future use.
    """
    if os.path.exists(PROCESSED_FILE) and not retrain:
        print(f"Loading preprocessed movie features from {PROCESSED_FILE}...")
        return pd.read_csv(PROCESSED_FILE)

    print("Processing movie features...")
    movies = load_movies_metadata()
    credits = load_credits()
    movies = movies.merge(credits, on='id', how='left')
    movies['director'] = movies['crew'].apply(extract_director)
    movies['actors'] = movies['cast'].apply(extract_actors)
    movies['combined_features'] = movies[['director', 'actors']].fillna('').agg(' '.join,
                                                                                axis=1)

    movies.to_csv(PROCESSED_FILE, index=False)
    print(f"Processed movie features saved to {PROCESSED_FILE}.")

    return movies


def build_tfidf_matrix(movies, feature_column, model_name, retrain=False):
    """Builds or loads a precomputed TF-IDF matrix."""

    filename = os.path.join(SAVE_DIRECTORY, f"{model_name}.pkl")

    if os.path.exists(filename) and not retrain:
        print(f"Loading saved TF-IDF matrix from {filename}...")
        return joblib.load(filename)  # Load precomputed matrix

    print(f"Computing TF-IDF matrix for {feature_column}...")

    tfidf = TfidfVectorizer(stop_words='english')

    tfidf_matrix = tfidf.fit_transform(movies[feature_column].fillna(''))

    joblib.dump(tfidf_matrix, filename)

    print(f"TF-IDF matrix saved as {filename}.")

    return tfidf_matrix


##################################
# Recommendation Methods #
##################################

def get_recommendations(movie_title, movies, tfidf_matrix, top_n=10):
    """Returns the top_n most similar movies to the given movie title, including director and actors."""

    indices = pd.Series(movies.index, index=movies['movieName']).drop_duplicates()

    matches = indices.index.str.contains(movie_title, case=False, na=False)

    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    idx = indices[matches].iloc[0]

    cosine_sim = cosine_similarity(tfidf_matrix[idx], tfidf_matrix).flatten()

    sim_scores = sorted(enumerate(cosine_sim), key=lambda x: x[1], reverse=True)[1:top_n + 1]

    recs = []
    for i, score in sim_scores:
        movie_data = movies.iloc[i]
        recs.append({
            'movieName': movie_data['movieName'],
            'director': movie_data['director'],
            'actors': str(movie_data['actors']),
            'imdbScore': float(movie_data['vote_average']),
        })

    return recs


def get_popular_movies(movies, views_df, top_n=10):
    """Returns the top_n most popular movies based on views and rating."""

    movies = movies.merge(views_df, on='id', how='left')
    movies['views'] = movies['views'].fillna(0)
    popular_movies = movies.sort_values(by=['views', 'vote_average'], ascending=False).head(
        top_n)
    return popular_movies[
        ['movieName', 'views', 'vote_average']]


###############
# Execution #
###############

def main():
    print("Initializing system...")
    movies = process_movie_features()
    views_df = compute_movie_views(load_ratings())

    # Automatically train if needed
    tfidf_matrix_credits = build_tfidf_matrix(movies, 'combined_features', 'tfidf_credits')
    tfidf_matrix_overview = build_tfidf_matrix(movies, 'overview', 'tfidf_overview')

    while True:
        print("\nChoose the recommendation type:")
        print("1 - Based on Directors and Actors")
        print("2 - Based on Movie Description")
        print("3 - Based on Popularity and Ratings")
        print("4 - Retrain Models")
        print("5 - Exit")
        choice = input("Enter the number of your choice: ").strip()

        if choice == "3":
            print("\nTop 10 Most Popular Movies:")
            popular_movies = get_popular_movies(movies, views_df)
            print(popular_movies.to_string(index=False))
            continue
        elif choice == "4":
            print("\nRetraining models...")
            movies = process_movie_features(retrain=True)
            views_df = compute_movie_views(load_ratings())
            tfidf_matrix_credits = build_tfidf_matrix(movies, 'combined_features', 'tfidf_credits', retrain=True)
            tfidf_matrix_overview = build_tfidf_matrix(movies, 'overview', 'tfidf_overview', retrain=True)
            continue
        elif choice == "5":
            print("Exiting the recommendation system. Goodbye!")
            break

        movie_title = input("\nEnter a movie title for recommendations: ").strip()
        tfidf_matrix = tfidf_matrix_credits if choice == "1" else tfidf_matrix_overview
        recs = get_recommendations(movie_title, movies, tfidf_matrix, top_n=10)

        if recs:
            print(f"\nRecommended movies for '{movie_title}':")
            for r in recs:
                print(r)


if __name__ == '__main__':
    main()
