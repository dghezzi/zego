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
@Table(name = "blacklist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Blacklist.findAll", query = "SELECT b FROM Blacklist b"),
    @NamedQuery(name = "Blacklist.findById", query = "SELECT b FROM Blacklist b WHERE b.id = :id"),
    @NamedQuery(name = "Blacklist.findByUid", query = "SELECT b FROM Blacklist b WHERE b.uid = :uid"),
    @NamedQuery(name = "Blacklist.findByDeviceid", query = "SELECT b FROM Blacklist b WHERE b.deviceid = :deviceid"),
    @NamedQuery(name = "Blacklist.findByBlacklistreason", query = "SELECT b FROM Blacklist b WHERE b.blacklistreason = :blacklistreason"),
    @NamedQuery(name = "Blacklist.findByBlacklistdate", query = "SELECT b FROM Blacklist b WHERE b.blacklistdate = :blacklistdate")})
public class Blacklist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "deviceid")
    private String deviceid;
    @Column(name = "blacklistreason")
    private String blacklistreason;
    @Column(name = "blacklistdate")
    private String blacklistdate;

    public Blacklist() {
    }

    public Blacklist(Integer id) {
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

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getBlacklistreason() {
        return blacklistreason;
    }

    public void setBlacklistreason(String blacklistreason) {
        this.blacklistreason = blacklistreason;
    }

    public String getBlacklistdate() {
        return blacklistdate;
    }

    public void setBlacklistdate(String blacklistdate) {
        this.blacklistdate = blacklistdate;
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
        if (!(object instanceof Blacklist)) {
            return false;
        }
        Blacklist other = (Blacklist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Blacklist[ id=" + id + " ]";
    }
    
}
