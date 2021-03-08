package blog

import blog.BlogpostGen._
import blog.model.Blogpost

import scala.compat.java8.FutureConverters.CompletionStageOps

object PopulateTables extends App {
  val size = hedgehog.Size.apply(scala.util.Random.nextInt())
  val seed = hedgehog.core.Seed.fromLong(scala.util.Random.nextLong())
  import blog.Server.{ctx, ex}

  val blogpostsGen = blogGenList(110).run(size, seed).value._2.get.distinctBy(_.id)
  import ctx._

  val insertQ = quote {
    liftQuery(blogpostsGen).foreach(e => query[Blogpost].insert(e))
  }

  ctx
    .transaction { implicit ex =>
      ctx.run(insertQ)
    }
    .toScala
    .onComplete { x =>
      println(x)
      println("Done!")
    }
}
