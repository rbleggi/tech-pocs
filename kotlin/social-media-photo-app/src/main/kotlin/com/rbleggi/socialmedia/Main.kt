package com.rbleggi.socialmedia

interface Observer {
    fun update(event: Event)
}

interface Subject {
    fun registerObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun notifyObservers(event: Event)
}

data class Photo(val id: String, val url: String, val tags: List<String> = emptyList())
data class Comment(val user: String, val text: String)

sealed class Event
data class PhotoPublished(val photo: Photo) : Event()
data class UserTagged(val photo: Photo, val taggedUser: String) : Event()
data class PhotoCommented(val photo: Photo, val comment: Comment) : Event()

class User(val name: String) : Subject {
    private val observers = mutableListOf<Observer>()
    private val timeline = mutableListOf<Photo>()

    fun publishPhoto(photo: Photo) {
        timeline.add(0, photo)
        notifyObservers(PhotoPublished(photo))
    }

    fun tagUser(photo: Photo, user: String) {
        notifyObservers(UserTagged(photo, user))
    }

    fun commentPhoto(photo: Photo, comment: Comment) {
        notifyObservers(PhotoCommented(photo, comment))
    }

    override fun registerObserver(observer: Observer) {
        observers.add(0, observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(event: Event) {
        observers.forEach { it.update(event) }
    }
}

class Follower(private val followerName: String) : Observer {
    override fun update(event: Event) {
        when (event) {
            is PhotoPublished -> println("[$followerName] New photo published: ${event.photo.url}")
            is UserTagged -> println("[$followerName] ${event.taggedUser} was tagged in photo: ${event.photo.url}")
            is PhotoCommented -> println("[$followerName] New comment by ${event.comment.user}: ${event.comment.text}")
        }
    }
}

fun main() {
    val alice = User("Alice")
    val bob = Follower("Bob")
    val charlie = Follower("Charlie")

    alice.registerObserver(bob)
    alice.registerObserver(charlie)

    val photo1 = Photo("1", "http://photos.com/1")
    alice.publishPhoto(photo1)
    alice.tagUser(photo1, "Charlie")
    alice.commentPhoto(photo1, Comment("Bob", "Nice photo!"))
}
