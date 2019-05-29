/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.rest.response.ride;

import it.elbuild.zego.entities.Service;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lu
 */
public class EtaResponse {

    public EtaResponse() {
        drivers = new ArrayList<>();
    }
    
    
    private Integer eta;
    private Integer dist;
    private String address;
    private List<CompactDriver> drivers;
    private List<Service> services;

    public Integer getEta() {
        return eta;
    }

    public void setEta(Integer eta) {
        this.eta = eta;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<CompactDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<CompactDriver> drivers) {
        this.drivers = drivers;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
    
    
    
}
