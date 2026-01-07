import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.rbleggi.chatbot.*

class ChatbotSpec extends AnyFlatSpec with Matchers:

  "GreetingCommand" should "match greeting inputs" in {
    val cmd = GreetingCommand()
    cmd.matches("hello") shouldBe true
    cmd.matches("hi there") shouldBe true
    cmd.matches("good morning") shouldBe true
    cmd.matches("weather") shouldBe false
  }

  it should "extract user name" in {
    val cmd = GreetingCommand()
    val context = ConversationContext()
    val (response, newContext) = cmd.execute("Hi, my name is Alice", context)
    newContext.userName shouldBe Some("Alice")
    response should include("Alice")
  }

  it should "remember user name in context" in {
    val cmd = GreetingCommand()
    val context = ConversationContext().withUserName("Bob")
    val (response, _) = cmd.execute("Hello again", context)
    response should include("Bob")
  }

  "WeatherCommand" should "match weather inputs" in {
    val cmd = WeatherCommand()
    cmd.matches("what's the weather") shouldBe true
    cmd.matches("temperature") shouldBe true
    cmd.matches("hello") shouldBe false
  }

  it should "extract city name" in {
    val cmd = WeatherCommand()
    val context = ConversationContext()
    val (response, newContext) = cmd.execute("What's the weather in Paris?", context)
    newContext.entities.get("city") shouldBe Some("Paris")
    response should include("Paris")
  }

  "TimeCommand" should "match time inputs" in {
    val cmd = TimeCommand()
    cmd.matches("what time is it") shouldBe true
    cmd.matches("show me the clock") shouldBe true
    cmd.matches("weather") shouldBe false
  }

  it should "return current time" in {
    val cmd = TimeCommand()
    val context = ConversationContext()
    val (response, _) = cmd.execute("what time is it?", context)
    response should include("current time")
  }

  "ReminderCommand" should "match reminder inputs" in {
    val cmd = ReminderCommand()
    cmd.matches("remind me to") shouldBe true
    cmd.matches("set a reminder") shouldBe true
    cmd.matches("hello") shouldBe false
  }

  it should "extract task from input" in {
    val cmd = ReminderCommand()
    val context = ConversationContext()
    val (response, newContext) = cmd.execute("Remind me to buy milk", context)
    newContext.entities.get("reminder") shouldBe Some("buy milk")
    response should include("buy milk")
  }

  "HelpCommand" should "match help inputs" in {
    val cmd = HelpCommand()
    cmd.matches("help") shouldBe true
    cmd.matches("what can you do") shouldBe true
  }

  it should "list available commands" in {
    val cmd = HelpCommand()
    val context = ConversationContext()
    val (response, _) = cmd.execute("help", context)
    response should include("Greetings")
    response should include("Weather")
    response should include("time")
  }

  "ContextCommand" should "show conversation context" in {
    val cmd = ContextCommand()
    val context = ConversationContext()
      .withUserName("Alice")
      .addEntity("city", "Paris")
    val (response, _) = cmd.execute("what do you know", context)
    response should include("Alice")
  }

  "Chatbot" should "process messages with appropriate commands" in {
    val commands = List(
      GreetingCommand(),
      WeatherCommand(),
      TimeCommand(),
      HelpCommand(),
      UnknownCommand()
    )
    val bot = Chatbot(commands)

    val response1 = bot.processMessage("hello")
    response1 should include("Hello")

    val response2 = bot.processMessage("help")
    response2 should include("help")
  }

  it should "maintain conversation context" in {
    val commands = List(
      GreetingCommand(),
      ContextCommand(),
      UnknownCommand()
    )
    val bot = Chatbot(commands)

    bot.processMessage("Hi, my name is Bob")
    bot.getContext.userName shouldBe Some("Bob")

    val response = bot.processMessage("what do you know")
    response should include("Bob")
  }

  it should "reset context" in {
    val commands = List(GreetingCommand(), UnknownCommand())
    val bot = Chatbot(commands)

    bot.processMessage("Hi, my name is Charlie")
    bot.getContext.userName shouldBe Some("Charlie")

    bot.resetContext()
    bot.getContext.userName shouldBe None
  }

  "ConversationContext" should "add messages to history" in {
    val context = ConversationContext()
      .addToHistory("first")
      .addToHistory("second")
    context.history should have length 2
    context.history.head shouldBe "second"
  }

  it should "store entities" in {
    val context = ConversationContext()
      .addEntity("key1", "value1")
      .addEntity("key2", "value2")
    context.entities should have size 2
    context.entities("key1") shouldBe "value1"
  }
