package fr.robotv2.robotclan.util;

public class StringUtil {

    public static String beautify(String message) {
        return (message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase()).replaceAll("_", "");
    }
}
