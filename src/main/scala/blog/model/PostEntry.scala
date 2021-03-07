package blog.model

final case class PostEntry (
														 title: String
														 , content: String
														 , tags: List[String]
														 , author: String
													 )