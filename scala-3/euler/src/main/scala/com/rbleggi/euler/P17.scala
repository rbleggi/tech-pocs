package com.rbleggi.euler

@main def runP17(): Unit = {
  val units = Map(
    1 -> "one", 2 -> "two", 3 -> "three", 4 -> "four", 5 -> "five",
    6 -> "six", 7 -> "seven", 8 -> "eight", 9 -> "nine"
  )
  val teens = Map(
    10 -> "ten", 11 -> "eleven", 12 -> "twelve", 13 -> "thirteen", 14 -> "fourteen",
    15 -> "fifteen", 16 -> "sixteen", 17 -> "seventeen", 18 -> "eighteen", 19 -> "nineteen"
  )
  val tens = Map(
    20 -> "twenty", 30 -> "thirty", 40 -> "forty", 50 -> "fifty",
    60 -> "sixty", 70 -> "seventy", 80 -> "eighty", 90 -> "ninety"
  )

  def numberToWords(n: Int): String = {
    if (n == 1000) "onethousand"
    else if (n >= 100) {
      val hundred = n / 100
      val rest = n % 100
      val hundredWord = units(hundred) + "hundred"
      if (rest == 0) hundredWord
      else hundredWord + "and" + numberToWords(rest)
    } else if (n >= 20) {
      val ten = (n / 10) * 10
      val unit = n % 10
      if (unit == 0) tens(ten)
      else tens(ten) + units(unit)
    } else if (n >= 10) teens(n)
    else if (n >= 1) units(n)
    else ""
  }

  val totalLetters = (1 to 1000).map(numberToWords).map(_.length).sum
  println(totalLetters)
}
