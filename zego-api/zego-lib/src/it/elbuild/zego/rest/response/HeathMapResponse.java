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
public class HeathMapResponse {
    
    public HeathMapResponse(){
        
    }
    
    public HeathMapResponse(Double lat, Double lng){
        l = lat;
        n = lng;
    }
    
    private Double l;
    private Double n;

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getN() {
        return n;
    }

    public void setN(Double n) {
        this.n = n;
    }
    
    
}
