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
@Table(name = "driverdata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Driverdata.findAll", query = "SELECT d FROM Driverdata d"),
    @NamedQuery(name = "Driverdata.findById", query = "SELECT d FROM Driverdata d WHERE d.id = :id"),
    @NamedQuery(name = "Driverdata.findByUid", query = "SELECT d FROM Driverdata d WHERE d.uid = :uid"),
    @NamedQuery(name = "Driverdata.findByBrand", query = "SELECT d FROM Driverdata d WHERE d.brand = :brand"),
    @NamedQuery(name = "Driverdata.findByModel", query = "SELECT d FROM Driverdata d WHERE d.model = :model"),
    @NamedQuery(name = "Driverdata.findByColor", query = "SELECT d FROM Driverdata d WHERE d.color = :color"),
    @NamedQuery(name = "Driverdata.findByPlate", query = "SELECT d FROM Driverdata d WHERE d.plate = :plate"),
    @NamedQuery(name = "Driverdata.findBySeat", query = "SELECT d FROM Driverdata d WHERE d.seat = :seat"),
    @NamedQuery(name = "Driverdata.findByYear", query = "SELECT d FROM Driverdata d WHERE d.year = :year"),
    @NamedQuery(name = "Driverdata.findByCarimg", query = "SELECT d FROM Driverdata d WHERE d.carimg = :carimg"),
    @NamedQuery(name = "Driverdata.findByInsdate", query = "SELECT d FROM Driverdata d WHERE d.insdate = :insdate"),
    @NamedQuery(name = "Driverdata.findByStatusBetweendateasc", query = "SELECT d FROM Driverdata d WHERE d.status = :status order by d.insdate asc"),
    @NamedQuery(name = "Driverdata.findByStatusBetweendatedesc", query = "SELECT d FROM Driverdata d WHERE d.status = :status order by d.insdate desc"),
    @NamedQuery(name = "Driverdata.findByExpdate", query = "SELECT d FROM Driverdata d WHERE d.expdate = :expdate"),
    @NamedQuery(name = "Driverdata.findByModdate", query = "SELECT d FROM Driverdata d WHERE d.moddate = :moddate"),
    @NamedQuery(name = "Driverdata.countByStatusAndArea", query = "SELECT count(d) FROM Driverdata d WHERE d.status = :status and d.area = :area and d.insdate between :start and :stop"),
    @NamedQuery(name = "Driverdata.countByStatusAndAreaNA", query = "SELECT count(d) FROM Driverdata d WHERE d.status = :status and d.area NOT IN :area and d.insdate between :start and :stop"),
    @NamedQuery(name = "Driverdata.countByStatusInAndArea", query = "SELECT count(d) FROM Driverdata d WHERE d.status IN :status and d.area = :area and d.insdate between :start and :stop"),
    @NamedQuery(name = "Driverdata.findByDocok", query = "SELECT d FROM Driverdata d WHERE d.docok = :docok")})
public class Driverdata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "color")
    private String color;
    @Column(name = "plate")
    private String plate;
    @Column(name = "seat")
    private Integer seat;
    @Column(name = "year")
    private Integer year;
    @Column(name = "carimg")
    private String carimg;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "expdate")
    private String expdate;
    @Column(name = "moddate")
    private String moddate;
    @Column(name = "insuranceimg")
    private String insuranceimg;
    @Column(name = "insuranceexpdate")
    private String insuranceexpdate;
    @Column(name = "docexpdate")
    private String docexpdate;
    @Column(name = "docimg")
    private String docimg;
    @Column(name = "birthcountry")
    private String birthcountry;
    @Column(name = "birthcity")
    private String birthcity;
    @Column(name = "cf")
    private String cf;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "cap")
    private String cap;
    @Column(name = "iban")
    private String iban;
    @Column(name = "area")
    private String area;
    @Column(name = "status")
    private Integer status;    
    @Column(name = "docok")
    private Integer docok;

    public static final Integer STATUS_CANDIDATE = 1;
    public static final Integer STATUS_ACCEPTED = 2;
    public static final Integer STATUS_REJECTED = 3;
    public static final Integer INTEGRATION_REQUESTED = 4;
    public static final Integer STATUS_DOC_SENT = 5;
    public static final Integer STATUS_DOC_EXPIRED = 6;
    
    @Transient
    User user;
    
    public Driverdata() {
    
    }

    public Driverdata(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getModdate() {
        return moddate;
    }

    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    public Integer getDocok() {
        return docok;
    }

    public void setDocok(Integer docok) {
        this.docok = docok;
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
        if (!(object instanceof Driverdata)) {
            return false;
        }
        Driverdata other = (Driverdata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Driverdata[ id=" + id + " ]";
    }

    public String getInsuranceimg() {
        return insuranceimg;
    }

    public void setInsuranceimg(String insuranceimg) {
        this.insuranceimg = insuranceimg;
    }

    public String getInsuranceexpdate() {
        return insuranceexpdate;
    }

    public void setInsuranceexpdate(String insuranceexpdate) {
        this.insuranceexpdate = insuranceexpdate;
    }

    public String getDocexpdate() {
        return docexpdate;
    }

    public void setDocexpdate(String docexpdate) {
        this.docexpdate = docexpdate;
    }

    public String getDocimg() {
        return docimg;
    }

    public void setDocimg(String docimg) {
        this.docimg = docimg;
    }

    public String getBirthcountry() {
        return birthcountry;
    }

    public void setBirthcountry(String birthcountry) {
        this.birthcountry = birthcountry;
    }

    public String getBirthcity() {
        return birthcity;
    }

    public void setBirthcity(String birthcity) {
        this.birthcity = birthcity;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
}
