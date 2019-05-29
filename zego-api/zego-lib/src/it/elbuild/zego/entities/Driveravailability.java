/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
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
@Table(name = "driveravailability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Driveravailability.findAll", query = "SELECT d FROM Driveravailability d"),
    @NamedQuery(name = "Driveravailability.findById", query = "SELECT d FROM Driveravailability d WHERE d.id = :id"),
    @NamedQuery(name = "Driveravailability.findByZid", query = "SELECT d FROM Driveravailability d WHERE d.zid = :zid"),
    @NamedQuery(name = "Driveravailability.findByZonename", query = "SELECT d FROM Driveravailability d WHERE d.zonename = :zonename"),
    @NamedQuery(name = "Driveravailability.findByDid", query = "SELECT d FROM Driveravailability d WHERE d.did = :did"),
    @NamedQuery(name = "Driveravailability.findByFname", query = "SELECT d FROM Driveravailability d WHERE d.fname = :fname"),
    @NamedQuery(name = "Driveravailability.findByLname", query = "SELECT d FROM Driveravailability d WHERE d.lname = :lname"),
    @NamedQuery(name = "Driveravailability.findByZidBetween", query = "SELECT d FROM Driveravailability d WHERE d.zid = :zid and d.avadate BETWEEN :start and :stop"),    
    @NamedQuery(name = "Driveravailability.findBetween", query = "SELECT d FROM Driveravailability d WHERE d.avadate BETWEEN :start and :stop"),    
    @NamedQuery(name = "Driveravailability.findByAvadate", query = "SELECT d FROM Driveravailability d WHERE d.avadate = :avadate")})
public class Driveravailability implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "zid")
    private Integer zid;
    @Column(name = "zonename")
    private String zonename;
    @Column(name = "did")
    private Integer did;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "avadate")
    private String avadate;

    @Transient
    private Integer offsetMillis = TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis());

    @Transient
    private static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
    
    public Driveravailability() {
    }

    public Driveravailability(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
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

    public String getAvadate() {
        return avadate;
    }

    public void setAvadate(String avadate) {
        this.avadate = avadate;
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
        if (!(object instanceof Driveravailability)) {
            return false;
        }
        Driveravailability other = (Driveravailability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public static String header() {
        return "Range orario;ID;Nome;Cognome\n";
    }
    
    public  String data() {        
        Long ll = Long.parseLong(avadate);
        ll = (ll / 900) * 900;
        ll = ll + offsetMillis/1000;        
        return SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(ll)))+";"+id+";"+fname+";"+lname+"\n";
    }
    
    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Driveravailability[ id=" + id + " ]";
    }
    
}
