package it.sharethecity.mobile.letzgo.dao;

/**
 * Created by lucabellaroba on 21/11/16.
 */

public class Conf {

    public static final String RIDE_MIN_COST = "pricing.minimum.zegofee";
    public static final String PRICING_MAXIMUM_FEE = "pricing.maximum.fee";

    private Integer id;
    private Integer pub;
    private String val;
    private String k;
    private String descr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPub() {
        return pub;
    }

    public void setPub(Integer pub) {
        this.pub = pub;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
