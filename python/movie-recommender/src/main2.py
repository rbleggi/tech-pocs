import ast
import os
import warnings

import numpy as np

import joblib
import pandas as pd
from sentence_transformers import SentenceTransformer
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from surprise import Dataset, Reader, SVD
from surprise import accuracy
from surprise.model_selection import train_test_split as surprise_train_test_split

warnings.filterwarnings("ignore")

SAVE_DIRECTORY = "../saved_models"
PROCESSED_FILE = os.path.join(SAVE_DIRECTORY, "processed_movies.csv")
EMBEDDINGS_FILE = os.path.join(SAVE_DIRECTORY, "movie_embeddings.npy")
EMBEDDING_MAPPING_FILE = os.path.join(SAVE_DIRECTORY, "embedding_movie_mapping.pkl")
MATRIX_FACTORIZATION_FILE = os.path.join(SAVE_DIRECTORY, "matrix_factorization_model.pkl")
TREE_MODEL_FILE = os.path.join(SAVE_DIRECTORY, "tree_based_model.pkl")
HYBRID_MODEL_FILE = os.path.join(SAVE_DIRECTORY, "hybrid_model.pkl")

if not os.path.exists(SAVE_DIRECTORY):
    os.makedirs(SAVE_DIRECTORY)


#############################################
# Loading and Preparing Data #
#############################################

def load_movies_metadata():
    """Loads and cleans the movies_metadata.csv file."""
    movies = pd.read_csv('../data/movies_metadata.csv', low_memory=False)  # Load dataset
    movies = movies[pd.to_numeric(movies['id'], errors='coerce').notnull()]  # Remove rows with invalid IDs
    movies['id'] = movies['id'].astype(int)  # Convert ID column to integers
    movies['overview'] = movies['overview'].fillna('')  # Replace missing descriptions with an empty string
    movies.rename(columns={'title': 'movieName'}, inplace=True)  # Rename column for consistency
    return movies[['id', 'movieName', 'overview', 'vote_average']]  # Return only relevant columns


def load_links():
    """Loads links.csv to map MovieLens movieId to TMDB id."""
    links = pd.read_csv('../data/links.csv', usecols=['movieId', 'tmdbId'])  # Load only required columns
    links['tmdbId'] = pd.to_numeric(links['tmdbId'], errors='coerce').dropna().astype(int)  # Convert IDs to integers
    return links


def load_ratings():
    """Loads ratings.csv and maps MovieLens movieId to TMDB id."""
    ratings = pd.read_csv('../data/ratings.csv')  # Load ratings dataset
    links = load_links()  # Load movie ID mappings
    ratings = ratings.merge(links, on='movieId', how='left').dropna(subset=['tmdbId'])  # Merge datasets
    ratings['tmdbId'] = ratings['tmdbId'].astype(int)  # Convert IDs to integers
    ratings.rename(columns={'tmdbId': 'id'}, inplace=True)  # Rename column for consistency
    return ratings


def compute_movie_views(ratings):
    """Counts the number of times each movie was rated."""
    return ratings.groupby('id')['rating'].count().reset_index().rename(columns={'rating': 'views'})


############################################
# Processing Credits Information #
############################################

def load_credits():
    """Loads credits.csv containing cast and crew information."""
    credits = pd.read_csv('../data/credits.csv', usecols=['id', 'cast', 'crew'])  # Load only required columns
    credits['id'] = pd.to_numeric(credits['id'], errors='coerce').dropna().astype(int)  # Convert IDs to integers
    return credits


def extract_director(crew_str):
    """Extracts the director's name from the 'crew' JSON field."""
    try:
        crew = ast.literal_eval(crew_str)  # Convert string to a list of dictionaries
        for member in crew:  # Iterate through crew members
            if member.get('job') == 'Director':  # If job title is 'Director'
                return member.get('name', '')  # Return the director's name
    except:
        return ''  # Return empty string if parsing fails
    return ''


def extract_actors(cast_str):
    """Extracts the top three actors from the 'cast' JSON field and ensures they are separated by commas."""
    try:
        cast = ast.literal_eval(cast_str)  # Convert string to a list of dictionaries
        actors = [member.get('name', '') for member in cast[:3]]  # Extract names of top 3 actors
        return ", ".join(actors)  # Join names with commas
    except:
        return ''  # Return empty string if parsing fails


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
    movies = load_movies_metadata()  # Load movie metadata
    credits = load_credits()  # Load movie credits
    movies = movies.merge(credits, on='id', how='left')  # Merge both datasets
    movies['director'] = movies['crew'].apply(extract_director)  # Extract director names
    movies['actors'] = movies['cast'].apply(extract_actors)  # Extract actor names
    movies['combined_features'] = movies[['director', 'actors']].fillna('').agg(' '.join,
                                                                                axis=1)  # Create a text feature

    # Save processed features to CSV for faster future loads
    movies.to_csv(PROCESSED_FILE, index=False)
    print(f"Processed movie features saved to {PROCESSED_FILE}.")

    return movies


def build_tfidf_matrix(movies, feature_column, model_name, retrain=False):
    """
    Builds or loads a precomputed TF-IDF matrix.

    **What is a TF-IDF matrix?**
    - It converts text into numerical values so that machine learning models can process it.
    - The matrix represents the importance of each word in the given dataset.
    - Words that appear frequently in many documents (e.g., "the", "is") are given **lower weights**.
    - Words that appear **rarely** but are important in a document are given **higher weights**.

    **Parameters:**
    - movies: Pandas DataFrame containing the movie dataset.
    - feature_column: Column to be converted into TF-IDF representation ('combined_features' or 'overview').
    - model_name: Name used to save the computed TF-IDF matrix.
    - retrain: If True, recalculates the TF-IDF matrix even if a saved version exists.

    **Returns:**
    - A TF-IDF matrix representing the given text feature.
    """

    filename = os.path.join(SAVE_DIRECTORY, f"{model_name}.pkl")  # Define save path for the TF-IDF model

    # If a precomputed TF-IDF matrix exists and retrain is False, load the saved model
    if os.path.exists(filename) and not retrain:
        print(f"Loading saved TF-IDF matrix from {filename}...")
        return joblib.load(filename)  # Load precomputed matrix

    print(f"Computing TF-IDF matrix for {feature_column}...")

    # Create a TF-IDF vectorizer that ignores common English words (stop words)
    tfidf = TfidfVectorizer(stop_words='english')

    # Convert the selected feature column (e.g., 'combined_features' or 'overview') into a TF-IDF matrix
    tfidf_matrix = tfidf.fit_transform(movies[feature_column].fillna(''))

    # Save the TF-IDF matrix to avoid recomputation in the future
    joblib.dump(tfidf_matrix, filename)

    print(f"TF-IDF matrix saved as {filename}.")

    return tfidf_matrix  # Return the computed TF-IDF matrix


##################################
# Recommendation Methods #
##################################

def get_recommendations(movie_title, movies, tfidf_matrix, top_n=10):
    """Returns the top_n most similar movies to the given movie title, including director and actors."""

    # Create an index lookup where movie titles are mapped to their corresponding dataframe indices.
    indices = pd.Series(movies.index, index=movies['movieName']).drop_duplicates()

    # Search for movie titles that match the input movie_title (case-insensitive).
    matches = indices.index.str.contains(movie_title, case=False, na=False)

    # If no matches are found, notify the user and return an empty list.
    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    idx = indices[matches].iloc[0]  # Get the index of the first matched movie.

    # Compute similarity scores between the selected movie and all other movies using cosine similarity.
    cosine_sim = cosine_similarity(tfidf_matrix[idx], tfidf_matrix).flatten()

    # Get the top N most similar movies, excluding the input movie itself (hence, skipping index 0).
    sim_scores = sorted(enumerate(cosine_sim), key=lambda x: x[1], reverse=True)[1:top_n + 1]

    recs = []  # List to store recommended movies.
    for i, score in sim_scores:  # Iterate through the top matches.
        movie_data = movies.iloc[i]  # Retrieve movie details from the dataset.

        recs.append({
            'movieName': movie_data['movieName'],  # Store the movie title.
            'director': movie_data['director'],  # Store the movie director.
            'actors': str(movie_data['actors']),  # Ensure actors are a string (comma-separated)
            'imdbScore': float(movie_data['vote_average']),  # Store the movie's IMDb score.
            'similarity': score  # Add similarity score
        })

    return recs  # Return the list of recommended movies.


def get_popular_movies(movies, views_df, top_n=10):
    """Returns the top_n most popular movies based on views and rating."""

    movies = movies.merge(views_df, on='id', how='left')  # Merge movies data with views data using 'id' as key.
    movies['views'] = movies['views'].fillna(0)  # Replace missing values in 'views' with 0 to avoid NaN issues.
    popular_movies = movies.sort_values(by=['views', 'vote_average'], ascending=False).head(
        top_n)  # Sort movies by number of views (popularity) and then by rating (vote_average) in descending order.
    return popular_movies[
        ['movieName', 'views', 'vote_average']]  # Return only relevant columns: name, views, and rating.


##################################
# NEW ALGORITHMS #
##################################

def build_sentence_transformer_embeddings(movies, retrain=False):
    """
    Builds or loads pre-trained sentence embeddings for movie descriptions.
    Uses pre-trained BERT model from the sentence-transformers library.

    Parameters:
    - movies: DataFrame containing movie data with 'overview' column
    - retrain: If True, recalculates embeddings even if they exist

    Returns:
    - embeddings: numpy array of embeddings
    - movie_indices: dictionary mapping movie titles to embedding indices
    """
    if os.path.exists(EMBEDDINGS_FILE) and os.path.exists(EMBEDDING_MAPPING_FILE) and not retrain:
        print(f"Loading pre-computed sentence embeddings...")
        embeddings = np.load(EMBEDDINGS_FILE)
        movie_indices = joblib.load(EMBEDDING_MAPPING_FILE)
        return embeddings, movie_indices

    print("Computing sentence embeddings using pre-trained BERT model...")
    # Load pre-trained sentence transformer model
    model = SentenceTransformer('paraphrase-MiniLM-L6-v2')

    # Get all movie overviews
    overviews = movies['overview'].fillna('').tolist()

    # Generate embeddings for all movie overviews
    embeddings = model.encode(overviews)

    # Create mapping from movie titles to embedding indices
    movie_indices = {title: idx for idx, title in enumerate(movies['movieName'])}

    # Save embeddings and mapping for future use
    np.save(EMBEDDINGS_FILE, embeddings)
    joblib.dump(movie_indices, EMBEDDING_MAPPING_FILE)

    print(f"Sentence embeddings computed and saved.")
    return embeddings, movie_indices


def get_embedding_recommendations(movie_title, movies, embeddings, movie_indices, top_n=10):
    """
    Returns movie recommendations based on sentence embeddings similarity.

    Parameters:
    - movie_title: Title of the movie to get recommendations for
    - movies: DataFrame containing movie data
    - embeddings: Pre-computed sentence embeddings
    - movie_indices: Dictionary mapping movie titles to embedding indices
    - top_n: Number of recommendations to return

    Returns:
    - List of recommended movies
    """
    # Find movies that match the input title (case-insensitive)
    matches = movies['movieName'].str.contains(movie_title, case=False, na=False)

    # If no matches are found, notify the user and return an empty list
    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    # Get the first matched movie
    matched_movie = movies.loc[matches, 'movieName'].iloc[0]

    # Get the index of the matched movie in the embeddings array
    if matched_movie not in movie_indices:
        print(f"Error: Movie '{matched_movie}' not found in embedding indices.")
        return []

    idx = movie_indices[matched_movie]

    # Compute cosine similarity between the matched movie and all other movies
    movie_embedding = embeddings[idx].reshape(1, -1)
    similarities = cosine_similarity(movie_embedding, embeddings).flatten()

    # Get the indices of the top N most similar movies
    similar_indices = similarities.argsort()[::-1][1:top_n + 1]

    # Get the movie details for the similar movies
    recs = []
    for i in similar_indices:
        movie_data = movies.iloc[i]
        recs.append({
            'movieName': movie_data['movieName'],
            'director': movie_data['director'] if 'director' in movie_data else '',
            'actors': str(movie_data['actors']) if 'actors' in movie_data else '',
            'imdbScore': float(movie_data['vote_average']),
            'similarity': similarities[i]
        })

    return recs


def build_matrix_factorization_model(ratings_df, retrain=False):
    """
    Builds or loads a matrix factorization model using Surprise SVD.

    Parameters:
    - ratings_df: DataFrame containing user-movie ratings
    - retrain: If True, retrains the model even if it exists

    Returns:
    - Trained SVD model
    - User-item interactions for making predictions
    """
    if os.path.exists(MATRIX_FACTORIZATION_FILE) and not retrain:
        print(f"Loading saved matrix factorization model...")
        return joblib.load(MATRIX_FACTORIZATION_FILE)

    print("Building matrix factorization model using SVD...")

    # Create a Surprise dataset
    reader = Reader(rating_scale=(0.5, 5))
    data = Dataset.load_from_df(ratings_df[['userId', 'id', 'rating']], reader)

    # Split data into training and test sets
    trainset, testset = surprise_train_test_split(data, test_size=0.2)

    # Build and train SVD model
    svd_model = SVD(n_factors=100, n_epochs=20, lr_all=0.005, reg_all=0.02)
    svd_model.fit(trainset)

    # Evaluate model performance
    predictions = svd_model.test(testset)
    rmse = accuracy.rmse(predictions)
    print(f"Matrix factorization model trained with RMSE: {rmse:.4f}")

    # Save the model
    joblib.dump(svd_model, MATRIX_FACTORIZATION_FILE)

    return svd_model


def get_collaborative_filtering_recommendations(movie_title, movies, ratings_df, svd_model, top_n=10):
    """
    Returns movie recommendations based on collaborative filtering using SVD.

    Parameters:
    - movie_title: Title of the movie to get recommendations for
    - movies: DataFrame containing movie data
    - ratings_df: DataFrame containing user-movie ratings
    - svd_model: Trained SVD model
    - top_n: Number of recommendations to return

    Returns:
    - List of recommended movies
    """
    # Find movies that match the input title (case-insensitive)
    matches = movies['movieName'].str.contains(movie_title, case=False, na=False)

    # If no matches are found, notify the user and return an empty list
    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    # Get the ID of the matched movie
    movie_id = movies.loc[matches, 'id'].iloc[0]

    # Find users who rated this movie
    users_who_rated = ratings_df[ratings_df['id'] == movie_id]['userId'].unique()

    # If no users rated this movie, return an empty list
    if len(users_who_rated) == 0:
        print(f"No users rated '{movie_title}'. Cannot provide collaborative filtering recommendations.")
        return []

    # Get all unique movie IDs
    all_movie_ids = movies['id'].unique()

    # Calculate predicted ratings for each user-movie pair
    movie_scores = {}
    for user_id in users_who_rated[:100]:  # Limit to 100 users for efficiency
        for movie_id in all_movie_ids:
            if movie_id not in movie_scores:
                movie_scores[movie_id] = []
            predicted_rating = svd_model.predict(user_id, movie_id).est
            movie_scores[movie_id].append(predicted_rating)

    # Calculate average predicted rating for each movie
    avg_scores = {movie_id: np.mean(scores) for movie_id, scores in movie_scores.items()}

    # Sort movies by predicted rating
    sorted_movies = sorted(avg_scores.items(), key=lambda x: x[1], reverse=True)

    # Get the top N movies
    top_movies = [movie_id for movie_id, _ in sorted_movies[:top_n + 10]]  # Get extra to filter out the input movie

    # Get movie details
    recs = []
    for movie_id in top_movies:
        if movie_id in movies['id'].values:
            movie_data = movies[movies['id'] == movie_id].iloc[0]
            if not movie_data['movieName'].lower() == movie_title.lower():  # Skip the input movie
                recs.append({
                    'movieName': movie_data['movieName'],
                    'director': movie_data['director'] if 'director' in movie_data else '',
                    'actors': str(movie_data['actors']) if 'actors' in movie_data else '',
                    'imdbScore': float(movie_data['vote_average']),
                    'predictedRating': avg_scores[movie_id]
                })
                if len(recs) >= top_n:
                    break

    return recs


def build_tree_based_model(movies, ratings_df, retrain=False):
    """
    Builds or loads a tree-based recommendation model (Gradient Boosting).

    Parameters:
    - movies: DataFrame contendo dados dos filmes
    - ratings_df: DataFrame contendo avaliações usuário-filme
    - retrain: Se True, refaz o treinamento mesmo se existir modelo salvo

    Returns:
    - Dicionário contendo o modelo treinado e encoders relevantes
    """
    if os.path.exists(TREE_MODEL_FILE) and not retrain:
        print(f"Loading saved tree-based model...")
        return joblib.load(TREE_MODEL_FILE)

    print("Building tree-based recommendation model using Gradient Boosting...")

    # Merge ratings com dados dos filmes
    data = ratings_df.merge(
        movies[['id', 'director', 'actors', 'vote_average']],
        on='id',
        how='inner'
    )

    # Tratamento explícito de valores faltantes (importante!)
    data['director'] = data['director'].fillna('unknown')
    data['actors'] = data['actors'].fillna('unknown')
    data['vote_average'] = data['vote_average'].fillna(data['vote_average'].mean())

    # Encode categorical features
    user_encoder = LabelEncoder()
    movie_encoder = LabelEncoder()
    director_encoder = LabelEncoder()

    data['user_encoded'] = user_encoder.fit_transform(data['userId'])
    data['movie_encoded'] = movie_encoder.fit_transform(data['id'])
    data['director_encoded'] = director_encoder.fit_transform(data['director'])

    # Criação das variáveis X (features) e y (target)
    X = data[['user_encoded', 'movie_encoded', 'director_encoded', 'vote_average']]
    y = data['rating']

    # Divisão treino/teste
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42
    )

    # Treinamento do modelo Gradient Boosting
    gb_model = GradientBoostingRegressor(
        n_estimators=100,
        max_depth=3,
        learning_rate=0.1,
        random_state=42
    )
    gb_model.fit(X_train, y_train)

    # Avaliação do modelo
    train_score = gb_model.score(X_train, y_train)
    test_score = gb_model.score(X_test, y_test)
    print(f"Gradient Boosting model trained. R² on training: {train_score:.4f}, on test: {test_score:.4f}")

    # Salva modelo e encoders
    model_data = {
        'model': gb_model,
        'user_encoder': user_encoder,
        'movie_encoder': movie_encoder,
        'director_encoder': director_encoder
    }
    joblib.dump(model_data, TREE_MODEL_FILE)

    return model_data


def get_tree_based_recommendations(movie_title, movies, ratings_df, tree_model_data, top_n=10):
    """
    Returns movie recommendations based on tree-based models.

    Parameters:
    - movie_title: Title of the movie to get recommendations for
    - movies: DataFrame containing movie data
    - ratings_df: DataFrame containing user-movie ratings
    - tree_model_data: Dictionary containing the trained model and encoders
    - top_n: Number of recommendations to return

    Returns:
    - List of recommended movies
    """
    # Extract model components
    gb_model = tree_model_data['model']
    user_encoder = tree_model_data['user_encoder']
    movie_encoder = tree_model_data['movie_encoder']
    director_encoder = tree_model_data['director_encoder']

    # Find movies that match the input title (case-insensitive)
    matches = movies['movieName'].str.contains(movie_title, case=False, na=False)

    # If no matches are found, notify the user and return an empty list
    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    # Get the matched movie details
    matched_movie = movies.loc[matches].iloc[0]

    # Find users who rated this movie highly (rating >= 4)
    target_movie_id = matched_movie['id']
    similar_users = ratings_df[(ratings_df['id'] == target_movie_id) & (ratings_df['rating'] >= 4)]['userId'].unique()

    # If no similar users found, use the top users by number of ratings
    if len(similar_users) == 0:
        similar_users = ratings_df.groupby('userId').size().sort_values(ascending=False).head(50).index

    # Get movies that these users rated but exclude the target movie
    candidate_ratings = ratings_df[ratings_df['userId'].isin(similar_users) & (ratings_df['id'] != target_movie_id)]
    candidate_movie_ids = candidate_ratings['id'].unique()

    # Prepare data for prediction
    predictions = []
    for movie_id in candidate_movie_ids:
        if movie_id in movies['id'].values:
            movie_data = movies[movies['id'] == movie_id].iloc[0]
            director = movie_data['director'] if 'director' in movie_data and pd.notna(
                movie_data['director']) else 'unknown'

            # Ensure the director is in the encoder's vocabulary
            if director not in director_encoder.classes_:
                director = 'unknown'

            # Prepare features for each user
            for user_id in similar_users[:10]:  # Limit to 10 users for efficiency
                try:
                    user_encoded = user_encoder.transform([user_id])[0]
                    movie_encoded = movie_encoder.transform([movie_id])[0]
                    director_encoded = director_encoder.transform([director])[0]
                    vote_avg = movie_data['vote_average']

                    # Create feature vector
                    features = np.array([[user_encoded, movie_encoded, director_encoded, vote_avg]])

                    # Predict rating
                    predicted_rating = gb_model.predict(features)[0]

                    predictions.append((movie_id, predicted_rating))
                except:
                    continue

    # Aggregate predictions by movie
    movie_scores = {}
    for movie_id, rating in predictions:
        if movie_id not in movie_scores:
            movie_scores[movie_id] = []
        movie_scores[movie_id].append(rating)

    # Calculate average predicted rating for each movie
    avg_scores = {movie_id: np.mean(scores) for movie_id, scores in movie_scores.items() if len(scores) > 0}

    # Sort movies by predicted rating
    sorted_movies = sorted(avg_scores.items(), key=lambda x: x[1], reverse=True)

    # Get movie details for recommendations
    recs = []
    for movie_id, score in sorted_movies[:top_n]:
        movie_data = movies[movies['id'] == movie_id].iloc[0]
        recs.append({
            'movieName': movie_data['movieName'],
            'director': movie_data['director'] if 'director' in movie_data else '',
            'actors': str(movie_data['actors']) if 'actors' in movie_data else '',
            'imdbScore': float(movie_data['vote_average']),
            'predictedRating': score
        })

    return recs


def build_hybrid_model(movies, ratings_df, retrain=False):
    """
    Builds a hybrid recommendation model combining content-based and collaborative approaches.

    Parameters:
    - movies: DataFrame containing movie data
    - ratings_df: DataFrame containing user-movie ratings
    - retrain: If True, retrains the model even if it exists

    Returns:
    - Dictionary containing all components needed for hybrid recommendations
    """
    if os.path.exists(HYBRID_MODEL_FILE) and not retrain:
        print(f"Loading saved hybrid model...")
        return joblib.load(HYBRID_MODEL_FILE)

    print("Building hybrid recommendation model...")

    # Build content-based components
    tfidf_matrix_overview = build_tfidf_matrix(movies, 'overview', 'tfidf_overview', retrain)
    embeddings, movie_indices = build_sentence_transformer_embeddings(movies, retrain)

    # Build collaborative filtering component
    svd_model = build_matrix_factorization_model(ratings_df, retrain)

    # Build tree-based component
    tree_model_data = build_tree_based_model(movies, ratings_df, retrain)

    # Create hybrid model container
    hybrid_model = {
        'tfidf_matrix_overview': tfidf_matrix_overview,
        'embeddings': embeddings,
        'movie_indices': movie_indices,
        'svd_model': svd_model,
        'tree_model_data': tree_model_data
    }

    # Save hybrid model
    joblib.dump(hybrid_model, HYBRID_MODEL_FILE)

    return hybrid_model


def get_hybrid_recommendations(movie_title, movies, ratings_df, hybrid_model, top_n=10):
    """
    Returns movie recommendations using a hybrid approach that combines:
    1. Content-based (TF-IDF & embeddings)
    2. Collaborative filtering (SVD)
    3. Tree-based prediction

    Parameters:
    - movie_title: Title of the movie to get recommendations for
    - movies: DataFrame containing movie data
    - ratings_df: DataFrame containing user-movie ratings
    - hybrid_model: Dictionary containing all model components
    - top_n: Number of recommendations to return

    Returns:
    - List of recommended movies
    """
    # Find movies that match the input title (case-insensitive)
    matches = movies['movieName'].str.contains(movie_title, case=False, na=False)

    # If no matches are found, notify the user and return an empty list
    if not matches.any():
        print(f"Movie '{movie_title}' not found. Try another title.")
        return []

    # Extract model components
    tfidf_matrix = hybrid_model['tfidf_matrix_overview']
    embeddings = hybrid_model['embeddings']
    movie_indices = hybrid_model['movie_indices']
    svd_model = hybrid_model['svd_model']
    tree_model_data = hybrid_model['tree_model_data']

    # Get recommendations from each approach
    content_recs_tfidf = get_recommendations(movie_title, movies, tfidf_matrix, top_n=top_n)
    content_recs_emb = get_embedding_recommendations(movie_title, movies, embeddings, movie_indices, top_n=top_n)
    collab_recs = get_collaborative_filtering_recommendations(movie_title, movies, ratings_df, svd_model, top_n=top_n)
    tree_recs = get_tree_based_recommendations(movie_title, movies, ratings_df, tree_model_data, top_n=top_n)

    # Collect all recommended movies
    all_recs = {}

    # Add TFIDF-based recommendations with weight 0.25
    for rec in content_recs_tfidf:
        movie_name = rec['movieName']
        if movie_name not in all_recs:
            all_recs[movie_name] = {'movie': rec, 'score': 0, 'count': 0}
        all_recs[movie_name]['score'] += rec.get('similarity', 0) * 0.25
        all_recs[movie_name]['count'] += 1

    # Add embedding-based recommendations with weight 0.25
    for rec in content_recs_emb:
        movie_name = rec['movieName']
        if movie_name not in all_recs:
            all_recs[movie_name] = {'movie': rec, 'score': 0, 'count': 0}
        all_recs[movie_name]['score'] += rec.get('similarity', 0) * 0.25
        all_recs[movie_name]['count'] += 1

    # Add collaborative filtering recommendations with weight 0.25
    for rec in collab_recs:
        movie_name = rec['movieName']
        if movie_name not in all_recs:
            all_recs[movie_name] = {'movie': rec, 'score': 0, 'count': 0}
        all_recs[movie_name]['score'] += min(rec.get('predictedRating', 0) / 5, 1) * 0.25
        all_recs[movie_name]['count'] += 1

    # Add tree-based recommendations with weight 0.25
    for rec in tree_recs:
        movie_name = rec['movieName']
        if movie_name not in all_recs:
            all_recs[movie_name] = {'movie': rec, 'score': 0, 'count': 0}
        all_recs[movie_name]['score'] += min(rec.get('predictedRating', 0) / 5, 1) * 0.25
        all_recs[movie_name]['count'] += 1

    # Calculate average score across all methods
    for movie_name, data in all_recs.items():
        if data['count'] > 0:
            data['final_score'] = data['score'] / data['count'] * data['count']
        else:
            data['final_score'] = 0

    # Sort by final score
    sorted_recs = sorted(all_recs.items(), key=lambda x: x[1]['final_score'], reverse=True)

    # Extract the top N recommendations
    final_recs = []
    for movie_name, data in sorted_recs[:top_n]:
        rec = data['movie']
        rec['hybridScore'] = data['final_score']
        final_recs.append(rec)

    return final_recs


###############
# Execution #
###############

def print_recommendations(recs):
    """Prints recommendation results in a formatted way."""
    if not recs:
        return

    # Determine which keys are available in the recommendations
    sample_rec = recs[0]
    keys = sample_rec.keys()

    # Print header
    print("\nRecommended movies:")
    print("-" * 80)

    # Print each recommendation with available information
    for i, rec in enumerate(recs, 1):
        print(f"{i}. {rec['movieName']}")
        if 'director' in keys and rec['director']:
            print(f"   Director: {rec['director']}")
        if 'actors' in keys and rec['actors']:
            print(f"   Actors: {rec['actors']}")
        if 'imdbScore' in keys:
            print(f"   IMDb Score: {rec['imdbScore']:.1f}")
        if 'similarity' in keys:
            print(f"   Similarity: {rec['similarity']:.3f}")
        if 'predictedRating' in keys:
            print(f"   Predicted Rating: {rec['predictedRating']:.2f}")
        if 'hybridScore' in keys:
            print(f"   Hybrid Score: {rec['hybridScore']:.3f}")
        print("-" * 80)


def main():
    """Main function to run the movie recommendation system."""
    print("Initializing recommendation system...")

    # Process and load data
    movies = process_movie_features()
    ratings_df = load_ratings()
    views_df = compute_movie_views(ratings_df)

    # Initialize algorithm components (only when needed)
    tfidf_matrix_credits = None
    tfidf_matrix_overview = None
    embeddings = None
    movie_indices = None
    svd_model = None
    tree_model_data = None
    hybrid_model = None

    while True:
        print("\n==== Movie Recommendation System ====")
        print("Choose algorithm type:")
        print("1 - Content-based (TF-IDF) using Directors and Actors")
        print("2 - Content-based (TF-IDF) using Movie Description")
        print("3 - Pre-trained Embeddings (BERT)")
        print("4 - Matrix Factorization (Collaborative Filtering)")
        print("5 - Tree-based Algorithms (Gradient Boosting)")
        print("6 - Hybrid Model")
        print("7 - Popular Movies")
        print("8 - Retrain All Models")
        print("9 - Exit")

        choice = input("Enter your choice (1-9): ").strip()

        if choice == "1":
            if tfidf_matrix_credits is None:
                tfidf_matrix_credits = build_tfidf_matrix(movies, 'combined_features', 'tfidf_credits')

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_recommendations(movie_title, movies, tfidf_matrix_credits)
            print_recommendations(recs)

        elif choice == "2":
            if tfidf_matrix_overview is None:
                tfidf_matrix_overview = build_tfidf_matrix(movies, 'overview', 'tfidf_overview')

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_recommendations(movie_title, movies, tfidf_matrix_overview)
            print_recommendations(recs)

        elif choice == "3":
            if embeddings is None or movie_indices is None:
                embeddings, movie_indices = build_sentence_transformer_embeddings(movies)

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_embedding_recommendations(movie_title, movies, embeddings, movie_indices)
            print_recommendations(recs)

        elif choice == "4":
            if svd_model is None:
                svd_model = build_matrix_factorization_model(ratings_df)

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_collaborative_filtering_recommendations(movie_title, movies, ratings_df, svd_model)
            print_recommendations(recs)

        elif choice == "5":
            if tree_model_data is None:
                tree_model_data = build_tree_based_model(movies, ratings_df)

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_tree_based_recommendations(movie_title, movies, ratings_df, tree_model_data)
            print_recommendations(recs)

        elif choice == "6":
            if hybrid_model is None:
                hybrid_model = build_hybrid_model(movies, ratings_df)

            movie_title = input("\nEnter a movie title for recommendations: ").strip()
            recs = get_hybrid_recommendations(movie_title, movies, ratings_df, hybrid_model)
            print_recommendations(recs)

        elif choice == "7":
            top_n = input("Enter number of popular movies to show (default 10): ").strip()
            try:
                top_n = int(top_n) if top_n else 10
            except ValueError:
                top_n = 10

            popular_movies = get_popular_movies(movies, views_df, top_n)
            print("\nTop Most Popular Movies:")
            print(popular_movies.to_string(index=False))

        elif choice == "8":
            print("\nRetraining all models...")
            # Process basic data
            movies = process_movie_features(retrain=True)
            ratings_df = load_ratings()
            views_df = compute_movie_views(ratings_df)

            # Retrain all individual models
            tfidf_matrix_credits = build_tfidf_matrix(movies, 'combined_features', 'tfidf_credits', retrain=True)
            tfidf_matrix_overview = build_tfidf_matrix(movies, 'overview', 'tfidf_overview', retrain=True)
            embeddings, movie_indices = build_sentence_transformer_embeddings(movies, retrain=True)
            svd_model = build_matrix_factorization_model(ratings_df, retrain=True)
            tree_model_data = build_tree_based_model(movies, ratings_df, retrain=True)

            # Rebuild hybrid model
            hybrid_model = build_hybrid_model(movies, ratings_df, retrain=True)

            print("All models have been retrained successfully.")

        elif choice == "9":
            print("Thank you for using the Movie Recommendation System. Goodbye!")
            break

        else:
            print("Invalid choice. Please try again.")


if __name__ == '__main__':
    main()
