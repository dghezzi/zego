/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.request;

/**
 *
 * @author Lu
 */
public class NexmoRequest {
    
    public NexmoRequest() {
        
    }
    
    private String api_secret;
    private String api_key;
    private String to;
    private String from;
    private String text;

    /**
     * @return the api_secret
     */
    public String getApi_secret() {
        return api_secret;
    }

    /**
     * @param api_secret the api_secret to set
     */
    public void setApi_secret(String api_secret) {
        this.api_secret = api_secret;
    }

    /**
     * @return the api_key
     */
    public String getApi_key() {
        return api_key;
    }

    /**
     * @param api_key the api_key to set
     */
    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
}
