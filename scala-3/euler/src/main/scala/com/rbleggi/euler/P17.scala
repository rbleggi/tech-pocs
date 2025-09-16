// Project Euler Problem 17
// If the numbers 1 to 5 are written out in words: one, two, three, four, five,
// then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
// If all the numbers from 1 to 1000 (one thousand) inclusive were written out in words,
// how many letters would be used?
// NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and forty-two) contains 23 letters and 115 (one hundred and fifteen) contains 20 letters. The use of "and" when writing out numbers is in compliance with British usage.

@main def runP17(): Unit = {
  // Maps for number words
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

  // Function to convert a number to its word representation (British usage)
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

  // Calculate the total number of letters used
  val totalLetters = (1 to 1000).map(numberToWords).map(_.length).sum
  println(totalLetters)
}
