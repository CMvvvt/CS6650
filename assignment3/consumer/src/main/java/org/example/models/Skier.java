package org.example.models;

public class Skier {

    private String liftID;
    private String resortID;
    private String seasonID;
    private String dayID;
    private String skierID;

    public Skier(String liftID, String resortID, String seasonID, String dayID, String skierID) {
        this.liftID = liftID;
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
    }

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
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

    public String getSkierID() {
        return skierID;
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }

    @Override
    public String toString() {
        return "Skier{" +
                "liftID='" + liftID + '\'' +
                ", resortID='" + resortID + '\'' +
                ", seasonID='" + seasonID + '\'' +
                ", dayID='" + dayID + '\'' +
                ", skierID='" + skierID + '\'' +
                '}';
    }
}
