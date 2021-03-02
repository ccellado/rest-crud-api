package FuncInterface

import FuncInterfacesealed.{Leaf, Node, Tree, TreeImpl}
import hedgehog._
import hedgehog.runner._

object TreeSpec extends Properties {

  def tests: List[Test] = List(
        example("TreeHeight", testPoint)
  )

  val nodeVal = (x: Int) => Node(Leaf, x, Leaf)

  val tree =
    Node(
      Node(
        Node(nodeVal(90), 70, nodeVal(66)),
        50,
        Node(nodeVal(44), 35, nodeVal(31))),
      25,
      Node(
        Node(nodeVal(24), 22, nodeVal(18)),
        15,
        Node(nodeVal(12), 10, nodeVal(4)))
    )

  def testPoint: Result = {
    val size = 4
    TreeImpl.height(tree) ==== size
  }
}


