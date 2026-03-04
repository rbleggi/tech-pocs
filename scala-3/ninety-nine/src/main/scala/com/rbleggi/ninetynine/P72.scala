package com.rbleggi.ninetynine

case class MTreeP72[+T](value: T, children: List[MTreeP72[T]]) {
  def this(value: T) = this(value, List())

  def postorder: List[T] = {
    children.flatMap(_.postorder) :+ value
  }
}

object MTreeP72 {
  def apply[T](value: T): MTreeP72[T] = new MTreeP72(value, List())
  def apply[T](value: T, children: List[MTreeP72[T]]): MTreeP72[T] = new MTreeP72(value, children)

  def fromString(s: String): MTreeP72[Char] = {
    def parse(chars: List[Char]): (MTreeP72[Char], List[Char]) = chars match {
      case Nil => throw new IllegalArgumentException("Empty string")
      case c :: rest if c == '^' => throw new IllegalArgumentException("Unexpected '^' at root")
      case c :: rest =>
        def childrenLoop(cs: List[Char], acc: List[MTreeP72[Char]]): (List[MTreeP72[Char]], List[Char]) = cs match {
          case Nil => (acc, Nil)
          case '^' :: tail => (acc, tail)
          case _ =>
            val (child, afterChild) = parse(cs)
            childrenLoop(afterChild, acc :+ child)
        }
        val (children, afterChildren) = childrenLoop(rest, Nil)
        (MTreeP72(c, children), afterChildren)
    }
    parse(s.toList)._1
  }

  @main def demoPostorder(): Unit = {
    println("P72: Construct the postorder sequence of the tree nodes.")
    val treeStr = "afg^^c^bd^e^^^"
    println(s"Input tree string: $treeStr")
    val tree = fromString(treeStr)
    println(s"Multiway tree: $tree")
    val post = tree.postorder
    println(s"Postorder sequence: $post") // Should print List(g, f, c, d, e, b, a)
  }
}

