/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import android.location.Location;

import java.io.Serializable;


/**
 *
 * @author Lu
 */

public class Position implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer uid;
    private Double lat;
    private Double lng;
    private Double accuracy;
    private String ldate;

    public Position() {
    }

    public Position(Location loc,int uid) {
        lat = loc.getLatitude();
        lng = loc.getLongitude();
        accuracy = Double.valueOf(loc.getAccuracy());
        ldate = String.valueOf(loc.getTime()/1000);
        this.uid = uid;
    }

    public Position(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String getLdate() {
        return ldate;
    }

    public void setLdate(String ldate) {
        this.ldate = ldate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Lat:" + lat + "\n");
        s.append("Lng:" + lng + "\n");
        s.append("Accuracy:" + accuracy + "\n");
        s.append("uid:" + uid + "\n");
        s.append("ldate:" + ldate + "\n");
        return  s.toString();
    }

}
