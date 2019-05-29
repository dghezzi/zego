/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.RESTDateUtil;
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
@Table(name = "service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s"),
    @NamedQuery(name = "Service.findById", query = "SELECT s FROM Service s WHERE s.id = :id"),
    @NamedQuery(name = "Service.findByName", query = "SELECT s FROM Service s WHERE s.name = :name"),
    @NamedQuery(name = "Service.findByDetails", query = "SELECT s FROM Service s WHERE s.details = :details"),
    @NamedQuery(name = "Service.findByInsdate", query = "SELECT s FROM Service s WHERE s.insdate = :insdate")})
public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "details")
    private String details;
    @Column(name = "nameen")
    private String nameen;
    @Column(name = "detailsen")
    private String detailsen;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "level")
    private Integer level;
    @Column(name = "tohr")
    private Integer tohr;
    @Column(name = "fromhr")
    private Integer fromhr;

    public Service() {
    }

    public Service(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Service[ id=" + id + " ]";
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getTohr() {
        return tohr;
    }

    public void setTohr(Integer tohr) {
        this.tohr = tohr;
    }

    public Integer getFromhr() {
        return fromhr;
    }

    public void setFromhr(Integer fromhr) {
        this.fromhr = fromhr;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public String getDetailsen() {
        return detailsen;
    }

    public void setDetailsen(String detailsen) {
        this.detailsen = detailsen;
    }
    
    public boolean isVisible() {
        
        Integer sec = Integer.parseInt(RESTDateUtil.getInstance().secondsNow())%(24*60*60);
        Integer min = sec / 60;
        System.out.println("NOW "+min);
        if(tohr == null || fromhr == null || tohr-fromhr == 0) {
            return true;
        } else {
            return fromhr < tohr ? (fromhr < min && min < tohr) : !(tohr < min && min < fromhr);
        }
    }
    
}
