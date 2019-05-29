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
public class GoogleDirectionRoute {
    
    private List<GoogleDirectionLeg> legs;
    private GoogleDirectionBounds bounds;
    private GoogleDirectionsEncodedPolyline overview_polyline;


    /**
     * @return the legs
     */
    public List<GoogleDirectionLeg> getLegs() {
        return legs;
    }

    /**
     * @param legs the legs to set
     */
    public void setLegs(List<GoogleDirectionLeg> legs) {
        this.legs = legs;
    }

    /**
     * @return the bounds
     */
    public GoogleDirectionBounds getBounds() {
        return bounds;
    }

    /**
     * @param bounds the bounds to set
     */
    public void setBounds(GoogleDirectionBounds bounds) {
        this.bounds = bounds;
    }

    /**
     * @return the overview_polyline
     */
    public GoogleDirectionsEncodedPolyline getOverview_polyline() {
        return overview_polyline;
    }

    /**
     * @param overview_polyline the overview_polyline to set
     */
    public void setOverview_polyline(GoogleDirectionsEncodedPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }
}
