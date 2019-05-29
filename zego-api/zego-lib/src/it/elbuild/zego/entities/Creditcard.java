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
@Table(name = "creditcard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Creditcard.findAll", query = "SELECT c FROM Creditcard c"),
    @NamedQuery(name = "Creditcard.findById", query = "SELECT c FROM Creditcard c WHERE c.id = :id"),
    @NamedQuery(name = "Creditcard.findByUid", query = "SELECT c FROM Creditcard c WHERE c.uid = :uid"),
    @NamedQuery(name = "Creditcard.findByType", query = "SELECT c FROM Creditcard c WHERE c.type = :type"),
    @NamedQuery(name = "Creditcard.findByLastfour", query = "SELECT c FROM Creditcard c WHERE c.lastfour = :lastfour"),
    @NamedQuery(name = "Creditcard.findByExpm", query = "SELECT c FROM Creditcard c WHERE c.expm = :expm"),
    @NamedQuery(name = "Creditcard.findByExpy", query = "SELECT c FROM Creditcard c WHERE c.expy = :expy"),
    @NamedQuery(name = "Creditcard.findByDef", query = "SELECT c FROM Creditcard c WHERE c.def = :def"),
    @NamedQuery(name = "Creditcard.findByInsdate", query = "SELECT c FROM Creditcard c WHERE c.insdate = :insdate"),
    @NamedQuery(name = "Creditcard.findByFingerprint", query = "SELECT c FROM Creditcard c WHERE c.fingerprint = :fingerprint")})
public class Creditcard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "type")
    private String type;
    @Column(name = "lastfour")
    private String lastfour;
    @Column(name = "expm")
    private Integer expm;
    @Column(name = "expy")
    private Integer expy;
    @Column(name = "def")
    private Integer def;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "fingerprint")
    private String fingerprint;

    public Creditcard() {
    }

    public Creditcard(Integer id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastfour() {
        return lastfour;
    }

    public void setLastfour(String lastfour) {
        this.lastfour = lastfour;
    }

    public Integer getExpm() {
        return expm;
    }

    public void setExpm(Integer expm) {
        this.expm = expm;
    }

    public Integer getExpy() {
        return expy;
    }

    public void setExpy(Integer expy) {
        this.expy = expy;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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
        if (!(object instanceof Creditcard)) {
            return false;
        }
        Creditcard other = (Creditcard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Creditcard[ id=" + id + " ]";
    }
    
}
