package dev.mayuna.nostalgiadiscord.utils;

public class Utils {

    public static String formatTime(long millis) {

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days != 0) {
            return days + "d " + hours % 24 + "h " + minutes % 60 + "m ";
        } else if (hours != 0) {
            return hours % 24 + "h " + minutes % 60 + "m ";
        } else if (minutes != 0) {
            return minutes % 60 + "m ";
        } else if (seconds != 0) {
            return seconds % 60 + "s";
        } else {
            return "N/A";
        }
    }
}
