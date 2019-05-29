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
import javax.persistence.Lob;
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
@Table(name = "zgpromo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgpromo.findAll", query = "SELECT z FROM Zgpromo z"),
    @NamedQuery(name = "Zgpromo.findByPromoId", query = "SELECT z FROM Zgpromo z WHERE z.promoId = :promoId"),
    @NamedQuery(name = "Zgpromo.findByPromoCode", query = "SELECT z FROM Zgpromo z WHERE z.promoCode = :promoCode"),
    @NamedQuery(name = "Zgpromo.findByDescr", query = "SELECT z FROM Zgpromo z WHERE z.descr = :descr"),
    @NamedQuery(name = "Zgpromo.findByPromoStart", query = "SELECT z FROM Zgpromo z WHERE z.promoStart = :promoStart"),
    @NamedQuery(name = "Zgpromo.findByPromoEnd", query = "SELECT z FROM Zgpromo z WHERE z.promoEnd = :promoEnd"),
    @NamedQuery(name = "Zgpromo.findByPromoValidityStart", query = "SELECT z FROM Zgpromo z WHERE z.promoValidityStart = :promoValidityStart"),
    @NamedQuery(name = "Zgpromo.findByPromoValidityEnd", query = "SELECT z FROM Zgpromo z WHERE z.promoValidityEnd = :promoValidityEnd"),
    @NamedQuery(name = "Zgpromo.findByReactivation", query = "SELECT z FROM Zgpromo z WHERE z.reactivation = :reactivation"),
    @NamedQuery(name = "Zgpromo.findByTrackingType", query = "SELECT z FROM Zgpromo z WHERE z.trackingType = :trackingType"),
    @NamedQuery(name = "Zgpromo.findByDiscount", query = "SELECT z FROM Zgpromo z WHERE z.discount = :discount"),
    @NamedQuery(name = "Zgpromo.findByPromoAmount", query = "SELECT z FROM Zgpromo z WHERE z.promoAmount = :promoAmount"),
    @NamedQuery(name = "Zgpromo.findByIssuerDiscount", query = "SELECT z FROM Zgpromo z WHERE z.issuerDiscount = :issuerDiscount"),
    @NamedQuery(name = "Zgpromo.findByIssuerPromoAmount", query = "SELECT z FROM Zgpromo z WHERE z.issuerPromoAmount = :issuerPromoAmount")})
public class Zgpromo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "promo_id")
    private Integer promoId;
    @Column(name = "promo_code")
    private String promoCode;
    @Column(name = "descr")
    private String descr;
    @Lob
    @Column(name = "long_descr")
    private String longDescr;
    @Column(name = "promo_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoStart;
    @Column(name = "promo_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoEnd;
    @Column(name = "promo_validity_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoValidityStart;
    @Column(name = "promo_validity_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date promoValidityEnd;
    @Basic(optional = false)
    @Column(name = "reactivation")
    private short reactivation;
    @Column(name = "tracking_type")
    private Short trackingType;
    @Column(name = "discount")
    private Integer discount;
    @Column(name = "promo_amount")
    private Integer promoAmount;
    @Column(name = "issuer_discount")
    private Integer issuerDiscount;
    @Column(name = "issuer_promo_amount")
    private Integer issuerPromoAmount;

    public Zgpromo() {
    }

    public Zgpromo(Integer promoId) {
        this.promoId = promoId;
    }

    public Zgpromo(Integer promoId, short reactivation) {
        this.promoId = promoId;
        this.reactivation = reactivation;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getLongDescr() {
        return longDescr;
    }

    public void setLongDescr(String longDescr) {
        this.longDescr = longDescr;
    }

    public Date getPromoStart() {
        return promoStart;
    }

    public void setPromoStart(Date promoStart) {
        this.promoStart = promoStart;
    }

    public Date getPromoEnd() {
        return promoEnd;
    }

    public void setPromoEnd(Date promoEnd) {
        this.promoEnd = promoEnd;
    }

    public Date getPromoValidityStart() {
        return promoValidityStart;
    }

    public void setPromoValidityStart(Date promoValidityStart) {
        this.promoValidityStart = promoValidityStart;
    }

    public Date getPromoValidityEnd() {
        return promoValidityEnd;
    }

    public void setPromoValidityEnd(Date promoValidityEnd) {
        this.promoValidityEnd = promoValidityEnd;
    }

    public short getReactivation() {
        return reactivation;
    }

    public void setReactivation(short reactivation) {
        this.reactivation = reactivation;
    }

    public Short getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(Short trackingType) {
        this.trackingType = trackingType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(Integer promoAmount) {
        this.promoAmount = promoAmount;
    }

    public Integer getIssuerDiscount() {
        return issuerDiscount;
    }

    public void setIssuerDiscount(Integer issuerDiscount) {
        this.issuerDiscount = issuerDiscount;
    }

    public Integer getIssuerPromoAmount() {
        return issuerPromoAmount;
    }

    public void setIssuerPromoAmount(Integer issuerPromoAmount) {
        this.issuerPromoAmount = issuerPromoAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promoId != null ? promoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgpromo)) {
            return false;
        }
        Zgpromo other = (Zgpromo) object;
        if ((this.promoId == null && other.promoId != null) || (this.promoId != null && !this.promoId.equals(other.promoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgpromo[ promoId=" + promoId + " ]";
    }
    
}
