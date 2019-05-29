/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import java.io.Serializable;



public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String HOME = "home";
    public static final String WORK = "work";
    public static final String SEARCH = "search";
    public static final String PICKUP_TYPE = "pickup";
    public static final String DROPOFF_TYPE = "dropoff";

    private Integer id;
    private Integer uid;
    private String address;
    private Double lat;
    private Double lng;
    private String type;
    private String insdate;

    private String placeid;
    private String topTerm;
    private String bottomTerm;

    public Address() {
        address = "";
    }

    public Address(GeoCodeResponse geoCodeResponse){
        address = geoCodeResponse.getAddress();
        lat = geoCodeResponse.getLat();
        lng = geoCodeResponse.getLng();
    }

    public Address(Integer id) {
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

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
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
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Address[ id=" + id + " ]";
    }


    public boolean isSearch(){
        return !type.equalsIgnoreCase(PICKUP_TYPE) && !type.equalsIgnoreCase(DROPOFF_TYPE);
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getTopTerm() {
        return topTerm;
    }

    public void setTopTerm(String topTerm) {
        this.topTerm = topTerm;
    }

    public String getBottomTerm() {
        return bottomTerm;
    }

    public void setBottomTerm(String bottomTerm) {
        this.bottomTerm = bottomTerm;
    }

    public boolean isAddressOk(){
        return address != null && !address.isEmpty() && lat != null && lng != null;
    }
}
