package com.rbleggi.socialmedia

import org.scalatest.funsuite.AnyFunSuite

class SocialMediaPhotoAppSpec extends AnyFunSuite {
  test("User should publish photo and notify observers") {
    var notified = false
    val testObserver = new Observer {
      override def update(event: Event): Unit = event match {
        case PhotoPublished(_) => notified = true
        case _ => ()
      }
    }

    val user = new User("Alice")
    user.registerObserver(testObserver)
    val photo = Photo("1", "http://test.com/photo.jpg")
    user.publishPhoto(photo)

    assert(notified)
  }

  test("User should tag another user and notify observers") {
    var taggedUser = ""
    val testObserver = new Observer {
      override def update(event: Event): Unit = event match {
        case UserTagged(_, user) => taggedUser = user
        case _ => ()
      }
    }

    val user = new User("Alice")
    user.registerObserver(testObserver)
    val photo = Photo("1", "http://test.com/photo.jpg")
    user.tagUser(photo, "Bob")

    assert(taggedUser == "Bob")
  }

  test("User should comment on photo and notify observers") {
    var receivedComment: Option[Comment] = None
    val testObserver = new Observer {
      override def update(event: Event): Unit = event match {
        case PhotoCommented(_, comment) => receivedComment = Some(comment)
        case _ => ()
      }
    }

    val user = new User("Alice")
    user.registerObserver(testObserver)
    val photo = Photo("1", "http://test.com/photo.jpg")
    val comment = Comment("Bob", "Nice photo!")
    user.commentPhoto(photo, comment)

    assert(receivedComment.isDefined)
    assert(receivedComment.get.text == "Nice photo!")
  }

  test("User should remove observer") {
    var notificationCount = 0
    val testObserver = new Observer {
      override def update(event: Event): Unit = notificationCount += 1
    }

    val user = new User("Alice")
    user.registerObserver(testObserver)
    val photo = Photo("1", "http://test.com/photo.jpg")
    user.publishPhoto(photo)

    user.removeObserver(testObserver)
    user.publishPhoto(Photo("2", "http://test.com/photo2.jpg"))

    assert(notificationCount == 1)
  }
}

