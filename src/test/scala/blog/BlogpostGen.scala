package blog

import blog.model.Blogpost
import hedgehog.{Gen, Range}

object BlogpostGen {
  def genString(from: Int, size: Int): Gen[String] =
    Gen.string(Gen.char('a', 'z'), Range.linear(from, size))

  def genTags: Gen[List[String]] =
    genString(3, 10).list(Range.linear(5, 50))

  def genAuthors: Gen[List[String]] =
    genString(3, 15).list(Range.linear(5, 10))

  def blogGen(size: Int, tags: List[String], authors: List[String]): Gen[Blogpost] = for {
    id        <- Gen.int(Range.constant(1, size))
    title     <- genString(10, 50)
    content   <- genString(140, 255)
    tagNumber <- Gen.int(Range.linear(4, tags.size))
    tagList = tags.slice(0, tagNumber)
    createdAt <- Gen.long(Range.linear(10000, 11000))
    editedAt  <- Gen.long(Range.linear(10000, 11000))
    authorNum <- Gen.int(Range.linear(0, authors.size))
    author = authors(authorNum)
  } yield Blogpost(id, title, content, tagList, createdAt, editedAt, author)

  def blogGenList(size: Int) = for {
    tags     <- genTags
    authors  <- genAuthors
    bloglist <- blogGen(size, tags, authors).list(Range.singleton(size))
  } yield bloglist

  val fixture = for {
    contents <- genString(140, 255).list(Range.linear(6, 6))
  } yield List(
    Blogpost(1, "A", contents(0), List("A", "B", "C"), 10, 11, "Az"),
    Blogpost(2, "A", contents(1), List("A", "C"), 10, 11, "Az"),
    Blogpost(3, "A", contents(2), List("B", "C"), 10, 11, "Cz"),
    Blogpost(4, "A", contents(3), List("A", "B"), 10, 11, "Fz"),
    Blogpost(5, "A", contents(4), List("B", "C"), 10, 11, "Fz"),
    Blogpost(6, "A", contents(5), List("A", "B"), 10, 11, "Ez")
  )
}
