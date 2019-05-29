/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import java.io.Serializable;


/**
 *
 * @author Lu
 */

public class Userpromo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer pid;
    private Integer uid;
    private String redeemdate;
    private String usagedate;
    private String expirydate;
    private Integer rideid;
    private Integer valueleft;
    private String ridelist;


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
    
}
