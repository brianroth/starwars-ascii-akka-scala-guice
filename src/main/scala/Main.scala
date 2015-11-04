import java.io.{FileInputStream, InputStream}
import java.util.concurrent.TimeUnit
import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import akkaguice.GuiceAkkaExtension
import com.google.inject.Guice
import gif.GifReader.DoRead
import gif.GifReader

object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5000, TimeUnit.MILLISECONDS)
  //Parse input
  var targetWidth = 100
  try {
    targetWidth = args(0).toInt
  } catch {
    case ex: Exception =>
      println("Usage: sbt \"run {required:integer:targetWidth} {optional:string:file}\"")
      println("{required:integer:targetWidth} - must be supplied and must be a positive Integer")
      println("{optional:string:file} - can be an absolute file path or one of the following:")
      println("\tbb8.gif\n\tdancetroopers.gif\n\tls-fight.gif\n\tmillenium-falcon.gif\n\tstormtrooper.gif\n")
      System.exit(0)
  }
  var fileName = "src/main/resources/stormtrooper.gif"
  try {
    fileName = args(1).toString
  } catch {
    case ex: ArrayIndexOutOfBoundsException =>
      println("using default gif file")
  }
  var is: InputStream = getClass.getResourceAsStream(fileName)
  if(is == null) {
    is = new FileInputStream(fileName)
  }
  //start application
  val injector = Guice.createInjector(new MainModule())
  implicit val system = injector.getInstance(classOf[ActorSystem])
  val gifReader = system.actorOf(propsForActor(GifReader.name))
  //load gif and play
  gifReader ! (DoRead(is, targetWidth))

  private def propsForActor(name: String)(implicit system: ActorSystem): Props = {
    GuiceAkkaExtension(system).props(name)
  }
}
