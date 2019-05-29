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
@Table(name = "zgridepayment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgridepayment.findAll", query = "SELECT z FROM Zgridepayment z"),
    @NamedQuery(name = "Zgridepayment.findById", query = "SELECT z FROM Zgridepayment z WHERE z.riderequestId = :id"),
    @NamedQuery(name = "Zgridepayment.findByRiderequestId", query = "SELECT z FROM Zgridepayment z WHERE z.riderequestId = :riderequestId"),
    @NamedQuery(name = "Zgridepayment.findByDriverId", query = "SELECT z FROM Zgridepayment z WHERE z.driverId = :driverId"),
    @NamedQuery(name = "Zgridepayment.findByAmount", query = "SELECT z FROM Zgridepayment z WHERE z.amount = :amount"),
    @NamedQuery(name = "Zgridepayment.findByFullAmount", query = "SELECT z FROM Zgridepayment z WHERE z.fullAmount = :fullAmount"),
    @NamedQuery(name = "Zgridepayment.findByFeeAmount", query = "SELECT z FROM Zgridepayment z WHERE z.feeAmount = :feeAmount"),
    @NamedQuery(name = "Zgridepayment.findByCurrencyId", query = "SELECT z FROM Zgridepayment z WHERE z.currencyId = :currencyId"),
    @NamedQuery(name = "Zgridepayment.findByTransactionId", query = "SELECT z FROM Zgridepayment z WHERE z.transactionId = :transactionId"),
    @NamedQuery(name = "Zgridepayment.findByDisplayId", query = "SELECT z FROM Zgridepayment z WHERE z.displayId = :displayId"),
    @NamedQuery(name = "Zgridepayment.findByPaymentType", query = "SELECT z FROM Zgridepayment z WHERE z.paymentType = :paymentType"),
    @NamedQuery(name = "Zgridepayment.findByPaymentMethod", query = "SELECT z FROM Zgridepayment z WHERE z.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Zgridepayment.findByCreditCard", query = "SELECT z FROM Zgridepayment z WHERE z.creditCard = :creditCard"),
    @NamedQuery(name = "Zgridepayment.findBySource", query = "SELECT z FROM Zgridepayment z WHERE z.source = :source"),
    @NamedQuery(name = "Zgridepayment.findByCreatedate", query = "SELECT z FROM Zgridepayment z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgridepayment.findByLastmoddate", query = "SELECT z FROM Zgridepayment z WHERE z.lastmoddate = :lastmoddate")})
public class Zgridepayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "riderequest_id")
    private Integer riderequestId;
    @Column(name = "driver_id")
    private Integer driverId;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "full_amount")
    private Integer fullAmount;
    @Column(name = "fee_amount")
    private Integer feeAmount;
    @Column(name = "currency_id")
    private Integer currencyId;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "display_id")
    private String displayId;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "payment_method")
    private Short paymentMethod;
    @Column(name = "credit_card")
    private String creditCard;
    @Column(name = "source")
    private Short source;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public Zgridepayment() {
    }

    public Zgridepayment(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public Integer getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(Integer riderequestId) {
        this.riderequestId = riderequestId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getAmount() {
        return amount == null ? 0 : amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getFullAmount() {
        return fullAmount == null ? 0 : fullAmount;
                
    }

    public void setFullAmount(Integer fullAmount) {
        this.fullAmount = fullAmount;
    }

    public Integer getFeeAmount() {
        return feeAmount == null ? 0 : feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Short getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Short paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
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
        hash += (riderequestId != null ? riderequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgridepayment)) {
            return false;
        }
        Zgridepayment other = (Zgridepayment) object;
        if ((this.riderequestId == null && other.riderequestId != null) || (this.riderequestId != null && !this.riderequestId.equals(other.riderequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgridepayment[ riderequestId=" + riderequestId + " ]";
    }
    
}
