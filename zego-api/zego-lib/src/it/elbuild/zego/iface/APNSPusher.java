/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.elbuild.zego.iface;

import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import java.util.List;

/**
 *
 * @author Lu
 */
public interface APNSPusher { 
    public void notifyUser(String message, String token, Integer iconBadgeNumber);
    public void notifyUser(String message, String token);
    public void descheduleNotificationForRide(Integer rid, boolean notifiy, List<RiderequestDrivers> links);
    public void scheduleMessage(String message, User u, Integer rid, String when);
    public void sendMessageNow(String message, User u, Integer rid);
    public void keepAlive(User u);
    public void factoryReset();
}
