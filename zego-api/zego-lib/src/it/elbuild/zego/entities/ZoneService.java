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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "zone_service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZoneService.findAll", query = "SELECT z FROM ZoneService z"),
    @NamedQuery(name = "ZoneService.findById", query = "SELECT z FROM ZoneService z WHERE z.id = :id"),
    @NamedQuery(name = "ZoneService.findByZid", query = "SELECT z FROM ZoneService z WHERE z.zid = :zid"),
    @NamedQuery(name = "ZoneService.findBySid", query = "SELECT z FROM ZoneService z WHERE z.sid = :sid"),
    @NamedQuery(name = "ZoneService.findByInsdate", query = "SELECT z FROM ZoneService z WHERE z.insdate = :insdate")})
public class ZoneService implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "zid")
    private Integer zid;
    @Column(name = "sid")
    private Integer sid;
    @Column(name = "insdate")
    private String insdate;

    @OneToOne
    @JoinColumn(name = "zid",insertable = false, updatable = false)    
    private Zone zone;
    
    @OneToOne
    @JoinColumn(name = "sid",insertable = false, updatable = false)    
    private Service service;
    
    public ZoneService() {
    }

    public ZoneService(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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
        if (!(object instanceof ZoneService)) {
            return false;
        }
        ZoneService other = (ZoneService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.ZoneService[ id=" + id + " ]";
    }
    
}
