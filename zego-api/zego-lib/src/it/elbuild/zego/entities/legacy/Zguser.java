/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities.legacy;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "zguser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zguser.findAll", query = "SELECT z FROM Zguser z"),
    @NamedQuery(name = "Zguser.findByUserId", query = "SELECT z FROM Zguser z WHERE z.userId = :userId"),
    @NamedQuery(name = "Zguser.findByUsername", query = "SELECT z FROM Zguser z WHERE z.username = :username"),
    @NamedQuery(name = "Zguser.findByPassword", query = "SELECT z FROM Zguser z WHERE z.password = :password"),
    @NamedQuery(name = "Zguser.findByUserIdentifier", query = "SELECT z FROM Zguser z WHERE z.userIdentifier = :userIdentifier"),
    @NamedQuery(name = "Zguser.findByFacebookId", query = "SELECT z FROM Zguser z WHERE z.facebookId = :facebookId"),
    @NamedQuery(name = "Zguser.findByOs", query = "SELECT z FROM Zguser z WHERE z.os = :os"),
    @NamedQuery(name = "Zguser.findByVos", query = "SELECT z FROM Zguser z WHERE z.vos = :vos"),
    @NamedQuery(name = "Zguser.findByVv", query = "SELECT z FROM Zguser z WHERE z.vv = :vv"),
    @NamedQuery(name = "Zguser.findByPushId", query = "SELECT z FROM Zguser z WHERE z.pushId = :pushId"),
    @NamedQuery(name = "Zguser.findByPrivacy", query = "SELECT z FROM Zguser z WHERE z.privacy = :privacy"),
    @NamedQuery(name = "Zguser.findByCommercial", query = "SELECT z FROM Zguser z WHERE z.commercial = :commercial"),
    @NamedQuery(name = "Zguser.findByCommercialThird", query = "SELECT z FROM Zguser z WHERE z.commercialThird = :commercialThird"),
    @NamedQuery(name = "Zguser.findByBanExpiryDate", query = "SELECT z FROM Zguser z WHERE z.banExpiryDate = :banExpiryDate"),
    @NamedQuery(name = "Zguser.findByBanPending", query = "SELECT z FROM Zguser z WHERE z.banPending = :banPending"),
    @NamedQuery(name = "Zguser.findByStatus", query = "SELECT z FROM Zguser z WHERE z.status = :status"),
    @NamedQuery(name = "Zguser.findByLastLon", query = "SELECT z FROM Zguser z WHERE z.lastLon = :lastLon"),
    @NamedQuery(name = "Zguser.findByLastLat", query = "SELECT z FROM Zguser z WHERE z.lastLat = :lastLat"),
    @NamedQuery(name = "Zguser.findByLocationTime", query = "SELECT z FROM Zguser z WHERE z.locationTime = :locationTime"),
    @NamedQuery(name = "Zguser.findBySource", query = "SELECT z FROM Zguser z WHERE z.source = :source"),
    @NamedQuery(name = "Zguser.findByCreatedate", query = "SELECT z FROM Zguser z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zguser.findByLastmoddate", query = "SELECT z FROM Zguser z WHERE z.lastmoddate = :lastmoddate")})
public class Zguser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "user_identifier")
    private String userIdentifier;
    @Column(name = "facebook_id")
    private String facebookId;
    @Lob
    @Column(name = "dtoken")
    private String dtoken;
    @Column(name = "os")
    private Short os;
    @Column(name = "vos")
    private String vos;
    @Column(name = "vv")
    private String vv;
    @Column(name = "push_id")
    private String pushId;
    @Basic(optional = false)
    @Column(name = "privacy")
    private short privacy;
    @Column(name = "commercial")
    private Short commercial;
    @Column(name = "commercial_third")
    private Short commercialThird;
    @Column(name = "ban_expiry_date")
    private String banExpiryDate;
    @Column(name = "ban_pending")
    private Short banPending;
    @Column(name = "status")
    private Short status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "last_lon")
    private Double lastLon;
    @Column(name = "last_lat")
    private Double lastLat;
    @Column(name = "location_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date locationTime;
    @Column(name = "source")
    private Short source;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;
    

    public Zguser() {
    }

    public Zguser(Integer userId) {
        this.userId = userId;
    }

    public Zguser(Integer userId, String username, short privacy) {
        this.userId = userId;
        this.username = username;
        this.privacy = privacy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getDtoken() {
        return dtoken;
    }

    public void setDtoken(String dtoken) {
        this.dtoken = dtoken;
    }

    public Short getOs() {
        return os;
    }

    public void setOs(Short os) {
        this.os = os;
    }

    public String getVos() {
        return vos;
    }

    public void setVos(String vos) {
        this.vos = vos;
    }

    public String getVv() {
        return vv;
    }

    public void setVv(String vv) {
        this.vv = vv;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public short getPrivacy() {
        return privacy;
    }

    public void setPrivacy(short privacy) {
        this.privacy = privacy;
    }

    public Short getCommercial() {
        return commercial;
    }

    public void setCommercial(Short commercial) {
        this.commercial = commercial;
    }

    public Short getCommercialThird() {
        return commercialThird;
    }

    public void setCommercialThird(Short commercialThird) {
        this.commercialThird = commercialThird;
    }

    public String getBanExpiryDate() {
        return banExpiryDate;
    }

    public void setBanExpiryDate(String banExpiryDate) {
        this.banExpiryDate = banExpiryDate;
    }

    public Short getBanPending() {
        return banPending;
    }

    public void setBanPending(Short banPending) {
        this.banPending = banPending;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Double getLastLon() {
        return lastLon;
    }

    public void setLastLon(Double lastLon) {
        this.lastLon = lastLon;
    }

    public Double getLastLat() {
        return lastLat;
    }

    public void setLastLat(Double lastLat) {
        this.lastLat = lastLat;
    }

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
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
        if (!(object instanceof Zguser)) {
            return false;
        }
        Zguser other = (Zguser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zguser[ userId=" + userId + " ]";
    }

    
    
}
