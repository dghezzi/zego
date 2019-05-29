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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "zguser_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZguserProfile.findAll", query = "SELECT z FROM ZguserProfile z"),
    @NamedQuery(name = "ZguserProfile.findByUserId", query = "SELECT z FROM ZguserProfile z WHERE z.userId = :userId"),
    @NamedQuery(name = "ZguserProfile.findByFirstName", query = "SELECT z FROM ZguserProfile z WHERE z.firstName = :firstName"),
    @NamedQuery(name = "ZguserProfile.findByLastName", query = "SELECT z FROM ZguserProfile z WHERE z.lastName = :lastName"),
    @NamedQuery(name = "ZguserProfile.findByEmail", query = "SELECT z FROM ZguserProfile z WHERE z.email = :email"),
    @NamedQuery(name = "ZguserProfile.findByBirthday", query = "SELECT z FROM ZguserProfile z WHERE z.birthday = :birthday"),
    @NamedQuery(name = "ZguserProfile.findByFiscalCode", query = "SELECT z FROM ZguserProfile z WHERE z.fiscalCode = :fiscalCode"),
    @NamedQuery(name = "ZguserProfile.findByBirthCity", query = "SELECT z FROM ZguserProfile z WHERE z.birthCity = :birthCity"),
    @NamedQuery(name = "ZguserProfile.findByHomeAddress", query = "SELECT z FROM ZguserProfile z WHERE z.homeAddress = :homeAddress"),
    @NamedQuery(name = "ZguserProfile.findByZipCode", query = "SELECT z FROM ZguserProfile z WHERE z.zipCode = :zipCode"),
    @NamedQuery(name = "ZguserProfile.findByHomeCity", query = "SELECT z FROM ZguserProfile z WHERE z.homeCity = :homeCity"),
    @NamedQuery(name = "ZguserProfile.findByCountryId", query = "SELECT z FROM ZguserProfile z WHERE z.countryId = :countryId"),
    @NamedQuery(name = "ZguserProfile.findByGender", query = "SELECT z FROM ZguserProfile z WHERE z.gender = :gender"),
    @NamedQuery(name = "ZguserProfile.findByProfileImageUrl", query = "SELECT z FROM ZguserProfile z WHERE z.profileImageUrl = :profileImageUrl"),
    @NamedQuery(name = "ZguserProfile.findByPhoneNumberPrefix", query = "SELECT z FROM ZguserProfile z WHERE z.phoneNumberPrefix = :phoneNumberPrefix"),
    @NamedQuery(name = "ZguserProfile.findByPhoneNumber", query = "SELECT z FROM ZguserProfile z WHERE z.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "ZguserProfile.findByPhoneNumberLocal", query = "SELECT z FROM ZguserProfile z WHERE z.phoneNumberLocal = :phoneNumberLocal"),
    @NamedQuery(name = "ZguserProfile.findByPhoneNumberValidated", query = "SELECT z FROM ZguserProfile z WHERE z.phoneNumberValidated = :phoneNumberValidated"),
    @NamedQuery(name = "ZguserProfile.findByHasPaymentInfo", query = "SELECT z FROM ZguserProfile z WHERE z.hasPaymentInfo = :hasPaymentInfo"),
    @NamedQuery(name = "ZguserProfile.findByValidationPin", query = "SELECT z FROM ZguserProfile z WHERE z.validationPin = :validationPin"),
    @NamedQuery(name = "ZguserProfile.findByUserCode", query = "SELECT z FROM ZguserProfile z WHERE z.userCode = :userCode"),
    @NamedQuery(name = "ZguserProfile.findByCompany", query = "SELECT z FROM ZguserProfile z WHERE z.company = :company"),
    @NamedQuery(name = "ZguserProfile.findByCreatedate", query = "SELECT z FROM ZguserProfile z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "ZguserProfile.findByLastmoddate", query = "SELECT z FROM ZguserProfile z WHERE z.lastmoddate = :lastmoddate")})
public class ZguserProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "fiscal_code")
    private String fiscalCode;
    @Basic(optional = false)
    @Column(name = "birth_city")
    private int birthCity;
    @Column(name = "home_address")
    private String homeAddress;
    @Column(name = "zip_code")
    private String zipCode;
    @Basic(optional = false)
    @Column(name = "home_city")
    private int homeCity;
    @Column(name = "country_id")
    private Integer countryId;
    @Column(name = "gender")
    private Short gender;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "phone_number_prefix")
    private String phoneNumberPrefix;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "phone_number_local")
    private String phoneNumberLocal;
    @Column(name = "phone_number_validated")
    private Short phoneNumberValidated;
    @Column(name = "has_payment_info")
    private Short hasPaymentInfo;
    @Column(name = "validation_pin")
    private String validationPin;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "company")
    private String company;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public ZguserProfile() {
    }

    public ZguserProfile(Integer userId) {
        this.userId = userId;
    }

    public ZguserProfile(Integer userId, int birthCity, int homeCity) {
        this.userId = userId;
        this.birthCity = birthCity;
        this.homeCity = homeCity;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public int getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(int birthCity) {
        this.birthCity = birthCity;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(int homeCity) {
        this.homeCity = homeCity;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPhoneNumberPrefix() {
        return phoneNumberPrefix;
    }

    public void setPhoneNumberPrefix(String phoneNumberPrefix) {
        this.phoneNumberPrefix = phoneNumberPrefix;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberLocal() {
        return phoneNumberLocal;
    }

    public void setPhoneNumberLocal(String phoneNumberLocal) {
        this.phoneNumberLocal = phoneNumberLocal;
    }

    public Short getPhoneNumberValidated() {
        return phoneNumberValidated;
    }

    public void setPhoneNumberValidated(Short phoneNumberValidated) {
        this.phoneNumberValidated = phoneNumberValidated;
    }

    public Short getHasPaymentInfo() {
        return hasPaymentInfo;
    }

    public void setHasPaymentInfo(Short hasPaymentInfo) {
        this.hasPaymentInfo = hasPaymentInfo;
    }

    public String getValidationPin() {
        return validationPin;
    }

    public void setValidationPin(String validationPin) {
        this.validationPin = validationPin;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
        if (!(object instanceof ZguserProfile)) {
            return false;
        }
        ZguserProfile other = (ZguserProfile) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.ZguserProfile[ userId=" + userId + " ]";
    }

    public boolean notImportable() {
        return nn(phoneNumberPrefix) || nn(phoneNumberLocal);
    }
    
    private boolean nn (String s) {
        return s == null || s.isEmpty() || s.trim().equals("");
    }
    
}
