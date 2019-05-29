/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import el.persistence.db.DBTuple;
import elbuild.utils.RandomPassword;
import it.elbuild.zego.entities.Address;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Notifications;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Zone;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.CashController;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.GooglePlaceInterface;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.iface.PassengerController;
import it.elbuild.zego.iface.PriceCalculator;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.persistence.DAOFactory;
import it.elbuild.zego.persistence.NativeSQLDAO;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.RideRequestAction;
import it.elbuild.zego.rest.response.StripeCard;
import it.elbuild.zego.rest.response.ride.CompactDriver;
import it.elbuild.zego.rest.response.ride.CompactUser;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.Pair;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class PassengerControllerBean implements PassengerController {

    @EJB
    StripeInterface stripe;

    @EJB
    DAOFactory factory;
    
    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Address> addrCtrl;

    EntityController<Integer, Driverdata> dataCtrl;

    EntityController<Integer, Riderequest> reqCtrl;

    EntityController<Integer, RiderequestDrivers> drivCtrl;

    EntityController<Integer, Zone> zoneCtrl;
    @EJB
    ConfController conf;
    
    @EJB
    APNSPusher pusher;
           
    @EJB
    MailSender mail;

    @EJB
    GooglePlaceInterface google;
     
    @EJB
    CashController cashCtrl;
    
    private GeoApiContext geoCtx;
    private NativeSQLDAO sql;

    @EJB
    PriceCalculator priceCalculator;

    private List<Zone> zones;
    
    @PostConstruct
    private void init() {
        userCtrl = provider.provide(User.class);
        reqCtrl = provider.provide(Riderequest.class);
        drivCtrl = provider.provide(RiderequestDrivers.class);
        addrCtrl = provider.provide(Address.class);
        dataCtrl = provider.provide(Driverdata.class);
        zoneCtrl = provider.provide(Zone.class);
        geoCtx = new GeoApiContext().setEnterpriseCredentials("", "");
        sql = factory.getNativeSQLDAO();
        zones = zoneCtrl.findAll();
    }

    @Override
    public List<StripeCard> userCards(Integer uid) throws RESTException {
        return stripe.userCards(uid);
    }

    @Override
    public Riderequest sendRequest(Riderequest req, boolean force) throws RESTException {
        // fetch costants
        if (zones == null || zones.isEmpty()) {
            zones = zoneCtrl.findAll();
        }
        Integer maxMeters = conf.getConfByKey(Conf.RADIUS_MAX_DISTANCE_METERS).asInteger(5000);
        Integer maxSeconds = conf.getConfByKey(Conf.RADIUS_MAX_LOC_AGE_SECONDS).asInteger(5000);
        Integer maxCanditates = conf.getConfByKey(Conf.MAX_CANDIDATES).asInteger(5);
        Integer maxDistance = conf.getConfByKey(Conf.ZEGO_LONGEST_ROUTE).asInteger(5);
        Integer minDistance = conf.getConfByKey(Conf.ZEGO_SHORTEST_ROUTE).asInteger(5);
        Integer maxPassDistance = conf.getConfByKey(Conf.ZEGO_PASS_PICKUP_MAX_DISTANCE).asInteger(5);
        Integer maxCashDue = conf.getConfByKey(Conf.ZEGO_MAX_CASH_DUE).asInteger(2000);

        
        User pass = userCtrl.findById(req.getPid());

        if (pass == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        }
        if (!pass.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_IDLE)) {
            throw new RESTException(ZegoK.Error.USER_IS_NOT_IDLE);
        }
        
        if(pass.getDebt() != null && pass.getDebt() > 0) {
            throw new RESTException(ZegoK.Error.USER_HAS_UNPAID_RIDES);
        }
        
        if(req.hasIssues()) {
            throw new RESTException(ZegoK.Error.REQUEST_IS_MISCONFIGURED);
        }
        
        Zone selectedZone = null;
        for (Zone z : zones) {
            if (z.include(req.getPulat(), req.getPulng())) {
                selectedZone = z;
                break;
            }
        }
        
        // ovveride
        if(selectedZone != null) {
            maxCanditates = selectedZone.getDrivercandidates();
            maxMeters = selectedZone.getDriverradius();
        }
        
        // checks on the passenger
        long passpickupdistance = -1;
        try {
            passpickupdistance = matrixDistance(req.getPulat(), req.getPulng(), pass.getLlat(), pass.getLlon());
        } catch (RESTException ex) {
          // silent google exception  
        } catch (NullPointerException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        

        if (!force && passpickupdistance != -1) {
            if (passpickupdistance > maxPassDistance) {
                throw new RESTException(ZegoK.Error.USER_TOO_FAR_FROM_PICKUP);
            }
        }

        if ((pass.getPayok() == null || pass.getPayok().equals(0)) && (req.getMethod() != null && !req.getMethod().equals(Riderequest.REQUEST_METHOD_CASH))) {
            throw new RESTException(ZegoK.Error.MISSING_PAYMENT_METHOD);
        }

        req.setReqdate(RESTDateUtil.getInstance().secondsNow());
        req.setFailctr(0);
        
        // checks on the ride
        long distance = -1;
        try {
           distance = matrixDistance(req.getPulat(), req.getPulng(), req.getDolat(), req.getDolng());
        } catch (RESTException ex) {
          // silent google exception  
        }
        

        if (!force && distance != -1) {
            if (distance < minDistance) {
                throw new RESTException(ZegoK.Error.RIDE_IS_TOO_SHORT);
            } else if (distance > maxDistance) {
                throw new RESTException(ZegoK.Error.RIDE_IS_TOO_LONG);
            }
        }
        System.out.println("PRICING: "+ req.getPassprice() +  " " + req.getExtprice() + " " + req.getDriverprice());

        req = priceRequest(req, true);
        req.setShid("sh_" + UUID.randomUUID().toString());
        req.setShortid(RandomPassword.generate(6).toUpperCase());
        req.setStatus(Riderequest.REQUEST_STATUS_IDLE);
        req = reqCtrl.save(req);

        // try to create a charge on stripe
        // PREAUTH IS REMOVED
        //req = stripe.preauthorize(req);
        if (false && req.getChargestatus().equalsIgnoreCase(Riderequest.REQUEST_CHARGE_STATUS_FAILED)) {
            req.setStatus(Riderequest.REQUEST_MISSING_FUNDS);
            reqCtrl.save(req);
            throw new RESTException(ZegoK.Error.MISSING_FUNDS);

        } else {

            // check if there are available drivers
            List<User> candidates = sql.getDriverCandidates(req.getPulat(), req.getPulng(), maxMeters, maxSeconds, maxCanditates, req.getMethod(), maxCashDue, req.getReqlevel());

            // set the status to CREATED
            pass = userCtrl.findById(req.getPid());
            if (!pass.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_IDLE)) {
                throw new RESTException(ZegoK.Error.USER_IS_NOT_IDLE);
            }
            
            if (candidates == null || candidates.isEmpty()) {
                req.setStatus(Riderequest.REQUEST_STATUS_NO_DRIVERS);
                reqCtrl.save(req);
                stripe.release(req);
                throw new RESTException(ZegoK.Error.NO_DRIVERS);
            } else {
                processCandidates(candidates, req);
            }

            pass.setCurrent(req.getId());
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_REQUEST_SENT);
            pass = userCtrl.save(pass);
            req.setStatus(Riderequest.REQUEST_STATUS_SUBMITTED);
            Integer now = Integer.parseInt(RESTDateUtil.getInstance().secondsNow());
            
            req.setFreecanceldate(String.valueOf(now+req.getDrivereta()+90));
            req = reqCtrl.save(req);
            
            // save pickup
            Address pickup = new Address();
            pickup.setAddress(req.getPuaddr());
            pickup.setInsdate(RESTDateUtil.getInstance().secondsNow());
            pickup.setLat(req.getPulat());
            pickup.setLng(req.getPulng());
            pickup.setType(Address.ADDRESS_PICKUP);
            pickup.setUid(pass.getId());
            addrCtrl.save(pickup);

            // save dropoff
            Address dropoff = new Address();
            dropoff.setAddress(req.getDoaddr());
            dropoff.setInsdate(RESTDateUtil.getInstance().secondsNow());
            dropoff.setLat(req.getDolat());
            dropoff.setLng(req.getDolng());
            dropoff.setType(Address.ADDRESS_DROPOFF);
            dropoff.setUid(pass.getId());
            addrCtrl.save(dropoff);
            
            return enrich(req, null, pass);
        }

    }

    private void processCandidates(List<User> candidates, Riderequest r) {

        try {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "CANDIDATES_DETECTED {0} candidates", candidates.size());
            Integer interleaving = conf.getConfByKey(Conf.CANDIDATES_INTERLEAVING).asInteger(10);

            List<LatLng> orgs = new ArrayList<>();
            for (User u : candidates) {
                orgs.add(new LatLng(u.getLlat(), u.getLlon()));
            }

            LatLng[] args = new LatLng[orgs.size()];

            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                    .origins(orgs.toArray(args))
                    .destinations(new LatLng(r.getPulat(), r.getPulng()));

            DistanceMatrix dm = req.await();
            if (dm != null && dm.rows.length > 0) {

                for (int i = 0; i < dm.rows.length; i++) {
                    DistanceMatrixElement el = dm.rows[i].elements[0];
                    if (el != null && el.status.equals(DistanceMatrixElementStatus.OK)) {
                        User cc = candidates.get(i);
                        RiderequestDrivers rd = new RiderequestDrivers();
                        rd.setCreated(RESTDateUtil.getInstance().secondsNow());
                        rd.setDid(cc.getId());
                        rd.setDmeters(((Long) el.distance.inMeters).intValue());
                        rd.setRid(r.getId());
                        rd.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED);
                        rd.setDsec(((Long) el.duration.inSeconds).intValue());
                        rd.setAddr(dm.originAddresses[i]);
                        // setta la ride nell'user e altera lo stato
                        cc.setRtstatus(User.REALTIME_STATUS_DRIVER_ANSWERING);
                        cc.setCurrent(r.getId());
                        cc = userCtrl.save(cc);

                        rd.setValidfrom(Integer.parseInt(RESTDateUtil.getInstance().secondsMillisAgo(-i * interleaving)));
                        rd = drivCtrl.save(rd);
                        
                        if(i == 0) {
                            pusher.sendMessageNow(r.asNotificationText(Notifications.DRIVER_NEW_REQUEST), cc, r.getId());
                        } else {
                            pusher.scheduleMessage(r.asNotificationText(Notifications.DRIVER_NEW_REQUEST), cc, r.getId(), String.valueOf(rd.getValidfrom()));
                        }
                               
                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(PassengerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Riderequest priceRequest(Riderequest r, boolean first) throws RESTException {
        return priceCalculator.updatePrice(r, first);        
    }

    @Override
    public Riderequest cancel(RideRequestAction act) throws RESTException {
        User pass = null;

        Riderequest req = fetchRequest(act);

        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)) {
            pass = fetchUser(act);
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);
            return enrich(req, null, pass);
        } else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_SUBMITTED)) {
            pass = fetchUser(act);
            
            req.setStatus(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED);
            req.setCanceldate(RESTDateUtil.getInstance().secondsNow());
            req = reqCtrl.save(req);
            
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            
            
            // reset all driver status
            List<RiderequestDrivers> links = fetchLinks(act);
            for (RiderequestDrivers rd : links) {
                if(rd.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED)) {
                    rd.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_CANCELED);
                    rd.setCanceled(RESTDateUtil.getInstance().secondsNow());
                    drivCtrl.save(rd);

                    User dri = userCtrl.findById(rd.getDid());
                    dri.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                    dri.setCurrent(null);
                    userCtrl.save(dri);
                }                
            }

            

            // release the charge
            req = stripe.release(req);

            pusher.descheduleNotificationForRide(req.getId(), true, links);
            
            return enrich(req, null, pass);
        } else {
            throw new RESTException(ZegoK.Error.USER_CANNOT_CANCEL_A_RIDE);
        }

    }

    @Override
    public Riderequest abort(RideRequestAction act) throws RESTException {
        User pass = fetchUser(act);

        Riderequest req = fetchRequest(act);

        User driver = userCtrl.findById(req.getDid());

        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)) {
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            if (driver != null) {
                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                driver.setCurrent(null);
                driver = userCtrl.save(driver);
            }
            return enrich(req, driver, pass);
        } else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED)) {

            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            if (driver != null) {
                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                driver.setCurrent(null);
                driver = userCtrl.save(driver);
            }

            req.setStatus(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED);
            req.setAbortdate(RESTDateUtil.getInstance().secondsNow());
            req.setAbortreason(act.getText());
            req = reqCtrl.save(req);

            Integer now = Integer.parseInt(RESTDateUtil.getInstance().secondsNow());
            Integer freeafter = Integer.parseInt(req.getFreecanceldate());
            boolean allowfree = now > freeafter || (req.getMethod() != null && req.getMethod().equalsIgnoreCase(Riderequest.REQUEST_METHOD_CASH));                   
            
            if (!allowfree && Math.abs(RESTDateUtil.getInstance().secondsDiff(nzero(req.getAssigndate()), RESTDateUtil.getInstance().secondsNow())) > 120) {
                req = stripe.capture(req, true);
                req.setStripedriverfee(0);
                // nel caso ci sia uno sconto può darsi la stripezegofee sia nulla, ma se penalty = true 
                // la promo non si applica e quindi la fee va rimessa a posto
                req.setStripezegofee(req.getZegofee()); 
                req = reqCtrl.save(req);
            } else {
                req = stripe.release(req);
                req.setStripedriverfee(0);
                req.setStripezegofee(0);
                req = reqCtrl.save(req);
            }
            pusher.sendMessageNow(Notifications.PASSENGER_ABORTED_TEXT, driver, req.getId());
            return enrich(req, null, pass);
        } else {
            throw new RESTException(ZegoK.Error.USER_CANNOT_CANCEL_A_RIDE);
        }
    }

    private User fetchUser(RideRequestAction act) throws RESTException {
        User pass = userCtrl.findById(act.getUid());
        if (pass == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else if (!Objects.equals(pass.getCurrent(), act.getRid())) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return pass;
        }
    }

    private Riderequest fetchRequest(RideRequestAction act) throws RESTException {
        Riderequest req = reqCtrl.findById(act.getRid());
        if (req == null) {
            throw new RESTException(ZegoK.Error.REQUEST_NOT_FOUND);
        } else {
            return req;
        }
    }

    private RiderequestDrivers fetchLink(RideRequestAction act) throws RESTException {
        RiderequestDrivers link = drivCtrl.findFirst("findByRidAndDid", Arrays.asList(new DBTuple("rid", act.getRid()), new DBTuple("did", act.getUid())), false);
        if (link == null) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return link;
        }
    }

    private List<RiderequestDrivers> fetchLinks(RideRequestAction act) throws RESTException {
        List<RiderequestDrivers> link = drivCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", act.getRid())));
        if (link == null) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return link;
        }
    }
    
    private List<RiderequestDrivers> fetchLinks(Riderequest req) throws RESTException {
        List<RiderequestDrivers> link = drivCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", req.getId())));
        if (link == null) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return link;
        }
    }

    private Riderequest enrich(Riderequest r, User driver, User rider) throws RESTException {
        if (r == null) {
            throw new RESTException(ZegoK.Error.DEFAULT_ERROR);
        } else {
            if (driver != null) {
                Driverdata dd = dataCtrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", driver.getId())), false);
                r.setDriver(new CompactDriver(driver, dd));
            }
            r.setRider(new CompactUser(rider));
            return r;
        }
    }

    private long matrixDistance(double pulat, double pulng, double dolat, double dolng) throws RESTException {
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                    .origins(new LatLng(pulat, pulng))
                    .destinations(new LatLng(dolat, dolng));

            DistanceMatrix dm = req.await();
            if (dm != null && dm.rows.length > 0) {

                DistanceMatrixElement el = dm.rows[0].elements[0];
                if (el != null && el.status.equals(DistanceMatrixElementStatus.OK)) {
                    return el.distance.inMeters;
                } else {
                    throw new RESTException(ZegoK.Error.GOOGLE_API_FAILURE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PassengerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTException(ZegoK.Error.GOOGLE_API_FAILURE);
        }
        return 0l;
    }

    @Override
    public Riderequest terminate(RideRequestAction act) throws RESTException {
        User pass = fetchUser(act);

        Riderequest req = fetchRequest(act);

        User driver = userCtrl.findById(req.getDid());

        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)) {
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            if (driver != null) {
                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                driver.setCurrent(null);
                driver = userCtrl.save(driver);
            }
            return enrich(req, driver, pass);
        } else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_ON_RIDE)) {

            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            if (driver != null) {
                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                driver.setCurrent(null);
                driver = userCtrl.save(driver);
            }

            req.setStatus(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED);
            req.setAbortdate(RESTDateUtil.getInstance().secondsNow());
            req.setAbortreason(act.getText());
            
            if (driver != null) {
                req.setRealdolat(driver.getLlat());
                req.setRealdolng(driver.getLlon());
                req.setRealdoaddr(google.addressForCoord(driver.getLlat(), driver.getLlon()).getAddress());
            }
            req = reqCtrl.save(req);
            if(req.getMethod() != null && req.getMethod().equals(Riderequest.REQUEST_METHOD_CASH)) {
               // SKIP STRIPE HOO
                Pair<User,Riderequest> result = cashCtrl.updateCashDue(req, driver, pass);
                driver = result.getFirst();
                req = result.getSecond();
            } else {
                req = stripe.capture(req, false);                
            }
            req = reqCtrl.save(req);
            if(req.getStatus().equals(Riderequest.REQUEST_STATUS_ABORTED_UNPAID)) {
                pass = userCtrl.findById(pass.getId());
            }
            pusher.sendMessageNow("Il passeggero ha interrotto il passaggio", driver, req.getId());
            
            try {
                HashMap<String, String> maps = new HashMap<>();
                maps.putAll(MandrillMapper.param(pass, "rider"));
                if (driver != null) {
                    maps.putAll(MandrillMapper.param(driver, "driver"));
                }
                maps.putAll(MandrillMapper.param(req, "request"));
                maps.putAll(req.mandrill());
                mail.sendMail(pass, Conf.RIDEREQUEST_TERMINATED, maps);
            } catch (Exception ex) {
                
            }
            
            
            return enrich(req, null, pass);
        } else if (req.getStatus() >= 9) { // unlock deadlock user
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);
            return enrich(req, null, pass);
        } else {
            throw new RESTException(ZegoK.Error.USER_CANNOT_TERMINATE_A_RIDE);
        }
    }

    @Override
    public User changeMode(User u, String newmode) throws RESTException {
        if (newmode == null) {
            throw new RESTException(ZegoK.Error.MODE_NOT_SUPPORTED);
        } else if (newmode.equalsIgnoreCase(User.UMODE_DRIVER)) {

            if (!u.getCandrive().equals(1)) {
                throw new RESTException(ZegoK.Error.USER_CANNOT_DRIVE_YET);
            } else if (u.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_IDLE)) {
                u.setUmode(newmode);
                u.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                return userCtrl.save(u);
            } else {
                throw new RESTException(ZegoK.Error.MODE_CHANGE_NOT_ALLOWED_NOW);
            }
        } else if (newmode.equalsIgnoreCase(User.UMODE_RIDER)) {
            if (u.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_IDLE)) {
                u.setUmode(newmode);
                u.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
                return userCtrl.save(u);
            } else {
                throw new RESTException(ZegoK.Error.MODE_CHANGE_NOT_ALLOWED_NOW);
            }
        } else {
            throw new RESTException(ZegoK.Error.MODE_NOT_SUPPORTED);
        }
    }

    @Override
    public List<StripeCard> changeDefaultCard(StripeCard sd, Integer uid) throws RESTException {
        return stripe.chageDefaultCard(uid, sd);
    }

    @Override
    public List<StripeCard> deleteUserCard(StripeCard sd, Integer uid) throws RESTException {
        List<StripeCard> cards = stripe.deleteCard(uid, sd);
        if (cards == null || cards.isEmpty()) {
            User u = userCtrl.findById(uid);
            if (u != null) {
                u.setPayok(0);
                userCtrl.save(u);
            }
        }
        return cards;
    }

    @Override
    public Riderequest systemCancel(Riderequest req) throws RESTException {
        User pass = userCtrl.findById(req.getPid());

         if (req.getStatus().equals(Riderequest.REQUEST_STATUS_SUBMITTED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_IDLE)) {

            // reset all driver status
            List<RiderequestDrivers> links = fetchLinks(req);
            for (RiderequestDrivers rd : links) {
                

                if(rd.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED)) {
                    rd.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_CANCELED);
                    rd.setCanceled(RESTDateUtil.getInstance().secondsNow());
                    drivCtrl.save(rd);
                
                    User dri = userCtrl.findById(rd.getDid());
                    dri.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                    dri.setCurrent(null);
                    userCtrl.save(dri);
                }
                
            }

            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass.setCurrent(null);
            pass = userCtrl.save(pass);

            req.setStatus(Riderequest.REQUEST_CANCELLED_BY_SERVER);
            req.setCanceldate(RESTDateUtil.getInstance().secondsNow());
            req = reqCtrl.save(req);

            // release the charge
            if(req.getCharge()!=null && !req.getCharge().startsWith("ZERO")) {
                req = stripe.release(req);
            }

            return enrich(req, null, pass);
        } else {
            throw new RESTException(ZegoK.Error.USER_CANNOT_CANCEL_A_RIDE);
        }
    }

    @Override
    public Riderequest priceUpdateRequest(Riderequest req) throws RESTException {
        Riderequest rr = priceCalculator.updatePrice(req, true); 
        return rr;
    }

    @Override
    public User paydebth(User u) throws RESTException {
        boolean dp = stripe.attemptPayUserDebt(u,false);
        if(!dp) {
            throw new RESTException(ZegoK.Error.STRIPE_CARD_ERROR);
        } else {
             u.setDebt(0);
             userCtrl.save(u);
        }
        return userCtrl.findById(u.getId());
    }

    @Override
    public User probono(User u) throws RESTException {
        u.setDebt(0);
        return userCtrl.save(u);
    }
    
    private String nzero(String z) {
        return z == null ? "0" : z;
    }
}
    
