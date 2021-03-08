package blog.api

import blog.model.{Blogpost, PostEntry}
import io.getquill.{Ord, PostgresJAsyncContext, SnakeCase}

import scala.compat.java8.FutureConverters.CompletionStageOps
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

trait RestApi {
  implicit val ex: ExecutionContext
  val ctx: PostgresJAsyncContext[SnakeCase.type]

  def fetchBlogpost(needle: Int): Future[ctx.RunQueryResult[Blogpost]] = {
    import ctx._
    ctx.transaction { implicit ex =>
      ctx.run(query[Blogpost].filter(b => b.id == lift(needle)))
    }.toScala
  }

  def parseKeyOrder(
      key: Option[String] = Some("createdat"),
      order: Option[String] = Some("desc")
  ) = {
    import ctx._
    val q   = quote(query[Blogpost])
    val res = (key, order) match {
      case (Some("asc"), Some("editedat"))   => quote(q.sortBy(_.id)(Ord.asc).sortBy(_.editedAt))
      case (Some("asc"), Some("createdat"))  => quote(q.sortBy(_.id)(Ord.asc).sortBy(_.createdAt))
      case (Some("desc"), Some("createdat")) => quote(q.sortBy(_.id)(Ord.desc).sortBy(_.createdAt))
      case (Some("desc"), Some("editedat"))  => quote(q.sortBy(_.id)(Ord.desc).sortBy(_.editedAt))
      case _                                 => q
    }
    ctx.transaction { implicit ex =>
      ctx.run(res)
    }.toScala
  }

  def getLatestId: Int = {
    import ctx._
    val f = ctx.transaction { implicit ex =>
      ctx.run(query[Blogpost].map(_.id).max)
    }.toScala
    Await.result(f, 5.seconds).headOption.getOrElse(0)
  }

  def insertPost(id: Int, post: PostEntry) = {
    import ctx._
    val time = System.currentTimeMillis
    val res  = Blogpost(id + 1, post.title, post.content, post.tags, time, time, post.author)

    ctx.transaction { implicit ex =>
      ctx.run(query[Blogpost].insert(lift(res)))
    }.toScala
  }

  def updatePost(id: Int, post: PostEntry) = {
    import ctx._
    val time = System.currentTimeMillis
    ctx.transaction { implicit ex =>
      ctx.run(
        query[Blogpost]
          .filter(_.id == lift(id))
          .update(
            _.title    -> lift(post.title),
            _.content  -> lift(post.content),
            _.tags     -> lift(post.tags),
            _.author   -> lift(post.author),
            _.editedAt -> lift(time)
          )
      )
    }.toScala
  }

  def deletePost(id: Int) = {
    import ctx._
    ctx.transaction { implicit ex =>
      ctx.run(query[Blogpost].filter(_.id == lift(id)).delete)
    }.toScala
  }
}
