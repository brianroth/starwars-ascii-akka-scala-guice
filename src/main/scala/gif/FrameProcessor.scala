package gif

import java.awt.image.BufferedImage

import akka.actor.{ActorRef, ActorLogging, Actor}
import akkaguice.NamedActor
import com.google.inject.Inject
import com.google.inject.name.Named
import gif.FrameProcessor.{FrameResult, ProcessFrame}

import scala.collection.mutable.{StringBuilder, ListBuffer}

/**
 * Created by dgoetsch on 11/4/15.
 */
class FrameProcessor @Inject() (@Named("asciiRenderer") asciiRenderer: ActorRef) extends Actor with ActorLogging{
  override def receive: Receive = {
    case req: ProcessFrame =>
      val height: Int = req.frame.getHeight()
      val width: Int = req.frame.getWidth()
      val widthMultiplier: Int = width / req.targetWidth
      val heightMultiplier: Int = height / req.targetHeight
      val sb = new StringBuilder(req.targetHeight * req.targetWidth)
      for(y <- 0 until height by heightMultiplier) {
        for(x <- 0 until width by widthMultiplier) {
          sb.append(calculateColor(req.frame, widthMultiplier, x, heightMultiplier, y))
        }
        sb.append("\n")
      }
      sender() ! FrameResult(sb.toString())
  }

  /**
   * returns an average darkness of the colors in a given square of pixels
   * i.e. what would the greyscale value be of the set of pixes in Buffered image starting at
   * x,y and proceeding by multiplier in both directions?
   * @param frame
   * @param widthMultiplier
   * @param x
   * @param heightMultiplier
   * @param y
   * @return
   */
  def calculateColor(
                      frame: BufferedImage,
                      widthMultiplier: Int,
                      x: Int,
                      heightMultiplier: Int,
                      y: Int): String = {
    val reds = new ListBuffer[Int]
    val blues = new ListBuffer[Int]
    val greens = new ListBuffer[Int]

    for(
      curX <- x until x + widthMultiplier;
      curY <- y until y + heightMultiplier
    ) try {
      val rgb: Int = frame.getRGB(curX, curY)
      reds += rgb & 0xFF0000 >> 16
      greens += rgb & 0x00FF00 >> 8
      blues += rgb & 0x0000FF >> 0
    } catch {
      case e: Exception => {}
    }

    val redAvg: Int = reds.foldLeft(0)(_ + _) / reds.length
    val blueAvg: Int = blues.foldLeft(0)(_ + _) / blues.length
    val greenAvg: Int = greens.foldLeft(0)(_ + _) / greens.length
    val color = (greenAvg + redAvg + blueAvg)/3

    val result = color match {
      case c if c < 30 => "@"
      case c if c < 60 => "%"
      case c if c < 90 => "#"
      case c if c < 120 => "*"
      case c if c < 150 => "+"
      case c if c < 180 => "="
      case c if c < 210 => "-"
      case c if c < 240 => ":"
      case c if c < 270 => "."
      case _ => " "
    }
    result
  }
}
object FrameProcessor extends NamedActor {
  override final val name = "frameProcessor"
  case class ProcessFrame(frame: BufferedImage, targetWidth: Int, targetHeight: Int)
  case class FrameResult(value: String)
}