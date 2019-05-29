/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.network.request;

import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.dao.User;

/**
 *
 * @author Lu
 */
public class ReferralRequest {

    public ReferralRequest() {
       User u = ApplicationController.getInstance().getUserLogged();
        if(u != null) uid = u.getId();

    }

    private String referral;
    private Integer uid;

    /**
     * @return the referral
     */
    public String getReferral() {
        return referral;
    }

    /**
     * @param referral the referral to set
     */
    public void setReferral(String referral) {
        this.referral = referral;
    }

    /**
     * @return the uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }



}
