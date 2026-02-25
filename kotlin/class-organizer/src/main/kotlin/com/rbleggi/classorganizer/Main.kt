package com.rbleggi.classorganizer

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
    println("Class Organizer")
}
