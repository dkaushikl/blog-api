package controllers.api
 
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Blog, BlogForm}
import play.api.data.FormError

import services.BlogService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
 import java.lang.ProcessBuilder.Redirect
 
class BlogController @Inject()(
    cc: ControllerComponents,
    blogService: BlogService
) extends AbstractController(cc) {
 
    implicit val blogFormat = Json.format[Blog]
 
    def getAll = Action.async { implicit request: Request[AnyContent] =>
        blogService.listAllItems map { item => 
        Ok(Json.toJson(item))}
    }

    def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        blogService.getItem(id) map { item =>
          Ok(Json.toJson(item))
        }
      }

    def add() = Action.async { implicit request: Request[AnyContent] => 
        BlogForm.form.bindFromRequest.fold(
            errorForm => {
                errorForm.errors.foreach(println)
                Future.successful(BadRequest("Error!"))
          },
          data => {
            val newBlogItem = Blog(0, data.title, data.description, data.authorId, data.isShown)
            blogService.addItem(newBlogItem).map( _ => Ok("Insert Successfully"))
          }
        )
    }

    def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        BlogForm.form.bindFromRequest.fold(
            errorForm => {
                errorForm.errors.foreach(println)
                Future.successful(BadRequest("Error!"))
            },
            data => {
                val blogItem = Blog(id, data.title, data.description, data.authorId, data.isShown)
                blogService.updateItem(blogItem).map(_ => Ok("Update Successfully"))
            }
        )
    }

    def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        blogService.deleteItem(id) map { res => 
            Ok("Delete Successfully")}
    }
}
