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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "riderequest_drivers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RiderequestDrivers.findAll", query = "SELECT r FROM RiderequestDrivers r"),
    @NamedQuery(name = "RiderequestDrivers.findById", query = "SELECT r FROM RiderequestDrivers r WHERE r.id = :id"),
    @NamedQuery(name = "RiderequestDrivers.findByRid", query = "SELECT r FROM RiderequestDrivers r WHERE r.rid = :rid"),
    @NamedQuery(name = "RiderequestDrivers.findByRidAndDid", query = "SELECT r FROM RiderequestDrivers r WHERE r.rid = :rid and r.did = :did order by r.id desc"),
    @NamedQuery(name = "RiderequestDrivers.findByDid", query = "SELECT r FROM RiderequestDrivers r WHERE r.did = :did order by r.id desc"),
    @NamedQuery(name = "RiderequestDrivers.findByStatus", query = "SELECT r FROM RiderequestDrivers r WHERE r.status = :status"),
    @NamedQuery(name = "RiderequestDrivers.findByCreated", query = "SELECT r FROM RiderequestDrivers r WHERE r.created = :created"),
    @NamedQuery(name = "RiderequestDrivers.findByCreatedBetween", query = "SELECT r FROM RiderequestDrivers r WHERE r.created BETWEEN :start and :stop"),
    @NamedQuery(name = "RiderequestDrivers.findByRejected", query = "SELECT r FROM RiderequestDrivers r WHERE r.rejected = :rejected"),
    @NamedQuery(name = "RiderequestDrivers.findByCanceled", query = "SELECT r FROM RiderequestDrivers r WHERE r.canceled = :canceled"),
    @NamedQuery(name = "RiderequestDrivers.findByAccepted", query = "SELECT r FROM RiderequestDrivers r WHERE r.accepted = :accepted"),
    @NamedQuery(name = "RiderequestDrivers.findByDmeters", query = "SELECT r FROM RiderequestDrivers r WHERE r.dmeters = :dmeters"),
    @NamedQuery(name = "RiderequestDrivers.findByValidfrom", query = "SELECT r FROM RiderequestDrivers r WHERE r.validfrom = :validfrom")})
public class RiderequestDrivers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "did")
    private Integer did;
    @Column(name = "status")
    private Integer status;
    @Column(name = "created")
    private String created;
    @Column(name = "rejected")
    private String rejected;
    @Column(name = "canceled")
    private String canceled;
    @Column(name = "accepted")
    private String accepted;
    @Column(name = "dmeters")
    private Integer dmeters;
    @Column(name = "dsec")
    private Integer dsec;
    @Column(name = "addr")
    private String addr;
    @Column(name = "validfrom")
    private Integer validfrom;

    @Transient
    private String fname;
    
    @Transient
    private String lname;
    
    public static final Integer RIDEREQUESTDRIVERS_CREATED = 1;
    public static final Integer RIDEREQUESTDRIVERS_CANCELED = 2;
    public static final Integer RIDEREQUESTDRIVERS_REJECTED = 3;
    public static final Integer RIDEREQUESTDRIVERS_ACCEPTED = 4;
    public static final Integer RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER = 5;
    public static final Integer RIDEREQUESTDRIVERS_TOOLATE = 6;
    public RiderequestDrivers() {
    }

    public RiderequestDrivers(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getCanceled() {
        return canceled;
    }

    public void setCanceled(String canceled) {
        this.canceled = canceled;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public Integer getDmeters() {
        return dmeters;
    }

    public void setDmeters(Integer dmeters) {
        this.dmeters = dmeters;
    }

    public Integer getValidfrom() {
        return validfrom;
    }

    public Integer getDsec() {
        return dsec;
    }

    public void setDsec(Integer dsec) {
        this.dsec = dsec;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    
    public void setValidfrom(Integer validfrom) {
        this.validfrom = validfrom;
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
        if (!(object instanceof RiderequestDrivers)) {
            return false;
        }
        RiderequestDrivers other = (RiderequestDrivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.RiderequestDrivers[ id=" + id + " ]";
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
    
    
}
