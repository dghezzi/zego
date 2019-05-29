/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Feedback;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.Service;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Zone;
import it.elbuild.zego.entities.ZoneService;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.GooglePlaceInterface;
import it.elbuild.zego.iface.ObfuscationController;
import it.elbuild.zego.iface.PriceCalculator;
import it.elbuild.zego.iface.RideController;
import it.elbuild.zego.persistence.DAOFactory;
import it.elbuild.zego.persistence.NativeSQLDAO;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.UpdateRatingRequest;
import it.elbuild.zego.rest.request.ride.EtaRequest;
import it.elbuild.zego.rest.request.ride.PriceRequest;
import it.elbuild.zego.rest.response.HeathMapResponse;
import it.elbuild.zego.rest.response.PriceEstimateResponse;
import it.elbuild.zego.rest.response.ride.CompactDriver;
import it.elbuild.zego.rest.response.ride.CompactUser;
import it.elbuild.zego.rest.response.ride.CompatRequest;
import it.elbuild.zego.rest.response.ride.EtaResponse;
import it.elbuild.zego.rest.response.ride.PriceResponse;
import it.elbuild.zego.util.Coordinate;
import it.elbuild.zego.util.ZegoK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lu
 */
@Stateless
public class RideControllerBean implements RideController {

    @EJB
    GooglePlaceInterface google;

    @EJB
    DAOFactory factory;

    @EJB
    ConfController conf;

    @EJB
    DAOProvider provider;

    EntityController<Integer, Driverdata> dataCtrl;

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Riderequest> requestCtrl;

    EntityController<Integer, Feedback> feedbackCtrl;

    EntityController<Integer, RiderequestDrivers> drivCtrl;

    EntityController<Integer, ZoneService> zoneServiceCtrl;

    EntityController<Integer, Zone> zoneCtrl;

    @EJB
    PriceCalculator priceCalculator;

    @EJB
    ObfuscationController obfuscate;

    private NativeSQLDAO sql;
    private GeoApiContext geoCtx;

    private List<Zone> zones;

    @PostConstruct
    private void init() {
        dataCtrl = provider.provide(Driverdata.class);
        requestCtrl = provider.provide(Riderequest.class);
        userCtrl = provider.provide(User.class);
        zoneServiceCtrl = provider.provide(ZoneService.class);
        zoneCtrl = provider.provide(Zone.class);
        feedbackCtrl = provider.provide(Feedback.class);
        sql = factory.getNativeSQLDAO();
        geoCtx = new GeoApiContext().setEnterpriseCredentials("", "");
        drivCtrl = provider.provide(RiderequestDrivers.class);
        zones = zoneCtrl.findAll();
    }

    @Override
    public EtaResponse eta(EtaRequest er, User uid) throws RESTException {
        EtaResponse eresp = new EtaResponse();

        if (zones == null || zones.isEmpty()) {
            zones = zoneCtrl.findAll();
        }

        try {
            // search zone
            Zone selectedZone = null;
            for (Zone z : zones) {
                if (z.include(er.getLat(), er.getLng())) {
                    selectedZone = z;
                    break;
                }
            }

            if (selectedZone == null) {
                throw new RESTException(ZegoK.Error.NO_ZEGO_HERE);
            }

            // fetch constants
            Integer radiusMeters = conf.getConfByKey(Conf.RADIUS_MAX_DISTANCE_METERS).asInteger(5000);
            Integer maxSeconds = conf.getConfByKey(Conf.RADIUS_MAX_LOC_AGE_SECONDS).asInteger(5000);
            String strategy = conf.getConfByKey(Conf.RADIUS_STRATEGY).getVal();

            
            // get list of drivers
            List<User> drvs = sql.getActiveUsersWithinRadius(er, radiusMeters, maxSeconds);

            // call google to get distance matrix
            List<LatLng> org = new ArrayList<>();
            for (User d : drvs) {
                org.add(new LatLng(d.getLlat(), d.getLlon()));
                Driverdata dd = dataCtrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", d.getId())), false);
                eresp.getDrivers().add(new CompactDriver(d, dd));
            }
            User u = uid == null ? null : userCtrl.findById(uid.getId());
            if (u != null && u.getUmode().equalsIgnoreCase(User.UMODE_RIDER)) {
                if (org.isEmpty()) {
                    // TODO ASK FABRIZIO WHAT WE SHOULD DO HERE
                    // throw new RESTException(ZegoK.Error.NO_DRIVERS);
                    LatLng[] args = new LatLng[1];
                    LatLng def = new LatLng(selectedZone.getSelat(),selectedZone.getSelng()); // un punto a caso, ELbuild
                    args[0] = def;

                    eresp.setEta(100 * 60);
                    eresp.setDist(50000);
                    DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                            .origins(args)
                            .destinations(new LatLng(er.getLat(), er.getLng()));

                    DistanceMatrix dm = req.await();

                    if (dm != null && dm.rows.length > 0) {

                        eresp.setAddress(nice(dm.destinationAddresses[0]));
                    }
                } else {
                    LatLng[] args = new LatLng[org.size()];
                    org.toArray(args);
                    DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                            .origins(args)
                            .destinations(new LatLng(er.getLat(), er.getLng()));

                    DistanceMatrix dm = req.await();

                    // determine eta using the supplied strategy
                    List<Long> etas = new ArrayList<>();
                    List<Long> dists = new ArrayList<>();
                    if (dm != null && dm.rows.length > 0) {

                        for (int i = 0; i < dm.rows.length; i++) {
                            DistanceMatrixElement el = dm.rows[i].elements[0];
                            if (el != null && el.status.equals(DistanceMatrixElementStatus.OK)) {
                                etas.add(el.duration.inSeconds);
                                dists.add(el.distance.inMeters);
                            }
                        }

                        eresp.setAddress(nice(dm.destinationAddresses[0]));
                    }

                    if (strategy.equalsIgnoreCase("optimistic")) {
                        Collections.sort(etas);
                        Collections.sort(dists);
                        eresp.setEta(etas.isEmpty() ? 31 * 60 : Math.max(60, ((Double) (etas.get(0) * 0.9d)).intValue()));
                        eresp.setDist(dists.isEmpty() ? 0 : dists.get(0).intValue());
                    } else if (strategy.equalsIgnoreCase("linear")) {
                        Long sum = 0l;
                        Long sumd = 0l;
                        for (Long l : etas) {
                            sum = sum + l;
                        }
                        for (Long l : dists) {
                            sumd = sumd + l;
                        }
                        eresp.setEta(sum.intValue() / etas.size());
                        eresp.setDist(sumd.intValue() / dists.size());
                    } else { // weighted

                    }
                }
                
                GeocodingResult[] grex = GeocodingApi.reverseGeocode(geoCtx, new LatLng(er.getLat(), er.getLng())).awaitIgnoreError();            
                if(grex.length > 0){
                    eresp.setAddress(nice(grex[0].formattedAddress));
                }
            }

            // link services
            List<ZoneService> zs = zoneServiceCtrl.findListCustom("findByZid", Arrays.asList(new DBTuple("zid", selectedZone.getId())));
            List<Service> servs = new ArrayList<>();
            for (ZoneService z : zs) {
                if(z.getService() != null && z.getService().isVisible()) {
                if(z.getService().getLevel().equals(Riderequest.REQUEST_LEVEL_PINK)) {
                    if(uid != null && uid.getGender() != null && uid.getGender().toLowerCase().startsWith("f")) {
                       servs.add(z.getService()); 
                    }
                } else {
                    servs.add(z.getService());
                }
                }
                
            }
            eresp.setServices(servs);

            for (CompactDriver cd : eresp.getDrivers()) {
                //System.out.println("OBFUSCATE DRIVER ID: " + cd.getDid() + " CALLER: " + uid);
                if (!Objects.equals(cd.getDid(), uid)) {
                    Coordinate obf = obfuscate.obfuscate(new Coordinate(cd.getLat(), cd.getLng()));
                    cd.setLat(obf.getLat());
                    cd.setLng(obf.getLng());
                }
            }
            
            
            
            return eresp;

        } catch (RESTException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(RideControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            return eresp;
        }
    }

    @Override
    public PriceResponse price(PriceRequest pr, Integer uid) throws RESTException {
        PriceResponse presp = new PriceResponse();
        try {
            PriceEstimateResponse per = priceCalculator.estimate(pr, uid);
            presp.updateWith(per);
            return presp;
        } catch (Exception ex) {
            if (ex instanceof RESTException) {
                throw ((RESTException) ex);
            } else {
                Logger.getLogger(RideControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                return presp;
            }

        }
    }

    private String nice(String address) {
        int comma = StringUtils.countMatches(address, ",");
        if (comma >= 2) {
            int idx = 0;

            for (int i = 0; i < 2; i++) {
                idx = address.indexOf(",", idx + 1);
            }
            return address.substring(0, idx);
        } else {
            return address;
        }
    }

    @Override
    public Riderequest get(Integer rid, User caller) throws RESTException {
        Riderequest req = requestCtrl.findById(rid);
        if (req == null) {
            throw new RESTException(ZegoK.Error.REQUEST_NOT_FOUND);
        }
        User pass = userCtrl.findById(req.getPid());
        User driver = null;
        if (req.getDid() != null) {
            driver = userCtrl.findById(req.getDid());
        }

        req = enrich(req, driver, pass);

        // if the request is in submitted state and the polling is performed by a driver
        // link that driver to the transient driver inside the request to ease the app
        // polling cycle
        if (caller.getUtype().equalsIgnoreCase("admin") || caller.getId().equals(req.getPid()) || (req.getDid() != null && caller.getId().equals(req.getDid()))) {
            if (caller.getId().equals(req.getPid()) && pass.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_WAITING_DRIVER) && driver != null) {
                LatLng[] args = new LatLng[1];
                LatLng def = new LatLng(driver.getLlat(), driver.getLlon()); 
                args[0] = def;

                DistanceMatrixApiRequest dmreq = DistanceMatrixApi.newRequest(geoCtx)
                        .origins(args)
                        .destinations(new LatLng(req.getPulat(), req.getPulng()));

                try {
                    DistanceMatrix dm = dmreq.await();
                    if (dmreq != null && dm.rows.length > 0) {

                        for (int i = 0; i < dm.rows.length; i++) {
                            DistanceMatrixElement el = dm.rows[i].elements[0];
                            if (el != null && el.status.equals(DistanceMatrixElementStatus.OK)) {
                                req.setDrivereta((int) ((Double) (el.duration.inSeconds * 0.85d)).intValue());
                            }
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(RideControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if ((req.getStatus().equals(Riderequest.REQUEST_STATUS_SUBMITTED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED))) {
                User u = userCtrl.findById(caller.getId());
                if (u.getUmode().equalsIgnoreCase(User.UMODE_DRIVER) && (u.getCurrent() == null || Objects.equals(u.getCurrent(), req.getId()))) {
                    req.setDriver(new CompactDriver(u, dataCtrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", u.getId())), false)));
                    RiderequestDrivers rd = drivCtrl.findFirst("findByRidAndDid", Arrays.asList(new DBTuple("rid", u.getCurrent()), new DBTuple("did", u.getId())), true);
                    if (rd != null) {
                        req.setDrivereta(((Double) (rd.getDsec() * 0.85d)).intValue());
                    }
                }
            } else if (req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED)) {
                User u = userCtrl.findById(caller.getId());
                // unlock
                
                if(u!= null && u.getCurrent() != null && u.getUmode().equalsIgnoreCase(User.UMODE_DRIVER) && req.getDid() != null && !Objects.equals(req.getDid(), u.getId()) && Objects.equals(req.getId(), u.getCurrent())) {
                    u.setCurrent(null);
                    u.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                    userCtrl.save(u);
                    Logger.getLogger(RideControllerBean.class.getName()).log(Level.SEVERE, "WARNING UNLOCKED USER {0}", u.getId());
                }
            }
            
            
        }

        return req;
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

    @Override
    public List<RiderequestDrivers> drivers(Integer id) throws RESTException {
        List<RiderequestDrivers> drivers = fetchLinks(id);
        for (RiderequestDrivers rr : drivers) {
            User u = userCtrl.findById(rr.getDid());
            if (u != null) {
                rr.setFname(u.getFname());
                rr.setLname(u.getLname());
            }
        }
        return drivers;
    }

    private List<RiderequestDrivers> fetchLinks(Integer rid) throws RESTException {
        List<RiderequestDrivers> link = drivCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", rid)));
        if (link == null) {
            throw new RESTException(ZegoK.Error.USER_CANNOT_ACT_ON_THIS_RIDE);
        } else {
            return link;
        }
    }

    @Override
    public List<CompatRequest> riderHistory(Integer uid, Integer start, Integer stop) {
        List<Riderequest> rr = requestCtrl.findListCustom("findHistoryByPid", Arrays.asList(new DBTuple("pid", uid)), start, stop);
        List<CompatRequest> history = new ArrayList<>();
        for (Riderequest r : rr) {
            history.add(new CompatRequest(r));
        }
        return history;
    }

    @Override
    public List<CompatRequest> driverHistory(Integer uid, Integer start, Integer stop) {
        List<Riderequest> rr = requestCtrl.findListCustom("findHistoryByDid", Arrays.asList(new DBTuple("did", uid)), start, stop);
        List<CompatRequest> history = new ArrayList<>();
        for (Riderequest r : rr) {
            history.add(new CompatRequest(r));
        }
        return history;
    }

    @Override
    public Riderequest getByShid(String shid, User caller) throws RESTException {
        Riderequest req = requestCtrl.findFirst("findByShid", Arrays.asList(new DBTuple("shid", shid)), true);
        if (req == null) {
            throw new RESTException(ZegoK.Error.REQUEST_NOT_FOUND);
        }
        User pass = userCtrl.findById(req.getPid());
        User driver = null;
        if (req.getDid() != null) {
            driver = userCtrl.findById(req.getDid());
        }

        req = enrich(req, driver, pass);

        return req;
    }

    @Override
    public List<Riderequest> webRiderHistory(Integer uid, String start, String stop) {
        List<Riderequest> rr = requestCtrl.findListCustom("findWebHistoryByPid", Arrays.asList(new DBTuple("pid", uid), new DBTuple("start", start), new DBTuple("stop", stop)));
        return rr;
    }

    @Override
    public List<Riderequest> webDriverHistory(Integer uid, String start, String stop) {
        List<Riderequest> rr = requestCtrl.findListCustom("findWebHistoryByDid", Arrays.asList(new DBTuple("did", uid), new DBTuple("start", start), new DBTuple("stop", stop)));
        return rr;
    }

    @Override
    public List<HeathMapResponse> heathMap(String start, String stop, Integer status) {
        return factory.getNativeSQLDAO().getHeathMap(start, stop, status);
    }

    @Override
    public Riderequest updateRating(UpdateRatingRequest req, Integer rid) throws RESTException {
        Riderequest request = requestCtrl.findById(rid);
        if (request != null) {
            if (req.getType().equals("rider")) {
                request.setPassrating(req.getNewrating());
                
                Feedback old = feedbackCtrl.findFirst("findByRidAndUid", Arrays.asList(new DBTuple("rid", request.getId()),new DBTuple("uid", request.getPid())), false);
                if(old!=null){
                    old.setRating(req.getNewrating());
                    feedbackCtrl.save(old);
                }
                
                List<Feedback> uf = feedbackCtrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid", request.getPid())));
                Integer r = 0;
                for (Feedback f : uf) {
                    r += f.getRating();
                }
                if (uf.size() > 0) {
                    User u = userCtrl.findById(request.getPid());
                    u.setPavg(r / (uf.size() + 0d));
                    userCtrl.save(u);
                }
            } else {
                request.setDrivrating(req.getNewrating());
                if (request.getDid() != null) {
                    
                    Feedback old = feedbackCtrl.findFirst("findByRidAndUid", Arrays.asList(new DBTuple("rid", request.getId()),new DBTuple("uid", request.getDid())), false);
                    if(old != null) {
                        old.setRating(req.getNewrating());
                        feedbackCtrl.save(old);
                    }
                
                    List<Feedback> uf = feedbackCtrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid", request.getDid())));
                    Integer r = 0;
                    for (Feedback f : uf) {
                        r += f.getRating();
                    }
                    if (uf.size() > 0) {
                        User u = userCtrl.findById(request.getDid());
                        u.setDavg(r / (uf.size() + 0d));
                        userCtrl.save(u);
                    }
                }
            }
            request = requestCtrl.save(request);
            return request;
        } else {
            throw new RESTException(ZegoK.Error.REQUEST_NOT_FOUND);
        }
    }
}
