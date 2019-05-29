/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

import it.elbuild.zego.entities.User;

/**
 *
 * @author Lu
 */
public class CompatDriver {
    
    
    private String lname;
    private String fname;
    private Integer did;
    private Integer status;
    private String llocdate;
    private Double lat;
    private Double lng;
    
    public CompatDriver() {
        
    }
    
    public CompatDriver(User u) {
        lname = u.getLname();
        fname = u.getFname();
        did = u.getId();
        status = u.getRtstatus();
        llocdate  = u.getLlocdate();
        lat = u.getLlat();
        lng = u.getLlon();
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLlocdate() {
        return llocdate;
    }

    public void setLlocdate(String llocdate) {
        this.llocdate = llocdate;
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
    
    
}
