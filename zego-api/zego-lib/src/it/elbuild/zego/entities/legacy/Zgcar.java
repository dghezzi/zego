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
@Table(name = "zgcar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgcar.findAll", query = "SELECT z FROM Zgcar z"),
    @NamedQuery(name = "Zgcar.findByCarId", query = "SELECT z FROM Zgcar z WHERE z.carId = :carId"),
    @NamedQuery(name = "Zgcar.findByUserId", query = "SELECT z FROM Zgcar z WHERE z.userId = :userId"),
    @NamedQuery(name = "Zgcar.findByPlate", query = "SELECT z FROM Zgcar z WHERE z.plate = :plate"),
    @NamedQuery(name = "Zgcar.findByManufacturerId", query = "SELECT z FROM Zgcar z WHERE z.manufacturerId = :manufacturerId"),
    @NamedQuery(name = "Zgcar.findByModel", query = "SELECT z FROM Zgcar z WHERE z.model = :model"),
    @NamedQuery(name = "Zgcar.findBySeats", query = "SELECT z FROM Zgcar z WHERE z.seats = :seats"),
    @NamedQuery(name = "Zgcar.findByTrunk", query = "SELECT z FROM Zgcar z WHERE z.trunk = :trunk"),
    @NamedQuery(name = "Zgcar.findByYear", query = "SELECT z FROM Zgcar z WHERE z.year = :year"),
    @NamedQuery(name = "Zgcar.findByBabyCarSeat", query = "SELECT z FROM Zgcar z WHERE z.babyCarSeat = :babyCarSeat"),
    @NamedQuery(name = "Zgcar.findByWheelchair", query = "SELECT z FROM Zgcar z WHERE z.wheelchair = :wheelchair"),
    @NamedQuery(name = "Zgcar.findByGroups", query = "SELECT z FROM Zgcar z WHERE z.groups = :groups"),
    @NamedQuery(name = "Zgcar.findByEmissionStandard", query = "SELECT z FROM Zgcar z WHERE z.emissionStandard = :emissionStandard"),
    @NamedQuery(name = "Zgcar.findByCreatedate", query = "SELECT z FROM Zgcar z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgcar.findByLastmoddate", query = "SELECT z FROM Zgcar z WHERE z.lastmoddate = :lastmoddate")})
public class Zgcar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "car_id")
    private Integer carId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "plate")
    private String plate;
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;
    @Column(name = "model")
    private String model;
    @Column(name = "seats")
    private Integer seats;
    @Column(name = "trunk")
    private Short trunk;
    @Column(name = "year")
    private Integer year;
    @Column(name = "baby_car_seat")
    private Short babyCarSeat;
    @Column(name = "wheelchair")
    private Short wheelchair;
    @Column(name = "groups")
    private Short groups;
    @Column(name = "emission_standard")
    private String emissionStandard;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public Zgcar() {
    }

    public Zgcar(Integer carId) {
        this.carId = carId;
    }

    public Zgcar(Integer carId, int userId) {
        this.carId = carId;
        this.userId = userId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Short getTrunk() {
        return trunk;
    }

    public void setTrunk(Short trunk) {
        this.trunk = trunk;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Short getBabyCarSeat() {
        return babyCarSeat;
    }

    public void setBabyCarSeat(Short babyCarSeat) {
        this.babyCarSeat = babyCarSeat;
    }

    public Short getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(Short wheelchair) {
        this.wheelchair = wheelchair;
    }

    public Short getGroups() {
        return groups;
    }

    public void setGroups(Short groups) {
        this.groups = groups;
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard;
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
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgcar)) {
            return false;
        }
        Zgcar other = (Zgcar) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgcar[ carId=" + carId + " ]";
    }
    
}
