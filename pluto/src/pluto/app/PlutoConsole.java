package pluto.app;

import io.github.cdimascio.dotenv.Dotenv;

public class PlutoConsole {
    public static String ANSI_RESET;
    public static String ANSI_RED;
    public static String ANSI_YELLOW;
    public static String ANSI_CYAN;
    private static String TAG_PLUTO;
    private static String TAG_DEV;
    private static String TAG_ERROR;
    private static boolean isDevEnv = true;

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

    public static void log(String... messages) {
        if (isDevEnv) {
            System.out.print(TAG_PLUTO + " " + TAG_DEV);
            for (String msg : messages) {
                System.out.print(" " + msg);
            }
            System.out.println();
        }
    }

    public static void taglessLog(String... messages) {
        if (isDevEnv) {
            for (String msg : messages) {
                System.out.print(msg);
            }
            System.out.println();
        }
    }

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
