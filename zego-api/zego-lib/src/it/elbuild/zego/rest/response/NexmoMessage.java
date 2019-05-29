/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Lu
 */
public class NexmoMessage {
    
    public NexmoMessage(){
        
    }
    
    private String to;
    private String status;
    
    @SerializedName("message-id")
    private String messageId;
    
    @SerializedName("message-price")
    private String messagePrice;
    
    private String network;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessagePrice() {
        return messagePrice;
    }

    public void setMessagePrice(String messagePrice) {
        this.messagePrice = messagePrice;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    
    
    
}
