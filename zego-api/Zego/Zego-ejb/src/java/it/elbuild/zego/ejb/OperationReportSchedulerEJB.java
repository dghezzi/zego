/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Driveravailability;
import it.elbuild.zego.entities.Location;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userlogin;
import it.elbuild.zego.entities.Zone;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Lu
 */
@Singleton
public class OperationReportSchedulerEJB {

    @EJB
    DAOProvider provider;

    EntityController<Integer, Userlogin> loginCtrl;
    EntityController<Integer, User> userCtrl;
    EntityController<Integer, Driveravailability> avaCtrl;
    EntityController<Integer, Location> locationCtrl;
    EntityController<Integer, Zone> zoneCtrl;

    private List<Zone> zones;

    @PostConstruct
    private void init() {
        loginCtrl = provider.provide(Userlogin.class);
        userCtrl = provider.provide(User.class);
        avaCtrl = provider.provide(Driveravailability.class);
        locationCtrl = provider.provide(Location.class);
        zoneCtrl = provider.provide(Zone.class);
        zones = zoneCtrl.findAll();
    }

    @Schedule(second = "0", minute = "10", hour = "0", persistent = false)
    private void logUserlogin() {
        String stop = RESTDateUtil.getInstance().secondsMillisAgo(10*60);
        String start = RESTDateUtil.getInstance().secondsMillisAgo((24 * 60 * 60)+(10*60));
        List<User> uu = userCtrl.findListCustom("findByLastseenBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop)));
        for(User u : uu) {
            Userlogin ul = new Userlogin();
            ul.setCandrive(u.getCandrive());
            ul.setFname(u.getFname());
            ul.setLname(u.getLname());
            ul.setUid(u.getId());
            ul.setLogindate(u.getLastseen());
            loginCtrl.save(ul);
        }
    }

    @Schedule(second = "0", minute = "*/15", hour = "*", persistent = false)
    private void logDriveravailability() {
        String stop = RESTDateUtil.getInstance().secondsNow();
        String start = RESTDateUtil.getInstance().secondsMillisAgo(15 * 60);
        List<Location> locs = locationCtrl.findListCustom("findByModeBetween", Arrays.asList(new DBTuple("start", start), new DBTuple("stop", stop), new DBTuple("mode", User.UMODE_DRIVER.substring(0, 1))));
        HashMap<Integer, Location> locmap = new HashMap<>();

        for (Location l : locs) {
            if (!locmap.containsKey(l.getUid())) {
                locmap.put(l.getUid(), l);
            }
        }

        for (Location ll : locmap.values()) {
            User u = userCtrl.findById(ll.getUid());
            Driveravailability a = new Driveravailability();
            a.setAvadate(ll.getLdate());
            a.setDid(ll.getUid());
            a.setFname(u.getFname());
            a.setLname(u.getLname());

            Zone selectedZone = null;
            for (Zone z : zones) {
                if (z.include(ll.getLat(), ll.getLng())) {
                    selectedZone = z;
                    break;
                }

            }
            a.setZid(selectedZone == null ? 0 : selectedZone.getId());
            a.setZonename(selectedZone == null ? "ND" : selectedZone.getName());
            avaCtrl.save(a);                    
        }
    }

}
