package pluto.helpers;

public class PlutoConsole {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String TAG_PLUTO = ANSI_CYAN + "[PLUTO]" + ANSI_RESET;
    private static final String TAG_DEV = ANSI_YELLOW + "[DEV]" + ANSI_RESET;
    private static final String TAG_ERROR = ANSI_RED + "[ERROR]" + ANSI_RESET;

    public static void log(String... messages) {
        System.out.print(TAG_PLUTO + " " + TAG_DEV + " ");
        for (String msg : messages) {
            System.out.print(msg);
        }
        System.out.println();
    }

    public static void err(String... messages) {
        System.out.print(TAG_PLUTO + " " + TAG_DEV + " " + TAG_ERROR + " ");
        for (String msg : messages) {
            System.out.print(msg);
        }
        System.out.println();
    }

    public static void msg(String msg) {
        System.out.println(TAG_PLUTO + " " + msg);
    }
}
