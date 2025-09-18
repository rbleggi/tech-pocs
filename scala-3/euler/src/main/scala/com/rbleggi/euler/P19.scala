package com.rbleggi.euler

// https://projecteuler.net/problem=19
@main def mainP19(): Unit = {

  def isLeap(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

  val monthDays = Array(31,28,31,30,31,30,31,31,30,31,30,31)

  var dayOfWeek = 0
  var count = 0

  for (year <- 1900 to 2000) {
    for (month <- 0 until 12) {
      if (year >= 1901 && dayOfWeek == 6) count += 1
      val days = if (month == 1 && isLeap(year)) 29 else monthDays(month)
      dayOfWeek = (dayOfWeek + days) % 7
    }
  }
  println(s"Number of Sundays on the first of the month (1901-2000): $count")
}
