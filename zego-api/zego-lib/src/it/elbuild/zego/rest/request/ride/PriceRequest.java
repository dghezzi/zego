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
public class PriceRequest {

    public PriceRequest() {
        
    }
    
    
    private Double pulat;
    private Double pulng;
    private Double dolat;
    private Double dolng;
    private String puplaceid;
    private String doplaceid;
    private Integer level;

    /**
     * @return the pulat
     */
    public Double getPulat() {
        return pulat;
    }

    /**
     * @param pulat the pulat to set
     */
    public void setPulat(Double pulat) {
        this.pulat = pulat;
    }

    /**
     * @return the pulng
     */
    public Double getPulng() {
        return pulng;
    }

    /**
     * @param pulng the pulng to set
     */
    public void setPulng(Double pulng) {
        this.pulng = pulng;
    }

    /**
     * @return the dolat
     */
    public Double getDolat() {
        return dolat;
    }

    /**
     * @param dolat the dolat to set
     */
    public void setDolat(Double dolat) {
        this.dolat = dolat;
    }

    /**
     * @return the dolng
     */
    public Double getDolng() {
        return dolng;
    }

    /**
     * @param dolng the dolng to set
     */
    public void setDolng(Double dolng) {
        this.dolng = dolng;
    }

    /**
     * @return the puplaceid
     */
    public String getPuplaceid() {
        return puplaceid;
    }

    /**
     * @param puplaceid the puplaceid to set
     */
    public void setPuplaceid(String puplaceid) {
        this.puplaceid = puplaceid;
    }

    /**
     * @return the doplaceid
     */
    public String getDoplaceid() {
        return doplaceid;
    }

    /**
     * @param doplaceid the doplaceid to set
     */
    public void setDoplaceid(String doplaceid) {
        this.doplaceid = doplaceid;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }
    
}
