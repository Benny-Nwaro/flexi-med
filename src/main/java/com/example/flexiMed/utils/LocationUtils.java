package com.example.flexiMed.utils;

/**
 * Utility class for location-related operations.
 * Provides methods to calculate distances between geographic coordinates.
 */
public class LocationUtils {

    /**
     * Calculates the great-circle distance between two points on the Earth's surface using the Haversine formula.
     *
     * @param lat1 The latitude of the first point in degrees.
     * @param lon1 The longitude of the first point in degrees.
     * @param lat2 The latitude of the second point in degrees.
     * @param lon2 The longitude of the second point in degrees.
     * @return The distance between the two points in kilometers.
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in km
    }
}