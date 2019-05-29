/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities.legacy;

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
@Table(name = "zgdrivrating")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgdrivrating.findAll", query = "SELECT z FROM Zgdrivrating z"),
    @NamedQuery(name = "Zgdrivrating.findByUserId", query = "SELECT z FROM Zgdrivrating z WHERE z.userId = :userId"),
    @NamedQuery(name = "Zgdrivrating.findById", query = "SELECT z FROM Zgdrivrating z WHERE z.userId = :id"),
    @NamedQuery(name = "Zgdrivrating.findByDriverRating", query = "SELECT z FROM Zgdrivrating z WHERE z.driverRating = :driverRating"),
    @NamedQuery(name = "Zgdrivrating.findByOccurrences", query = "SELECT z FROM Zgdrivrating z WHERE z.occurrences = :occurrences")})
public class Zgdrivrating implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "driver_rating")
    private Double driverRating;
    @Column(name = "occurrences")
    private Integer occurrences;

    public Zgdrivrating() {
    }

    public Zgdrivrating(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(Double driverRating) {
        this.driverRating = driverRating;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgdrivrating)) {
            return false;
        }
        Zgdrivrating other = (Zgdrivrating) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgdrivrating[ userId=" + userId + " ]";
    }
    
}
