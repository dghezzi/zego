/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

import com.stripe.model.Card;

/**
 *
 * @author Lu
 */
public class StripeCard {

    public StripeCard() {
    
    }
    
    public StripeCard(Card pt, String defSource) {
        expmonth = pt.getExpMonth();
        lastdigit = pt.getLast4();
        brand = pt.getBrand();
        expyear = pt.getExpYear();
        card = pt.getId();
        customer = pt.getCustomer();
        preferred = card.equalsIgnoreCase(defSource) ? 1 : 0;
    }
    
    private String card;
    private String customer;
    private String brand;
    private String lastdigit;
    private Integer expmonth;
    private Integer expyear;
    private Integer preferred;
    
    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLastdigit() {
        return lastdigit;
    }

    public void setLastdigit(String lastdigit) {
        this.lastdigit = lastdigit;
    }

    public Integer getExpmonth() {
        return expmonth;
    }

    public void setExpmonth(Integer expmonth) {
        this.expmonth = expmonth;
    }

    public Integer getExpyear() {
        return expyear;
    }

    public void setExpyear(Integer expyear) {
        this.expyear = expyear;
    }

    public Integer getPreferred() {
        return preferred;
    }

    public void setPreferred(Integer preferred) {
        this.preferred = preferred;
    }
    
    
}
