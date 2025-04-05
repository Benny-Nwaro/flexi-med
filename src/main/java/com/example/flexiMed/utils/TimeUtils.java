package com.example.flexiMed.utils;

/**
 * Utility class for time-related operations.
 * Provides methods to format time durations into human-readable strings.
 */
public class TimeUtils {

    /**
     * Formats a time duration given in minutes into a human-readable string.
     *
     * @param minutes The time duration in minutes.
     * @return A formatted string representing the time duration.
     * Examples: "1 hour(s) and 30 minute(s)", "45 minute(s)", "0 second(s)".
     * @throws IllegalArgumentException if the input minutes are negative.
     */
    public static String formatTime(long minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative.");
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;
        long seconds = minutes * 60;

        if (hours > 0) {
            return String.format("%d hour(s) and %d minute(s)", hours, remainingMinutes);
        } else if (minutes > 0){
            return String.format("%d minute(s)", minutes);
        } else {
            return String.format("%d second(s)", seconds);
        }
    }
}