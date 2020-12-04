package pluto.app;

import io.github.cdimascio.dotenv.Dotenv;

/***
 * Singleton System.out wrapper class, used for custom console logging.
 */
public class PlutoConsole {
    public static String ANSI_RESET;
    public static String ANSI_RED;
    public static String ANSI_YELLOW;
    public static String ANSI_CYAN;
    private static String TAG_PLUTO;
    private static String TAG_DEV;
    private static String TAG_ERROR;

    /***
     * A flag for deciding whether we want to see logs while running.
     */
    private static boolean isDevEnv = false;

    /***
     * Reads environment variables and sets up the class for further work.
     * This method needs to be called before using PlutoConsole efficiently.
     */
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        boolean useColor = dotenv.get("PLUTO_CONSOLE_COLORFUL").equals("TRUE");
        isDevEnv = dotenv.get("PLUTO_ENVIRONMENT").equals("DEV");
        ANSI_RESET = useColor? "\u001B[0m" : "";
        ANSI_RED = useColor? "\u001B[31m" : "";
        ANSI_YELLOW = useColor? "\u001B[33m" : "";
        ANSI_CYAN = useColor? "\u001B[36m" : "";
        TAG_PLUTO = ANSI_CYAN + "[PLUTO]" + ANSI_RESET;
        TAG_DEV = ANSI_YELLOW + "[DEV]" + ANSI_RESET;
        TAG_ERROR = ANSI_RED + "[ERROR]" + ANSI_RESET;
    }

    /***
     * Simple logger function with Pluto trademark signal.
     *
     * @param messages vararg that accepts String messages
     */
    public static void log(String... messages) {
        if (isDevEnv) {
            System.out.print(TAG_PLUTO + " " + TAG_DEV);
            for (String msg : messages) {
                System.out.print(" " + msg);
            }
            System.out.println();
        }
    }

    /***
     * Same as log() but does not show the trademark signal.
     *
     * @param messages vararg that accepts String messages
     */
    public static void taglessLog(String... messages) {
        if (isDevEnv) {
            for (String msg : messages) {
                System.out.print(msg);
            }
            System.out.println();
        }
    }

    /***
     * Same as log() but shows an error tag as an extra.
     *
     * @param messages vararg that accepts String messages
     */
    public static void err(String... messages) {
        if (isDevEnv) {
            System.out.print(TAG_PLUTO + " " + TAG_DEV + " " + TAG_ERROR + " ");
            for (String msg : messages) {
                System.out.print(msg);
            }
            System.out.println();
        }
    }

    /***
     * Messages something to the user - this function is not dependent of the environment
     *
     * @param msg the message conveyed on stdout
     */
    public static void msg(String msg) {
        System.out.println(TAG_PLUTO + " " + msg);
    }

    /***
     * Method creates a Pluto trademark signalled string from arguments given.
     * Might be used for PlutoConsole's log later on.
     *
     * @param messages vararg that accepts String messages
     * @return concatenated String containing the "plutonized" messages
     */
    public static String createLog(String... messages) {
        if (isDevEnv) {
            String result = "";
            for (String msg : messages) {
                result += TAG_PLUTO + " " + TAG_DEV + " " + msg + "\n";
            }
            return result;
        }
        return "";
    }
}
