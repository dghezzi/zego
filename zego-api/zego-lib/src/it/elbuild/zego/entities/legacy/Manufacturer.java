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
@Table(name = "manufacturer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Manufacturer.findAll", query = "SELECT m FROM Manufacturer m"),
    @NamedQuery(name = "Manufacturer.findByManufacturerId", query = "SELECT m FROM Manufacturer m WHERE m.manufacturerId = :manufacturerId"),
    @NamedQuery(name = "Manufacturer.findByManufacturer", query = "SELECT m FROM Manufacturer m WHERE m.manufacturer = :manufacturer")})
public class Manufacturer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;
    @Column(name = "manufacturer")
    private String manufacturer;

    public Manufacturer() {
    }

    public Manufacturer(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (manufacturerId != null ? manufacturerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Manufacturer)) {
            return false;
        }
        Manufacturer other = (Manufacturer) object;
        if ((this.manufacturerId == null && other.manufacturerId != null) || (this.manufacturerId != null && !this.manufacturerId.equals(other.manufacturerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Manufacturer[ manufacturerId=" + manufacturerId + " ]";
    }
    
}
