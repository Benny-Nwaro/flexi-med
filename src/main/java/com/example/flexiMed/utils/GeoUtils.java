package com.example.flexiMed.utils;

/**
 * Utility class for geographic calculations.
 * Provides methods to calculate estimated time of arrival (ETA) based on geographic coordinates.
 */
public class GeoUtils {

    private static final int EARTH_RADIUS = 6371; // Radius of the Earth in km

    /**
     * Calculates the estimated time of arrival (ETA) in minutes between two geographic coordinates.
     * This method uses the Haversine formula to calculate the distance and assumes an average ambulance speed of 60 km/h.
     *
     * @param lat1 The latitude of the starting point in degrees.
     * @param lon1 The longitude of the starting point in degrees.
     * @param lat2 The latitude of the destination point in degrees.
     * @param lon2 The longitude of the destination point in degrees.
     * @return The estimated time of arrival in minutes.
     */
    public static long calculateETA(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // Distance in km

        // Assuming average speed of ambulance is 60 km/h
        double averageSpeed = 60; // km/h
        double timeInHours = distance / averageSpeed; // Time in hours
        return (long) (timeInHours * 60); // Convert to minutes
    }
}