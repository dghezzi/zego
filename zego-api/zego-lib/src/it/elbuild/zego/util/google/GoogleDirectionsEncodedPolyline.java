/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util.google;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lu
 */
public class GoogleDirectionsEncodedPolyline {
    
    private String points;

    /**
     * @return the points
     */
    public String getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(String points) {
        this.points = points;
    }
    
    public List<Point> decode()
    {
        return points == null ? new ArrayList<Point>() : PolylineDecoder.decode(points);
    }
}
