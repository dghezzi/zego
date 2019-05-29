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
@Table(name = "fraud")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fraud.findAll", query = "SELECT f FROM Fraud f"),
    @NamedQuery(name = "Fraud.findById", query = "SELECT f FROM Fraud f WHERE f.id = :id"),
    @NamedQuery(name = "Fraud.findByUid", query = "SELECT f FROM Fraud f WHERE f.uid = :uid"),
    @NamedQuery(name = "Fraud.findByFrauddate", query = "SELECT f FROM Fraud f WHERE f.frauddate = :frauddate"),
    @NamedQuery(name = "Fraud.findByStripedata", query = "SELECT f FROM Fraud f WHERE f.stripedata = :stripedata")})
public class Fraud implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "frauddate")
    private String frauddate;
    @Column(name = "stripedata")
    private String stripedata;

    public Fraud() {
    }

    public Fraud(Integer id) {
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

    public String getFrauddate() {
        return frauddate;
    }

    public void setFrauddate(String frauddate) {
        this.frauddate = frauddate;
    }

    public String getStripedata() {
        return stripedata;
    }

    public void setStripedata(String stripedata) {
        this.stripedata = stripedata;
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
        if (!(object instanceof Fraud)) {
            return false;
        }
        Fraud other = (Fraud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Fraud[ id=" + id + " ]";
    }
    
}
