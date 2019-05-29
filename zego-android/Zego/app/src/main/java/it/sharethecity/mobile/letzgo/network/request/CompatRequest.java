/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.network.request;


import java.io.Serializable;

/**
 *
 * @author Lu
 */
public class CompatRequest implements Serializable{

    private static final String CASH = "cash";
    private static final String CARD = "card";
    private Integer rid;
    private String shortid;
    private Integer drivprice;
    private Integer lev;
    private Integer passprice;
    private String pickup;
    private String dropoff;
    private String reqdate;
    private String shid;
    private Integer status;
    private String method;

    public CompatRequest() {

    }

    public CompatRequest(Riderequest req) {
        rid = req.getId();
        shortid = req.getShortid();
        drivprice = req.getDriverfee();
        passprice = req.getPassprice();
        pickup = req.getPuaddr();
        dropoff = req.getDoaddr();
    }



    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public Integer getDrivprice() {
        return drivprice;
    }

    public void setDrivprice(Integer drivprice) {
        this.drivprice = drivprice;
    }

    public Integer getPassprice() {
        return passprice;
    }

    public void setPassprice(Integer passprice) {
        this.passprice = passprice;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDropoff() {
        return dropoff;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }


    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getShid() {
        return shid;
    }

    public void setShid(String shid) {
        this.shid = shid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isCard() {
        return method == null || method.equalsIgnoreCase(CARD);
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public boolean isLadyRide(){
        return lev != null && lev == 2;
    }

    public boolean isControlRide(){
        return lev != null && lev == 4;
    }
}
