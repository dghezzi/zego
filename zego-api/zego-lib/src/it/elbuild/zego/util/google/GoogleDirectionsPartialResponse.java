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
public class GoogleDirectionsPartialResponse {
    
    private String status;
    private List<GoogleDirectionRoute> routes;
    private List<Point> points;
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the routes
     */
    public List<GoogleDirectionRoute> getRoutes() {
        return routes;
    }

    /**
     * @param routes the routes to set
     */
    public void setRoutes(List<GoogleDirectionRoute> routes) {
        this.routes = routes;
    }

    /**
     * @return the points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
