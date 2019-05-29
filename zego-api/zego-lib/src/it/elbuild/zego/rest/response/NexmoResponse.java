/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author Lu
 */
public class NexmoResponse {
    
    public NexmoResponse() {
        
    }
    
    @SerializedName("message-count")
    private String messageCount;
    
    private List<NexmoMessage> messages;

    /**
     * @return the messageCount
     */
    public String getMessageCount() {
        return messageCount;
    }

    /**
     * @param messageCount the messageCount to set
     */
    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * @return the messages
     */
    public List<NexmoMessage> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<NexmoMessage> messages) {
        this.messages = messages;
    }
}
