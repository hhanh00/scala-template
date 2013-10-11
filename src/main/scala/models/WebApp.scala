package models

import akka.actor._
import akka.io.IO
import spray.routing.{ HttpService, RequestContext }
import spray.can.Http
import spray.http.MediaTypes
import org.fusesource.scalate._
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import spray.routing.PathMatchers

class WebApp extends Actor with HttpService {
  def actorRefFactory = context

  val route = {
    get {
      path("") {
        respondWithMediaType(MediaTypes.`text/html`) {
          complete(scalateEngine.layout("test.scaml"))
        }
      } ~
        path("js" / PathMatchers.Rest) {
          script =>
            println(script)
            getFromResource(script)
        }
    } ~
      post {
        (path("postName") & formFields('name)) {
          (name) =>
            println(name)
            complete("Submitted")
        }
      }
  }

  val scalateEngine = {
    val engine = new TemplateEngine
    engine.packagePrefix = "webTmpl"
    engine.layoutStrategy = new DefaultLayoutStrategy(engine, "layout/default.scaml")
    engine.classpath = "tmp/classes"
    engine.combinedClassPath = true
    engine.importStatements = List("import models._")
    engine
  }

  def receive = runRoute(route)
}

object WebApp extends App {
  implicit val system = ActorSystem()
  val service = system.actorOf(Props(classOf[WebApp]))
  IO(Http) ! Http.Bind(service, "localhost", port = 8180)
}