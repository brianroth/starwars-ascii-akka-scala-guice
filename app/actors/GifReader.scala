package actors

import java.io.{InputStream, File}
import java.util.concurrent.TimeUnit
import javax.imageio.{ImageIO, ImageReader}
import actors.FrameProcessor.{FrameResult, ProcessFrame}
import actors.GifReader.{TransformResult, DoRead}
import akka.pattern.ask
import akka.actor.{ActorRef, ActorLogging, Actor}
import akka.util.Timeout
import javax.inject._
import com.sun.imageio.plugins.gif.{GIFImageReaderSpi, GIFImageReader}
import scala.concurrent.Await

import scala.concurrent.Future

@Singleton
class GifReader @Inject() (
                            @Named("frameProcessor") frameProcessor: ActorRef)
  extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5000, TimeUnit.MILLISECONDS)

  override def receive: Receive = {
    case req : DoRead => {
      //read gif
      val ir: ImageReader = new GIFImageReader(new GIFImageReaderSpi)
      ir.setInput(ImageIO.createImageInputStream(req.inputStream))

      //asynchronously transform all of the gif images into ascii art
      val processedFrameFuture = for {
        processedFrames <- Future.sequence((0 until ir.getNumImages(true)).map {
          number => frameProcessor.ask(ProcessFrame(ir.read(number), req.targetWidth, req.targetHeight)).mapTo[FrameResult]
        })
      } yield processedFrames

      sender() ! TransformResult(Await.result(processedFrameFuture, timeout.duration).map( f => f.value).toSeq)
    }
  }
}

object GifReader extends NamedActor {
  case class DoRead(inputStream: InputStream, targetWidth: Int, targetHeight: Int)
  case class TransformResult(frames: Seq[String])

  override def name: String = "gifReader"
}
