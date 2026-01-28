package com.rbleggi.classorganizer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface Observer {
    void update(Schedule schedule);
}

interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

record ClassSession(
    String id,
    String subject,
    LocalDateTime start,
    LocalDateTime end,
    Teacher teacher
) {}

class Teacher implements Observer {
    private final String name;

    public Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(Schedule schedule) {
        System.out.println("Teacher " + name + " notified of schedule change. Current sessions:");
        schedule.getClassSessions().stream()
            .filter(session -> session.teacher() == this)
            .forEach(session -> System.out.println("- " + session.subject() + " from " + session.start() + " to " + session.end()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(name, teacher.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

class Schedule implements Subject {
    private final List<ClassSession> classSessions = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    public List<ClassSession> getClassSessions() {
        return List.copyOf(classSessions);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    public boolean addClassSession(ClassSession session) {
        boolean conflict = classSessions.stream()
            .anyMatch(existing ->
                existing.teacher() == session.teacher() &&
                session.start().isBefore(existing.end()) &&
                session.end().isAfter(existing.start())
            );
        if (conflict) return false;
        classSessions.add(session);
        notifyObservers();
        return true;
    }

    public boolean removeClassSession(ClassSession session) {
        boolean removed = classSessions.remove(session);
        if (removed) notifyObservers();
        return removed;
    }

    public void optimizeSchedule() {
        System.out.println("Optimizing schedule...");
        notifyObservers();
    }
}

public class ClassOrganizerDemo {
    public static void main(String[] args) {
        System.out.println("Class Organizer Demo");
    }
}
