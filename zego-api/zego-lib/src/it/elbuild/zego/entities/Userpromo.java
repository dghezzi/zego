/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Cacheable(false)
@Entity
@Table(name = "userpromo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userpromo.findAll", query = "SELECT u FROM Userpromo u"),
    @NamedQuery(name = "Userpromo.findById", query = "SELECT u FROM Userpromo u WHERE u.id = :id"),
    @NamedQuery(name = "Userpromo.findByPid", query = "SELECT u FROM Userpromo u WHERE u.pid = :pid"),
    @NamedQuery(name = "Userpromo.findUnusedByPid", query = "SELECT u FROM Userpromo u WHERE u.pid = :pid and u.usagedate IS NULL and (u.burnt = 0 or u.burnt is null) and u.expirydate > :now"),
    @NamedQuery(name = "Userpromo.findByUid", query = "SELECT u FROM Userpromo u WHERE u.uid = :uid order by u.expirydate ASC"),
    @NamedQuery(name = "Userpromo.findNotUsedByUid", query = "SELECT u FROM Userpromo u WHERE u.uid = :uid and u.usagedate IS NULL and (u.burnt = 0 or u.burnt is null) and u.expirydate > :now order by u.expirydate ASC"),
    @NamedQuery(name = "Userpromo.findByUidAndPid", query = "SELECT u FROM Userpromo u WHERE u.pid = :pid and u.uid = :uid order by u.expirydate ASC"),
    @NamedQuery(name = "Userpromo.findByRedeemdate", query = "SELECT u FROM Userpromo u WHERE u.redeemdate = :redeemdate"),
    @NamedQuery(name = "Userpromo.findByRedeemdateBetween", query = "SELECT u FROM Userpromo u WHERE u.promo.insdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Userpromo.findByPromoRedeemdateBetween", query = "SELECT u FROM Userpromo u WHERE u.redeemdate is not null and u.redeemdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Userpromo.findByUsagedateBetween", query = "SELECT u FROM Userpromo u WHERE  u.usagedate is not null and u.usagedate BETWEEN :start and :stop"),
    @NamedQuery(name = "Userpromo.findByUsagedate", query = "SELECT u FROM Userpromo u WHERE u.usagedate = :usagedate"),
    @NamedQuery(name = "Userpromo.findByExpirydate", query = "SELECT u FROM Userpromo u WHERE u.expirydate = :expirydate"),
    @NamedQuery(name = "Userpromo.findByRideid", query = "SELECT u FROM Userpromo u WHERE u.rideid = :rideid"),
    @NamedQuery(name = "Userpromo.findByValueleft", query = "SELECT u FROM Userpromo u WHERE u.valueleft = :valueleft"),
    @NamedQuery(name = "Userpromo.findByRidelist", query = "SELECT u FROM Userpromo u WHERE u.ridelist = :ridelist")})
public class Userpromo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "redeemdate")
    private String redeemdate;
    @Column(name = "usagedate")
    private String usagedate;
    @Column(name = "expirydate")
    private String expirydate;
    @Column(name = "rideid")
    private Integer rideid;
    @Column(name = "valueleft")
    private Integer valueleft;
    @Column(name = "ridelist")
    private String ridelist;
    @Column(name = "burnt")
    private Integer burnt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "pid",insertable = false, updatable = false)    
    private Promo promo;
     
    public Userpromo() {
    }

    public Userpromo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRedeemdate() {
        return redeemdate;
    }

    public void setRedeemdate(String redeemdate) {
        this.redeemdate = redeemdate;
    }

    public String getUsagedate() {
        return usagedate;
    }

    public void setUsagedate(String usagedate) {
        this.usagedate = usagedate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public Integer getRideid() {
        return rideid;
    }

    public void setRideid(Integer rideid) {
        this.rideid = rideid;
    }

    public Integer getValueleft() {
        return valueleft;
    }

    public void setValueleft(Integer valueleft) {
        this.valueleft = valueleft;
    }

    public String getRidelist() {
        return ridelist;
    }

    public void setRidelist(String ridelist) {
        this.ridelist = ridelist;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
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
        if (!(object instanceof Userpromo)) {
            return false;
        }
        Userpromo other = (Userpromo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Userpromo[ id=" + id + " ]";
    }
    
    public Integer discount (Integer amount) {
        switch(promo.getType()){
            case Promo.EURO: 
                return Math.min(amount, promo.getValue());
            case Promo.FREERIDE:
                return amount;
            case Promo.PERCENT:
                return new Double((amount*promo.getValue())/100.d).intValue();
            case Promo.WALLET:
                return Math.min(amount, getValueleft());
            default: 
                return 0;
        }
    }

    public Integer getBurnt() {
        return burnt;
    }

    public void setBurnt(Integer burnt) {
        this.burnt = burnt;
    }
    
    
}
