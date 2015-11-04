package nerdery.jvm.challenge.starwars.ascii;

/**
 * Created by dgoetsch on 11/4/15.
 */
public class ConsoleUtils {
    public final static void clearConsole()
    {
        try
        {
            System.out.print(String.format("\033[2J"));
            System.out.flush();
        }
        catch (final Exception e)
        {
            System.out.println(e);
        }
    }
}
