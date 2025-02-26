public final class ColorString {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    private ColorString() {};

    public static void print(String str, String color) {
        if (color.toLowerCase().equals("green")) {
            System.out.print(ANSI_GREEN_BACKGROUND + str + ANSI_RESET);
        }
        else if (color.toLowerCase().equals("yellow")) {
            System.out.print(ANSI_YELLOW_BACKGROUND + str + ANSI_RESET);
        }
        else {
            System.out.print(ANSI_RED_BACKGROUND + str + ANSI_RESET);
        }
    }
}