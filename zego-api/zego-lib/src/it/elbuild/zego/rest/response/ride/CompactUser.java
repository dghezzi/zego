/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response.ride;

import it.elbuild.zego.entities.User;

/**
 *
 * @author Lu
 */
public class CompactUser {

    public CompactUser() {
    
    }
    
    public CompactUser(User u) {
        did = u.getId();
        lat = u.getLlat();
        lng = u.getLlon();
        name = u.getFname();
        userimg = u.getImg();
        rating = u.getPavg();
        rtstatus = u.getRtstatus();
        current = u.getCurrent();
    }
    
    private Integer current;
    private Integer did;
    private Double lat;
    private Double lng;
    private String name; 
    private Integer rtstatus;
    private String userimg;
    private Double rating;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    
    
    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }  

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }        

    public Integer getRtstatus() {
        return rtstatus;
    }

    public void setRtstatus(Integer rtstatus) {
        this.rtstatus = rtstatus;
    }
    
    
}
