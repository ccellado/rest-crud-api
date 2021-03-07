package blog

import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.generic.ProductHint

final case class Postgres(
	  url: String
	, user: String
	, password: String
	, port: String
	, database: String
	, host: String
												 )

final case class AppConf(
										postgres: Postgres
									)

class AppConfig(name: String = "application.conf") {
	implicit def productHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

	val source = ConfigSource.resources("application.conf").loadOrThrow[AppConf]
	val config = ConfigWriter[AppConf].to(source).atPath("app")
}
