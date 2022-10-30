package io.swagger.client.assignment1_client;

import io.swagger.client.model.LiftRide;

import java.util.Random;

public class RandomLiftRide {
    private LiftRide body;
    private Integer resortID;
    private String seasonID;
    private String dayID;
    private Integer skierID;
    private Random rand = new Random();

    public RandomLiftRide() {
        this.body = new LiftRide();
        this.resortID = this.rand.nextInt(10) + 1;
        this.seasonID = "2022";
        this.dayID = "1";
        this.skierID = this.rand.nextInt(100_000) + 1;
    }

    public LiftRide getBody() {
        return body;
    }

    public void setBody(LiftRide body) {
        this.body = body;
    }

    public Integer getResortID() {
        return resortID;
    }

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public Integer getSkierID() {
        return skierID;
    }

    public void setSkierID(Integer skierID) {
        this.skierID = skierID;
    }
}
