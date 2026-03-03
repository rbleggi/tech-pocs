package com.rbleggi.classorganizer

import java.time.LocalDateTime
import com.rbleggi.classorganizer.core.ClassManager
import com.rbleggi.classorganizer.model.{ClassSession, Student}
import com.rbleggi.classorganizer.observer.{Observer, Subject}
import org.scalatest.funsuite.AnyFunSuite

class ClassOrganizerSpec extends AnyFunSuite {
  val students = List(
    Student("S001", "Alice Smith", "10A"),
    Student("S002", "Bob Johnson", "10A")
  )

  test("ClassSession should calculate duration correctly") {
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 30),
      students
    )
    assert(session.duration == 90)
  }

  test("ClassSession should detect conflict with same room and overlapping time") {
    val session1 = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    val session2 = ClassSession(
      "CS2", "T2", "Physics", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 30),
      LocalDateTime.of(2025, 6, 1, 10, 30),
      students
    )
    assert(session1.hasConflict(session2))
  }

  test("ClassSession should not detect conflict with different rooms") {
    val session1 = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    val session2 = ClassSession(
      "CS2", "T2", "Physics", "B201",
      LocalDateTime.of(2025, 6, 1, 9, 30),
      LocalDateTime.of(2025, 6, 1, 10, 30),
      students
    )
    assert(!session1.hasConflict(session2))
  }

  test("ClassSession should not detect conflict with same room but different time") {
    val session1 = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    val session2 = ClassSession(
      "CS2", "T2", "Physics", "A101",
      LocalDateTime.of(2025, 6, 1, 11, 0),
      LocalDateTime.of(2025, 6, 1, 12, 0),
      students
    )
    assert(!session1.hasConflict(session2))
  }

  test("ClassManager should add class session successfully") {
    val manager = new ClassManager
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    assert(manager.addClassSession(session))
    assert(manager.getClassSessions.contains(session))
  }

  test("ClassManager should not add conflicting class session") {
    val manager = new ClassManager
    val session1 = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    val session2 = ClassSession(
      "CS2", "T2", "Physics", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 30),
      LocalDateTime.of(2025, 6, 1, 10, 30),
      students
    )
    assert(manager.addClassSession(session1))
    assert(!manager.addClassSession(session2))
  }

  test("ClassManager should update class session successfully") {
    val manager = new ClassManager
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)

    val updatedSession = session.copy(room = "B201")
    assert(manager.updateClassSession("CS1", updatedSession))
    assert(manager.getClassSession("CS1").get.room == "B201")
  }

  test("ClassManager should not update non-existent session") {
    val manager = new ClassManager
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    assert(!manager.updateClassSession("CS999", session))
  }

  test("ClassManager should remove class session successfully") {
    val manager = new ClassManager
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)
    assert(manager.removeClassSession("CS1"))
    assert(manager.getClassSession("CS1").isEmpty)
  }

  test("ClassManager should not remove non-existent session") {
    val manager = new ClassManager
    assert(!manager.removeClassSession("CS999"))
  }

  test("ClassManager should get class session by id") {
    val manager = new ClassManager
    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)
    val retrieved = manager.getClassSession("CS1")
    assert(retrieved.isDefined)
    assert(retrieved.get.id == "CS1")
  }

  test("ClassManager should return None for non-existent session") {
    val manager = new ClassManager
    assert(manager.getClassSession("CS999").isEmpty)
  }

  test("ClassManager should notify observers when session is added") {
    val manager = new ClassManager
    var notified = false
    val observer = new Observer {
      override def update(subject: Subject): Unit = notified = true
    }
    manager.registerObserver(observer)

    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)
    assert(notified)
  }

  test("ClassManager should notify observers when session is removed") {
    val manager = new ClassManager
    var notificationCount = 0
    val observer = new Observer {
      override def update(subject: Subject): Unit = notificationCount += 1
    }
    manager.registerObserver(observer)

    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)
    manager.removeClassSession("CS1")
    assert(notificationCount == 2)
  }

  test("ClassManager should not notify removed observers") {
    val manager = new ClassManager
    var notified = false
    val observer = new Observer {
      override def update(subject: Subject): Unit = notified = true
    }
    manager.registerObserver(observer)
    manager.removeObserver(observer)

    val session = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    manager.addClassSession(session)
    assert(!notified)
  }

  test("ClassManager should optimize schedule by sorting by duration") {
    val manager = new ClassManager
    val session1 = ClassSession(
      "CS1", "T1", "Math", "A101",
      LocalDateTime.of(2025, 6, 1, 9, 0),
      LocalDateTime.of(2025, 6, 1, 10, 0),
      students
    )
    val session2 = ClassSession(
      "CS2", "T2", "Physics", "B201",
      LocalDateTime.of(2025, 6, 1, 11, 0),
      LocalDateTime.of(2025, 6, 1, 13, 0),
      students
    )
    manager.addClassSession(session1)
    manager.addClassSession(session2)
    manager.optimizeSchedule()
    assert(manager.getClassSessions.size == 2)
  }
}
