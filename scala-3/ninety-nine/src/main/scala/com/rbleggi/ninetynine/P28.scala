package com.rbleggi.ninetynine

object P28 {
  def lsort[A](lists: List[List[A]]): List[List[A]] =
    lists.sortBy(_.length)

  def lsortFreq[A](lists: List[List[A]]): List[List[A]] = {
    val lengthFreq = lists.groupBy(_.length).view.mapValues(_.size).toMap
    lists.sortBy(l => lengthFreq(l.length))
  }
}

@main def mainP28(): Unit = {
  println("Sorting a list of lists according to length of sublists.")
  val lists = List(
    List("a", "b", "c"),
    List("d", "e"),
    List("f", "g", "h"),
    List("d", "e"),
    List("i", "j", "k", "l"),
    List("m", "n"),
    List("o")
  )
  println(s"Original: $lists")
  val sorted = P28.lsort(lists)
  println(s"Sorted by length: $sorted")
  val sortedFreq = P28.lsortFreq(lists)
  println(s"Sorted by length frequency: $sortedFreq")
}
