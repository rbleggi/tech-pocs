package com.rbleggi.socialmedia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SocialMediaTest {
    private User user;
    private TestFollower follower1;
    private TestFollower follower2;

    @BeforeEach
    void setUp() {
        user = new User("Alice");
        follower1 = new TestFollower("Bob");
        follower2 = new TestFollower("Charlie");
    }

    @Test
    @DisplayName("User should notify followers when photo is published")
    void user_publishPhoto_notifiesFollowers() {
        user.registerObserver(follower1);
        user.registerObserver(follower2);

        var photo = new Photo("1", "http://photo.com/1", List.of());
        user.publishPhoto(photo);

        assertEquals(1, follower1.events.size());
        assertEquals(1, follower2.events.size());
        assertInstanceOf(PhotoPublished.class, follower1.events.get(0));
    }

    @Test
    @DisplayName("User should notify followers when user is tagged")
    void user_tagUser_notifiesFollowers() {
        user.registerObserver(follower1);

        var photo = new Photo("1", "http://photo.com/1", List.of());
        user.tagUser(photo, "Charlie");

        assertEquals(1, follower1.events.size());
        assertInstanceOf(UserTagged.class, follower1.events.get(0));
        assertEquals("Charlie", ((UserTagged) follower1.events.get(0)).taggedUser());
    }

    @Test
    @DisplayName("User should notify followers when photo is commented")
    void user_commentPhoto_notifiesFollowers() {
        user.registerObserver(follower1);

        var photo = new Photo("1", "http://photo.com/1", List.of());
        var comment = new Comment("Bob", "Great photo!");
        user.commentPhoto(photo, comment);

        assertEquals(1, follower1.events.size());
        assertInstanceOf(PhotoCommented.class, follower1.events.get(0));
    }

    @Test
    @DisplayName("User should be able to register multiple observers")
    void user_registerMultipleObservers_registersAll() {
        user.registerObserver(follower1);
        user.registerObserver(follower2);

        var photo = new Photo("1", "http://photo.com/1", List.of());
        user.publishPhoto(photo);

        assertEquals(1, follower1.events.size());
        assertEquals(1, follower2.events.size());
    }

    @Test
    @DisplayName("User should be able to remove observer")
    void user_removeObserver_stopsNotifications() {
        user.registerObserver(follower1);
        user.removeObserver(follower1);

        var photo = new Photo("1", "http://photo.com/1", List.of());
        user.publishPhoto(photo);

        assertEquals(0, follower1.events.size());
    }

    @Test
    @DisplayName("Follower should handle PhotoPublished event")
    void follower_photoPublished_handlesEvent() {
        var follower = new Follower("TestFollower");
        var photo = new Photo("1", "http://photo.com/1", List.of());
        var event = new PhotoPublished(photo);
        assertDoesNotThrow(() -> follower.update(event));
    }

    @Test
    @DisplayName("Follower should handle UserTagged event")
    void follower_userTagged_handlesEvent() {
        var follower = new Follower("TestFollower");
        var photo = new Photo("1", "http://photo.com/1", List.of());
        var event = new UserTagged(photo, "SomeUser");
        assertDoesNotThrow(() -> follower.update(event));
    }

    @Test
    @DisplayName("Follower should handle PhotoCommented event")
    void follower_photoCommented_handlesEvent() {
        var follower = new Follower("TestFollower");
        var photo = new Photo("1", "http://photo.com/1", List.of());
        var comment = new Comment("User", "Nice!");
        var event = new PhotoCommented(photo, comment);
        assertDoesNotThrow(() -> follower.update(event));
    }

    @Test
    @DisplayName("Photo should hold correct data")
    void photo_holdsCorrectData() {
        var photo = new Photo("1", "http://photo.com/1", List.of("tag1", "tag2"));
        assertEquals("1", photo.id());
        assertEquals("http://photo.com/1", photo.url());
        assertEquals(2, photo.tags().size());
    }

    @Test
    @DisplayName("Comment should hold correct data")
    void comment_holdsCorrectData() {
        var comment = new Comment("User", "Great photo!");
        assertEquals("User", comment.user());
        assertEquals("Great photo!", comment.text());
    }

    private static class TestFollower implements Observer {
        private final String name;
        private final List<Event> events = new ArrayList<>();

        TestFollower(String name) {
            this.name = name;
        }

        @Override
        public void update(Event event) {
            events.add(event);
        }
    }
}
