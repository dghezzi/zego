/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Location;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Zone;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("location")
public class LocationFacadeREST extends BaseFacadeREST<Integer, Location> {

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Riderequest> reqCtrl;

    EntityController<Integer, Zone> zoneCtrl;

    private List<Zone> zones;

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Location.class);
        userCtrl = provider.provide(User.class);
        reqCtrl = provider.provide(Riderequest.class);
        zoneCtrl = provider.provide(Zone.class);
        zones = zoneCtrl.findAll();
        changeMode(USER_ENDPOINT);
    }

    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Location createRecord(Location entity) throws RESTException {

        if (entity.getUid() == null || entity.getUid().equals(0)) {
            entity.setUid(caller().getId());
        }

        authenticate(entity.getUid());
        User u = userCtrl.findById(entity.getUid());
        if (u == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            if (entity.getLat() != 0) {
                u.setLlat(entity.getLat());
                u.setLlon(entity.getLng());

                Zone selectedZone = null;
                for (Zone z : zones) {
                    if (z.include(entity.getLat(), entity.getLng())) {
                        selectedZone = z;
                        break;
                    }

                }
                if (selectedZone != null) {
                    u.setArea(selectedZone.getName());
                }

            }
            u.setLlocdate(RESTDateUtil.getInstance().secondsNow());
            userCtrl.save(u);

            if (entity.getLat() != 0) {
                entity.setLdate(RESTDateUtil.getInstance().secondsNow());
                entity.setMode(u.getUmode().substring(0, 1));
                entity = ctrl.save(entity);
            }
        }
        return entity;
    }

    @GET
    @Path("ride/{rid}")
    @Produces({"application/json; charset=UTF-8"})
    public List<Location> getRideLocations(@PathParam("rid") Integer rid) throws RESTException {

        Riderequest r = reqCtrl.findById(rid);
        if (r == null) {
            killWithCode(ZegoK.Error.REQUEST_NOT_FOUND);
            return null;
        } else {

            String end = r.getEnddate() != null ? r.getEnddate() : (r.getAbortdate() != null ? r.getAbortdate() : r.getCanceldate() != null ? r.getCanceldate() : RESTDateUtil.getInstance().secondsNow());
            List<Location> locs = ctrl.findListCustom("findByUidBetween", Arrays.asList(new DBTuple("uid", r.getDid()), new DBTuple("start", r.getStartdate()), new DBTuple("stop", end)));
            return locs;
        }

    }

}
