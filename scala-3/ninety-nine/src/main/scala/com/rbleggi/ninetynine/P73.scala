package com.rbleggi.ninetynine

case class MTreeP73[+T](value: T, children: List[MTreeP73[T]]) {
  def this(value: T) = this(value, List())

  def lispyTree: String = {
    if (children.isEmpty) value.toString
    else s"(${value.toString} ${children.map(_.lispyTree).mkString(" ")})"
  }
}

object MTreeP73 {
  def apply[T](value: T): MTreeP73[T] = new MTreeP73(value, List())
  def apply[T](value: T, children: List[MTreeP73[T]]): MTreeP73[T] = new MTreeP73(value, children)

  def fromLispyString(s: String): MTreeP73[String] = {
    val tokens = s.replace("(", " ( ").replace(")", " ) ").split(" ").filter(_.nonEmpty).toList
    def parse(tokens: List[String]): (MTreeP73[String], List[String]) = tokens match {
      case Nil => throw new IllegalArgumentException("Empty input")
      case "(" :: atom :: rest =>
        def childrenLoop(ts: List[String], acc: List[MTreeP73[String]]): (List[MTreeP73[String]], List[String]) = ts match {
          case ")" :: tail => (acc, tail)
          case Nil => throw new IllegalArgumentException("Unmatched parenthesis")
          case _ =>
            val (child, afterChild) = parse(ts)
            childrenLoop(afterChild, acc :+ child)
        }
        val (children, afterChildren) = childrenLoop(rest, Nil)
        (MTreeP73(atom, children), afterChildren)
      case atom :: rest if atom != "(" && atom != ")" => (MTreeP73(atom), rest)
      case ")" :: rest => throw new IllegalArgumentException("Unexpected closing parenthesis")
    }
    val (tree, remaining) = parse(tokens)
    if (remaining.nonEmpty) throw new IllegalArgumentException("Extra tokens after parsing")
    tree
  }

  @main def demoLispyTree(): Unit = {
    println("P73: Lisp-like tree representation.")
    val tree = MTreeP73("a", List(
      MTreeP73("f", List(MTreeP73("g"))),
      MTreeP73("c"),
      MTreeP73("b", List(MTreeP73("d"), MTreeP73("e")))
    ))
    println("Original tree: " + tree)
    val lispy = tree.lispyTree
    println("Lisp-like string: " + lispy)
    val parsed = fromLispyString(lispy)
    println("Parsed tree from Lisp-like string: " + parsed)
  }
}

