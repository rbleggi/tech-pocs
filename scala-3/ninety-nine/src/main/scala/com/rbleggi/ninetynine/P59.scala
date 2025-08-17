package com.rbleggi.ninetynine

// P59 (**) Construct height-balanced binary trees.
// Tree.hbalTrees(h, x): all height-balanced trees of height h with value x in all nodes.

sealed trait TreeHBal[+A]
case object EndHBal extends TreeHBal[Nothing]
case class NodeHBal[+A](value: A, left: TreeHBal[A] = EndHBal, right: TreeHBal[A] = EndHBal) extends TreeHBal[A]

object TreeHBal {
  // Height-balanced binary trees of height h
  def hbalTrees[A](h: Int, x: A): List[TreeHBal[A]] = {
    if (h == 0) List(EndHBal)
    else if (h == 1) List(NodeHBal(x))
    else {
      val trees = for {
        l <- hbalTrees(h - 1, x)
        r <- hbalTrees(h - 1, x)
      } yield NodeHBal(x, l, r)
      val treesLeft = for {
        l <- hbalTrees(h - 1, x)
        r <- hbalTrees(h - 2, x)
      } yield NodeHBal(x, l, r)
      val treesRight = for {
        l <- hbalTrees(h - 2, x)
        r <- hbalTrees(h - 1, x)
      } yield NodeHBal(x, l, r)
      trees ++ treesLeft ++ treesRight
    }
  }
}

@main def mainP59(): Unit = {
  val trees = TreeHBal.hbalTrees(3, "x")
  trees.foreach(println)
  println(s"Total trees: ${trees.size}")
}
