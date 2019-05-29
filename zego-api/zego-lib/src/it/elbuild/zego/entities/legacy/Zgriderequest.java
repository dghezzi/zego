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
@Table(name = "zgriderequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgriderequest.findAll", query = "SELECT z FROM Zgriderequest z"),
    @NamedQuery(name = "Zgriderequest.findByRiderequestId", query = "SELECT z FROM Zgriderequest z WHERE z.riderequestId = :riderequestId"),
    @NamedQuery(name = "Zgriderequest.findByPassenger", query = "SELECT z FROM Zgriderequest z WHERE z.passenger = :passenger"),
    @NamedQuery(name = "Zgriderequest.findByCancelledByUser", query = "SELECT z FROM Zgriderequest z WHERE z.cancelledByUser = :cancelledByUser"),
    @NamedQuery(name = "Zgriderequest.findByHandled", query = "SELECT z FROM Zgriderequest z WHERE z.handled = :handled"),
    @NamedQuery(name = "Zgriderequest.findBySubmissionDate", query = "SELECT z FROM Zgriderequest z WHERE z.submissionDate = :submissionDate"),
    @NamedQuery(name = "Zgriderequest.findByCancellationDate", query = "SELECT z FROM Zgriderequest z WHERE z.cancellationDate = :cancellationDate"),
    @NamedQuery(name = "Zgriderequest.findByConfirmationDate", query = "SELECT z FROM Zgriderequest z WHERE z.confirmationDate = :confirmationDate"),
    @NamedQuery(name = "Zgriderequest.findByPickupAddress", query = "SELECT z FROM Zgriderequest z WHERE z.pickupAddress = :pickupAddress"),
    @NamedQuery(name = "Zgriderequest.findByPickupLocationLon", query = "SELECT z FROM Zgriderequest z WHERE z.pickupLocationLon = :pickupLocationLon"),
    @NamedQuery(name = "Zgriderequest.findByPickupLocationLat", query = "SELECT z FROM Zgriderequest z WHERE z.pickupLocationLat = :pickupLocationLat"),
    @NamedQuery(name = "Zgriderequest.findByDropoffAddress", query = "SELECT z FROM Zgriderequest z WHERE z.dropoffAddress = :dropoffAddress"),
    @NamedQuery(name = "Zgriderequest.findByDropoffLocationLon", query = "SELECT z FROM Zgriderequest z WHERE z.dropoffLocationLon = :dropoffLocationLon"),
    @NamedQuery(name = "Zgriderequest.findByDropoffLocationLat", query = "SELECT z FROM Zgriderequest z WHERE z.dropoffLocationLat = :dropoffLocationLat"),
    @NamedQuery(name = "Zgriderequest.findBySuggestedPrice", query = "SELECT z FROM Zgriderequest z WHERE z.suggestedPrice = :suggestedPrice"),
    @NamedQuery(name = "Zgriderequest.findBySuggestedPriceCurrency", query = "SELECT z FROM Zgriderequest z WHERE z.suggestedPriceCurrency = :suggestedPriceCurrency"),
    @NamedQuery(name = "Zgriderequest.findBySuggestedFee", query = "SELECT z FROM Zgriderequest z WHERE z.suggestedFee = :suggestedFee"),
    @NamedQuery(name = "Zgriderequest.findByDriverId", query = "SELECT z FROM Zgriderequest z WHERE z.driverId = :driverId"),
    @NamedQuery(name = "Zgriderequest.findByDriverDistance", query = "SELECT z FROM Zgriderequest z WHERE z.driverDistance = :driverDistance"),
    @NamedQuery(name = "Zgriderequest.findByDriverEta", query = "SELECT z FROM Zgriderequest z WHERE z.driverEta = :driverEta"),
    @NamedQuery(name = "Zgriderequest.findByBabyCarSeat", query = "SELECT z FROM Zgriderequest z WHERE z.babyCarSeat = :babyCarSeat"),
    @NamedQuery(name = "Zgriderequest.findByWheelchair", query = "SELECT z FROM Zgriderequest z WHERE z.wheelchair = :wheelchair"),
    @NamedQuery(name = "Zgriderequest.findByGroups", query = "SELECT z FROM Zgriderequest z WHERE z.groups = :groups"),
    @NamedQuery(name = "Zgriderequest.findByCreatedate", query = "SELECT z FROM Zgriderequest z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgriderequest.findByLastmoddate", query = "SELECT z FROM Zgriderequest z WHERE z.lastmoddate = :lastmoddate")})
public class Zgriderequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "riderequest_id")
    private Integer riderequestId;
    @Basic(optional = false)
    @Column(name = "passenger")
    private int passenger;
    @Column(name = "cancelled_by_user")
    private Short cancelledByUser;
    @Column(name = "handled")
    private Short handled;
    @Column(name = "submission_date")
    private String submissionDate;
    @Column(name = "cancellation_date")
    private String cancellationDate;
    @Column(name = "confirmation_date")
    private String confirmationDate;
    @Column(name = "pickup_address")
    private String pickupAddress;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pickup_location_lon")
    private Double pickupLocationLon;
    @Column(name = "pickup_location_lat")
    private Double pickupLocationLat;
    @Column(name = "dropoff_address")
    private String dropoffAddress;
    @Column(name = "dropoff_location_lon")
    private Double dropoffLocationLon;
    @Column(name = "dropoff_location_lat")
    private Double dropoffLocationLat;
    @Column(name = "suggested_price")
    private Integer suggestedPrice;
    @Column(name = "suggested_price_currency")
    private Integer suggestedPriceCurrency;
    @Column(name = "suggested_fee")
    private Integer suggestedFee;
    @Column(name = "driver_id")
    private Integer driverId;
    @Column(name = "driver_distance")
    private Integer driverDistance;
    @Column(name = "driver_eta")
    private Integer driverEta;
    @Basic(optional = false)
    @Column(name = "baby_car_seat")
    private short babyCarSeat;
    @Basic(optional = false)
    @Column(name = "wheelchair")
    private short wheelchair;
    @Basic(optional = false)
    @Column(name = "groups")
    private short groups;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public Zgriderequest() {
    }

    public Zgriderequest(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public Zgriderequest(Integer riderequestId, int passenger, short babyCarSeat, short wheelchair, short groups) {
        this.riderequestId = riderequestId;
        this.passenger = passenger;
        this.babyCarSeat = babyCarSeat;
        this.wheelchair = wheelchair;
        this.groups = groups;
    }

    public Integer getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public Short getCancelledByUser() {
        return cancelledByUser;
    }

    public void setCancelledByUser(Short cancelledByUser) {
        this.cancelledByUser = cancelledByUser;
    }

    public Short getHandled() {
        return handled;
    }

    public void setHandled(Short handled) {
        this.handled = handled;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Double getPickupLocationLon() {
        return pickupLocationLon;
    }

    public void setPickupLocationLon(Double pickupLocationLon) {
        this.pickupLocationLon = pickupLocationLon;
    }

    public Double getPickupLocationLat() {
        return pickupLocationLat;
    }

    public void setPickupLocationLat(Double pickupLocationLat) {
        this.pickupLocationLat = pickupLocationLat;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public Double getDropoffLocationLon() {
        return dropoffLocationLon;
    }

    public void setDropoffLocationLon(Double dropoffLocationLon) {
        this.dropoffLocationLon = dropoffLocationLon;
    }

    public Double getDropoffLocationLat() {
        return dropoffLocationLat;
    }

    public void setDropoffLocationLat(Double dropoffLocationLat) {
        this.dropoffLocationLat = dropoffLocationLat;
    }

    public Integer getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(Integer suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public Integer getSuggestedPriceCurrency() {
        return suggestedPriceCurrency;
    }

    public void setSuggestedPriceCurrency(Integer suggestedPriceCurrency) {
        this.suggestedPriceCurrency = suggestedPriceCurrency;
    }

    public Integer getSuggestedFee() {
        return suggestedFee == null ? 0 : suggestedFee;
    }

    public void setSuggestedFee(Integer suggestedFee) {
        this.suggestedFee = suggestedFee;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getDriverDistance() {
        return driverDistance;
    }

    public void setDriverDistance(Integer driverDistance) {
        this.driverDistance = driverDistance;
    }

    public Integer getDriverEta() {
        return driverEta;
    }

    public void setDriverEta(Integer driverEta) {
        this.driverEta = driverEta;
    }

    public short getBabyCarSeat() {
        return babyCarSeat;
    }

    public void setBabyCarSeat(short babyCarSeat) {
        this.babyCarSeat = babyCarSeat;
    }

    public short getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(short wheelchair) {
        this.wheelchair = wheelchair;
    }

    public short getGroups() {
        return groups;
    }

    public void setGroups(short groups) {
        this.groups = groups;
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
        if (!(object instanceof Zgriderequest)) {
            return false;
        }
        Zgriderequest other = (Zgriderequest) object;
        if ((this.riderequestId == null && other.riderequestId != null) || (this.riderequestId != null && !this.riderequestId.equals(other.riderequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgriderequest[ riderequestId=" + riderequestId + " ]";
    }
    
}
