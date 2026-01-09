import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.rbleggi.recommendationsystem.*

class RecommendationSystemSpec extends AnyFlatSpec with Matchers:

  val items = List(
    Item("item1", "action"),
    Item("item2", "drama"),
    Item("item3", "comedy"),
    Item("item4", "action")
  )

  val ratings = List(
    Rating("user1", "item1", 5.0),
    Rating("user1", "item2", 3.0),
    Rating("user2", "item1", 4.0),
    Rating("user2", "item3", 5.0),
    Rating("user3", "item2", 5.0)
  )

  "PopularityBased" should "recommend popular items" in {
    val strategy = PopularityBased()
    val recommendations = strategy.recommend("user1", ratings, items, 2)
    recommendations should not be empty
  }

  it should "not recommend already rated items" in {
    val strategy = PopularityBased()
    val recommendations = strategy.recommend("user1", ratings, items, 5)
    recommendations should not contain "item1"
    recommendations should not contain "item2"
  }

  "CategoryBased" should "recommend items from favorite category" in {
    val strategy = CategoryBased()
    val recommendations = strategy.recommend("user1", ratings, items, 2)
    recommendations should not be empty
  }

  it should "return empty for new users" in {
    val strategy = CategoryBased()
    val recommendations = strategy.recommend("newUser", ratings, items, 5)
    recommendations shouldBe empty
  }

  "RecommendationSystem" should "use provided strategy" in {
    val system = RecommendationSystem(PopularityBased())
    val recommendations = system.recommend("user1", ratings, items, 3)
    recommendations should not be empty
  }

  it should "limit results to topN" in {
    val system = RecommendationSystem(PopularityBased())
    val recommendations = system.recommend("user1", ratings, items, 1)
    recommendations.length should be <= 1
  }
