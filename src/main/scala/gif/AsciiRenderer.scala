package gif

import akka.actor.{ActorLogging, Actor}
import akkaguice.NamedActor
import com.google.inject.Inject
import gif.AsciiRenderer.Start
import gif.util.{Window, ConsoleUtils}

/**
 * Created by dgoetsch on 11/4/15.
 */
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
  override final val name: String = "asciiRenderer"
  case class Start(frames: Seq[String], name: String)
}
