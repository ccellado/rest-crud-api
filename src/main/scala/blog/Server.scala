package blog

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, concat, get, onSuccess, path, pathPrefix, _}
import blog.api.RestApi
import blog.model.{Blogpost, PostEntry, Summary}
import com.typesafe.config.Config
import io.getquill.{SnakeCase, _}
import spray.json.DefaultJsonProtocol._
import org.slf4j.{LoggerFactory, Logger}

import scala.io.AnsiColor._
import scala.io.StdIn

object Server extends RestApi {
	val log: Logger = LoggerFactory.getLogger(this.getClass)
	implicit val system = ActorSystem(Behaviors.empty, "rest-api-crud")
	implicit val ex = system.executionContext
	lazy val ctx = new PostgresJAsyncContext(SnakeCase, config.getConfig("app.postgres"))
	val config: Config = (new AppConfig).config

	def main(args: Array[String]): Unit = {
		implicit val blogpostFormat = jsonFormat7(Blogpost)
		implicit val postEntryFormat = jsonFormat4(PostEntry)
		implicit val summaryFormat = jsonFormat3(Summary)

		val route =
			concat(
				get {
					pathPrefix("blog" / IntNumber) { id =>
						val maybeItem = fetchBlogpost(id)
						onSuccess(maybeItem) {
							case item => complete(item.head)
							case Seq() => complete(StatusCodes.NotFound)
						}
					}
				},
				get {
					path("blog") {
						parameters("key".optional, "order".optional) {
							(key, order) =>
								val maybeItem = parseKeyOrder(key, order)
								onSuccess(maybeItem) {
									case Seq() => complete(StatusCodes.NotFound)
									case item => complete(item)
								}
							}
						}
					},
				post {
					path("blog") {
						entity(as[PostEntry]) { entry =>
							val id = getLatestId
							val postItem = insertPost(id, entry)
							onSuccess(postItem) {
								case 1 => complete(s"Successfully added ${entry.title} post.")
								case 0 => complete(s"Could not create new post. Check all required fields. HINT: title,content,tags[],author")
							}
						}
					}
				},
				put {
						pathPrefix("blog" / IntNumber) { id =>
							entity(as[PostEntry]) { entry =>
								val postItem = updatePost(id, entry)
								onSuccess(postItem) {
									case 1 => complete(s"Successfully updated ${entry.title} post at ${id}.")
									case 0 => complete(s"Could not update post. Maybe it does not exist?")
								}
							}
						}
				},
				delete {
					pathPrefix("blog" / IntNumber) { id =>
							val res = deletePost(id)
							onSuccess(res) {
								case 1 => complete(s"Successfully removed post at ${id}.")
								case 0 => complete(s"Could not delete post. Try another id.")
							}
					}
				}
			)


		val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

		println(s"-----------------------------------------------------------------${YELLOW}")
		println("""        _
							|       /_/_      .'''.
							|    =O(_)))) ...'     `.
							|       \_\         colby`.    .'''
							|                          `..'cellador
							|""".stripMargin)
		print(s"${RESET}")
		println(s"Server ${GREEN}online${RESET} at http://localhost:8080/\nPress RETURN to stop...")
		println("-----------------------------------------------------------------")

		StdIn.readLine() // let it run until user presses return
		bindingFuture
			.flatMap(_.unbind()) // trigger unbinding from the port
			.onComplete(_ => system.terminate()) // and shutdown when done
	}
}
