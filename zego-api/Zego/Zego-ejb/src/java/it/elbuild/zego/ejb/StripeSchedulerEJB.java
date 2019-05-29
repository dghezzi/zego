/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.rest.exception.RESTException;
import java.util.ArrayList;
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
public class StripeSchedulerEJB {

    @EJB
    APNSPusher pusher;

    @EJB
    ConfController conf;

    @EJB
    DAOProvider provider;

    EntityController<Integer, Riderequest> reqCtrl;

    EntityController<Integer, User> uCtrl;

    @EJB
    StripeInterface stripeController;

   

    @PostConstruct
    private void init() {
        reqCtrl = provider.provide(Riderequest.class);
        uCtrl = provider.provide(User.class);
    }

    @Schedule(second = "0", minute = "0", hour = "4", persistent = false)
    private void checkUnpaidRides() {

        List<User> us = uCtrl.findListCustom("findWithDebt", new ArrayList<DBTuple>());
        for (User u : us) {
            try {
                boolean b = stripeController.attemptPayUserDebt(u,true);
                if (b) {
                    u.setDebt(0);
                    uCtrl.save(u);
                }
            } catch (RESTException ex) {
                Logger.getLogger(StripeSchedulerEJB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
