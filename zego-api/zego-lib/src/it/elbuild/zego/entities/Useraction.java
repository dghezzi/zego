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
@Table(name = "useraction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Useraction.findAll", query = "SELECT u FROM Useraction u"),
    @NamedQuery(name = "Useraction.findById", query = "SELECT u FROM Useraction u WHERE u.id = :id"),
    @NamedQuery(name = "Useraction.findByUmode", query = "SELECT u FROM Useraction u WHERE u.umode = :umode"),
    @NamedQuery(name = "Useraction.findByUid", query = "SELECT u FROM Useraction u WHERE u.uid = :uid"),
    @NamedQuery(name = "Useraction.findByRid", query = "SELECT u FROM Useraction u WHERE u.rid = :rid"),
    @NamedQuery(name = "Useraction.findByType", query = "SELECT u FROM Useraction u WHERE u.type = :type"),
    @NamedQuery(name = "Useraction.findByInsdate", query = "SELECT u FROM Useraction u WHERE u.insdate = :insdate")})
public class Useraction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "umode")
    private String umode;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "type")
    private String type;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "raw")
    private String raw;
    @Column(name = "errdata")
    private String errdata;
    
    public Useraction() {
    }

    public Useraction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUmode() {
        return umode;
    }

    public void setUmode(String umode) {
        this.umode = umode;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
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
        if (!(object instanceof Useraction)) {
            return false;
        }
        Useraction other = (Useraction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Useraction[ id=" + id + " ]";
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * @return the errdata
     */
    public String getErrdata() {
        return errdata;
    }

    /**
     * @param errdata the errdata to set
     */
    public void setErrdata(String errdata) {
        this.errdata = errdata;
    }
    
    
}
