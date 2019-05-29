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
@Table(name = "zgpassrating")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgpassrating.findAll", query = "SELECT z FROM Zgpassrating z"),
    @NamedQuery(name = "Zgpassrating.findById", query = "SELECT z FROM Zgpassrating z WHERE z.userId = :id"),
    @NamedQuery(name = "Zgpassrating.findByUserId", query = "SELECT z FROM Zgpassrating z WHERE z.userId = :userId"),
    @NamedQuery(name = "Zgpassrating.findByPassengerRating", query = "SELECT z FROM Zgpassrating z WHERE z.passengerRating = :passengerRating"),
    @NamedQuery(name = "Zgpassrating.findByOccurrences", query = "SELECT z FROM Zgpassrating z WHERE z.occurrences = :occurrences")})
public class Zgpassrating implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "passenger_rating")
    private Double passengerRating;
    @Column(name = "occurrences")
    private Integer occurrences;

    public Zgpassrating() {
    }

    public Zgpassrating(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(Double passengerRating) {
        this.passengerRating = passengerRating;
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
        if (!(object instanceof Zgpassrating)) {
            return false;
        }
        Zgpassrating other = (Zgpassrating) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgpassrating[ userId=" + userId + " ]";
    }
    
}
