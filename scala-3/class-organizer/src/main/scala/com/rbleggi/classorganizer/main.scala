package com.rbleggi.classorganizer

import java.time.LocalDateTime
import scala.collection.mutable.{ListBuffer, Map}

package model:
  case class Student(id: String, name: String, grade: String)

  case class ClassSession(
    id: String,
    teacherId: String,
    subject: String,
    room: String,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    students: List[Student]
  ):
    def hasConflict(other: ClassSession): Boolean =
      room == other.room &&
        ((startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime)) ||
          (endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime)) ||
          (startTime.isBefore(other.startTime) && endTime.isAfter(other.endTime)) ||
          (startTime.isEqual(other.startTime) || endTime.isEqual(other.endTime)))

    def duration: Long =
      java.time.Duration.between(startTime, endTime).toMinutes

package observer:
  import com.rbleggi.classorganizer.model.ClassSession

  trait Subject:
    def registerObserver(observer: Observer): Unit
    def removeObserver(observer: Observer): Unit
    def notifyObservers(): Unit

  trait Observer:
    def update(subject: Subject): Unit

package core:
  import com.rbleggi.classorganizer.model.ClassSession
  import com.rbleggi.classorganizer.observer.{Observer, Subject}

  class ClassManager extends Subject:
    private val observers: ListBuffer[Observer] = ListBuffer()
    private val classes: Map[String, ClassSession] = Map()

    override def registerObserver(observer: Observer): Unit = observers += observer
    override def removeObserver(observer: Observer): Unit = observers -= observer
    override def notifyObservers(): Unit = observers.foreach(_.update(this))

    def addClassSession(session: ClassSession): Boolean =
      val hasConflict = classes.values.exists(_.hasConflict(session))
      if hasConflict then false
      else
        classes += (session.id -> session)
        notifyObservers()
        true

    def updateClassSession(id: String, updatedSession: ClassSession): Boolean =
      if !classes.contains(id) then return false
      val currentSession = classes(id)
      classes -= id
      val hasConflict = classes.values.exists(_.hasConflict(updatedSession))
      if hasConflict then
        classes += (id -> currentSession)
        false
      else
        classes += (updatedSession.id -> updatedSession)
        notifyObservers()
        true

    def removeClassSession(id: String): Boolean =
      if classes.contains(id) then
        classes -= id
        notifyObservers()
        true
      else false

    def getClassSessions: List[ClassSession] = classes.values.toList
    def getClassSession(id: String): Option[ClassSession] = classes.get(id)

    def optimizeSchedule(): Unit =
      val sortedClasses = classes.values.toList.sortBy(-_.duration)
      classes.clear()
      sortedClasses.foreach(addClassSession)
      notifyObservers()

package observers:
  import com.rbleggi.classorganizer.observer.{Observer, Subject}
  import com.rbleggi.classorganizer.core.ClassManager
  import com.rbleggi.classorganizer.model.ClassSession

  class ResourceMonitor extends Observer:
    private var availableRooms: Set[String] = Set("A101", "A102", "B201", "B202", "C301")
    override def update(subject: Subject): Unit = subject match
      case manager: ClassManager =>
        val sessions = manager.getClassSessions
        val occupiedRooms = sessions.map(_.room).toSet
        val currentlyAvailable = availableRooms -- occupiedRooms
        println(s"Resource Monitor Update:")
        println(s"Currently occupied rooms: ${occupiedRooms.mkString(", ")}")
        println(s"Available rooms: ${currentlyAvailable.mkString(", ")}")
      case _ => println("Unknown subject type")

  class ScheduleDisplay extends Observer:
    override def update(subject: Subject): Unit = subject match
      case manager: ClassManager =>
        val sessions = manager.getClassSessions
        println("\nCurrent Class Schedule:")
        println("======================")
        if sessions.isEmpty then println("No classes scheduled")
        else sessions.sortBy(_.startTime).foreach { session =>
          println(s"${session.subject} (ID: ${session.id})")
          println(s"  Room: ${session.room}")
          println(s"  Time: ${session.startTime} to ${session.endTime}")
          println(s"  Teacher: ${session.teacherId}")
          println(s"  Students: ${session.students.size}")
          println()
        }
      case _ => println("Unknown subject type")

  class NotificationService extends Observer:
    override def update(subject: Subject): Unit = subject match
      case manager: ClassManager =>
        println("\nNotification Service:")
        println("====================")
        println("Changes detected in class schedule.")
        println("Notifications would be sent to affected teachers and students.")
      case _ => println("Unknown subject type")

@main def run(): Unit =
  println("Class Organizer")
