package blog.api

import blog.model.{Blogpost, Summary}

object Api {
  def homepage(posts: List[Blogpost]): List[Summary] =
    posts.map(post => Summary(post.title, post.createdAt, post.content.take(140)))

  def facet(posts: List[Blogpost]): List[(String, Int)] =
    posts.flatMap(_.tags).groupBy(identity).mapValues(_.map(_ => 1).sum).toList

  def contributors(posts: List[Blogpost]): List[String] =
    posts.map(_.author).groupBy(identity).keys.toList.sorted
}
