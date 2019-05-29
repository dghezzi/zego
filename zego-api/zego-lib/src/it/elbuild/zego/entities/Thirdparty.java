/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "thirdparty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Thirdparty.findAll", query = "SELECT t FROM Thirdparty t"),
    @NamedQuery(name = "Thirdparty.findById", query = "SELECT t FROM Thirdparty t WHERE t.id = :id"),
    @NamedQuery(name = "Thirdparty.findByReqdate", query = "SELECT t FROM Thirdparty t WHERE t.reqdate = :reqdate"),
    @NamedQuery(name = "Thirdparty.findByPath", query = "SELECT t FROM Thirdparty t WHERE t.path = :path"),
    @NamedQuery(name = "Thirdparty.findBetween", query = "SELECT t FROM Thirdparty t WHERE t.reqdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Thirdparty.findByRespcode", query = "SELECT t FROM Thirdparty t WHERE t.respcode = :respcode")})
public class Thirdparty implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reqdate")
    private String reqdate;
    @Column(name = "path")
    private String path;
    @Column(name = "respcode")
    private String respcode;
    
    @Transient
    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public Thirdparty() {
    }

    public Thirdparty(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
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
        if (!(object instanceof Thirdparty)) {
            return false;
        }
        Thirdparty other = (Thirdparty) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Thirdparty[ id=" + id + " ]";
    }
    
    public static String header() {
        return "ID;API;TIMESTAMP;ESITO\n";
    }
    
    public  String data() {
        return id+";"+path+";"+SDF.format(RESTDateUtil.getInstance().formatDateSeconds(reqdate))+";"+respcode+"\n";
    }
    
}
