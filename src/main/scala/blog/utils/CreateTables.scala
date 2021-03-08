package blog.utils

import java.sql.DriverManager

object CreateTables extends App {
  val createBlogposts =
    """CREATE TABLE IF NOT EXISTS public.blogpost (
      |  id INTEGER NOT NULL
      |, title TEXT NOT NULL
      |, content TEXT NOT NULL
      |, tags TEXT[]
      |, created_at BIGINT NOT NULL
      |, edited_at BIGINT NOT NULL
      |, author TEXT NOT NULL
      |)""".stripMargin

  val createSummaries =
    """CREATE TABLE IF NOT EXISTS public.summary (
      |  title TEXT NOT NULL
      |, date BIGINT NOT NULL
      |, snippet TEXT NOT NULL
      |)""".stripMargin

  Class.forName("org.postgresql.Driver").newInstance

  def connect() = {
    DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "postgres")
  }

  def generate(statement: String) = {
    val db = connect()
    val st = db.createStatement
    val c1 = st.executeUpdate(statement)
    println(c1)
  }

  generate(createBlogposts)
  generate(createSummaries)
}
