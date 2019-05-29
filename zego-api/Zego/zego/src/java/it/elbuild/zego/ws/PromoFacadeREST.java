/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.BatchRedeemRequest;
import it.elbuild.zego.rest.request.RedeemRequest;
import it.elbuild.zego.rest.response.BaseResponse;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("promo")
public class PromoFacadeREST extends BaseFacadeREST<Integer, Promo> {

    EntityController<Integer, Userpromo> upromoCtrl;

    EntityController<Integer, Riderequest> requestCtrl;

    EntityController<Integer, User> uCtrl;

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Promo.class);
        upromoCtrl = provider.provide(Userpromo.class);
        requestCtrl = provider.provide(Riderequest.class);
        uCtrl = provider.provide(User.class);
        changeMode(ADMIN_ENDPOINT);
    }

    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Promo createRecord(Promo entity) throws RESTException {
        authenticateAdmin();
        if (entity == null) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
        } else if (entity.missingMandatory()) {
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
        } else if (entity.wrongEnableDates() || entity.wrongValidDates()) {
            killWithCode(ZegoK.Error.PROMO_WRONG_DATES);
        } else if (entity.wrongValue()) {
            killWithCode(ZegoK.Error.PROMO_WRONG_VALUE);
        }

        Promo p = ctrl.findFirst("findByCode", Arrays.asList(new DBTuple("code", entity.getCode().toLowerCase())), false);

        if (p != null) {
            killWithCode(ZegoK.Error.PROMO_CODE_ALREADY_EXIST);
        }

        return ctrl.save(entity);
    }

    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Promo updateRecord(Promo entity) throws RESTException {
        authenticateAdmin();
        if (entity == null) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
        } else if (entity.missingMandatory()) {
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
        } else if (entity.wrongEnableDates() || entity.wrongValidDates()) {
            killWithCode(ZegoK.Error.PROMO_WRONG_DATES);
        } else if (entity.wrongValue()) {
            killWithCode(ZegoK.Error.PROMO_WRONG_VALUE);
        }
        return ctrl.save(entity);
    }

    @POST
    @Path("redeem")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public List<Userpromo> redeem(RedeemRequest entity) throws RESTException {
        authenticate(entity.getUid());
        User u = uCtrl.findById(entity.getUid());
        Promo p = ctrl.findFirst("findByCode", Arrays.asList(new DBTuple("code", entity.getCode().toLowerCase())), true);

        if (u == null) {
            killWithCode(ZegoK.Error.USER_NOT_FOUND);
        } else if (p == null) {
            killWithCode(ZegoK.Error.PROMO_DOESNT_EXIST);
        } else if (p.getMaxusages() <= p.getCurrentusages()) {
            killWithCode(ZegoK.Error.PROMO_IS_OVERUSED);
        } else if (!p.canBeRedeemed()) {
            killWithCode(ZegoK.Error.PROMO_CANNOT_BE_REEDEMED_NOW);
        } else if (p.isExpired()) {
            killWithCode(ZegoK.Error.PROMO_IS_EXPIRED);
        } else if (p.getFirstonly() == 1) {
            List<Riderequest> rr = requestCtrl.findListCustom("findHistoryByPid", Arrays.asList(new DBTuple("pid", entity.getUid())), 0, 10);
            if (rr != null && !rr.isEmpty()) {
                killWithCode(ZegoK.Error.PROMO_IS_FIRST_USE_ONLY);
            }
        }

        List<Userpromo> prev = upromoCtrl.findListCustom("findByUidAndPid", Arrays.asList(new DBTuple("pid", p.getId()), new DBTuple("uid", entity.getUid())));
        if (!prev.isEmpty() && prev.size() >= p.getMaxperuser()) {
            killWithCode(ZegoK.Error.PROMO_ALREADY_USED_BY_THIS_USER);
        }

        Userpromo red = new Userpromo();
        red.setExpirydate(p.getValidto());
        red.setPid(p.getId());
        red.setRedeemdate(RESTDateUtil.getInstance().secondsNow());
        red.setUid(entity.getUid());
        red.setValueleft(p.getType().equalsIgnoreCase(Promo.WALLET) || p.getType().equalsIgnoreCase(Promo.FREERIDE) ? p.getValue() : 0);
        red.setBurnt(0);
        upromoCtrl.save(red);

        if (p.getType().equals(Promo.FREERIDE) || p.getType().equals(Promo.WALLET)) {
            p.setCurrentusages(p.getCurrentusages() + 1);
            ctrl.save(p);
        }

        return upromoCtrl.findListCustom("findNotUsedByUid", Arrays.asList(new DBTuple("uid", entity.getUid()), new DBTuple("now", RESTDateUtil.getInstance().secondsNow())));

    }

    @POST
    @Path("batchredeem")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public BaseResponse batchredeem(BatchRedeemRequest entity) throws RESTException {
        authenticate();
        BaseResponse br = new BaseResponse();
        Promo p = ctrl.findFirst("findByCode", Arrays.asList(new DBTuple("code", entity.getCode().toLowerCase())), true);
        if (p == null) {
            killWithCode(ZegoK.Error.PROMO_DOESNT_EXIST);
        } else if (p.getMaxusages() <= p.getCurrentusages()) {
            killWithCode(ZegoK.Error.PROMO_IS_OVERUSED);
        } else if (!p.canBeRedeemed()) {
            killWithCode(ZegoK.Error.PROMO_CANNOT_BE_REEDEMED_NOW);
        } else if (p.isExpired()) {
            killWithCode(ZegoK.Error.PROMO_IS_EXPIRED);
        }
        User u = null;
        StringBuilder sb = new StringBuilder();
        sb.append("USER_ID;ERRORE<br/>");
        int i = 0;
        for (Integer uid : entity.getUsers()) {
            if (u == null) {
                sb.append(uid).append(";").append(ZegoK.Error.USER_NOT_FOUND).append("<br/>");
            } else if (p.getFirstonly() == 1) {
                List<Riderequest> rr = requestCtrl.findListCustom("findHistoryByPid", Arrays.asList(new DBTuple("pid", uid)), 0, 10);
                if (rr != null && !rr.isEmpty()) {
                    sb.append(uid).append(";").append(ZegoK.Error.PROMO_IS_FIRST_USE_ONLY).append("<br/>");                    
                }
            }

            List<Userpromo> prev = upromoCtrl.findListCustom("findByUidAndPid", Arrays.asList(new DBTuple("pid", p.getId()), new DBTuple("uid", uid)));
            if (!prev.isEmpty() && prev.size() >= p.getMaxperuser()) {
                sb.append(uid).append(";").append(ZegoK.Error.PROMO_ALREADY_USED_BY_THIS_USER).append("<br/>");
            }

            Userpromo red = new Userpromo();
            red.setExpirydate(p.getValidto());
            red.setPid(p.getId());
            red.setRedeemdate(RESTDateUtil.getInstance().secondsNow());
            red.setUid(uid);
            red.setValueleft(p.getType().equalsIgnoreCase(Promo.WALLET) || p.getType().equalsIgnoreCase(Promo.FREERIDE) ? p.getValue() : 0);
            red.setBurnt(0);
            upromoCtrl.save(red);

            if (p.getType().equals(Promo.FREERIDE) || p.getType().equals(Promo.WALLET)) {
                p.setCurrentusages(p.getCurrentusages() + 1);
                ctrl.save(p);
            }
            i++;
        }
        br.setMsg(i+" promo inserite.<br/><br/>"+sb.toString());
        return br;
    }

    @GET
    @Path("pfx/{pfx}")
    @Produces({"application/json; charset=UTF-8"})
    public List<Promo> pfx(@PathParam("pfx") String pfx) throws RESTException {
        authenticateAdmin();
        return ctrl.findListCustom("findByPfx", Arrays.asList(new DBTuple("pfx", "%" + pfx + "%")));
    }

}
