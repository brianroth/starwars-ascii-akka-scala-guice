package controllers

import java.io.{FileInputStream, InputStream}
import java.util.concurrent.TimeUnit
import javax.inject.{Named, Inject, Singleton}

import actors.GifReader.{TransformResult, DoRead}
import akka.actor.ActorRef
import akka.util.Timeout
import play.api.Play
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import akka.pattern.ask
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.Await

@Singleton
class EvilController @Inject() (@Named("gifReader") gifReader: ActorRef,
                                 @Named("asciiRenderer") asciiRenderer: ActorRef) extends Controller {
  val GIFHTML = "<script type='text/javascript' src=\"/assets/javascript/render.js\"></script><div id=\"gifBody\"></div><div id=\"gifData\" hidden>%PLACEHOLDER%</div>"
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5000, TimeUnit.MILLISECONDS)
  val displayRequest: Form[DisplayRequest] = Form(
    mapping(
      "name" -> text,
      "width" -> number,
      "height" -> number
    )(DisplayRequest.apply)(DisplayRequest.unapply)
  )
  def index = Action {
    Ok(views.html.index(displayRequest))
  }

  def showGif = Action(parse.form(displayRequest)) { request =>
    val displayRequest = request.body
    Ok(views.html.gif(Json.toJson(addGif(displayRequest.name, displayRequest.width, displayRequest.height)).toString()))
    //Ok(views.html(GIFHTML.replace("%PLACEHOLDER%", .toString())))
//    var height: Int = if(request.queryString.get("height").isEmpty) 100 else request.queryString("height")(0).toInt
//    var width: Int = if(request.queryString.get("width").isEmpty) 400 else request.queryString("width")(0).toInt
//    var gifToUse = if(request.queryString.get("gif").isEmpty) "bb8" else request.queryString("gif")(0)

    //Ok(Json.toJson(addGif(gifToUse, width, height)))
  }

  def addGif(fileName: String, targetWidth: Int, targetHeight: Int): Seq[String] = {
    var qualifiedName = if(fileName.startsWith("public/")) fileName else "public/" + fileName
    qualifiedName = if(qualifiedName.endsWith(".gif")) qualifiedName else qualifiedName + ".gif"
    var is: InputStream = Play.classloader.getResourceAsStream(qualifiedName)
    if(is == null) {
      is = new FileInputStream(fileName)
    }
    val framesF = gifReader.ask(DoRead(is, targetWidth, targetHeight)).mapTo[TransformResult]
    //await frames
    Await.result(framesF, timeout.duration).frames
  }


}
case class DisplayRequest(name: String, width: Int, height: Int)