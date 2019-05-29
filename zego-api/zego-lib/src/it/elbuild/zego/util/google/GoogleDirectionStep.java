/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util.google;

/**
 *
 * @author Lu
 */
public class GoogleDirectionStep {
    
    private GoogleDirectionMeasure distance;
    private GoogleDirectionMeasure duration;
    private GoogleDirectionLocation end_location;
    private GoogleDirectionLocation start_location;
    private String travel_mode;

    /**
     * @return the distance
     */
    public GoogleDirectionMeasure getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(GoogleDirectionMeasure distance) {
        this.distance = distance;
    }

    /**
     * @return the duration
     */
    public GoogleDirectionMeasure getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(GoogleDirectionMeasure duration) {
        this.duration = duration;
    }

    /**
     * @return the end_location
     */
    public GoogleDirectionLocation getEnd_location() {
        return end_location;
    }

    /**
     * @param end_location the end_location to set
     */
    public void setEnd_location(GoogleDirectionLocation end_location) {
        this.end_location = end_location;
    }

    /**
     * @return the start_location
     */
    public GoogleDirectionLocation getStart_location() {
        return start_location;
    }

    /**
     * @param start_location the start_location to set
     */
    public void setStart_location(GoogleDirectionLocation start_location) {
        this.start_location = start_location;
    }

    /**
     * @return the travel_mode
     */
    public String getTravel_mode() {
        return travel_mode;
    }

    /**
     * @param travel_mode the travel_mode to set
     */
    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }
}
