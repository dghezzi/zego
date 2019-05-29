/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findById", query = "SELECT a FROM Address a WHERE a.id = :id"),   
    @NamedQuery(name = "Address.findByUid", query = "SELECT a FROM Address a WHERE a.uid = :uid"),
    @NamedQuery(name = "Address.findByUidAndType", query = "SELECT a FROM Address a WHERE a.uid = :uid and a.type = :type"),
    @NamedQuery(name = "Address.findByAddress", query = "SELECT a FROM Address a WHERE a.address = :address"),
    @NamedQuery(name = "Address.findByLat", query = "SELECT a FROM Address a WHERE a.lat = :lat"),
    @NamedQuery(name = "Address.findByLng", query = "SELECT a FROM Address a WHERE a.lng = :lng"),
    @NamedQuery(name = "Address.findByType", query = "SELECT a FROM Address a WHERE a.type = :type"),
    @NamedQuery(name = "Address.findByInsdate", query = "SELECT a FROM Address a WHERE a.insdate = :insdate")})
@NamedNativeQueries({
     @NamedNativeQuery(name = "Address.findLastByTypeAndUidNative", query = "SELECT * FROM address  WHERE uid = ?1 and (type = 'pickup' or type = 'dropoff') order by insdate desc limit 10", resultClass = Address.class)
})
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "address")
    private String address;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lng")
    private Double lng;
    @Column(name = "type")
    private String type;
    @Column(name = "insdate")
    private String insdate;

    public static final String ADDRESS_PICKUP = "pickup";
    public static final String ADDRESS_DROPOFF = "dropoff";
    
    @Transient
    private String placeid;
    
    public Address() {
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
        return address;
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

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
    
    
    
}
