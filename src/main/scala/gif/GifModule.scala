package gif

import akka.actor.{ActorRef, ActorSystem, Actor}
import akkaguice.GuiceAkkaActorRefProvider
import com.google.inject.name.{Named, Names}
import com.google.inject._

class GifModule extends AbstractModule with GuiceAkkaActorRefProvider {
  //override def configure(): Unit = ???
  override def configure(): Unit = {
    bindActor(FrameProcessor.name, classOf[FrameProcessor])
    bindActor(AsciiRenderer.name, classOf[AsciiRenderer])
    bindActor(GifReader.name, classOf[GifReader])
  }

  @Provides
  @Singleton
  @Named("asciiRenderer")
  def provideAsciiRendererRef(@Inject() system: ActorSystem): ActorRef = system.actorOf(propsFor(system, AsciiRenderer.name), AsciiRenderer.name)

  @Provides
  @Singleton
  @Named("frameProcessor")
  def provideFrameProcessorRef(@Inject() system: ActorSystem): ActorRef = system.actorOf(propsFor(system, FrameProcessor.name), FrameProcessor.name)

  @Provides
  @Singleton
  @Named("gifReader")
  def providegifReaderRef(@Inject() system: ActorSystem): ActorRef = system.actorOf(propsFor(system, GifReader.name), GifReader.name)

  private def bindActor[T <: Actor](name: String, actorClass: Class[T]): Unit = {
    bind(classOf[Actor]).annotatedWith(Names.named(name)).to(actorClass)
  }
}
