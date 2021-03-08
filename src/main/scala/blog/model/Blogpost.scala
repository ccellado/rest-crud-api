package blog.model

final case class Blogpost(
    id: Int,
    title: String,
    content: String,
    tags: List[String],
    createdAt: Long,
    editedAt: Long,
    author: String
)
