/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

/**
 *
 * @author Lu
 */
public class PriceEstimateResponse {

    public PriceEstimateResponse() {

    }
    private Integer extprice;
    private Integer driverfee;
    private Integer zegofee;
    private Integer stripedriverfee;
    private Integer stripezegofee;
    private Integer discount;
    private Integer seconds;
    private Integer meters;
    private Integer promoid;
    private String dropoff;
     private String promocode;

    public Integer getExtprice() {
        return extprice;
    }

    public void setExtprice(Integer extprice) {
        this.extprice = extprice;
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

    public Integer getPromoid() {
        return promoid;
    }

    public void setPromoid(Integer promoid) {
        this.promoid = promoid;
    }

    /**
     * @return the dropoff
     */
    public String getDropoff() {
        return dropoff;
    }

    /**
     * @param dropoff the dropoff to set
     */
    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getMeters() {
        return meters;
    }

    public void setMeters(Integer meters) {
        this.meters = meters;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }    

    public Integer getStripedriverfee() {
        return stripedriverfee;
    }

    public void setStripedriverfee(Integer stripedriverfee) {
        this.stripedriverfee = stripedriverfee;
    }

    public Integer getStripezegofee() {
        return stripezegofee;
    }

    public void setStripezegofee(Integer stripezegofee) {
        this.stripezegofee = stripezegofee;
    }
    
    
}
