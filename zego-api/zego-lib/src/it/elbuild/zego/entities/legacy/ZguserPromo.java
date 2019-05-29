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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "zguser_promo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZguserPromo.findAll", query = "SELECT z FROM ZguserPromo z"),
    @NamedQuery(name = "ZguserPromo.findByUserPromoId", query = "SELECT z FROM ZguserPromo z WHERE z.userPromoId = :userPromoId"),
    @NamedQuery(name = "ZguserPromo.findByUserId", query = "SELECT z FROM ZguserPromo z WHERE z.userId = :userId"),
    @NamedQuery(name = "ZguserPromo.findByPromoCodeId", query = "SELECT z FROM ZguserPromo z WHERE z.promoCodeId = :promoCodeId"),
    @NamedQuery(name = "ZguserPromo.findByActivationDate", query = "SELECT z FROM ZguserPromo z WHERE z.activationDate = :activationDate"),
    @NamedQuery(name = "ZguserPromo.findByExpirationDate", query = "SELECT z FROM ZguserPromo z WHERE z.expirationDate = :expirationDate"),
    @NamedQuery(name = "ZguserPromo.findByRedeemDate", query = "SELECT z FROM ZguserPromo z WHERE z.redeemDate = :redeemDate"),
    @NamedQuery(name = "ZguserPromo.findByStatus", query = "SELECT z FROM ZguserPromo z WHERE z.status = :status"),
    @NamedQuery(name = "ZguserPromo.findByUserType", query = "SELECT z FROM ZguserPromo z WHERE z.userType = :userType"),
    @NamedQuery(name = "ZguserPromo.findByRiderequestId", query = "SELECT z FROM ZguserPromo z WHERE z.riderequestId = :riderequestId")})
public class ZguserPromo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_promo_id")
    private Integer userPromoId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "promo_code_id")
    private int promoCodeId;
    @Column(name = "activation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationDate;
    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @Column(name = "redeem_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date redeemDate;
    @Column(name = "status")
    private Short status;
    @Basic(optional = false)
    @Column(name = "user_type")
    private short userType;
    @Column(name = "riderequest_id")
    private Integer riderequestId;

    public ZguserPromo() {
    }

    public ZguserPromo(Integer userPromoId) {
        this.userPromoId = userPromoId;
    }

    public ZguserPromo(Integer userPromoId, int userId, int promoCodeId, short userType) {
        this.userPromoId = userPromoId;
        this.userId = userId;
        this.promoCodeId = promoCodeId;
        this.userType = userType;
    }

    public Integer getUserPromoId() {
        return userPromoId;
    }

    public void setUserPromoId(Integer userPromoId) {
        this.userPromoId = userPromoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public short getUserType() {
        return userType;
    }

    public void setUserType(short userType) {
        this.userType = userType;
    }

    public Integer getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userPromoId != null ? userPromoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZguserPromo)) {
            return false;
        }
        ZguserPromo other = (ZguserPromo) object;
        if ((this.userPromoId == null && other.userPromoId != null) || (this.userPromoId != null && !this.userPromoId.equals(other.userPromoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.ZguserPromo[ userPromoId=" + userPromoId + " ]";
    }
    
}
