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
@Table(name = "appversion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appversion.findAll", query = "SELECT a FROM Appversion a"),
    @NamedQuery(name = "Appversion.findById", query = "SELECT a FROM Appversion a WHERE a.id = :id"),
    @NamedQuery(name = "Appversion.findByVers", query = "SELECT a FROM Appversion a WHERE a.vers = :vers and a.os = :os"),
    @NamedQuery(name = "Appversion.findByReleasedate", query = "SELECT a FROM Appversion a WHERE a.releasedate = :releasedate"),
    @NamedQuery(name = "Appversion.findByStatus", query = "SELECT a FROM Appversion a WHERE a.status = :status"),
    @NamedQuery(name = "Appversion.findByDeprecatedate", query = "SELECT a FROM Appversion a WHERE a.deprecatedate = :deprecatedate"),
    @NamedQuery(name = "Appversion.findByNote", query = "SELECT a FROM Appversion a WHERE a.note = :note")})
public class Appversion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "os")
    private Integer os;
    @Column(name = "vers")
    private String vers;
    @Column(name = "releasedate")
    private String releasedate;
    @Column(name = "status")
    private String status;
    @Column(name = "deprecatedate")
    private String deprecatedate;
    @Column(name = "note")
    private String note;

    public static final String STATUS_OBSOLETE = "obsolete";
    public static final String STATUS_DEPRECATED = "deprecated";
    public static final String STATUS_CURRENT = "current";
    
    public Appversion() {
    }

    public Appversion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVers() {
        return vers;
    }

    public void setVers(String vers) {
        this.vers = vers;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeprecatedate() {
        return deprecatedate;
    }

    public void setDeprecatedate(String deprecatedate) {
        this.deprecatedate = deprecatedate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
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
        if (!(object instanceof Appversion)) {
            return false;
        }
        Appversion other = (Appversion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Appversion[ id=" + id + " ]";
    }
    
}
