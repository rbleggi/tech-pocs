package com.rbleggi.socialmedia

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SocialMediaTest {
    @Test
    fun testObserverPattern() {
        val user = User("Alice")
        var eventCount = 0

        val testObserver = object : Observer {
            override fun update(event: Event) {
                eventCount++
            }
        }

        user.registerObserver(testObserver)

        val photo = Photo("1", "http://photos.com/1")
        user.publishPhoto(photo)
        user.tagUser(photo, "Bob")
        user.commentPhoto(photo, Comment("Charlie", "Great!"))

        assertEquals(3, eventCount)
    }

    @Test
    fun testPhotoPublishedEvent() {
        val user = User("Alice")
        var publishedPhoto: Photo? = null

        val observer = object : Observer {
            override fun update(event: Event) {
                if (event is PhotoPublished) {
                    publishedPhoto = event.photo
                }
            }
        }

        user.registerObserver(observer)
        val photo = Photo("1", "http://photos.com/1", listOf("vacation", "beach"))
        user.publishPhoto(photo)

        assertEquals("1", publishedPhoto?.id)
        assertEquals("http://photos.com/1", publishedPhoto?.url)
        assertEquals(2, publishedPhoto?.tags?.size)
    }

    @Test
    fun testUserTaggedEvent() {
        val user = User("Alice")
        var taggedUser: String? = null
        var taggedPhoto: Photo? = null

        val observer = object : Observer {
            override fun update(event: Event) {
                if (event is UserTagged) {
                    taggedUser = event.taggedUser
                    taggedPhoto = event.photo
                }
            }
        }

        user.registerObserver(observer)
        val photo = Photo("1", "http://photos.com/1")
        user.tagUser(photo, "Bob")

        assertEquals("Bob", taggedUser)
        assertEquals("1", taggedPhoto?.id)
    }

    @Test
    fun testPhotoCommentedEvent() {
        val user = User("Alice")
        var receivedComment: Comment? = null
        var commentedPhoto: Photo? = null

        val observer = object : Observer {
            override fun update(event: Event) {
                if (event is PhotoCommented) {
                    receivedComment = event.comment
                    commentedPhoto = event.photo
                }
            }
        }

        user.registerObserver(observer)
        val photo = Photo("1", "http://photos.com/1")
        val comment = Comment("Bob", "Amazing shot!")
        user.commentPhoto(photo, comment)

        assertEquals("Bob", receivedComment?.user)
        assertEquals("Amazing shot!", receivedComment?.text)
        assertEquals("1", commentedPhoto?.id)
    }

    @Test
    fun testMultipleObservers() {
        val user = User("Alice")
        var observer1Events = 0
        var observer2Events = 0

        val observer1 = object : Observer {
            override fun update(event: Event) {
                observer1Events++
            }
        }

        val observer2 = object : Observer {
            override fun update(event: Event) {
                observer2Events++
            }
        }

        user.registerObserver(observer1)
        user.registerObserver(observer2)

        val photo = Photo("1", "http://photos.com/1")
        user.publishPhoto(photo)

        assertEquals(1, observer1Events)
        assertEquals(1, observer2Events)
    }

    @Test
    fun testRemoveObserver() {
        val user = User("Alice")
        var eventCount = 0

        val observer = object : Observer {
            override fun update(event: Event) {
                eventCount++
            }
        }

        user.registerObserver(observer)
        val photo1 = Photo("1", "http://photos.com/1")
        user.publishPhoto(photo1)

        assertEquals(1, eventCount)

        user.removeObserver(observer)
        val photo2 = Photo("2", "http://photos.com/2")
        user.publishPhoto(photo2)

        assertEquals(1, eventCount)
    }

    @Test
    fun testNoObservers() {
        val user = User("Alice")
        val photo = Photo("1", "http://photos.com/1")

        user.publishPhoto(photo)
        user.tagUser(photo, "Bob")
        user.commentPhoto(photo, Comment("Charlie", "Nice!"))
    }

    @Test
    fun testMultiplePhotos() {
        val user = User("Alice")
        val publishedPhotos = mutableListOf<Photo>()

        val observer = object : Observer {
            override fun update(event: Event) {
                if (event is PhotoPublished) {
                    publishedPhotos.add(event.photo)
                }
            }
        }

        user.registerObserver(observer)

        val photo1 = Photo("1", "http://photos.com/1")
        val photo2 = Photo("2", "http://photos.com/2")
        val photo3 = Photo("3", "http://photos.com/3")

        user.publishPhoto(photo1)
        user.publishPhoto(photo2)
        user.publishPhoto(photo3)

        assertEquals(3, publishedPhotos.size)
        assertEquals("1", publishedPhotos[0].id)
        assertEquals("2", publishedPhotos[1].id)
        assertEquals("3", publishedPhotos[2].id)
    }

    @Test
    fun testFollowerReceivesAllEventTypes() {
        val user = User("Alice")
        val follower = Follower("Bob")
        val events = mutableListOf<Event>()

        val trackingObserver = object : Observer {
            override fun update(event: Event) {
                events.add(event)
            }
        }

        user.registerObserver(follower)
        user.registerObserver(trackingObserver)

        val photo = Photo("1", "http://photos.com/1")
        user.publishPhoto(photo)
        user.tagUser(photo, "Charlie")
        user.commentPhoto(photo, Comment("Bob", "Great!"))

        assertEquals(3, events.size)
        assertTrue(events[0] is PhotoPublished)
        assertTrue(events[1] is UserTagged)
        assertTrue(events[2] is PhotoCommented)
    }

    @Test
    fun testPhotoWithTags() {
        val photo = Photo("1", "http://photos.com/1", listOf("sunset", "beach", "vacation"))

        assertEquals("1", photo.id)
        assertEquals("http://photos.com/1", photo.url)
        assertEquals(3, photo.tags.size)
        assertTrue(photo.tags.contains("sunset"))
        assertTrue(photo.tags.contains("beach"))
        assertTrue(photo.tags.contains("vacation"))
    }

    @Test
    fun testPhotoWithoutTags() {
        val photo = Photo("1", "http://photos.com/1")

        assertEquals("1", photo.id)
        assertEquals("http://photos.com/1", photo.url)
        assertTrue(photo.tags.isEmpty())
    }

    @Test
    fun testComment() {
        val comment = Comment("Alice", "Beautiful photo!")

        assertEquals("Alice", comment.user)
        assertEquals("Beautiful photo!", comment.text)
    }
}
