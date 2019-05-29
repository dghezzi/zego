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
@Table(name = "zgpromo_codes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZgpromoCodes.findAll", query = "SELECT z FROM ZgpromoCodes z"),
    @NamedQuery(name = "ZgpromoCodes.findByPromoCodeId", query = "SELECT z FROM ZgpromoCodes z WHERE z.promoCodeId = :promoCodeId"),
    @NamedQuery(name = "ZgpromoCodes.findByPromoId", query = "SELECT z FROM ZgpromoCodes z WHERE z.promoId = :promoId"),
    @NamedQuery(name = "ZgpromoCodes.findByCode", query = "SELECT z FROM ZgpromoCodes z WHERE z.code = :code"),
    @NamedQuery(name = "ZgpromoCodes.findByUserId", query = "SELECT z FROM ZgpromoCodes z WHERE z.userId = :userId"),
    @NamedQuery(name = "ZgpromoCodes.findByPromoter", query = "SELECT z FROM ZgpromoCodes z WHERE z.promoter = :promoter"),
    @NamedQuery(name = "ZgpromoCodes.findByPromoCodeStart", query = "SELECT z FROM ZgpromoCodes z WHERE z.promoCodeStart = :promoCodeStart"),
    @NamedQuery(name = "ZgpromoCodes.findByPromoCodeEnd", query = "SELECT z FROM ZgpromoCodes z WHERE z.promoCodeEnd = :promoCodeEnd")})
public class ZgpromoCodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "promo_code_id")
    private Integer promoCodeId;
    @Basic(optional = false)
    @Column(name = "promo_id")
    private int promoId;
    @Column(name = "code")
    private String code;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "promoter")
    private String promoter;
    @Column(name = "promo_code_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoCodeStart;
    @Column(name = "promo_code_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoCodeEnd;

    public ZgpromoCodes() {
    }

    public ZgpromoCodes(Integer promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public ZgpromoCodes(Integer promoCodeId, int promoId) {
        this.promoCodeId = promoCodeId;
        this.promoId = promoId;
    }

    public Integer getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Integer promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public Date getPromoCodeStart() {
        return promoCodeStart;
    }

    public void setPromoCodeStart(Date promoCodeStart) {
        this.promoCodeStart = promoCodeStart;
    }

    public Date getPromoCodeEnd() {
        return promoCodeEnd;
    }

    public void setPromoCodeEnd(Date promoCodeEnd) {
        this.promoCodeEnd = promoCodeEnd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promoCodeId != null ? promoCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZgpromoCodes)) {
            return false;
        }
        ZgpromoCodes other = (ZgpromoCodes) object;
        if ((this.promoCodeId == null && other.promoCodeId != null) || (this.promoCodeId != null && !this.promoCodeId.equals(other.promoCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.ZgpromoCodes[ promoCodeId=" + promoCodeId + " ]";
    }
    
}
