/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.network.request;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.AppLaunchChecker;
import android.util.Log;

import it.sharethecity.mobile.letzgo.BuildConfig;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 *
 * @author Lu
 */
public class BootRequest {

    private String fbid;
    private String country;
    private String prefix;
    private String mobile;
    private String device;
    private String os;
    private String pushid;
    private Integer uid;
    private String vos;
    private String vapp;
    private String deviceId;
    private String uxlang;

    public BootRequest() {
        os = "Android";
        device = UtilityFunctions.getDeviceName();
        vos = Build.VERSION.RELEASE;
        vapp = BuildConfig.VERSION_NAME;
        deviceId = ApplicationController.getInstance().getImeiCode();
        uxlang = ApplicationController.getInstance().getsDefSystemLanguage();
        pushid = ApplicationController.getInstance().getPushMsg();
        User user = ApplicationController.getInstance().getUserLogged();

        if(user != null){
            if(ZegoConstants.DEBUG){
                Log.d("BootRequest:",user.toString());
            }
            fbid = user.getFbid();
            country = user.getCountry();
            prefix = user.getPrefix();
            mobile = user.getMobile();
            uid = user.getId();
        }
    }


    /**
     * @return the fbid
     */
    public String getFbid() {
        return fbid;
    }

    /**
     * @param fbid the fbid to set
     */
    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return the pushid
     */
    public String getPushid() {
        return pushid;
    }

    /**
     * @param pushid the pushid to set
     */
    public void setPushid(String pushid) {
        this.pushid = pushid;
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

    /**
     * @return the vos
     */
    public String getVos() {
        return vos;
    }

    /**
     * @param vos the vos to set
     */
    public void setVos(String vos) {
        this.vos = vos;
    }

    /**
     * @return the vapp
     */
    public String getVapp() {
        return vapp;
    }

    /**
     * @param vapp the vapp to set
     */
    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUxlang() {
        return uxlang;
    }

    public void setUxlang(String uxlang) {
        this.uxlang = uxlang;
    }
}
