/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.request;

/**
 *
 * @author Lu
 */
public class GeoCodeRequest {
    
    private String address;
    private String placeid;

    /**
     * @return the address
     */
    public String getAddress() {
        return address == null ? "" : address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the placeid
     */
    public String getPlaceid() {
        return placeid;
    }

    /**
     * @param placeid the placeid to set
     */
    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
}
