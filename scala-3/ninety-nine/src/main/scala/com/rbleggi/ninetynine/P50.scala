package com.rbleggi.ninetynine


object P50 {
  sealed trait Node {
    def freq: Int
  }
  case class Leaf(symbol: String, freq: Int) extends Node
  case class Internal(left: Node, right: Node, freq: Int) extends Node

  def huffman(freqs: List[(String, Int)]): List[(String, String)] = {
    var nodes: List[Node] = freqs.map { case (s, f) => Leaf(s, f) }
    while (nodes.length > 1) {
      val sorted = nodes.sortBy(_.freq)
      val left = sorted.head
      val right = sorted.tail.head
      val merged = Internal(left, right, left.freq + right.freq)
      nodes = merged :: sorted.drop(2)
    }
    def assignCodes(node: Node, prefix: String): List[(String, String)] = node match {
      case Leaf(s, _) => List((s, prefix))
      case Internal(l, r, _) =>
        assignCodes(l, prefix + "0") ++ assignCodes(r, prefix + "1")
    }
    assignCodes(nodes.head, "")
  }

  @main def mainP50(): Unit = {
    println("Huffman code for example input:")
    val input = List(("a", 45), ("b", 13), ("c", 12), ("d", 16), ("e", 9), ("f", 5))
    val result = P50.huffman(input)
    result.foreach { case (sym, code) => println(s"$sym: $code") }
  }
}
