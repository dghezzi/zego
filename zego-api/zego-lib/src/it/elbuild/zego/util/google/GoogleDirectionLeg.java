/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util.google;

import java.util.List;

/**
 *
 * @author Lu
 */
public class GoogleDirectionLeg {
    
    private GoogleDirectionMeasure distance;
    private GoogleDirectionMeasure duration;
    private String end_address;
    private String start_address;
    private GoogleDirectionLocation end_location;
    private GoogleDirectionLocation start_location;
    private GoogleDirectionsEncodedPolyline polyline;
    private List<GoogleDirectionStep> steps;

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
     * @return the end_address
     */
    public String getEnd_address() {
        return end_address;
    }

    /**
     * @param end_address the end_address to set
     */
    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    /**
     * @return the start_address
     */
    public String getStart_address() {
        return start_address;
    }

    /**
     * @param start_address the start_address to set
     */
    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    /**
     * @return the steps
     */
    public List<GoogleDirectionStep> getSteps() {
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(List<GoogleDirectionStep> steps) {
        this.steps = steps;
    }

    /**
     * @return the polyline
     */
    public GoogleDirectionsEncodedPolyline getPolyline() {
        return polyline;
    }

    /**
     * @param polyline the polyline to set
     */
    public void setPolyline(GoogleDirectionsEncodedPolyline polyline) {
        this.polyline = polyline;
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
}
