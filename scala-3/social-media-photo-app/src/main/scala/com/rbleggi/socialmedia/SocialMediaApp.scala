package com.rbleggi.socialmedia

trait Observer {
  def update(event: Event): Unit
}

trait Subject {
  def registerObserver(observer: Observer): Unit
  def removeObserver(observer: Observer): Unit
  def notifyObservers(event: Event): Unit
}

case class Photo(id: String, url: String, tags: List[String] = List())
case class Comment(user: String, text: String)
sealed trait Event
case class PhotoPublished(photo: Photo) extends Event
case class UserTagged(photo: Photo, taggedUser: String) extends Event
case class PhotoCommented(photo: Photo, comment: Comment) extends Event

class User(val name: String) extends Subject {
  private var observers: List[Observer] = List()
  private var timeline: List[Photo] = List()

  def publishPhoto(photo: Photo): Unit = {
    timeline = photo :: timeline
    notifyObservers(PhotoPublished(photo))
  }

  def tagUser(photo: Photo, user: String): Unit = {
    notifyObservers(UserTagged(photo, user))
  }

  def commentPhoto(photo: Photo, comment: Comment): Unit = {
    notifyObservers(PhotoCommented(photo, comment))
  }

  override def registerObserver(observer: Observer): Unit = {
    observers = observer :: observers
  }

  override def removeObserver(observer: Observer): Unit = {
    observers = observers.filterNot(_ == observer)
  }

  override def notifyObservers(event: Event): Unit = {
    observers.foreach(_.update(event))
  }
}

class Follower(val followerName: String) extends Observer {
  override def update(event: Event): Unit = event match {
    case PhotoPublished(photo) => println(s"[$followerName] New photo published: ${photo.url}")
    case UserTagged(photo, user) => println(s"[$followerName] $user was tagged in photo: ${photo.url}")
    case PhotoCommented(photo, comment) => println(s"[$followerName] New comment by ${comment.user}: ${comment.text}")
  }
}

@main def run(): Unit =
  println("Social Media Photo App")
