package modules

import actors.{GifReader, FrameProcessor}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

/**
 * Created by dgoetsch on 11/6/15.
 */
class AkkaModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[FrameProcessor](FrameProcessor.name)
    bindActor[GifReader](GifReader.name)
  }
}