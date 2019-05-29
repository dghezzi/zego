/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities.legacy;

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
@Table(name = "zgriderequest_drivers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZgriderequestDrivers.findAll", query = "SELECT z FROM ZgriderequestDrivers z"),
    @NamedQuery(name = "ZgriderequestDrivers.findByRiderequestId", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.riderequestId = :riderequestId"),
    @NamedQuery(name = "ZgriderequestDrivers.findByStatus", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.status = :status"),
    @NamedQuery(name = "ZgriderequestDrivers.findByCreatedate", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "ZgriderequestDrivers.findByLastmoddate", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.lastmoddate = :lastmoddate"),
    @NamedQuery(name = "ZgriderequestDrivers.findByValidfromdate", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.validfromdate = :validfromdate"),
    @NamedQuery(name = "ZgriderequestDrivers.findByDistance", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.distance = :distance"),
    @NamedQuery(name = "ZgriderequestDrivers.findById", query = "SELECT z FROM ZgriderequestDrivers z WHERE z.id = :id")})
public class ZgriderequestDrivers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "riderequest_id")
    private int riderequestId;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;
    @Column(name = "validfromdate")
    private String validfromdate;
    @Column(name = "distance")
    private Double distance;
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public ZgriderequestDrivers() {
    }

    public ZgriderequestDrivers(Integer id) {
        this.id = id;
    }

    public ZgriderequestDrivers(Integer id, int riderequestId, short status) {
        this.id = id;
        this.riderequestId = riderequestId;
        this.status = status;
    }

    public int getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(int riderequestId) {
        this.riderequestId = riderequestId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getLastmoddate() {
        return lastmoddate;
    }

    public void setLastmoddate(String lastmoddate) {
        this.lastmoddate = lastmoddate;
    }

    public String getValidfromdate() {
        return validfromdate;
    }

    public void setValidfromdate(String validfromdate) {
        this.validfromdate = validfromdate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        if (!(object instanceof ZgriderequestDrivers)) {
            return false;
        }
        ZgriderequestDrivers other = (ZgriderequestDrivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.ZgriderequestDrivers[ id=" + id + " ]";
    }
    
}
