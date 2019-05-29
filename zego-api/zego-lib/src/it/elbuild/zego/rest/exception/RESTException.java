/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.exception;

import it.elbuild.zego.entities.Errormsg;
import it.elbuild.zego.util.ZegoK;

/**
 *
 * @author Lu
 */
public class RESTException extends Exception {

    public RESTException() {
        code = ZegoK.Error.DEFAULT_ERROR;
        msg = "";
        title = "";
    }
    
    public RESTException(Integer c) {
        code = c;
        msg = "";
        title = "";
    }
    
    public RESTException(Integer c, String m) {
        code = c;
        msg = m;
        title = "";
    }

    public RESTException(Integer c, String m, String title) {
        code = c;
        msg = m;
        title = m;
    }
    
    public RESTException(Integer c, Errormsg e) {
        code = c;
        if(e!=null){
            msg = e.getMessage();
            title = e.getTitle();
        } else {
            msg = "Missing message for code "+code;
            title = "Missing title for code "+code;
        }
    }
    
    private Integer code;
    private String msg;
    private String title;

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
