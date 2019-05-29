/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Pin;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.response.NexmoResponse;

/**
 *
 * @author Lu
 */
public interface SmsSender {
    
    public NexmoResponse sendSms(Pin p, String target) throws RESTException;        
}
