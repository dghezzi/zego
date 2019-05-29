/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Cacheable(false)
@Entity
@Table(name = "payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id"),
    @NamedQuery(name = "Payment.findByUid", query = "SELECT p FROM Payment p WHERE p.uid = :uid"),
    @NamedQuery(name = "Payment.findByStatus", query = "SELECT p FROM Payment p WHERE p.status = :status"),
    @NamedQuery(name = "Payment.findByRid", query = "SELECT p FROM Payment p WHERE p.rid = :rid"),
    @NamedQuery(name = "Payment.findByDid", query = "SELECT p FROM Payment p WHERE p.did = :did"),
    @NamedQuery(name = "Payment.findByMode", query = "SELECT p FROM Payment p WHERE p.mode = :mode"),
    @NamedQuery(name = "Payment.findByCurrency", query = "SELECT p FROM Payment p WHERE p.currency = :currency"),
    @NamedQuery(name = "Payment.findByCardbrand", query = "SELECT p FROM Payment p WHERE p.cardbrand = :cardbrand"),
    @NamedQuery(name = "Payment.findByLastdigit", query = "SELECT p FROM Payment p WHERE p.lastdigit = :lastdigit"),
    @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount"),
    @NamedQuery(name = "Payment.findByType", query = "SELECT p FROM Payment p WHERE p.type = :type"),
    @NamedQuery(name = "Payment.findByDriverfee", query = "SELECT p FROM Payment p WHERE p.driverfee = :driverfee"),
    @NamedQuery(name = "Payment.findByZegofee", query = "SELECT p FROM Payment p WHERE p.zegofee = :zegofee"),
    @NamedQuery(name = "Payment.findByDiscount", query = "SELECT p FROM Payment p WHERE p.discount = :discount"),
    @NamedQuery(name = "Payment.findByUpromoid", query = "SELECT p FROM Payment p WHERE p.upromoid = :upromoid"),
    @NamedQuery(name = "Payment.findByUpromocode", query = "SELECT p FROM Payment p WHERE p.upromocode = :upromocode"),
    @NamedQuery(name = "Payment.findByPaymentdate", query = "SELECT p FROM Payment p WHERE p.paymentdate = :paymentdate"),
    @NamedQuery(name = "Payment.findByStripeid", query = "SELECT p FROM Payment p WHERE p.stripeid = :stripeid"),
    @NamedQuery(name = "Payment.findByStripecharge", query = "SELECT p FROM Payment p WHERE p.stripecharge = :stripecharge"),
    @NamedQuery(name = "Payment.findByStriperror", query = "SELECT p FROM Payment p WHERE p.striperror = :striperror"),
    @NamedQuery(name = "Payment.findByTransactionid", query = "SELECT p FROM Payment p WHERE p.transactionid = :transactionid"),
    @NamedQuery(name = "Payment.findByPayoutstatus", query = "SELECT p FROM Payment p WHERE p.payoutstatus = :payoutstatus"),
    @NamedQuery(name = "Payment.findByPayoutdate", query = "SELECT p FROM Payment p WHERE p.payoutdate = :payoutdate"),
    @NamedQuery(name = "Payment.findByPayouttransid", query = "SELECT p FROM Payment p WHERE p.payouttransid = :payouttransid"),
    @NamedQuery(name = "Payment.findByDriveraccount", query = "SELECT p FROM Payment p WHERE p.driveraccount = :driveraccount")})
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "did")
    private Integer did;
    @Column(name = "mode")
    private String mode;
    @Column(name = "status")
    private String status;
    @Column(name = "currency")
    private String currency;
    @Column(name = "cardbrand")
    private String cardbrand;
    @Column(name = "lastdigit")
    private String lastdigit;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "type")
    private String type;
    @Column(name = "driverfee")
    private Integer driverfee;
    @Column(name = "refund")
    private Integer refund;
    @Column(name = "zegofee")
    private Integer zegofee;
    @Column(name = "discount")
    private Integer discount;
    @Column(name = "upromoid")
    private Integer upromoid;
    @Column(name = "upromocode")
    private String upromocode;
    @Column(name = "paymentdate")
    private String paymentdate;
    @Column(name = "stripeid")
    private String stripeid;
    @Column(name = "stripecharge")
    private String stripecharge;
    @Column(name = "striperror")
    private String striperror;
    @Column(name = "transactionid")
    private String transactionid;
    @Column(name = "payoutstatus")
    private String payoutstatus;
    @Column(name = "payoutdate")
    private String payoutdate;
    @Column(name = "payouttransid")
    private String payouttransid;
    @Column(name = "driveraccount")
    private String driveraccount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pid", referencedColumnName = "id")
    private List<Paymentaction> actions;
    
    public static final String MODE_AUTO = "auto";
    public static final String MODE_BACKOFFICE = "backoffice";
    
    public static final String PAYMENT_TYPE_PREAUTH = "preauth";
    public static final String PAYMENT_TYPE_STANDARD = "standard";
    public static final String PAYMENT_TYPE_EXTRA = "extra";
    public static final String PAYMENT_TYPE_PENALTY = "penalty";
    public static final String PAYMENT_TYPE_CASH = "cash";
    
    public static final String PAYMENT_STATUS_AUTH = "authorized";
    public static final String PAYMENT_STATUS_CAPT = "captured";
    public static final String PAYMENT_STATUS_FAIL = "failed";
    public static final String PAYMENT_STATUS_REFUND = "refunded";
    public static final String PAYMENT_STATUS_RELEASED = "released";
    
    public Payment() {
    }

    public Payment(Integer id) {
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

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardbrand() {
        return cardbrand;
    }

    public void setCardbrand(String cardbrand) {
        this.cardbrand = cardbrand;
    }

    public String getLastdigit() {
        return lastdigit;
    }

    public void setLastdigit(String lastdigit) {
        this.lastdigit = lastdigit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDriverfee() {
        return driverfee;
    }

    public void setDriverfee(Integer driverfee) {
        this.driverfee = driverfee;
    }

    public Integer getZegofee() {
        return zegofee;
    }

    public void setZegofee(Integer zegofee) {
        this.zegofee = zegofee;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getUpromoid() {
        return upromoid;
    }

    public void setUpromoid(Integer upromoid) {
        this.upromoid = upromoid;
    }

    public String getUpromocode() {
        return upromocode;
    }

    public void setUpromocode(String upromocode) {
        this.upromocode = upromocode;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getStripeid() {
        return stripeid;
    }

    public void setStripeid(String stripeid) {
        this.stripeid = stripeid;
    }

    public String getStripecharge() {
        return stripecharge;
    }

    public void setStripecharge(String stripecharge) {
        this.stripecharge = stripecharge;
    }

    public String getStriperror() {
        return striperror;
    }

    public void setStriperror(String striperror) {
        this.striperror = striperror;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getPayoutstatus() {
        return payoutstatus;
    }

    public void setPayoutstatus(String payoutstatus) {
        this.payoutstatus = payoutstatus;
    }

    public String getPayoutdate() {
        return payoutdate;
    }

    public void setPayoutdate(String payoutdate) {
        this.payoutdate = payoutdate;
    }

    public String getPayouttransid() {
        return payouttransid;
    }

    public void setPayouttransid(String payouttransid) {
        this.payouttransid = payouttransid;
    }

    public String getDriveraccount() {
        return driveraccount;
    }

    public void setDriveraccount(String driveraccount) {
        this.driveraccount = driveraccount;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Payment[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRefund() {
        return refund == null ? 0 : refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public List<Paymentaction> getActions() {
        return actions;
    }

    public void setActions(List<Paymentaction> actions) {
        this.actions = actions;
    }
    
    
}
