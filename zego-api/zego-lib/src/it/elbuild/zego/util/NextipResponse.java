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
public class NextipResponse {
    
    public NextipResponse() {
        
    }
    
    public NextipResponse(Integer c, String conn) {
        code = c;
        connect_to = conn;
                
    }
    
    public static final Integer OK = 0;
    public static final Integer CONNECTION_NOT_FOUND = 1;
    public static final Integer CALLER_NOT_FOUND = 2;
    public static final Integer GENERAL_ERROR = 99;
    
    private Integer code;
    private String connect_to;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getConnect_to() {
        return connect_to;
    }

    public void setConnect_to(String connect_to) {
        this.connect_to = connect_to;
    }

    
    
    
}
