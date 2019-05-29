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
@Table(name = "userlogin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userlogin.findAll", query = "SELECT u FROM Userlogin u"),
    @NamedQuery(name = "Userlogin.findById", query = "SELECT u FROM Userlogin u WHERE u.id = :id"),
    @NamedQuery(name = "Userlogin.findByLogindate", query = "SELECT u FROM Userlogin u WHERE u.logindate = :logindate"),
    @NamedQuery(name = "Userlogin.findByUid", query = "SELECT u FROM Userlogin u WHERE u.uid = :uid"),
    @NamedQuery(name = "Userlogin.findByFname", query = "SELECT u FROM Userlogin u WHERE u.fname = :fname"),
    @NamedQuery(name = "Userlogin.findByLname", query = "SELECT u FROM Userlogin u WHERE u.lname = :lname"),
    @NamedQuery(name = "Userlogin.findBetween", query = "SELECT u FROM Userlogin u WHERE u.logindate BETWEEN :start and :stop"),
    @NamedQuery(name = "Userlogin.findByCandrive", query = "SELECT u FROM Userlogin u WHERE u.candrive = :candrive")})
public class Userlogin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "logindate")
    private String logindate;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "candrive")
    private Integer candrive;
    
    @Transient
    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public Userlogin() {
    }

    public Userlogin(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Integer getCandrive() {
        return candrive;
    }

    public void setCandrive(Integer candrive) {
        this.candrive = candrive;
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
        if (!(object instanceof Userlogin)) {
            return false;
        }
        Userlogin other = (Userlogin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public static String header() {
        return "ID;CANDRIVE;NAME;SURNAME;LOGIN DAY\n";
    }
    
    public  String data() {
        return uid+";"+candrive+";"+fname+";"+lname+";"+SDF.format(RESTDateUtil.getInstance().formatDateSeconds(logindate))+"\n";
    }
    
    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Userlogin[ id=" + id + " ]";
    }
    
}
