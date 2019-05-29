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
@Table(name = "zgcity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgcity.findAll", query = "SELECT z FROM Zgcity z"),
    @NamedQuery(name = "Zgcity.findByCityId", query = "SELECT z FROM Zgcity z WHERE z.cityId = :cityId"),
    @NamedQuery(name = "Zgcity.findByCity", query = "SELECT z FROM Zgcity z WHERE z.city = :city"),
    @NamedQuery(name = "Zgcity.findByCode", query = "SELECT z FROM Zgcity z WHERE z.code = :code"),
    @NamedQuery(name = "Zgcity.findByZipCode", query = "SELECT z FROM Zgcity z WHERE z.zipCode = :zipCode"),
    @NamedQuery(name = "Zgcity.findByProvince", query = "SELECT z FROM Zgcity z WHERE z.province = :province"),
    @NamedQuery(name = "Zgcity.findByRegion", query = "SELECT z FROM Zgcity z WHERE z.region = :region")})
public class Zgcity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "city_id")
    private Integer cityId;
    @Basic(optional = false)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "province")
    private String province;
    @Column(name = "region")
    private String region;

    public Zgcity() {
    }

    public Zgcity(Integer cityId) {
        this.cityId = cityId;
    }

    public Zgcity(Integer cityId, String city, String code, String zipCode) {
        this.cityId = cityId;
        this.city = city;
        this.code = code;
        this.zipCode = zipCode;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cityId != null ? cityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgcity)) {
            return false;
        }
        Zgcity other = (Zgcity) object;
        if ((this.cityId == null && other.cityId != null) || (this.cityId != null && !this.cityId.equals(other.cityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgcity[ cityId=" + cityId + " ]";
    }
    
}
