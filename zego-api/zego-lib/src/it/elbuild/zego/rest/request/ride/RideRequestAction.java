/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.request.ride;

/**
 *
 * @author Lu
 */
public class RideRequestAction {

    public static final String DRIVER_REJECT_ACTION = "driverreject";
    public static final String DRIVER_ACCEPT_ACTION = "driveraccpet";
    public static final String DRIVER_ABORT_ACTION = "driverabort";
    public static final String DRIVER_START_ACTION = "driverstart";
    public static final String DRIVER_END_ACTION = "driverend";
    public static final String DRIVER_IAM_HERE = "iamhere";
    
    public static final String RIDER_CANCEL_ACTION = "ridercancel";
    public static final String RIDER_ABORT_ACTION = "riderabort";
    public static final String RIDER_TERMINATE_ACTION = "riderterminate";
    
    public RideRequestAction() {
        
    }
    
    private String action;
    private Integer uid;
    private Integer rid;
    private String text;
    private Integer priceupdate;
    private Integer capture;
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @return the rid
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * @param rid the rid to set
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }      

    public Integer getPriceupdate() {
        return priceupdate;
    }

    public void setPriceupdate(Integer priceupdate) {
        this.priceupdate = priceupdate;
    }

    /**
     * @return the capture
     */
    public Integer getCapture() {
        return capture;
    }

    /**
     * @param capture the capture to set
     */
    public void setCapture(Integer capture) {
        this.capture = capture;
    }
    
    
}
