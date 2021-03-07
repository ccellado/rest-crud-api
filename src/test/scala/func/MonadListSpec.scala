package func

import hedgehog._
import hedgehog.runner._

object MonadListSpec extends Properties {

  def tests: List[Test] = List(
        property("MonadListPoint", testPoint)
      , property("MonadListMap", testMap)
      , property("MonadListApplicative", testAp)
      , property("MonadListFlatMap", testFMap)
  )

  def testPoint: Property =
    for {
      x <- Gen.int(Range.linear(-100, 100)).forAll
    } yield MonadList.point(x) ==== List(x)

  def testMap: Property = {
    val testFunc = (x: Int) => x + 1
    for {
      x <- Gen.int(Range.linear(-100, 100)).list(Range.linear(0, 100)).forAll
    } yield MonadList.map(x, testFunc) ==== x.map(testFunc)
  }

  def testAp: Property = {
    val testFunc = (x: Int) => (y: Int) => x + y
    for {
      x <- Gen.int(Range.linear(-100, 100)).list(Range.linear(0, 100)).forAll
      funcList = for (el <- x) yield testFunc(el)
    } yield MonadList.ap(funcList, x) ==== (for { el <- x; f <- funcList } yield f(el))
  }

  def testFMap: Property = {
    val testFunc = (x: String) => x.toList
    for {
      x <- Gen.string(Gen.char('a', 'z'),Range.linear(0, 100)).list(Range.linear(0, 100)).forAll
    } yield MonadList.flatMap(x, testFunc) ==== x.flatMap(testFunc)
  }


}


