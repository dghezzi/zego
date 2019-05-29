/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.network.request;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import it.sharethecity.mobile.letzgo.dao.CompactDriver;
import it.sharethecity.mobile.letzgo.dao.CompactUser;


public class Riderequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final long EXPIRY_TIME_MINUTES = 120; // in second
    public static final Integer REQUEST_TIME_OUT = 14;
    public static final String CASH = "cash";
    public static final String CARD = "card";


    private Integer id;
    private Integer pid;
    private Integer did;
    private Integer status;
    private Double pulat;
    private Double pulng;
    private String puaddr;
    private Double dolat;
    private Double dolng;
    private String doaddr;
    private String reqdate;
    private String assigndate;
    private String canceldate;
    private String abortdate;
    private String startdate;
    private String enddate;
    private Integer extmeters;
    private Integer extsecond;
    private Integer extshort;
    private Integer drivereta;
    private Double realpulat;
    private Double realpulng;
    private String realpuaddr;
    private Double realdolat;
    private Double realdolng;
    private String realdoaddr;
    private Integer extprice;
    private Integer driverfee;
    private Integer zegofee;
    private Integer passprice;
    private Integer numpass;
    private Integer reqlevel;
    private Integer passrating;
    private Integer drivrating;
    private Integer discount;
    private Integer promoid;
    private String shid;
    private String abortreason;
    private String cancelreason;
    private String shortid;
    private Integer driverprice;
    private String method;

    public static final Integer STATUS_IDLE = 0;
    public static final Integer STATUS_NO_DRIVERS = 1;
    public static final Integer STATUS_SUBMITTED = 2;

    public static final Integer REQUEST_STATUS_DRIVER_ANSWERED = 3;
    public static final Integer REQUEST_STATUS_PASSENGER_CANCELED = 4;
    public static final Integer REQUEST_STATUS_DRIVER_CANCELED = 5;
    public static final Integer REQUEST_STATUS_ON_RIDE = 6;
    public static final Integer REQUEST_STATUS_DRIVER_ABORTED = 7;
    public static final Integer REQUEST_STATUS_PASSENGER_ABORTED = 8;
    public static final Integer REQUEST_STATUS_ENDED = 9;
    public static final Integer REQUEST_STATUS_PAID = 10;
    public static final Integer REQUEST_STATUS_PASSENGER_TERMINATED = 11;
    public static final Integer REQUEST_PAYMENT_FAILED = 12;


    private CompactDriver driver;


    private CompactUser rider;

    public Riderequest() {
        numpass = 0;
    }

    public Riderequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPulat() {
        return pulat;
    }

    public void setPulat(Double pulat) {
        this.pulat = pulat;
    }

    public Double getPulng() {
        return pulng;
    }

    public void setPulng(Double pulng) {
        this.pulng = pulng;
    }

    public String getPuaddr() {
        return puaddr;
    }

    public void setPuaddr(String puaddr) {
        this.puaddr = puaddr;
    }

    public Double getDolat() {
        return dolat;
    }

    public void setDolat(Double dolat) {
        this.dolat = dolat;
    }

    public Double getDolng() {
        return dolng;
    }

    public void setDolng(Double dolng) {
        this.dolng = dolng;
    }

    public String getDoaddr() {
        return doaddr;
    }

    public void setDoaddr(String doaddr) {
        this.doaddr = doaddr;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getAssigndate() {
        return assigndate;
    }

    public void setAssigndate(String assigndate) {
        this.assigndate = assigndate;
    }

    public String getCanceldate() {
        return canceldate;
    }

    public void setCanceldate(String canceldate) {
        this.canceldate = canceldate;
    }

    public String getAbortdate() {
        return abortdate;
    }

    public void setAbortdate(String abortdate) {
        this.abortdate = abortdate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Integer getExtmeters() {
        return extmeters;
    }

    public void setExtmeters(Integer extmeters) {
        this.extmeters = extmeters;
    }

    public Integer getExtsecond() {
        return extsecond;
    }

    public void setExtsecond(Integer extsecond) {
        this.extsecond = extsecond;
    }

    public Integer getExtshort() {
        return extshort;
    }

    public void setExtshort(Integer extshort) {
        this.extshort = extshort;
    }

    public Integer getDrivereta() {
        return drivereta;
    }

    public void setDrivereta(Integer drivereta) {
        this.drivereta = drivereta;
    }

    public Double getRealpulat() {
        return realpulat;
    }

    public void setRealpulat(Double realpulat) {
        this.realpulat = realpulat;
    }

    public Double getRealpulng() {
        return realpulng;
    }

    public void setRealpulng(Double realpulng) {
        this.realpulng = realpulng;
    }

    public String getRealpuaddr() {
        return realpuaddr;
    }

    public void setRealpuaddr(String realpuaddr) {
        this.realpuaddr = realpuaddr;
    }

    public Double getRealdolat() {
        return realdolat;
    }

    public void setRealdolat(Double realdolat) {
        this.realdolat = realdolat;
    }

    public Double getRealdolng() {
        return realdolng;
    }

    public void setRealdolng(Double realdolng) {
        this.realdolng = realdolng;
    }

    public String getRealdoaddr() {
        return realdoaddr;
    }

    public void setRealdoaddr(String realdoaddr) {
        this.realdoaddr = realdoaddr;
    }

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

    public Integer getPassprice() {
        return passprice == null ? 0 : passprice;
    }

    public void setPassprice(Integer passprice) {
        this.passprice = passprice;
    }

    public Integer getNumpass() {
        return numpass;
    }

    public void setNumpass(Integer numpass) {
        this.numpass = numpass;
    }

    public Integer getReqlevel() {
        return reqlevel;
    }

    public void setReqlevel(Integer reqlevel) {
        this.reqlevel = reqlevel;
    }

    public Integer getPassrating() {
        return passrating;
    }

    public void setPassrating(Integer passrating) {
        this.passrating = passrating;
    }

    public Integer getDrivrating() {
        return drivrating;
    }

    public void setDrivrating(Integer drivrating) {
        this.drivrating = drivrating;
    }

    public Integer getDiscount() {
        return discount == null ? 0 : discount;
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

    public String getShid() {
        return shid;
    }

    public void setShid(String shid) {
        this.shid = shid;
    }

    public String getAbortreason() {
        return abortreason;
    }

    public void setAbortreason(String abortreason) {
        this.abortreason = abortreason;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }

    public CompactDriver getDriver() {
        return driver;
    }

    public void setDriver(CompactDriver driver) {
        this.driver = driver;
    }

    public CompactUser getRider() {
        return rider;
    }

    public void setRider(CompactUser rider) {
        this.rider = rider;
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
        if (!(object instanceof Riderequest)) {
            return false;
        }
        Riderequest other = (Riderequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id:" + id + "\n" +
                "status:" + status + "\n" +
                "reqlevel:" + reqlevel + "\n" +
                "Passenger rtStatus:" +(rider != null? rider.getRtstatus() : "null") + "\n" +
                "Driver rtStatus:" +(driver != null? driver.getRtstatus() : "null") + "\n" +
                "PuAdd:" + pulat +  "," + pulng + "\n" +
                "DoAdd:" + dolat +  "," + dolng + "\n" ;
    }


    public boolean isDoAddressValid(){
        return doaddr != null && !doaddr.isEmpty() && dolng != null && dolat != null;
    }

    public boolean isPuAddressValid(){
        return puaddr != null && !puaddr.isEmpty() && pulat != null && pulat != null;
    }


    public boolean isFreeCancelTimeElapsed(){
        if(assigndate ==  null || assigndate.isEmpty()) return false;

        long now = Calendar.getInstance(TimeZone.getTimeZone("CET")).getTimeInMillis()/1000;
        long assignedDateLong = Long.valueOf(assigndate);
         return now - assignedDateLong > EXPIRY_TIME_MINUTES;

    }

    public LatLng getPuLatLng(){
        return new LatLng(pulat,pulng);
    }

    public LatLng getDoLatLng(){
        return new LatLng(dolat,dolng);
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public Integer getDriverprice() {
        return driverprice;
    }

    public void setDriverprice(Integer driverprice) {
        this.driverprice = driverprice;
    }

    public Integer getDiscountedPrice(){

        return Math.max(0,driverfee + zegofee - getDiscount());
    }

    public Integer getOriginalPrice(){
        return driverfee + zegofee;
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
}
