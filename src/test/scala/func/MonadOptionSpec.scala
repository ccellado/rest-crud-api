package func

import hedgehog._
import hedgehog.runner._

object MonadOptionSpec extends Properties {

  def tests: List[Test] = List(
    property("MonadOptionPoint", testPoint),
    property("MonadOptionmap", testMap),
    property("MonadOptionApplicative", testAp),
    property("MonadOptionFlatMap", testFMap)
  )

  def testPoint: Property =
    for {
      x <- Gen.int(Range.linear(-100, 100)).forAll
    } yield MonadOption.point(x) ==== Some(x)

  def testMap: Property = {
    val testFunc = (x: Int) => x + 1
    for {
      x <- Gen.int(Range.linear(-100, 100)).forAll
    } yield MonadOption.map(Some(x), testFunc) ==== Some(testFunc(x))
  }

  def testAp: Property = {
    val testFunc = (x: Int) => x + 1
    for {
      x <- Gen.int(Range.linear(-100, 100)).forAll
    } yield MonadOption.ap(Some(testFunc), Some(x)) ==== Some(testFunc(x))
  }

  def testFMap: Property = {
    val testFunc = (x: Int) => if (x > 10) Some(x + 1) else None
    for {
      x <- Gen.int(Range.linear(-100, 100)).forAll
    } yield MonadOption.flatMap(Some(x), testFunc) ==== testFunc(x)
  }
}
