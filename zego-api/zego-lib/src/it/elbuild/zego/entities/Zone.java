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
@Table(name = "zone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zone.findAll", query = "SELECT z FROM Zone z"),
    @NamedQuery(name = "Zone.findById", query = "SELECT z FROM Zone z WHERE z.id = :id"),
    @NamedQuery(name = "Zone.findByName", query = "SELECT z FROM Zone z WHERE z.name = :name"),
    @NamedQuery(name = "Zone.findByNwlat", query = "SELECT z FROM Zone z WHERE z.nwlat = :nwlat"),
    @NamedQuery(name = "Zone.findByNwlng", query = "SELECT z FROM Zone z WHERE z.nwlng = :nwlng"),
    @NamedQuery(name = "Zone.findBySelat", query = "SELECT z FROM Zone z WHERE z.selat = :selat"),
    @NamedQuery(name = "Zone.findBySelng", query = "SELECT z FROM Zone z WHERE z.selng = :selng"),
    @NamedQuery(name = "Zone.findByInsdate", query = "SELECT z FROM Zone z WHERE z.insdate = :insdate")})
public class Zone implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nwlat")
    private Double nwlat;
    @Column(name = "nwlng")
    private Double nwlng;
    @Column(name = "selat")
    private Double selat;
    @Column(name = "selng")
    private Double selng;
    @Column(name = "insdate")
    private String insdate;

    @Column(name = "obfuscationradius")
    private Integer obfuscationradius;
    @Column(name = "drivercandidates")
    private Integer drivercandidates;
    @Column(name = "driverradius")
    private Integer driverradius;
    @Column(name = "driverradiusnum")
    private Integer driverradiusnum;
    @Column(name = "priceperkm")
    private Integer priceperkm;
    @Column(name = "driverinterleaving")
    private Integer driverinterleaving;
    @Column(name = "baseprice")
    private Integer baseprice;

    public Zone() {
    }

    public Zone(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNwlat() {
        return nwlat;
    }

    public void setNwlat(Double nwlat) {
        this.nwlat = nwlat;
    }

    public Double getNwlng() {
        return nwlng;
    }

    public void setNwlng(Double nwlng) {
        this.nwlng = nwlng;
    }

    public Double getSelat() {
        return selat;
    }

    public void setSelat(Double selat) {
        this.selat = selat;
    }

    public Double getSelng() {
        return selng;
    }

    public void setSelng(Double selng) {
        this.selng = selng;
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

    public Integer getObfuscationradius() {
        return obfuscationradius;
    }

    public void setObfuscationradius(Integer obfuscationradius) {
        this.obfuscationradius = obfuscationradius;
    }

    public Integer getDrivercandidates() {
        return drivercandidates;
    }

    public void setDrivercandidates(Integer drivercandidates) {
        this.drivercandidates = drivercandidates;
    }

    public Integer getDriverradius() {
        return driverradius;
    }

    public void setDriverradius(Integer driverradius) {
        this.driverradius = driverradius;
    }

    public Integer getPriceperkm() {
        return priceperkm;
    }

    public void setPriceperkm(Integer priceperkm) {
        this.priceperkm = priceperkm;
    }

    public Integer getDriverinterleaving() {
        return driverinterleaving;
    }

    public void setDriverinterleaving(Integer driverinterleaving) {
        this.driverinterleaving = driverinterleaving;
    }

    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zone)) {
            return false;
        }
        Zone other = (Zone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Zone[ id=" + id + " ]";
    }
 
    public boolean include(Double lat, Double lng) {               
        return (lat < nwlat) && (lat > selat) && (lng > nwlng) && (lng < selng);
    }

    public Integer getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(Integer baseprice) {
        this.baseprice = baseprice;
    }

    public Integer getDriverradiusnum() {
        return driverradiusnum;
    }

    public void setDriverradiusnum(Integer driverradiusnum) {
        this.driverradiusnum = driverradiusnum;
    }
    
    
}
