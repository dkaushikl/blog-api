package services
 
import com.google.inject.Inject
import models.{Blog, BlogList}
 
import scala.concurrent.Future
 
class BlogService @Inject() (items: BlogList) {
 
  def addItem(item: Blog): Future[String] = {
    items.add(item)
  }
 
  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }
 
  def updateItem(item: Blog): Future[Int] = {
    items.update(item)
  }
 
  def getItem(id: Long): Future[Option[Blog]] = {
    items.get(id)
  }
 
  def listAllItems: Future[Seq[Blog]] = {
    items.listAll
  }
}
