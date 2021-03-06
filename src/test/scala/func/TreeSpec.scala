package func

import func.{Leaf, Node, Tree, TreeImpl}
import hedgehog._
import hedgehog.runner._

object TreeSpec extends Properties {

  def tests: List[Test] = List(
        example("TreeHeight", testPoint)
      , example("TreePreOrder", testPreOrder)
      , example("TreeInOrder", testInOrder)
      , example("TreePostOrder", testPostOrder)
  )

  val nodeVal = (x: Int) => Node(Leaf, x, Leaf)

  val tree =
    Node(
        Node(
          Node(nodeVal(4), 10, nodeVal(12)),
          15,
          Node(nodeVal(18), 22, nodeVal(24))),
      25,
        Node(
          Node(nodeVal(31), 35, nodeVal(44)),
          50,
          Node(nodeVal(66), 70, nodeVal(90)))
  )

  def testPoint: Result = {
    val size = 3
    TreeImpl.height(tree) ==== size
  }

  def testPreOrder: Result = {
    val res = List(25, 15, 10, 4, 12, 22, 18, 24, 50, 35, 31, 44, 70, 66, 90)
    TreeImpl.preOrder(tree) ==== res
  }

  def testInOrder: Result = {
    val res = List(4, 10, 12, 15, 18, 22, 24, 25, 31, 35, 44, 50, 66, 70, 90)
    TreeImpl.inOrder(tree) ==== res
  }

  def testPostOrder: Result = {
    val res = List(4, 12, 10, 18, 24, 22, 15, 31, 44, 35, 66, 90, 70, 50, 25)
    TreeImpl.postOrder(tree) ==== res
  }
}


