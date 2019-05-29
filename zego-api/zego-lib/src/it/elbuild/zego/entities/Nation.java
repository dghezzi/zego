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
@Table(name = "nation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nation.findAll", query = "SELECT n FROM Nation n"),
    @NamedQuery(name = "Nation.findByIdStati", query = "SELECT n FROM Nation n WHERE n.idStati = :idStati"),
    @NamedQuery(name = "Nation.findByName", query = "SELECT n FROM Nation n WHERE n.name = :name"),
    @NamedQuery(name = "Nation.findByIso3", query = "SELECT n FROM Nation n WHERE n.iso3 = :iso3"),
    @NamedQuery(name = "Nation.findByIso2", query = "SELECT n FROM Nation n WHERE n.iso2 = :iso2")})
public class Nation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_stati")
    private Integer idStati;
    @Column(name = "name")
    private String name;
    @Column(name = "iso3")
    private String iso3;
    @Column(name = "iso2")
    private String iso2;

    public Nation() {
    }

    public Nation(Integer idStati) {
        this.idStati = idStati;
    }

    public Integer getIdStati() {
        return idStati;
    }

    public void setIdStati(Integer idStati) {
        this.idStati = idStati;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStati != null ? idStati.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nation)) {
            return false;
        }
        Nation other = (Nation) object;
        if ((this.idStati == null && other.idStati != null) || (this.idStati != null && !this.idStati.equals(other.idStati))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Nation[ idStati=" + idStati + " ]";
    }
    
}
