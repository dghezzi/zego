/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response.ride;

import it.elbuild.zego.entities.Riderequest;

/**
 *
 * @author Lu
 */
public class CompatRequest {

    public CompatRequest() {
    
    }
    
    public CompatRequest(Riderequest req) {
        rid = req.getId();
        shortid = req.getShortid();
        drivprice = req.getDriverfee()+req.getZegofee();
        passprice = req.getDriverfee()+req.getZegofee()-req.getDiscount();
        pickup = req.getPuaddr();
        dropoff = req.getDoaddr();
        reqdate = req.getReqdate();
        shid = req.getShid();
        discount = req.getDiscount();
        status = req.getStatus();
        method = req.getMethod() == null ? Riderequest.REQUEST_METHOD_CARD : req.getMethod();
        lev = req.getReqlevel();
    }
    
    private Integer rid;
    private String shortid;
    private Integer drivprice;
    private Integer passprice;
    private Integer discount;
    private Integer status;
    private String pickup;
    private String dropoff;
    private String reqdate;
    private String shid;
    private String method;
    private Integer lev;

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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }
    
    
}
