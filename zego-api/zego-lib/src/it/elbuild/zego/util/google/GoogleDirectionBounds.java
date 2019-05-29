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
public class GoogleDirectionBounds {
    
    private GoogleDirectionLocation northeast;
    private GoogleDirectionLocation southwest;

    /**
     * @return the northeast
     */
    public GoogleDirectionLocation getNortheast() {
        return northeast;
    }

    /**
     * @param northeast the northeast to set
     */
    public void setNortheast(GoogleDirectionLocation northeast) {
        this.northeast = northeast;
    }

    /**
     * @return the southwest
     */
    public GoogleDirectionLocation getSouthwest() {
        return southwest;
    }

    /**
     * @param southwest the southwest to set
     */
    public void setSouthwest(GoogleDirectionLocation southwest) {
        this.southwest = southwest;
    }
}
