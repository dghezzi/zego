/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class Coordinate {
    
    public Coordinate(){
        
    }
    
    public Coordinate(Double l, Double t){
        lat = l;
        lng = t;
    }
    
    private Double lat;
    private Double lng;
    private Integer vid;

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

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }
    
    
}
