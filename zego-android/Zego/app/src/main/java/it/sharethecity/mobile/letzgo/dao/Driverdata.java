/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import java.io.Serializable;


public class Driverdata implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer uid;
    private String brand;
    private String model;
    private String color;
    private String plate;
    private Integer seat;
    private Integer year;
    private String carimg;
    private String insdate;
    private String expdate;
    private String moddate;
    private String insuranceimg;
    private String insuranceexpdate;
    private String docexpdate;
    private String docimg;
    private String birthcountry;
    private String birthcity;
    private String cf;
    private String address;
    private String city;
    private String cap;
    private String iban;
    private String area;
    private Integer status;
    private Integer docok;

    public static final Integer STATUS_CANDIDATE = 1;
    public static final Integer STATUS_ACCEPTED = 2;
    public static final Integer STATUS_REJECTED = 3;
    public static final Integer STATUS_MISSING_DOCUMENTS = 4;
    public static final Integer STATUS_DOCUMENTS_UNDER_EVALUATION = 5;
    public static final Integer STATUS_DOCUMENTS_EXPIRED = 6;

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


}
