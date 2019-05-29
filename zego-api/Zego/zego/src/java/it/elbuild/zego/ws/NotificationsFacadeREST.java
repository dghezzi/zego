/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Notifications;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("notifications")
public class NotificationsFacadeREST extends BaseFacadeREST<Integer, Notifications> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Notifications.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   
}
