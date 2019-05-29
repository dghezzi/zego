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
public class UpdateRatingRequest {
    
    public UpdateRatingRequest() {
        
    }
    
    private String type;
    private Integer newrating;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNewrating() {
        return newrating;
    }

    public void setNewrating(Integer newrating) {
        this.newrating = newrating;
    }

    

   
    
}
