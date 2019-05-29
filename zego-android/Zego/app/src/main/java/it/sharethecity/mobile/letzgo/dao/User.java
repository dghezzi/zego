/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;


/**
 *
 * @author Lu
 */

public class User implements Serializable {

    public static final Integer STATUS_IDLE = 0;
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_BANNED = 2;


    public static final int REALTIME_STATUS_PASSENGER_IDLE = 100;
    public static final int REALTIME_STATUS_PASSENGER_REQUEST_SENT = 101;
    public static final int REALTIME_STATUS_PASSENGER_WAITING_DRIVER = 102;
    public static final int REALTIME_STATUS_PASSENGER_ONRIDE = 103;
    public static final int REALTIME_STATUS_PASSENGER_PAYMENT_DUE = 104;
    public static final int REALTIME_STATUS_PASSENGER_FEEDBACK_DUE = 105;

    public static final int REALTIME_STATUS_DRIVER_IDLE = 200;
    public static final int REALTIME_STATUS_DRIVER_ANSWERING = 201;
    public static final int REALTIME_STATUS_DRIVER_PICKINGUP = 202;
    public static final int REALTIME_STATUS_DRIVER_ONRIDE = 203;
    public static final int REALTIME_STATUS_DRIVER_FEEDBACKDUE = 204;

    public static final String UTYPE_ADMIN = "admin";
    public static final String UTYPE_USER = "user";

    public static final String UMODE_RIDER = "rider";
    public static final String UMODE_DRIVER = "driver";

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String utype;
    private String umode;
    private Integer candrive;
    private String fname;
    private String lname;
    private String email;
    private String wemail;
    private String country;
    private String prefix;
    private String mobile;
    private String insdate;
    private String moddate;
    private String lastseen;
    private String img;
    private String fbid;
    private Integer payok;
    private Integer mobok;
    private String refcode;
    private Integer refuid;
    private String device;
    private String vos;
    private String os;
    private String pushid;
    private String vapp;
    private String gender;
    private String birthdate;
    private String deviceid;
    private Double llat;
    private Double llon;
    private String llocdate;
    private Integer status;
    private Integer rtstatus;
    private Integer bitmask;
    private String banexpdate;
    private String banreason;
    private String stripeid;
    private String zegotoken;
    private Integer tcok;
    private Double pavg;
    private Double davg;
    private Integer current;
    private Integer debt;
    private Integer cashdue;
    private Integer cardonly;
    private Integer cashused;


    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getFname() {
        return fname == null ? "" : fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname == null ? "" : lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWemail() {
        return wemail;
    }

    public void setWemail(String wemail) {
        this.wemail = wemail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrefix() {
        return  prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getImg() {
        return img;
    }

    public boolean hasImage(){
        return img != null && !img.isEmpty();
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public Integer getPayok() {
        return payok;
    }

    public void setPayok(Integer payok) {
        this.payok = payok;
    }

    public Integer getMobok() {
        return mobok;
    }

    public void setMobok(Integer mobok) {
        this.mobok = mobok;
    }

    public String getRefcode() {
        return refcode;
    }

    public void setRefcode(String refcode) {
        this.refcode = refcode;
    }

    public Integer getRefuid() {
        return refuid;
    }

    public void setRefuid(Integer refuid) {
        this.refuid = refuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getVos() {
        return vos;
    }

    public void setVos(String vos) {
        this.vos = vos;
    }

    public String getVapp() {
        return vapp;
    }

    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public Double getLlat() {
        return llat;
    }

    public void setLlat(Double llat) {
        this.llat = llat;
    }

    public Double getLlon() {
        return llon;
    }

    public void setLlon(Double llon) {
        this.llon = llon;
    }

    public String getLlocdate() {
        return llocdate;
    }

    public void setLlocdate(String llocdate) {
        this.llocdate = llocdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRtstatus() {
        return rtstatus;
    }

    public void setRtstatus(Integer rtstatus) {
        this.rtstatus = rtstatus;
    }

    public Integer getBitmask() {
        return bitmask;
    }

    public void setBitmask(Integer bitmask) {
        this.bitmask = bitmask;
    }

    public String getBanexpdate() {
        return banexpdate;
    }

    public void setBanexpdate(String banexpdate) {
        this.banexpdate = banexpdate;
    }

    public String getBanreason() {
        return banreason;
    }

    public void setBanreason(String banreason) {
        this.banreason = banreason;
    }

    public String getStripeid() {
        return stripeid;
    }

    public void setStripeid(String stripeid) {
        this.stripeid = stripeid;
    }

    public String getZegotoken() {
        return zegotoken;
    }

    public void setZegotoken(String zegotoken) {
        this.zegotoken = zegotoken;
    }

    public void updateUserFromCompactUser(CompactUser cmpUser) {
        if(cmpUser != null){
            llat = cmpUser.getLat();
            llon = cmpUser.getLng();
            rtstatus = cmpUser.getRtstatus();
            current = cmpUser.getCurrent();
        }

    }

    public void updateDriverFromCompactDriver(CompactDriver cmpDriver) {
        if(cmpDriver!=null){
            llat = cmpDriver.getLat();
            llon = cmpDriver.getLng();
            rtstatus = cmpDriver.getRtstatus();
            current = cmpDriver.getCurrent();
        }
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id:" + id + "\n" +
         "rtstatus:" + rtstatus + "\n" +
         "lat:" + llat + "\n" +
         "lng:" + llon + "\n" +
         "email:" + email + "\n" +
         "phone:" + mobile + "\n" +
         "umode:" + umode + "\n" +
         "token:" + zegotoken + "\n" +
         "current:" + current + "\n" ;
    }

    /**
     * @return the umode
     */
    public String getUmode() {
        return umode;
    }

    /**
     * @param umode the umode to set
     */
    public void setUmode(String umode) {
        this.umode = umode;
    }

    /**
     * @return the candrive
     */
    public Integer getCandrive() {
        return candrive;
    }

    /**
     * @param candrive the candrive to set
     */
    public void setCandrive(Integer candrive) {
        this.candrive = candrive;
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


    public boolean isDriver() {
        return umode != null && umode.equalsIgnoreCase(UMODE_DRIVER);
    }

    public String getProfImgUrl() {
        return ZegoConstants.AWS3.URL_BACKET + img;
    }

    public boolean isFB() {
        return fbid != null && !fbid.isEmpty();
    }

    public String getFormattedMobile(){
        return  (mobile!= null &&  mobile.length() > 3 )? (mobile.substring(0,3) + " " + mobile.substring(3,mobile.length())) : "";

    }

    public Integer getTcok() {
        return tcok;
    }

    public void setTcok(Integer tcok) {
        this.tcok = tcok;
    }

    public Double getPavg() {
        return pavg;
    }

    public void setPavg(Double pavg) {
        this.pavg = pavg;
    }

    public Double getDavg() {
        return davg;
    }

    public void setDavg(Double davg) {
        this.davg = davg;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }


    public LatLng getMyPositionLatLng(){
        if(llat != null && llon != null){
            return new LatLng(llat,llon);
        }else {
            return new LatLng(ZegoConstants.MILANO_LAT,ZegoConstants.MILANO_LNG);
        }
    }

    public LatLng setMyLastPosition(LatLng lastPosition){
        if(lastPosition != null){
            llat = lastPosition.latitude;
            llon = lastPosition.longitude;
        }

        return getMyPositionLatLng();
    }

    public Double getRatingByCanDriveStatus() {
        Double avg = candrive == 1 ? davg : pavg;
        return  avg == null ? 0d : avg;
    }

    public Integer getDebt() {
        return debt == null ? 0 : debt;
    }

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    public Integer getCashdue() {
        return cashdue;
    }

    public void setCashdue(Integer cashdue) {
        this.cashdue = cashdue;
    }

    public Integer getCardonly() {
        return cardonly;
    }

    public void setCardonly(Integer cardonly) {
        this.cardonly = cardonly;
    }

    public Integer getCashused() {
        return cashused;
    }

    public void setCashused(Integer cashused) {
        this.cashused = cashused;
    }


    public boolean isFirstCashUse(){
        if ((cashused == null || cashused == 0) && ApplicationController.getInstance().isFirstCash()){
            return true;
        }else if (!ApplicationController.getInstance().isFirstCash()){
            return false;
        }else{
            return false;
        }
        //return  ApplicationController.getInstance().isFirstCash();
    }
}
