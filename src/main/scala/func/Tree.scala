package func

trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](left: Tree[A], value: A, right: Tree[A]) extends Tree[A]

object TreeImpl {

  def height[A](t: Tree[A]): Int = {
    def traverse[A](t: Tree[A]): Int = t match {
      case Leaf                 => 0
      case Node(left, _, right) =>
        scala.math.max(traverse(left) + 1, traverse(right) + 1)
    }
    traverse(t) - 1
  }

  def preOrder[A](t: Tree[A]): List[A] = t match {
    case Leaf                 => List()
    case Node(left, x, right) => List(x) ::: preOrder(left) ::: preOrder(right)
  }

  def inOrder[A](t: Tree[A]): List[A] = t match {
    case Leaf                 => List()
    case Node(left, x, right) => inOrder(left) ::: List(x) ::: inOrder(right)
  }

  def postOrder[A](t: Tree[A]): List[A] = t match {
    case Leaf                 => List()
    case Node(left, x, right) => postOrder(left) ::: postOrder(right) ::: List(x)
  }

}
