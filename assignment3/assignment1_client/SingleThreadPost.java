package io.swagger.client.assignment1_client;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class SingleThreadPost {
    final private static int NUM_REQUESTS = 10_00;
    final private static String LOCAL_PATH = "http://localhost:8080/lab1_java_servlet_war_exploded";
    final private static String EC2_PATH = "http://35.174.170.42/lab1-java-servlet_war/";

    public static void main(String[] args) throws ApiException {

        // Method 1
        long start = System.currentTimeMillis();
        SkiersApi skiersApi = new SkiersApi();
        skiersApi.getApiClient().setBasePath(EC2_PATH);
        for (int i = 0; i < NUM_REQUESTS; i++) {
            RandomLiftRide randomLiftRide = new RandomLiftRide();
            LiftRide liftRide = randomLiftRide.getBody();
            Integer resortID = randomLiftRide.getResortID();
            String seasonID = randomLiftRide.getSeasonID();
            String dayID = randomLiftRide.getDayID();
            Integer skierID = randomLiftRide.getSkierID();
            ApiResponse res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, resortID, seasonID, dayID, skierID);
            System.out.println(i + " " + res.getStatusCode());
        }
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed for " + NUM_REQUESTS + " post requests is: " + (end - start) + "ms");
        System.out.println("Time per request is: " + (double) ((end - start)) / NUM_REQUESTS + " ms");


    }
}
