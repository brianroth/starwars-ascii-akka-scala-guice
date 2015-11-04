package gif.util;

/**
 * Created by dgoetsch on 11/4/15.
 */
public class ConsoleUtils {

    /**
     * This doesn't really work very well.
     *
     * At first I tried to run clear/cls (UNIX/Windows) via Runtime,
     * but it didn't work for reasons unknown as of yet.
     */
    public final static void clearConsole()
    {
        //This does not work
//        try
//        {
//            final String os = System.getProperty("os.name");
//
//            if (os.contains("Windows"))
//            {
//                Runtime.getRuntime().exec("cls");
//            }
//            else
//            {
//                Runtime.getRuntime().exec("clear");
//            }
//        }
        //this is not an actual solution
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
