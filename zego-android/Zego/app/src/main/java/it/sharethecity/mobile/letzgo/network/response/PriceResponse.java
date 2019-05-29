/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.network.response;


/**
 *
 * @author Lu
 */
public class PriceResponse {

    public PriceResponse() {
        
    }
    
    private String dropoff;
    private Integer price;
    private Integer seconds;
    private Integer meters;
    private Integer zegofee;
    private Integer driverfee;
    private Integer discount;
    private String promocode;
    private Integer promoid;


    public String getDropoff() {
        return dropoff;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Integer getZegofee() {
        return zegofee;
    }

    public void setZegofee(Integer zegofee) {
        this.zegofee = zegofee;
    }

    public Integer getDriverfee() {
        return driverfee;
    }

    public void setDriverfee(Integer driverfee) {
        this.driverfee = driverfee;
    }

    public Integer getDiscount() {
        return discount == null ? 0 : discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public Integer getPromoid() {
        return promoid;
    }

    public void setPromoid(Integer promoid) {
        this.promoid = promoid;
    }



    public Integer getDiscountedPrice(){
        return driverfee + zegofee - getDiscount();
    }

    public Integer getOriginalPrice(){
        return driverfee + zegofee;
    }
    
}
