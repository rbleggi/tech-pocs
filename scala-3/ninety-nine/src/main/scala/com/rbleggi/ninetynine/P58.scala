package com.rbleggi.ninetynine

// P58 (**) Generate-and-test paradigm.
// Construct all symmetric, completely balanced binary trees with a given number of nodes.

sealed trait Tree[+A]
case object End extends Tree[Nothing]
case class Node[+A](value: A, left: Tree[A] = End, right: Tree[A] = End) extends Tree[A]

object Tree {
  // Generate all completely balanced binary trees with n nodes
  def cBalanced[A](n: Int, x: A): List[Tree[A]] = {
    if (n == 0) List(End)
    else if (n % 2 == 1) {
      val sub = cBalanced((n - 1) / 2, x)
      for (l <- sub; r <- sub) yield Node(x, l, r)
    } else {
      val sub1 = cBalanced(n / 2, x)
      val sub2 = cBalanced(n / 2 - 1, x)
      (for (l <- sub1; r <- sub2) yield Node(x, l, r)) ++
      (for (l <- sub2; r <- sub1) yield Node(x, l, r))
    }
  }

  // Check if a tree is symmetric
  def isMirrorOf[A](t1: Tree[A], t2: Tree[A]): Boolean = (t1, t2) match {
    case (End, End) => true
    case (Node(_, l1, r1), Node(_, l2, r2)) => isMirrorOf(l1, r2) && isMirrorOf(r1, l2)
    case _ => false
  }

  def isSymmetric[A](tree: Tree[A]): Boolean = tree match {
    case End => true
    case Node(_, l, r) => isMirrorOf(l, r)
  }

  // Generate all symmetric, completely balanced binary trees with n nodes
  def symmetricBalancedTrees[A](n: Int, x: A): List[Tree[A]] =
    cBalanced(n, x).filter(isSymmetric)
}

@main def mainP58(): Unit = {
  val trees = Tree.symmetricBalancedTrees(5, "x")
  trees.foreach(println)
}

