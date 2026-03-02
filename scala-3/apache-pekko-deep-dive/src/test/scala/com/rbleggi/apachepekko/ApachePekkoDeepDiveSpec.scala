package com.rbleggi.apachepekko

import org.apache.pekko.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import com.rbleggi.GreeterDemo._

class ApachePekkoDeepDiveSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {
  "Greeter actor" should {
    "respond to Greet message" in {
      val replyProbe = createTestProbe[Greeted]()
      val greeterActor = spawn(greeter)

      greeterActor ! Greet("Alice", replyProbe.ref)

      val response = replyProbe.receiveMessage()
      assert(response.whom == "Alice")
    }

    "handle multiple greetings" in {
      val replyProbe = createTestProbe[Greeted]()
      val greeterActor = spawn(greeter)

      greeterActor ! Greet("Alice", replyProbe.ref)
      greeterActor ! Greet("Bob", replyProbe.ref)

      val response1 = replyProbe.receiveMessage()
      val response2 = replyProbe.receiveMessage()

      assert(response1.whom == "Alice")
      assert(response2.whom == "Bob")
    }
  }

  "Replier actor" should {
    "receive Greeted message" in {
      val replierActor = spawn(replier)

      replierActor ! Greeted("Charlie")

      Thread.sleep(100)
    }
  }
}

