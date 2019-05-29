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
@Table(name = "errordata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Errordata.findAll", query = "SELECT e FROM Errordata e"),
    @NamedQuery(name = "Errordata.findById", query = "SELECT e FROM Errordata e WHERE e.id = :id"),
    @NamedQuery(name = "Errordata.findByCode", query = "SELECT e FROM Errordata e WHERE e.code = :code"),
    @NamedQuery(name = "Errordata.findByMsg", query = "SELECT e FROM Errordata e WHERE e.msg = :msg"),
    @NamedQuery(name = "Errordata.findBetween", query = "SELECT e FROM Errordata e WHERE e.errdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Errordata.findByErrdate", query = "SELECT e FROM Errordata e WHERE e.errdate = :errdate"),
    @NamedQuery(name = "Errordata.findByUid", query = "SELECT e FROM Errordata e WHERE e.uid = :uid")})
public class Errordata implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
    private Integer code;
    @Column(name = "msg")
    private String msg;
    @Column(name = "errdate")
    private String errdate;
    @Column(name = "uid")
    private Integer uid;

    @Transient
    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public Errordata() {
    }

    public Errordata(Integer id) {
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrdate() {
        return errdate;
    }

    public void setErrdate(String errdate) {
        this.errdate = errdate;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
        if (!(object instanceof Errordata)) {
            return false;
        }
        Errordata other = (Errordata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public static String header() {
        return "CODICE;DESCRIZIONE;ORA\n";
    }
    
    public  String data() {
        return code+";"+msg+";"+SDF.format(RESTDateUtil.getInstance().formatDateSeconds(errdate))+"\n";
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Errordata[ id=" + id + " ]";
    }
    
}
