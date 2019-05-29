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
@Table(name = "pin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pin.findAll", query = "SELECT p FROM Pin p"),
    @NamedQuery(name = "Pin.findById", query = "SELECT p FROM Pin p WHERE p.id = :id"),
    @NamedQuery(name = "Pin.findByUid", query = "SELECT p FROM Pin p WHERE p.uid = :uid order by p.id desc"),
    @NamedQuery(name = "Pin.findByUidValid", query = "SELECT p FROM Pin p WHERE p.uid = :uid and p.expirydate > :expirydate"),
    @NamedQuery(name = "Pin.findByPin", query = "SELECT p FROM Pin p WHERE p.pin = :pin"),
    @NamedQuery(name = "Pin.findBySentdate", query = "SELECT p FROM Pin p WHERE p.sentdate = :sentdate"),
    @NamedQuery(name = "Pin.findByExpirydate", query = "SELECT p FROM Pin p WHERE p.expirydate = :expirydate")})
public class Pin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "pin")
    private String pin;
    @Column(name = "sentdate")
    private String sentdate;
    @Column(name = "expirydate")
    private String expirydate;
    @Column(name = "nexmoid")
    private String nexmoid;
    @Column(name = "nexmostatus")
    private String nexmostatus;
    @Column(name = "raw")
    private String raw;

    public Pin() {
    }

    public Pin(Integer id) {
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSentdate() {
        return sentdate;
    }

    public void setSentdate(String sentdate) {
        this.sentdate = sentdate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
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
        if (!(object instanceof Pin)) {
            return false;
        }
        Pin other = (Pin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Pin[ id=" + id + " ]";
    }

    /**
     * @return the nexmoid
     */
    public String getNexmoid() {
        return nexmoid;
    }

    /**
     * @param nexmoid the nexmoid to set
     */
    public void setNexmoid(String nexmoid) {
        this.nexmoid = nexmoid;
    }

    /**
     * @return the nexmostatus
     */
    public String getNexmostatus() {
        return nexmostatus;
    }

    /**
     * @param nexmostatus the nexmostatus to set
     */
    public void setNexmostatus(String nexmostatus) {
        this.nexmostatus = nexmostatus;
    }

    /**
     * @return the raw
     */
    public String getRaw() {
        return raw;
    }

    /**
     * @param raw the raw to set
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }
    
}
