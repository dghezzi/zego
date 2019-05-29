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
@Table(name = "lang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lang.findAll", query = "SELECT l FROM Lang l"),
    @NamedQuery(name = "Lang.findById", query = "SELECT l FROM Lang l WHERE l.id = :id"),
    @NamedQuery(name = "Lang.findByLang", query = "SELECT l FROM Lang l WHERE l.lang = :lang"),
    @NamedQuery(name = "Lang.findByIsocode", query = "SELECT l FROM Lang l WHERE l.isocode = :isocode"),
    @NamedQuery(name = "Lang.findBySort", query = "SELECT l FROM Lang l WHERE l.sort = :sort")})
public class Lang implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "lang")
    private String lang;
    @Column(name = "isocode")
    private String isocode;
    @Column(name = "sort")
    private Integer sort;

    public Lang() {
    }

    public Lang(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getIsocode() {
        return isocode;
    }

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        if (!(object instanceof Lang)) {
            return false;
        }
        Lang other = (Lang) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Lang[ id=" + id + " ]";
    }
    
}
