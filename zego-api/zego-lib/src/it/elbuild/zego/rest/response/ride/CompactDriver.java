/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response.ride;

import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.User;

/**
 *
 * @author Lu
 */
public class CompactDriver {

    public CompactDriver() {
    
    }
    
    public CompactDriver(User u, Driverdata d) {
        did = u.getId();
        lat = u.getLlat();
        lng = u.getLlon();
        name = u.getFname();
        userimg = u.getImg();
        rating = u.getDavg();
        if(d!=null) {
            brand = d.getBrand();
            model = d.getModel();
            year = d.getYear();
            carimg = d.getCarimg();
            color = d.getColor();        
        }
        current = u.getCurrent();
        rtstatus = u.getRtstatus();
    }
    
    private Integer current;
    private Integer rtstatus;
    private Integer did;
    private Double lat;
    private Double lng;
    private String name;    
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String carimg;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
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
