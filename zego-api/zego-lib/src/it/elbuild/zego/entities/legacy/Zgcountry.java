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
@Table(name = "zgcountry")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgcountry.findAll", query = "SELECT z FROM Zgcountry z"),
    @NamedQuery(name = "Zgcountry.findByCountryId", query = "SELECT z FROM Zgcountry z WHERE z.countryId = :countryId"),
    @NamedQuery(name = "Zgcountry.findById", query = "SELECT z FROM Zgcountry z WHERE z.countryId = :id"),
    @NamedQuery(name = "Zgcountry.findByCountry", query = "SELECT z FROM Zgcountry z WHERE z.country = :country"),
    @NamedQuery(name = "Zgcountry.findByCountryCode", query = "SELECT z FROM Zgcountry z WHERE z.countryCode = :countryCode")})
public class Zgcountry implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "country_id")
    private Integer countryId;
    @Basic(optional = false)
    @Column(name = "country")
    private String country;
    @Column(name = "country_code")
    private String countryCode;

    public Zgcountry() {
    }

    public Zgcountry(Integer countryId) {
        this.countryId = countryId;
    }

    public Zgcountry(Integer countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (countryId != null ? countryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgcountry)) {
            return false;
        }
        Zgcountry other = (Zgcountry) object;
        if ((this.countryId == null && other.countryId != null) || (this.countryId != null && !this.countryId.equals(other.countryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgcountry[ countryId=" + countryId + " ]";
    }
    
}
