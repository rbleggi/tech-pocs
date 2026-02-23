package com.rbleggi.socialmediaphotoapp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Social Media Photo App");
    }
}

interface Observer {
    void update(Event event);
}

interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Event event);
}

record Photo(String id, String url, List<String> tags) {}

record Comment(String user, String text) {}

sealed interface Event permits PhotoPublished, UserTagged, PhotoCommented {}

record PhotoPublished(Photo photo) implements Event {}

record UserTagged(Photo photo, String taggedUser) implements Event {}

record PhotoCommented(Photo photo, Comment comment) implements Event {}

class User implements Subject {
    private final String name;
    private final List<Observer> observers = new ArrayList<>();
    private final List<Photo> timeline = new ArrayList<>();

    User(String name) {
        this.name = name;
    }

    void publishPhoto(Photo photo) {
        timeline.add(0, photo);
        notifyObservers(new PhotoPublished(photo));
    }

    void tagUser(Photo photo, String user) {
        notifyObservers(new UserTagged(photo, user));
    }

    void commentPhoto(Photo photo, Comment comment) {
        notifyObservers(new PhotoCommented(photo, comment));
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(0, observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observers.forEach(o -> o.update(event));
    }
}

class Follower implements Observer {
    private final String followerName;

    Follower(String followerName) {
        this.followerName = followerName;
    }

    @Override
    public void update(Event event) {
        switch (event) {
            case PhotoPublished e -> System.out.println("[" + followerName + "] New photo published: " + e.photo().url());
            case UserTagged e -> System.out.println("[" + followerName + "] " + e.taggedUser() + " was tagged in photo: " + e.photo().url());
            case PhotoCommented e -> System.out.println("[" + followerName + "] New comment by " + e.comment().user() + ": " + e.comment().text());
        }
    }
}
