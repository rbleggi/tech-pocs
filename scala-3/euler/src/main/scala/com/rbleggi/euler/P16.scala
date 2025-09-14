// Project Euler Problem 16
// What is the sum of the digits of the number 2^1000?

@main def runP16(): Unit = {

  val number = BigInt(2).pow(1000)

  val digitSum = number.toString.map(_.asDigit).sum

  println(s"The sum of the digits of 2^1000 is: $digitSum")
}

