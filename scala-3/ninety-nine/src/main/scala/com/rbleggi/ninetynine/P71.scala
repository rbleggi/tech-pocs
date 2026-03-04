package com.rbleggi.ninetynine

case class MTreeP71[+T](value: T, children: List[MTreeP71[T]]) {
  def this(value: T) = this(value, List())

  def internalPathLength: Int = {
    def helper(tree: MTreeP71[T], depth: Int): Int = {
      depth + tree.children.map(child => helper(child, depth + 1)).sum
    }
    helper(this, 0)
  }
}

object MTreeP71 {
  def apply[T](value: T): MTreeP71[T] = new MTreeP71(value, List())
  def apply[T](value: T, children: List[MTreeP71[T]]): MTreeP71[T] = new MTreeP71(value, children)

  @main def demoInternalPathLength(): Unit = {
    val tree = MTreeP71('a', List(
      MTreeP71('f', List(MTreeP71('g'))),
      MTreeP71('c'),
      MTreeP71('b', List(MTreeP71('d'), MTreeP71('e')))
    ))
    println(s"Internal path length: ${tree.internalPathLength}") // Should print 9
  }
}

