/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities.legacy;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "zgdriver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgdriver.findAll", query = "SELECT z FROM Zgdriver z"),
    @NamedQuery(name = "Zgdriver.findByUserId", query = "SELECT z FROM Zgdriver z WHERE z.userId = :userId"),
    @NamedQuery(name = "Zgdriver.findByStatus", query = "SELECT z FROM Zgdriver z WHERE z.status = :status"),
    @NamedQuery(name = "Zgdriver.findByAvailable", query = "SELECT z FROM Zgdriver z WHERE z.available = :available"),
    @NamedQuery(name = "Zgdriver.findByCurrentlyRiding", query = "SELECT z FROM Zgdriver z WHERE z.currentlyRiding = :currentlyRiding"),
    @NamedQuery(name = "Zgdriver.findByLicense", query = "SELECT z FROM Zgdriver z WHERE z.license = :license"),
    @NamedQuery(name = "Zgdriver.findByLicenseExpiringDate", query = "SELECT z FROM Zgdriver z WHERE z.licenseExpiringDate = :licenseExpiringDate"),
    @NamedQuery(name = "Zgdriver.findByInsuranceExpiringDate", query = "SELECT z FROM Zgdriver z WHERE z.insuranceExpiringDate = :insuranceExpiringDate"),
    @NamedQuery(name = "Zgdriver.findByIban", query = "SELECT z FROM Zgdriver z WHERE z.iban = :iban"),
    @NamedQuery(name = "Zgdriver.findByPaypalAccount", query = "SELECT z FROM Zgdriver z WHERE z.paypalAccount = :paypalAccount"),
    @NamedQuery(name = "Zgdriver.findByCreditMode", query = "SELECT z FROM Zgdriver z WHERE z.creditMode = :creditMode"),
    @NamedQuery(name = "Zgdriver.findByDocumentStatus", query = "SELECT z FROM Zgdriver z WHERE z.documentStatus = :documentStatus"),
    @NamedQuery(name = "Zgdriver.findByInsuranceFilename", query = "SELECT z FROM Zgdriver z WHERE z.insuranceFilename = :insuranceFilename"),
    @NamedQuery(name = "Zgdriver.findByLicenseFilename", query = "SELECT z FROM Zgdriver z WHERE z.licenseFilename = :licenseFilename"),
    @NamedQuery(name = "Zgdriver.findByCreatedate", query = "SELECT z FROM Zgdriver z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgdriver.findByLastmoddate", query = "SELECT z FROM Zgdriver z WHERE z.lastmoddate = :lastmoddate")})
public class Zgdriver implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "status")
    private Short status;
    @Column(name = "available")
    private Short available;
    @Column(name = "currently_riding")
    private Short currentlyRiding;
    @Column(name = "license")
    private String license;
    @Column(name = "license_expiring_date")
    @Temporal(TemporalType.DATE)
    private Date licenseExpiringDate;
    @Column(name = "insurance_expiring_date")
    @Temporal(TemporalType.DATE)
    private Date insuranceExpiringDate;
    @Column(name = "iban")
    private String iban;
    @Column(name = "paypal_account")
    private String paypalAccount;
    @Column(name = "credit_mode")
    private Short creditMode;
    @Column(name = "document_status")
    private Short documentStatus;
    @Column(name = "insurance_filename")
    private String insuranceFilename;
    @Column(name = "license_filename")
    private String licenseFilename;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;
   
   

    public Zgdriver() {
    }

    public Zgdriver(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getAvailable() {
        return available;
    }

    public void setAvailable(Short available) {
        this.available = available;
    }

    public Short getCurrentlyRiding() {
        return currentlyRiding;
    }

    public void setCurrentlyRiding(Short currentlyRiding) {
        this.currentlyRiding = currentlyRiding;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Date getLicenseExpiringDate() {
        return licenseExpiringDate;
    }

    public void setLicenseExpiringDate(Date licenseExpiringDate) {
        this.licenseExpiringDate = licenseExpiringDate;
    }

    public Date getInsuranceExpiringDate() {
        return insuranceExpiringDate;
    }

    public void setInsuranceExpiringDate(Date insuranceExpiringDate) {
        this.insuranceExpiringDate = insuranceExpiringDate;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public Short getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(Short creditMode) {
        this.creditMode = creditMode;
    }

    public Short getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Short documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getInsuranceFilename() {
        return insuranceFilename;
    }

    public void setInsuranceFilename(String insuranceFilename) {
        this.insuranceFilename = insuranceFilename;
    }

    public String getLicenseFilename() {
        return licenseFilename;
    }

    public void setLicenseFilename(String licenseFilename) {
        this.licenseFilename = licenseFilename;
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
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgdriver)) {
            return false;
        }
        Zgdriver other = (Zgdriver) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgdriver[ userId=" + userId + " ]";
    }
    
}
