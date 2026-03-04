package com.rbleggi.ninetynine


sealed trait TreeSymmetric[+A] {
  def isMirrorOf[B >: A](other: TreeSymmetric[B]): Boolean = (this, other) match {
    case (EmptySymmetric, EmptySymmetric) => true
    case (NodeSymmetric(_, l1, r1), NodeSymmetric(_, l2, r2)) => l1.isMirrorOf(r2) && r1.isMirrorOf(l2)
    case _ => false
  }

  def isSymmetric: Boolean = this match {
    case EmptySymmetric => true
    case NodeSymmetric(_, l, r) => l.isMirrorOf(r)
  }
}

case object EmptySymmetric extends TreeSymmetric[Nothing]
case class NodeSymmetric[+A](value: A, left: TreeSymmetric[A] = EmptySymmetric, right: TreeSymmetric[A] = EmptySymmetric) extends TreeSymmetric[A]

@main def mainP56(): Unit = {
  val t1 = NodeSymmetric('a', NodeSymmetric('b'), NodeSymmetric('c'))
  val t2 = NodeSymmetric('a', NodeSymmetric('b', EmptySymmetric, NodeSymmetric('d')), NodeSymmetric('c', NodeSymmetric('d'), EmptySymmetric))
  val t3 = NodeSymmetric('a', NodeSymmetric('b'), NodeSymmetric('c', NodeSymmetric('d'), EmptySymmetric))

  println(s"t1 is symmetric: ${t1.isSymmetric}")
  println(s"t2 is symmetric: ${t2.isSymmetric}")
  println(s"t3 is symmetric: ${t3.isSymmetric}")
}
