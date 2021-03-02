package FuncInterfacesealed

trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](left: Tree[A], value: A, right: Tree[A]) extends Tree[A]

object TreeImpl {

  def height[A](t: Tree[A]): Int = t match {
      case Leaf => 0
      case Node(left, _, right) =>
        scala.math.max(height(left) + 1, height(right) + 1)
  }

}