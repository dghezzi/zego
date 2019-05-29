/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

/**
 *
 * @author Lu
 */
public class BaseResponse {
    
    public BaseResponse() {
        
    }
    
    public BaseResponse(String m) {
        msg = m;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
       
       
    }
