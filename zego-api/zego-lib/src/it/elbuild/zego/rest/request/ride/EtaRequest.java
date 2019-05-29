/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.request.ride;

/**
 *
 * @author Lu
 */
public class EtaRequest {

    public static final Integer PICKUP_REQ = 0;
    
    public EtaRequest() {
    
    }
    
    
    private Double lat;
    private Double lng;
    private String placeid;
    private Integer level;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
    
    
}
