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
@Table(name = "dialog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dialog.findAll", query = "SELECT d FROM Dialog d"),
    @NamedQuery(name = "Dialog.findById", query = "SELECT d FROM Dialog d WHERE d.id = :id"),
    @NamedQuery(name = "Dialog.findByType", query = "SELECT d FROM Dialog d WHERE d.type = :type ORDER by d.validfrom DESC"),
    @NamedQuery(name = "Dialog.findByValidfrom", query = "SELECT d FROM Dialog d WHERE d.validfrom = :validfrom"),
    @NamedQuery(name = "Dialog.findByValidto", query = "SELECT d FROM Dialog d WHERE d.validto = :validto"),
    @NamedQuery(name = "Dialog.findByUrl", query = "SELECT d FROM Dialog d WHERE d.url = :url"),
    @NamedQuery(name = "Dialog.findByTitle", query = "SELECT d FROM Dialog d WHERE d.title = :title"),
    @NamedQuery(name = "Dialog.findByMessage", query = "SELECT d FROM Dialog d WHERE d.message = :message")})
public class Dialog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "type")
    private String type;
    @Column(name = "validfrom")
    private String validfrom;
    @Column(name = "validto")
    private String validto;
    @Column(name = "url")
    private String url;
    @Column(name = "title")
    private String title;
    @Column(name = "message")
    private String message;

    public static final String DIALOG_TYPE_TC = "tc";
    public static final String DIALOG_TYPE_DISCLAIMER = "disclaimer";
    public Dialog() {
    }

    public Dialog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof Dialog)) {
            return false;
        }
        Dialog other = (Dialog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Dialog[ id=" + id + " ]";
    }
    
}
