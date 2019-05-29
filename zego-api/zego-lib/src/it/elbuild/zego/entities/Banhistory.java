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
@Table(name = "banhistory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Banhistory.findAll", query = "SELECT b FROM Banhistory b"),
    @NamedQuery(name = "Banhistory.findById", query = "SELECT b FROM Banhistory b WHERE b.id = :id"),
    @NamedQuery(name = "Banhistory.findByUid", query = "SELECT b FROM Banhistory b WHERE b.uid = :uid"),
    @NamedQuery(name = "Banhistory.findByBandate", query = "SELECT b FROM Banhistory b WHERE b.bandate = :bandate"),
    @NamedQuery(name = "Banhistory.findByBanreason", query = "SELECT b FROM Banhistory b WHERE b.banreason = :banreason"),
    @NamedQuery(name = "Banhistory.findByBanexpiry", query = "SELECT b FROM Banhistory b WHERE b.banexpiry = :banexpiry")})
public class Banhistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "bandate")
    private String bandate;
    @Column(name = "banreason")
    private String banreason;
    @Column(name = "banexpiry")
    private String banexpiry;

    public Banhistory() {
    }

    public Banhistory(Integer id) {
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

    public String getBandate() {
        return bandate;
    }

    public void setBandate(String bandate) {
        this.bandate = bandate;
    }

    public String getBanreason() {
        return banreason;
    }

    public void setBanreason(String banreason) {
        this.banreason = banreason;
    }

    public String getBanexpiry() {
        return banexpiry;
    }

    public void setBanexpiry(String banexpiry) {
        this.banexpiry = banexpiry;
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
        if (!(object instanceof Banhistory)) {
            return false;
        }
        Banhistory other = (Banhistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Banhistory[ id=" + id + " ]";
    }
    
}
