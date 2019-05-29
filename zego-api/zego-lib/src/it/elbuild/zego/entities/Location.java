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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findById", query = "SELECT l FROM Location l WHERE l.id = :id"),
    @NamedQuery(name = "Location.findByUid", query = "SELECT l FROM Location l WHERE l.uid = :uid order by l.id desc"),
    @NamedQuery(name = "Location.findByLat", query = "SELECT l FROM Location l WHERE l.lat = :lat"),
    @NamedQuery(name = "Location.findByUidBetween", query = "SELECT l FROM Location l WHERE l.uid = :uid and l.ldate BETWEEN :start and :stop and l.accuracy < 100.0 order by l.id desc"),
    @NamedQuery(name = "Location.findByModeBetween", query = "SELECT l FROM Location l WHERE l.mode = :mode and l.ldate BETWEEN :start and :stop"),
    @NamedQuery(name = "Location.findByLng", query = "SELECT l FROM Location l WHERE l.lng = :lng"),
    @NamedQuery(name = "Location.findByAccuracy", query = "SELECT l FROM Location l WHERE l.accuracy = :accuracy"),
    @NamedQuery(name = "Location.findByLdate", query = "SELECT l FROM Location l WHERE l.ldate = :ldate")})
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lng")
    private Double lng;
    @Column(name = "accuracy")
    private Double accuracy;
    @Column(name = "ldate")
    private String ldate;
    @Column(name = "mode")
    private String mode;

    public Location() {
    }

    public Location(Integer id) {
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
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Location[ id=" + id + " ]";
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    
}
