package com.rbleggi.ninetynine

import scala.annotation.tailrec

object P06 {
  @tailrec
  def isPalindrome[A](items: List[A]): Boolean =
    items match
      case Nil | _ :: Nil => true
      case head :: tail
        if head == tail.last => isPalindrome(tail.init)
      case _ => false
}

@main def mainP06(): Unit =
  println("Find out whether a list is a palindrome.")
  val palindromeList = List(1, 2, 3, 2, 1)
  println(s"The list is: $palindromeList")
  val resultPalindrome = P06.isPalindrome(palindromeList)
  println(s"Is the list a palindrome? $resultPalindrome")
  println()
  val nonPalindromeList = List(1, 2, 3, 4, 5)
  println(s"The list is: $nonPalindromeList")
  val resultNonPalindrome = P06.isPalindrome(nonPalindromeList)
  println(s"Is the list a palindrome? $resultNonPalindrome")
