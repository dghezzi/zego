/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.maps.GeoApiContext;
import el.persistence.db.DBTuple;
import elbuild.utils.RandomPassword;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Notifications;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.CashController;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.DriverController;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.GooglePlaceInterface;
import it.elbuild.zego.iface.PriceCalculator;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.persistence.DAOFactory;
import it.elbuild.zego.persistence.NativeSQLDAO;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.RideRequestAction;
import it.elbuild.zego.rest.response.GeoCodeResponse;
import it.elbuild.zego.rest.response.ride.CompactDriver;
import it.elbuild.zego.rest.response.ride.CompactUser;
import it.elbuild.zego.util.Pair;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
public class DriverControllerBean implements DriverController {

    @EJB
    APNSPusher pusher;

    @EJB
    StripeInterface stripe;

    @EJB
    DAOFactory factory;

    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Driverdata> dataCtrl;

    EntityController<Integer, Riderequest> reqCtrl;

    EntityController<Integer, RiderequestDrivers> drivCtrl;

    EntityController<Integer, Userpromo> upromoCtrl;

    EntityController<Integer, Promo> promoCtrl;

    @EJB
    ConfController conf;

    @EJB
    GooglePlaceInterface google;
    
    @EJB
    CashController cashCtrl;

    private GeoApiContext geoCtx;
    private NativeSQLDAO sql;

    @EJB
    PriceCalculator priceCalculator;

    @PostConstruct
    private void init() {
        upromoCtrl = provider.provide(Userpromo.class);
        promoCtrl = provider.provide(Promo.class);
        userCtrl = provider.provide(User.class);
        reqCtrl = provider.provide(Riderequest.class);
        drivCtrl = provider.provide(RiderequestDrivers.class);
        dataCtrl = provider.provide(Driverdata.class);
        geoCtx = new GeoApiContext().setEnterpriseCredentials("", "");
        sql = factory.getNativeSQLDAO();
    }

    @Override
    public Riderequest reject(RideRequestAction act) throws RESTException {

        // get the driver
        User driver = fetchUser(act);

        // get driver ride link
        RiderequestDrivers link = fetchLink(act);

        // get the request
        Riderequest req = fetchRequest(act);

        // change the link status
        link.setRejected(RESTDateUtil.getInstance().secondsNow());
        link.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_REJECTED);
        drivCtrl.save(link);

        // change the user status
        driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
        driver.setCurrent(null);
        driver = userCtrl.save(driver);

        return enrich(req, driver, userCtrl.findById(req.getPid()));

    }

    @Override
    public Riderequest accept(RideRequestAction act) throws RESTException {

        User driver = null;
        User rider = null;

        // get driver ride link
        RiderequestDrivers link = fetchLink(act);

        // get the request
        Riderequest req = fetchRequest(act);

        // if the request is accepted by someone else 
        if (link.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER)) {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_ACCEPT_ACCEPTED_BY_OTHERS);
        } // if the request is canceled
        else if (link.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CANCELED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_IDLE)) {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_ACCEPT_CANCELED);
        } // if the request can be accepted, or it's already accepted by me
        else if (link.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED)) {
            return enrich(req, fetchUser(act), userCtrl.findById(req.getPid()));
        } // if can be accepted
        else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_SUBMITTED)) {

            // update the request
            req.setStatus(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED);
            req.setAssigndate(RESTDateUtil.getInstance().secondsNow());
            req.setDid(act.getUid());
            req.setFreecanceldate(String.valueOf(Integer.parseInt(RESTDateUtil.getInstance().secondsNow()) + req.getDrivereta() + 90));
            req = reqCtrl.save(req);

            // update the rider user
            rider = userCtrl.findById(req.getPid());
            rider.setRtstatus(User.REALTIME_STATUS_PASSENGER_WAITING_DRIVER);
            rider = userCtrl.save(rider);

            // get the driver
            driver = fetchUser(act);
            List<RiderequestDrivers> links = fetchLinks(act);
            for (RiderequestDrivers rd : links) {
                // if it's me
                if (rd.getDid().equals(act.getUid())) {
                    rd.setAccepted(RESTDateUtil.getInstance().secondsNow());
                    rd.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED);
                    drivCtrl.save(rd);

                    // update the driver user
                    driver.setRtstatus(User.REALTIME_STATUS_DRIVER_PICKINGUP);
                    driver = userCtrl.save(driver);

                    req.setDrivereta(((Double) (rd.getDsec() * 0.85d)).intValue()); // init with the driver original ETA
                    req.setFreecanceldate(String.valueOf(Integer.parseInt(RESTDateUtil.getInstance().secondsNow()) + req.getDrivereta() + 90));
                    req = reqCtrl.save(req);

                    pusher.descheduleNotificationForRide(req.getId(), false, links);

                } else {

                    if (rd.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED)) {
                        rd.setStatus(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER);
                        drivCtrl.save(rd);

                        // update the driver user
                        User innerd = userCtrl.findById(rd.getDid());
                        innerd.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                        innerd.setCurrent(null);
                        userCtrl.save(innerd);
                    }
                }
            }
        } else {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_ACCEPT_ACCEPTED_BY_OTHERS);
        }
        req = enrich(req, driver, rider);
        pusher.sendMessageNow(req.asNotificationText(Notifications.USER_DRIVER_ACCEPT), rider, req.getId());
        return req;
    }

    @Override
    public Riderequest abort(RideRequestAction act) throws RESTException {
        // get the driver
        User driver = fetchUser(act);

        // get the request
        Riderequest req = fetchRequest(act);

        // get the passenger
        User pass = userCtrl.findById(req.getPid());

        // already aborted -> send back the same req
        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)
                || (req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED) && driver.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_PICKINGUP))
                || (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED) && driver.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_PICKINGUP))) {

            pass.setCurrent(null);
            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            pass = userCtrl.save(pass);

            driver.setCurrent(null);
            driver.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
            driver = userCtrl.save(driver);

            req.setAbortdate(RESTDateUtil.getInstance().secondsNow());
            req.setAbortreason(act.getText());
            req.setStatus(Riderequest.REQUEST_STATUS_DRIVER_ABORTED);
            req = reqCtrl.save(req);

            if (act.getCapture() == null || !act.getCapture().equals(1)) {
                req = stripe.release(req);
                req.setStripedriverfee(0);
                req.setStripezegofee(0);
                req = reqCtrl.save(req);
            } else {
                req = stripe.capture(req, true);
                req.setStripedriverfee(0);
                if (req.getDiscount() != null && req.getDiscount() > 0) {
                    req.setStripezegofee(req.getZegofee());
                }
                req = reqCtrl.save(req);
            }

            pusher.sendMessageNow(Notifications.DRIVER_ABORTED_TEXT, pass, req.getId());
            return enrich(req, driver, pass);
        } else {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_ABORT_A_RIDE);
        }

    }

    @Override
    public Riderequest start(RideRequestAction act) throws RESTException {
        // get the driver
        User driver = fetchUser(act);

        // get the request
        Riderequest req = fetchRequest(act);

        // get the passenger
        User pass = userCtrl.findById(req.getPid());

        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_ON_RIDE)
                || (req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED) && driver.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_PICKINGUP))) {

            pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_ONRIDE);
            pass = userCtrl.save(pass);

            driver.setRtstatus(User.REALTIME_STATUS_DRIVER_ONRIDE);
            driver = userCtrl.save(driver);

            req.setStartdate(RESTDateUtil.getInstance().secondsNow());
            req.setRealpulat(driver.getLlat());
            req.setRealpulng(driver.getLlon());
            req.setStatus(Riderequest.REQUEST_STATUS_ON_RIDE);
            req = reqCtrl.save(req);
            req.setRealpuaddr(google.addressForCoord(driver.getLlat(), driver.getLlon()).getAddress());
            req = reqCtrl.save(req);

            return enrich(req, driver, pass);
        } else {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_START_A_RIDE);
        }
    }

    @Override
    public Riderequest end(RideRequestAction act) throws RESTException {
        // get the driver
        User driver = fetchUser(act);

        // get the request
        Riderequest req = fetchRequest(act);

        // get the passenger
        User pass = null;

        if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_ENDED)) {
            driver.setRtstatus(req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) ? User.REALTIME_STATUS_DRIVER_IDLE : User.REALTIME_STATUS_DRIVER_FEEDBACKDUE);
            if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)) {
                driver.setCurrent(null);
            }
            pass = userCtrl.findById(req.getPid());
            driver = userCtrl.save(driver);
            return enrich(req, driver, pass);
        } else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_ON_RIDE)
                || (req.getStatus().equals(Riderequest.REQUEST_STATUS_ON_RIDE) && driver.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_ONRIDE))) {

            
            
            if (req.getDriverend() == null) {
            
                // check if it is a cash ride and handle accordingly
            if(req.getMethod()!=null && req.getMethod().equalsIgnoreCase(Riderequest.REQUEST_METHOD_CASH)) {
                req.setDriverend(RESTDateUtil.getInstance().secondsNow());
                req = reqCtrl.save(req);

                pass = userCtrl.findById(req.getPid());
                pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_FEEDBACK_DUE);
                pass.setCashused(1);
                pass = userCtrl.save(pass);

                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_FEEDBACKDUE);
                driver = userCtrl.save(driver);

                req.setEnddate(RESTDateUtil.getInstance().secondsNow());
                req.setRealdolat(driver.getLlat());
                req.setRealdolng(driver.getLlon());
                GeoCodeResponse grea = google.addressForCoord(driver.getLlat(), driver.getLlon());
                req.setRealdoaddr(grea == null ? "" : grea.getAddress());
                req.setStatus(Riderequest.REQUEST_STATUS_ENDED);
                
                if (act.getPriceupdate() != null && act.getPriceupdate() > 0) {
                    req.setDriverprice(act.getPriceupdate());
                }
                req = priceCalculator.updatePrice(req, false);

                // create the cash payment data structure
                Pair<User,Riderequest> result = cashCtrl.updateCashDue(req, driver, pass);
                driver = result.getFirst();
                req = result.getSecond();

                if (req.getChargestatus().equalsIgnoreCase(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED)) {
                    req.setStatus(Riderequest.REQUEST_STATUS_PAID);
                } else {
                // leave the status we set in the capture method
                    // reload the pass, as we increase the debt
                    pass = userCtrl.findById(pass.getId());
                }

                req = reqCtrl.save(req);
                return enrich(req, driver, pass);
            } 
            // if it is a credit card ride
            else {
                req.setDriverend(RESTDateUtil.getInstance().secondsNow());
                req = reqCtrl.save(req);

                pass = userCtrl.findById(req.getPid());
                pass.setRtstatus(User.REALTIME_STATUS_PASSENGER_FEEDBACK_DUE);
                pass = userCtrl.save(pass);

                driver.setRtstatus(User.REALTIME_STATUS_DRIVER_FEEDBACKDUE);
                driver = userCtrl.save(driver);

                req.setEnddate(RESTDateUtil.getInstance().secondsNow());
                req.setRealdolat(driver.getLlat());
                req.setRealdolng(driver.getLlon());
                GeoCodeResponse grea = google.addressForCoord(driver.getLlat(), driver.getLlon());
                req.setRealdoaddr(grea == null ? "" : grea.getAddress());
                req.setStatus(Riderequest.REQUEST_STATUS_ENDED);
                if (act.getPriceupdate() != null && act.getPriceupdate() > 0) {
                    req.setDriverprice(act.getPriceupdate());
                }
                req = priceCalculator.updatePrice(req, false);

                // capture the payment
                req = stripe.capture(req, false);
                
                // decrement the driver cashdue
                driver = cashCtrl.insertUserPayment(req.getDriverfee(), req.getShortid(), driver);
                
                if (req.getChargestatus().equalsIgnoreCase(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED)) {
                    req.setStatus(Riderequest.REQUEST_STATUS_PAID);
                } else {
                    // leave the status we set in the capture method
                    // reload the pass, as we increase the debt
                    pass = userCtrl.findById(pass.getId());
                }

                req = reqCtrl.save(req);
                try {
                    if (pass.getRefuid() != null) {
                        if (pass.getRefawarded() != null && pass.getRefawarded().equals(0)) {
                            User redeemer = userCtrl.findById(pass.getRefuid());

                            if (redeemer != null) {
                                Promo p = new Promo();
                                p.setType(Promo.EURO);
                                p.setCode("AWA-" + RandomPassword.generate(5).toUpperCase());
                                p.setCurrentusages(0);
                                p.setEnablestart(RESTDateUtil.getInstance().secondsNow());
                                Integer days = conf.getConfByKey(Conf.PROMO_AWA_EXPIRY_DAYS).asInteger(93);
                                p.setEnablestop(RESTDateUtil.getInstance().secondsMillisAgo(-1 * days * 24 * 3600));
                                p.setFeeonly(0);
                                p.setFirstonly(0);
                                p.setInsdate(RESTDateUtil.getInstance().secondsNow());
                                p.setMaxperuser(1);
                                p.setMaxusages(1);
                                p.setModdate(RESTDateUtil.getInstance().secondsNow());
                                p.setNote("");
                                p.setPromodesc(conf.getConfByKey(Conf.PROMO_AWA_MSG_BODY).getVal().replace("{fname}", pass.getFname()));
                                p.setPromotitle(conf.getConfByKey(Conf.PROMO_AWA_MSG_TITLE).getVal());
                                p.setValidfrom(RESTDateUtil.getInstance().secondsNow());
                                p.setValidto(RESTDateUtil.getInstance().secondsMillisAgo(-1 * days * 24 * 3600));
                                p.setValue(conf.getConfByKey(Conf.PROMO_AWA_VALUE_CENT).asInteger(500));
                                p = promoCtrl.save(p);
                                pusher.sendMessageNow(conf.getConfByKey(Conf.PROMO_AWA_MSG_BODY).getVal().replace("{fname}", pass.getFname()), redeemer, 0);

                                if (p != null) {
                                    Userpromo up = new Userpromo();
                                    up.setBurnt(0);
                                    up.setExpirydate(p.getValidto());
                                    up.setPid(p.getId());
                                    up.setRedeemdate(RESTDateUtil.getInstance().secondsNow());
                                    up.setUid(redeemer.getId());
                                    up.setValueleft(p.getType().equalsIgnoreCase(Promo.WALLET) ? p.getValue() : 0);
                                    upromoCtrl.save(up);
                                }
                            }

                        }
                    }
                    pass.setRefawarded(1);
                    pass = userCtrl.save(pass);

                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                }
                return enrich(req, driver, pass);
            }
                
            } else {
                // THIS IS AN ATTEMPT TO AVOID DUPLICATE PAYMENTS
                driver.setRtstatus(req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) ? User.REALTIME_STATUS_DRIVER_IDLE : User.REALTIME_STATUS_DRIVER_FEEDBACKDUE);
                if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)) {
                    driver.setCurrent(null);
                }
                pass = userCtrl.findById(req.getPid());
                driver = userCtrl.save(driver);
                return enrich(req, driver, pass);
            }
        } else {
            throw new RESTException(ZegoK.Error.DRIVER_CANNOT_END_A_RIDE);
        }
    }

    private User fetchUser(RideRequestAction act) throws RESTException {
        User driver = userCtrl.findById(act.getUid());
        if (driver == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else if (!Objects.equals(driver.getCurrent(), act.getRid())) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return driver;
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

    private Riderequest enrich(Riderequest r, User driver, User rider) throws RESTException {
        if (r == null) {
            throw new RESTException(ZegoK.Error.DEFAULT_ERROR);
        } else {
            Driverdata dd = dataCtrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", driver.getId())), false);
            r.setDriver(new CompactDriver(driver, dd));
            r.setRider(new CompactUser(rider));
            return r;
        }
    }

    @Override
    public Riderequest notify(RideRequestAction act) throws RESTException {
        // get the request
        Riderequest req = fetchRequest(act);

        // get the rider
        User rider = userCtrl.findById(req.getPid());

        if (rider == null || req.getStatus() > Riderequest.REQUEST_STATUS_DRIVER_ANSWERED) {

        } else {
            pusher.sendMessageNow(req.asNotificationText(Notifications.USER_DRIVER_IAMHERE), rider, req.getId());
        }

        return req;
    }
}
