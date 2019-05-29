/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.request;

import java.util.List;

/**
 *
 * @author Lu
 */
public class BatchRedeemRequest {
    
    public BatchRedeemRequest(){
        
    }
    
    private List<Integer> users;
    private String code;

    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }
    
    
    
}
