/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class PromoReportEntry implements Comparable<PromoReportEntry>{
    
    private String code;
    private Integer value;

    public PromoReportEntry() {
    }

    public PromoReportEntry(String code, Integer value) {
        this.code = code;
        this.value = value;
    }
    
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(PromoReportEntry o) {
        return o.getValue().compareTo(value);
    }
    
    
}
