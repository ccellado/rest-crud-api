package blog

import blog.BlogpostGen._
import blog.api.Api
import hedgehog._
import hedgehog.runner._

object ApiSpec extends Properties {

  def tests: List[Test] = List(
    property("Facet", testFacet),
    property("Contributors", testContributors)
  )

  def testFacet: Property =
    for {
      blogs <- fixture.forAll
    } yield Api.facet(blogs) ==== List(
      ("A", 4),
      ("B", 5),
      ("C", 4)
    )

  def testContributors: Property =
    for {
      blogs <- fixture.forAll
    } yield Api.contributors(blogs) ==== List(
      "Az",
      "Cz",
      "Ez",
      "Fz"
    )

}
