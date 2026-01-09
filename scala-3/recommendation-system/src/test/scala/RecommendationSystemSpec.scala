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
