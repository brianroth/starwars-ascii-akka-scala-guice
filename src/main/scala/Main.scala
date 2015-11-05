import java.io.{File, FileInputStream, InputStream}
import java.util.concurrent.TimeUnit
import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import akkaguice.GuiceAkkaExtension
import com.google.inject.Guice
import gif.AsciiRenderer.Start
import gif.GifReader.{TransformResult, DoRead}
import gif.{AsciiRenderer, GifReader}
import scala.collection.JavaConversions._

object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5000, TimeUnit.MILLISECONDS)
  //Parse input
  val options = parseCommandLine(args.toList)
  if((options contains Options.Help) || (options isEmpty)) {
    println("usage:")
    println("\tsbt \"run --height {targetheight} --width {targetwidth} --file {gifFilePath}\"")
    println("\tex:\tsbt \"run --height 75 --width 150 --file /home/dgoetsch/Downloads/bb8.gif")
    println("\tor if you don't have sbt installed, run with activator")
    println("\tex:\t./activator \"run --height 75 --width 150 --file /home/dgoetsch/Downloads/bb8.gif")
    println("file can be any of the following:")
    new File("src/main/resources").listFiles().foreach { it => println("\t\t" + it.getName) }
    println("\t or a path to any file on your system")
    println
    println("all options have default values and are not required")
    System.exit(0)

  }
  val targetWidth : Int = options(Options.Width).toString.toInt
  val targetHeight : Int = options(Options.Height).toString.toInt
  val fileName : String = options(Options.GifFile).toString
  var is: InputStream = getClass.getResourceAsStream(fileName)
  if(is == null) {
    is = new FileInputStream(fileName)
  }
  //start application
  val injector = Guice.createInjector(new MainModule())
  implicit val system = injector.getInstance(classOf[ActorSystem])
  val gifReader = system.actorOf(propsForActor(GifReader.name))
  val asciiRenderer = system.actorOf(propsForActor(AsciiRenderer.name))
  //load gif
  val framesF = gifReader.ask(DoRead(is, targetWidth, targetHeight)).mapTo[TransformResult]
  //await gif load and play
  framesF.onSuccess {
    case transformResult =>
      asciiRenderer ! Start(transformResult.frames, fileName)
  }

  private def propsForActor(name: String)(implicit system: ActorSystem): Props = {
    GuiceAkkaExtension(system).props(name)
  }

  object Options {
    val Help = 'help
    val Unknown = 'unknown
    val Height = 'height
    val Width = 'width
    val GifFile = 'file
  }

  private def parseCommandLine(args: List[String]): Map[Symbol, Any] = {
      def nextOption(result: Map[Symbol, Any], args: List[String]): Map[Symbol, Any] = {
        args match {
          case Nil => result
          case "--height" :: height :: tail => nextOption(result ++ Map(Options.Height -> height), tail)
          case "--width" :: width :: tail => nextOption(result ++ Map(Options.Width -> width), tail)
          case "--help" :: tail => result ++ Map(Options.Help -> true)
          case "--file" :: file :: tail => nextOption(result ++ Map(Options.GifFile -> file), tail)
          case _ => result ++ Map(Options.Help -> true)
        }
      }
    def setDefaults(options : Map[Symbol, Any]): Map[Symbol, Any] = {
        var result: Map[Symbol, Any] = Map() ++ options
        if(options contains Options.Help) {
          return Map(Options.Help-> true)
        } else {
          if(!(options contains Options.Height)) result = result ++ Map(Options.Height -> 100)
          if(!(options contains Options.Width)) result = result ++ Map(Options.Width -> 100)
          if(!(options contains Options.GifFile)) result = result ++ Map(Options.GifFile -> "bb8.gif")
        }
      return result
      }

      setDefaults(nextOption(Map(), args))
    }
}
