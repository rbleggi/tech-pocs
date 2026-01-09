import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.rbleggi.recommendationsystem.*

class RecommendationSystemSpec extends AnyFlatSpec with Matchers:

  val items = List(
    Item("item1", Map("feature1" -> 0.8, "feature2" -> 0.2)),
    Item("item2", Map("feature1" -> 0.2, "feature2" -> 0.9)),
    Item("item3", Map("feature1" -> 0.5, "feature2" -> 0.5)),
    Item("item4", Map("feature1" -> 0.9, "feature2" -> 0.1))
  )

  val ratings = List(
    Rating("user1", "item1", 5.0),
    Rating("user1", "item2", 3.0),
    Rating("user2", "item1", 4.0),
    Rating("user2", "item3", 5.0),
    Rating("user3", "item2", 5.0)
  )

  "SimilarityMetrics" should "calculate cosine similarity" in {
    val v1 = Map("a" -> 1.0, "b" -> 0.0)
    val v2 = Map("a" -> 1.0, "b" -> 0.0)
    SimilarityMetrics.cosineSimilarity(v1, v2) shouldBe 1.0
  }

  it should "return 0 for orthogonal vectors" in {
    val v1 = Map("a" -> 1.0, "b" -> 0.0)
    val v2 = Map("a" -> 0.0, "b" -> 1.0)
    SimilarityMetrics.cosineSimilarity(v1, v2) shouldBe 0.0
  }

  it should "calculate pearson correlation" in {
    val v1 = Map("a" -> 1.0, "b" -> 2.0, "c" -> 3.0)
    val v2 = Map("a" -> 1.0, "b" -> 2.0, "c" -> 3.0)
    SimilarityMetrics.pearsonCorrelation(v1, v2) shouldBe 1.0 +- 0.01
  }

  "UserBasedCollaborativeFiltering" should "recommend items" in {
    val strategy = UserBasedCollaborativeFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 2)
    recommendations should not be empty
  }

  it should "not recommend already rated items" in {
    val strategy = UserBasedCollaborativeFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 5)
    recommendations.exists(_.itemId == "item1") shouldBe false
    recommendations.exists(_.itemId == "item2") shouldBe false
  }

  "ItemBasedCollaborativeFiltering" should "recommend items" in {
    val strategy = ItemBasedCollaborativeFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 2)
    recommendations should not be empty
  }

  it should "base recommendations on item similarity" in {
    val strategy = ItemBasedCollaborativeFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 3)
    recommendations.forall(_.reason.contains("Similar to items")) shouldBe true
  }

  "ContentBasedFiltering" should "recommend items" in {
    val strategy = ContentBasedFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 2)
    recommendations should not be empty
  }

  it should "base recommendations on content similarity" in {
    val strategy = ContentBasedFiltering()
    val recommendations = strategy.recommend("user1", ratings, items, 3)
    recommendations.forall(_.reason.contains("preferences")) shouldBe true
  }

  it should "return empty for new users" in {
    val strategy = ContentBasedFiltering()
    val recommendations = strategy.recommend("newUser", ratings, items, 5)
    recommendations shouldBe empty
  }

  "HybridRecommendation" should "combine strategies" in {
    val strategies = List(
      UserBasedCollaborativeFiltering(),
      ContentBasedFiltering()
    )
    val hybrid = HybridRecommendation(strategies, List(0.5, 0.5))
    val recommendations = hybrid.recommend("user1", ratings, items, 3)
    recommendations should not be empty
  }

  it should "enforce weight sum constraint" in {
    assertThrows[IllegalArgumentException] {
      HybridRecommendation(
        List(UserBasedCollaborativeFiltering()),
        List(0.5)
      )
    }
  }

  "RecommendationSystem" should "use provided strategy" in {
    val system = RecommendationSystem(UserBasedCollaborativeFiltering())
    val recommendations = system.recommend("user1", ratings, items, 3)
    recommendations should not be empty
  }

  it should "return strategy name" in {
    val system = RecommendationSystem(ContentBasedFiltering())
    system.getStrategyName shouldBe "Content-Based Filtering"
  }

  it should "limit results to topN" in {
    val system = RecommendationSystem(UserBasedCollaborativeFiltering())
    val recommendations = system.recommend("user1", ratings, items, 1)
    recommendations.length should be <= 1
  }
