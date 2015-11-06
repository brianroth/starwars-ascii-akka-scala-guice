package actors

import actors.AsciiRenderer.Start
import akka.actor.{ActorLogging, Actor}
import javax.inject._
import utils.Window

@Singleton
class AsciiRenderer @Inject() extends Actor with ActorLogging {

  override def receive: Receive = {
    case req: Start =>
      val window = new Window(req.name)
      while(true) {
        req.frames.foreach { frame =>
          window.updateText(frame)
          Thread.sleep(100)
        }
      }
  }
}
object AsciiRenderer extends NamedActor {

  case class Start(frames: Seq[String], name: String)

  override def name = "asciiRenderer"
}
