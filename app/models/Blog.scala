package models

import java.util.Date
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class Blog(id: Long, title: String, description: String, authorId: Long, isShown: Boolean = true)
case class BlogFormData(title: String, description: String, authorId: Long, isShown: Boolean)

object BlogForm {
  val form = Form(mapping(
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "authorId" -> longNumber,
      "isShown" -> boolean
    )(BlogFormData.apply)(BlogFormData.unapply)
  )
}

class BlogTableDef(tag: Tag) extends Table[Blog](tag, "blog") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description")
  def authorId = column[Long]("authorId")
  def isShown = column[Boolean]("isShown")
 
  override def * = (id, title, description, authorId, isShown) <> (Blog.tupled, Blog.unapply)
}

class BlogList @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
 
  var blogList = TableQuery[BlogTableDef]
 
  def add(blogItem: Blog): Future[String] = {
      println(blogItem)
    dbConfig.db
      .run(blogList += blogItem)
      .map(res => "Blog successfully added")
      .recover {
        case ex: Exception => {
            printf(ex.getMessage())
            ex.getMessage
        }
      }
  }
 
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(blogList.filter(_.id === id).delete)
  }
 
  def update(blogItem: Blog): Future[Int] = {
    dbConfig.db
      .run(blogList.filter(_.id === blogItem.id)
            .map(x => (x.title, x.description, x.authorId, x.isShown))
            .update(blogItem.title, blogItem.description, blogItem.authorId, blogItem.isShown)
      )
  }
 
  def get(id: Long): Future[Option[Blog]] = {
    dbConfig.db.run(blogList.filter(_.id === id).result.headOption)
  }
 
  def listAll: Future[Seq[Blog]] = {
    dbConfig.db.run(blogList.result)
  }
}
