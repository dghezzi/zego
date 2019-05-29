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
@Table(name = "errormsg")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Errormsg.findAll", query = "SELECT e FROM Errormsg e"),
    @NamedQuery(name = "Errormsg.findById", query = "SELECT e FROM Errormsg e WHERE e.id = :id"),
    @NamedQuery(name = "Errormsg.findByCode", query = "SELECT e FROM Errormsg e WHERE e.code = :code"),
    @NamedQuery(name = "Errormsg.findByLang", query = "SELECT e FROM Errormsg e WHERE e.lang = :lang"),
    @NamedQuery(name = "Errormsg.findByTitle", query = "SELECT e FROM Errormsg e WHERE e.title = :title"),
    @NamedQuery(name = "Errormsg.findByMessage", query = "SELECT e FROM Errormsg e WHERE e.message = :message")})
public class Errormsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
    private Integer code;
    @Column(name = "lang")
    private String lang;
    @Column(name = "title")
    private String title;
    @Column(name = "message")
    private String message;

    public Errormsg() {
    }

    public Errormsg(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof Errormsg)) {
            return false;
        }
        Errormsg other = (Errormsg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Errormsg[ id=" + id + " ]";
    }
    
}
