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

public class Main {
    public static void main(String[] args) {
        var teacherA = new Teacher("Alice");
        var teacherB = new Teacher("Bob");
        var schedule = new Schedule();
        schedule.registerObserver(teacherA);
        schedule.registerObserver(teacherB);

        var session1 = new ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA);
        var session2 = new ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacherA);
        var session3 = new ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherB);

        System.out.println("Adding sessions...");
        schedule.addClassSession(session1);
        schedule.addClassSession(session2);
        schedule.addClassSession(session3);

        System.out.println("Trying to add overlapping session for Alice...");
        var overlapSession = new ClassSession("4", "Biology", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacherA);
        boolean added = schedule.addClassSession(overlapSession);
        System.out.println("Overlapping session added: " + added);

        System.out.println("Optimizing schedule...");
        schedule.optimizeSchedule();

        System.out.println("Removing a session...");
        schedule.removeClassSession(session1);
    }
}
