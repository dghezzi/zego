/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Notifications;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.PassengerController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Lu
 */
@Singleton
public class SchedulerEJB {

    @EJB
    APNSPusher pusher;

    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Notifications> notCtrl;

    EntityController<Integer, Riderequest> reqCtrl;
    
    EntityController<Integer, RiderequestDrivers> drivCtrl;

    EntityController<Integer, User> uCtrl;

    @EJB
    PassengerController passCtrl;

    @PostConstruct
    private void init() {
        notCtrl = provider.provide(Notifications.class);
        reqCtrl = provider.provide(Riderequest.class);
        drivCtrl = provider.provide(RiderequestDrivers.class);
        uCtrl = provider.provide(User.class);
    }

    @Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
    private void checkNotifications() {
        //Logger.getLogger(SchedulerEJB.class.getName()).log(Level.INFO, "Checking notifications rides");
        String now = RESTDateUtil.getInstance().secondsNow();
        List<Notifications> not = notCtrl.findListCustom("findNotSent", Arrays.asList(new DBTuple("now", now)));

        for (Notifications n : not) {
            pusher.notifyUser(n.getMessage(), n.getToken());
            n.setSentdate(now);
            n.setStatus(Notifications.STATUS_SENT);
            notCtrl.save(n);
        }

    }

    @Schedule(second = "13,26,39,52", minute = "*", hour = "*", persistent = false)
    private void checkRides() {
        Logger.getLogger(SchedulerEJB.class.getName()).log(Level.INFO, "Checking expired rides");
        String now = RESTDateUtil.getInstance().secondsMillisAgo(120);
        List<Riderequest> rexp = reqCtrl.findListCustom("findExpired", Arrays.asList(new DBTuple("now", now)));
        

        for (Riderequest r : rexp) {
            try {                
                pusher.descheduleNotificationForRide(r.getId(), false, drivCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", r.getId()))));
                passCtrl.systemCancel(r);
            } catch (RESTException ex) {
                Logger.getLogger(SchedulerEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
    private void keepAlive() {
        //Logger.getLogger(SchedulerEJB.class.getName()).log(Level.INFO, "Keeping iOS drivers alive");
        String lloc = RESTDateUtil.getInstance().secondsMillisAgo(60 * 10);
        List<User> iosActiveDrivers = uCtrl.findListCustom("findIosDrivers", Arrays.asList(new DBTuple("locdate", lloc)));
        try {
            Logger.getLogger(SchedulerEJB.class.getName()).log(Level.INFO, "{0} iOS Drivers found", iosActiveDrivers.size());
            for (User d : iosActiveDrivers) {
                pusher.keepAlive(d);
            }
        } catch (Exception ex) {
            Logger.getLogger(SchedulerEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
