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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "zgride")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgride.findAll", query = "SELECT z FROM Zgride z"),
    @NamedQuery(name = "Zgride.findById", query = "SELECT z FROM Zgride z WHERE z.riderequestId = :id"),
    @NamedQuery(name = "Zgride.findByRiderequestId", query = "SELECT z FROM Zgride z WHERE z.riderequestId = :riderequestId"),
    @NamedQuery(name = "Zgride.findByRealPickupAddress", query = "SELECT z FROM Zgride z WHERE z.realPickupAddress = :realPickupAddress"),
    @NamedQuery(name = "Zgride.findByEnded", query = "SELECT z FROM Zgride z WHERE z.ended = :ended"),
    @NamedQuery(name = "Zgride.findByRealDropoffAddress", query = "SELECT z FROM Zgride z WHERE z.realDropoffAddress = :realDropoffAddress"),
    @NamedQuery(name = "Zgride.findByAborted", query = "SELECT z FROM Zgride z WHERE z.aborted = :aborted"),
    @NamedQuery(name = "Zgride.findByRealPickupLon", query = "SELECT z FROM Zgride z WHERE z.realPickupLon = :realPickupLon"),
    @NamedQuery(name = "Zgride.findByRealPickupLat", query = "SELECT z FROM Zgride z WHERE z.realPickupLat = :realPickupLat"),
    @NamedQuery(name = "Zgride.findByRealPickupTime", query = "SELECT z FROM Zgride z WHERE z.realPickupTime = :realPickupTime"),
    @NamedQuery(name = "Zgride.findByRealDropoffLon", query = "SELECT z FROM Zgride z WHERE z.realDropoffLon = :realDropoffLon"),
    @NamedQuery(name = "Zgride.findByRealDropoffLat", query = "SELECT z FROM Zgride z WHERE z.realDropoffLat = :realDropoffLat"),
    @NamedQuery(name = "Zgride.findByRealDropoffTime", query = "SELECT z FROM Zgride z WHERE z.realDropoffTime = :realDropoffTime"),
    @NamedQuery(name = "Zgride.findByRiderequestAborted", query = "SELECT z FROM Zgride z WHERE z.riderequestAborted = :riderequestAborted"),
    @NamedQuery(name = "Zgride.findByCreatedate", query = "SELECT z FROM Zgride z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgride.findByLastmoddate", query = "SELECT z FROM Zgride z WHERE z.lastmoddate = :lastmoddate")})
public class Zgride implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "riderequest_id")
    private Integer riderequestId;
    @Column(name = "real_pickup_address")
    private String realPickupAddress;
    @Column(name = "ended")
    private Short ended;
    @Column(name = "real_dropoff_address")
    private String realDropoffAddress;
    @Column(name = "aborted")
    private Short aborted;
    @Lob
    @Column(name = "abort_reason")
    private String abortReason;
    @Lob
    @Column(name = "driver_locations")
    private String driverLocations;
    @Lob
    @Column(name = "passenger_locations")
    private String passengerLocations;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "real_pickup_lon")
    private Double realPickupLon;
    @Column(name = "real_pickup_lat")
    private Double realPickupLat;
    @Column(name = "real_pickup_time")
    private String realPickupTime;
    @Column(name = "real_dropoff_lon")
    private Double realDropoffLon;
    @Column(name = "real_dropoff_lat")
    private Double realDropoffLat;
    @Column(name = "real_dropoff_time")
    private String realDropoffTime;
    @Column(name = "riderequest_aborted")
    private Short riderequestAborted;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public Zgride() {
    }

    public Zgride(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public Integer getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public String getRealPickupAddress() {
        return realPickupAddress;
    }

    public void setRealPickupAddress(String realPickupAddress) {
        this.realPickupAddress = realPickupAddress;
    }

    public Short getEnded() {
        return ended;
    }

    public void setEnded(Short ended) {
        this.ended = ended;
    }

    public String getRealDropoffAddress() {
        return realDropoffAddress;
    }

    public void setRealDropoffAddress(String realDropoffAddress) {
        this.realDropoffAddress = realDropoffAddress;
    }

    public Short getAborted() {
        return aborted;
    }

    public void setAborted(Short aborted) {
        this.aborted = aborted;
    }

    public String getAbortReason() {
        return abortReason;
    }

    public void setAbortReason(String abortReason) {
        this.abortReason = abortReason;
    }

    public String getDriverLocations() {
        return driverLocations;
    }

    public void setDriverLocations(String driverLocations) {
        this.driverLocations = driverLocations;
    }

    public String getPassengerLocations() {
        return passengerLocations;
    }

    public void setPassengerLocations(String passengerLocations) {
        this.passengerLocations = passengerLocations;
    }

    public Double getRealPickupLon() {
        return realPickupLon;
    }

    public void setRealPickupLon(Double realPickupLon) {
        this.realPickupLon = realPickupLon;
    }

    public Double getRealPickupLat() {
        return realPickupLat;
    }

    public void setRealPickupLat(Double realPickupLat) {
        this.realPickupLat = realPickupLat;
    }

    public String getRealPickupTime() {
        return realPickupTime;
    }

    public void setRealPickupTime(String realPickupTime) {
        this.realPickupTime = realPickupTime;
    }

    public Double getRealDropoffLon() {
        return realDropoffLon;
    }

    public void setRealDropoffLon(Double realDropoffLon) {
        this.realDropoffLon = realDropoffLon;
    }

    public Double getRealDropoffLat() {
        return realDropoffLat;
    }

    public void setRealDropoffLat(Double realDropoffLat) {
        this.realDropoffLat = realDropoffLat;
    }

    public String getRealDropoffTime() {
        return realDropoffTime;
    }

    public void setRealDropoffTime(String realDropoffTime) {
        this.realDropoffTime = realDropoffTime;
    }

    public Short getRiderequestAborted() {
        return riderequestAborted;
    }

    public void setRiderequestAborted(Short riderequestAborted) {
        this.riderequestAborted = riderequestAborted;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riderequestId != null ? riderequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgride)) {
            return false;
        }
        Zgride other = (Zgride) object;
        if ((this.riderequestId == null && other.riderequestId != null) || (this.riderequestId != null && !this.riderequestId.equals(other.riderequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgride[ riderequestId=" + riderequestId + " ]";
    }
    
}
