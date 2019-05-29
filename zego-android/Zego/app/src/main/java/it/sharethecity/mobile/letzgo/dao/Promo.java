/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import android.content.Context;

import java.io.Serializable;

import it.sharethecity.mobile.letzgo.R;


public class Promo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String PERCENT = "percent";
    public static final String EURO = "euro";
    public static final String FREERIDE = "freeride";
    public static final String WALLET = "wallet";
    public static final String MGM = "mgm";

    private Integer id;
    private String code;
    private String promotitle;
    private String promodesc;
    private String enablestart;
    private String enablestop;
    private String validfrom;
    private String validto;
    private String type;
    private Integer feeonly;
    private Integer maxusages;
    private Integer maxperuser;
    private String insdate;
    private String moddate;
    private Integer value;

    public Promo() {
    }

    public Promo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPromotitle() {
        return promotitle;
    }

    public void setPromotitle(String promotitle) {
        this.promotitle = promotitle;
    }

    public String getPromodesc() {
        return promodesc;
    }

    public void setPromodesc(String promodesc) {
        this.promodesc = promodesc;
    }

    public String getEnablestart() {
        return enablestart;
    }

    public void setEnablestart(String enablestart) {
        this.enablestart = enablestart;
    }

    public String getEnablestop() {
        return enablestop;
    }

    public void setEnablestop(String enablestop) {
        this.enablestop = enablestop;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFeeonly() {
        return feeonly;
    }

    public void setFeeonly(Integer feeonly) {
        this.feeonly = feeonly;
    }

    public Integer getMaxusages() {
        return maxusages;
    }

    public void setMaxusages(Integer maxusages) {
        this.maxusages = maxusages;
    }

    public Integer getMaxperuser() {
        return maxperuser;
    }

    public void setMaxperuser(Integer maxperuser) {
        this.maxperuser = maxperuser;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public String getModdate() {
        return moddate;
    }

    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
        if (!(object instanceof Promo)) {
            return false;
        }
        Promo other = (Promo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Promo[ id=" + id + " ]";
    }


}
