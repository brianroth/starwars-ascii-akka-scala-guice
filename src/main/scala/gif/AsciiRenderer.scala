package gif

import akka.actor.{ActorLogging, Actor}
import akkaguice.NamedActor
import com.google.inject.Inject
import gif.AsciiRenderer.Start
import nerdery.jvm.challenge.starwars.ascii.ConsoleUtils

/**
 * Created by dgoetsch on 11/4/15.
 */
class AsciiRenderer @Inject() extends Actor with ActorLogging {

  override def receive: Receive = {
    case req: Start =>
      while(true) {
        req.frames.foreach { frame =>
          ConsoleUtils.clearConsole()
          print(frame)
          System.out.flush()
          Thread.sleep(100)
        }
      }
  }
}
object AsciiRenderer extends NamedActor {
  override final val name: String = "asciiRenderer"
  case class Start(frames: Seq[String])
}
