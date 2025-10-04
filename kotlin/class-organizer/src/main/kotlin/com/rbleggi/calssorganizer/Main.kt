package com.rbleggi.calssorganizer

import java.time.LocalDateTime

interface Observer {
    fun update(schedule: Schedule)
}

interface Subject {
    fun registerObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun notifyObservers()
}

data class ClassSession(
    val id: String,
    val subject: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val teacher: Teacher
)

class Teacher(val name: String) : Observer {
    override fun update(schedule: Schedule) {
        println("Teacher $name notified of schedule change. Current sessions:")
        schedule.classSessions.filter { it.teacher == this }.forEach {
            println("- ${it.subject} from ${it.start} to ${it.end}")
        }
    }
}

class Schedule : Subject {
    val classSessions = mutableListOf<ClassSession>()
    private val observers = mutableListOf<Observer>()

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach { it.update(this) }
    }

    fun addClassSession(session: ClassSession): Boolean {
        val conflict = classSessions.any {
            it.teacher == session.teacher &&
            (session.start < it.end && session.end > it.start)
        }
        if (conflict) return false
        classSessions.add(session)
        notifyObservers()
        return true
    }

    fun removeClassSession(session: ClassSession): Boolean {
        val removed = classSessions.remove(session)
        if (removed) notifyObservers()
        return removed
    }

    fun optimizeSchedule() {
        println("Optimizing schedule...")
        notifyObservers()
    }
}

fun main() {
    val teacherA = Teacher("Alice")
    val teacherB = Teacher("Bob")
    val schedule = Schedule()
    schedule.registerObserver(teacherA)
    schedule.registerObserver(teacherB)

    val session1 = ClassSession("1", "Math", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherA)
    val session2 = ClassSession("2", "Physics", LocalDateTime.of(2025, 10, 4, 10, 0), LocalDateTime.of(2025, 10, 4, 11, 0), teacherA)
    val session3 = ClassSession("3", "Chemistry", LocalDateTime.of(2025, 10, 4, 9, 0), LocalDateTime.of(2025, 10, 4, 10, 0), teacherB)

    println("Adding sessions...")
    schedule.addClassSession(session1)
    schedule.addClassSession(session2)
    schedule.addClassSession(session3)

    println("Trying to add overlapping session for Alice...")
    val overlapSession = ClassSession("4", "Biology", LocalDateTime.of(2025, 10, 4, 9, 30), LocalDateTime.of(2025, 10, 4, 10, 30), teacherA)
    val added = schedule.addClassSession(overlapSession)
    println("Overlapping session added: $added")

    println("Optimizing schedule...")
    schedule.optimizeSchedule()

    println("Removing a session...")
    schedule.removeClassSession(session1)
}
