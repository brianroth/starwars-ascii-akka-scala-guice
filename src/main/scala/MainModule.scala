import akkaguice.AkkaModule
import com.google.inject.AbstractModule
import config.ConfigModule
import gif.GifModule

/**
 * Created by dgoetsch on 11/4/15.
 */
class MainModule extends AbstractModule {
  override def configure(): Unit = {
    install(new AkkaModule())
    install(new GifModule())
    install(new ConfigModule())
  }

}
