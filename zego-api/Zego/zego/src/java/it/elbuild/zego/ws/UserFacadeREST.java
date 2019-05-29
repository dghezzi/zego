/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Fraud;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.PassengerController;
import it.elbuild.zego.iface.SignupFunnelController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.BootRequest;
import it.elbuild.zego.rest.request.PinRequest;
import it.elbuild.zego.rest.request.PinResendRequest;
import it.elbuild.zego.rest.request.ReferralRequest;
import it.elbuild.zego.rest.request.StripeCreateCustomerRequest;
import it.elbuild.zego.rest.request.UnlockRequest;
import it.elbuild.zego.rest.response.StripeCard;
import it.elbuild.zego.rest.response.ride.CompactDriver;
import it.elbuild.zego.util.CompatDriver;
import it.elbuild.zego.util.GenericSearch;
import it.elbuild.zego.util.LoginRequest;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author Lu
 */
@Stateless
@Path("user")
public class UserFacadeREST extends BaseFacadeREST<Integer, User> {
    
    @EJB
    SignupFunnelController signupCtrl;
    
    @EJB
    PassengerController passengerCtrl;
    
    EntityController<Integer,RiderequestDrivers> drivCtrl;
    
    EntityController<Integer,Fraud> fraudCtrl;
    
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(User.class);
        drivCtrl = provider.provide(RiderequestDrivers.class);
        fraudCtrl = provider.provide(Fraud.class);
    }
    
    @POST
    @Path("signup")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User signup(BootRequest br) throws RESTException {
        try {
            return signupCtrl.signup(br);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("update")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User update(User u) throws RESTException {
        try {
            authenticate(u.getId());
            return signupCtrl.updateUserData(u, User.UPDATE_PHASE_REGULAR);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("complete")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User complete(User u) throws RESTException {
        try {
            authenticate(u.getId());
            return signupCtrl.updateUserData(u, User.UPDATE_PHASE_SIGNUP);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("kill")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User kill(User u) throws RESTException {
        try {
            authenticate(u.getId());
            auth.erase(u.getZegotoken());
            u = ctrl.findById(u.getId());
            if(u.getUmode().equalsIgnoreCase(User.UMODE_DRIVER)) {
                u.setLlocdate(String.valueOf(Long.valueOf(u.getLlocdate())-15*60));
                u = ctrl.save(u);
            }                       
            return u;
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("fraudreset")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User fraudreset(User u) throws RESTException {
        try {
            authenticate(u.getId());
            List<Fraud> ff = fraudCtrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid",u.getId())));
            for(Fraud f : ff) {
                fraudCtrl.delete(f.getId());
            }
            return u;
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("ban")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User ban(User u) throws RESTException {
        try {
            authenticate();
            return signupCtrl.ban(u);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("unban")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User unban(User u) throws RESTException {
        try {
            authenticate();
            return signupCtrl.unban(u);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    
    @POST
    @Path("login")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User login(BootRequest br) throws RESTException {
        try {
            return signupCtrl.login(br);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("adminlogin")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User adminlogin(LoginRequest br) throws RESTException {
        try {
            return signupCtrl.adminlogin(br);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("silent")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User silent(BootRequest br) throws RESTException {
        try {
            authenticate(br.getUid());
            return signupCtrl.silentLogin(br);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    
    @POST
    @Path("validate")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User validate(PinRequest pr) throws RESTException {
        try {
            authenticate(pr.getUid());
            return signupCtrl.validatePin(pr);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("unlock")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User unlock(UnlockRequest pr) throws RESTException {
        try {
            authenticate(pr.getUid());
            User u = ctrl.findById(pr.getUid());
            if(u.getUmode().equals(User.UMODE_DRIVER)) {
                u.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                u.setCurrent(null);
            } else {
                u.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
                u.setCurrent(null);
            }
            return ctrl.save(u);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("referral")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User referral(ReferralRequest rr) throws RESTException {
        try {
            authenticate(rr.getUid());
            return signupCtrl.checkReferral(rr);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("stripe")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User saveStripe(StripeCreateCustomerRequest sr) throws RESTException {
        try {
            authenticate(sr.getUid());
            return signupCtrl.createStripeCustomer(sr);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("resend")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User resendPin(PinResendRequest prr) throws RESTException {
        try {
            authenticate(prr.getUid());
            return signupCtrl.resendPin(prr);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("webcreate")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User webcreate(User u) throws RESTException {
        try {
            authenticate();
            u.setBitmask(0);
            u.setUmode("rider");
            u.setCandrive(0);
            u.setChannel("backoffice");
            u.setDavg(0d);
            u.setInsdate(RESTDateUtil.getInstance().secondsNow());
            u.setLastseen(RESTDateUtil.getInstance().secondsNow());
            u.setMobok(0);
            u.setModdate(u.getInsdate());
            u.setPavg(0d);
            u.setPayok(0);
            u.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            u.generateRefCore();
            u.setStatus(User.STATUS_ACTIVE);
            u.setTcok(1);
            u.setZegotoken("us_" + UUID.randomUUID().toString());
            
            u = ctrl.save(u);
            auth.addOrUpdate(u);
            return u; 
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public User createRecord(User entity) throws RESTException {
        if(entity.getId()!=null) {
            authenticate(entity.getId());
        } else {
            authenticate();
        }
        
        try {
            return signupCtrl.updateUserData(entity, User.UPDATE_PHASE_REGULAR);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    /*
    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public User updateRecord(User entity) throws RESTException {
        User old = ctrl.findById(entity.getId());
        User put = super.updateRecord(entity);
        put.setZgid(old.getZgid());
        put.setChannel("legacy");
        put.setRefawarded(1);
        return ctrl.save(put);                
    }
    */
    
    @GET
    @Path("cards/{id}")
    @Produces({"application/json; charset=UTF-8"})
    public List<StripeCard> cards(@PathParam("id") Integer id) throws RESTException {
        authenticate(id);
        return passengerCtrl.userCards(id);
    }
    
    @POST
    @Path("{uid}/card/makedefault")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public List<StripeCard> makeCardDefault(@PathParam("uid") Integer uid, StripeCard sd) throws RESTException {
        authenticate(uid);
        return passengerCtrl.changeDefaultCard(sd, uid);
    }
    
    @POST
    @Path("{uid}/card/delete")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public List<StripeCard> deleteCard(@PathParam("uid") Integer uid, StripeCard sd) throws RESTException {
        authenticate(uid);
        return passengerCtrl.deleteUserCard(sd, uid);
    }
    
    @POST
    @Path("changemodeto/{mode}")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User changeMode(@PathParam("mode") String mode, User u) throws RESTException {
        authenticate(u.getId());
        u = ctrl.findById(u.getId());
        return passengerCtrl.changeMode(u, mode);
    }
    
    @GET
    @Path("{id}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public User getById(@PathParam("id") Integer id) throws RESTException {
        authenticate();
        
        User u = ctrl.findById(id);
        User caller = caller();
        
        if(caller!=null && u!=null && u.getRtstatus() != null && Objects.equals(u.getId(), caller.getId()) && u.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_IDLE)) {
            u.setLlocdate(RESTDateUtil.getInstance().secondsNow());
            u = ctrl.save(u);
        }
        
        if(caller!=null && u!=null && u.getRtstatus() != null && Objects.equals(u.getId(), caller.getId()) && u.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_ANSWERING)) {
            RiderequestDrivers rd = drivCtrl.findFirst("findByRidAndDid", Arrays.asList(new DBTuple("rid", u.getCurrent()),new DBTuple("did", u.getId())), true);
            if(rd.getValidfrom() > Integer.parseInt(RESTDateUtil.getInstance().secondsNow())) {
                u.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
            }
        }
        
        

        return u;
    }
    
    @GET
    @Path("drivingnow")
    @Produces({"application/json; charset=UTF-8"})
    public List<CompatDriver> driving(@PathParam("id") Integer id) throws RESTException {
        authenticate();
        String lloc = RESTDateUtil.getInstance().secondsMillisAgo(60*15);
        List<User> dr = ctrl.findListCustom("findDriving", Arrays.asList(new DBTuple("locdate", lloc)));
        List<CompatDriver> driv = new ArrayList<>();
        for (User d : dr) {
            driv.add(new CompatDriver(d));
        }
        return driv;
    }
    
    @POST
    @Path("paydebt")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User paydebth(User u) throws RESTException {
        try {
            authenticate(u.getId());
            u = ctrl.findById(u.getId());
            
            return passengerCtrl.paydebth(u);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @POST
    @Path("probono")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public User probono(User u) throws RESTException {
        try {
            authenticate(u.getId());
            u = ctrl.findById(u.getId());
            
            return passengerCtrl.probono(u);
        } catch (RESTException ex) {
            killWithCode(ex.getCode());
            return null;
        }
    }
    
    @GET
    @Path("adminexport")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public Response adminExport() throws RESTException {
        List<User> ll =  ctrl.findListCustom("findByUtype", Arrays.asList(new DBTuple("utype", "admin")));
        StringBuilder sb = new StringBuilder();
        Integer i = 0;
        Collection h = null;
        for (User e : ll) {
            
            if(i == 0){
                h = MandrillMapper.param(e).keySet();
                int j = 0;
                for(Object o : h){
                    sb.append(o.toString()).append( j == h.size()-1 ? "" : ";");                            
                }
                sb.append("\n");
            }
                Map<String, String> d = MandrillMapper.param(e);
                int j = 0;
                for(Object o : h){
                    sb.append(d.get(o.toString())).append( j == d.size()-1 ? "" : ";");                            
                }
                sb.append("\n");
                i++;
        }
        return fileize(sb, "zego_export_"+RESTDateUtil.getInstance().secondsNow()+".csv");
    }
    
    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public User updateRecord(User entity) throws RESTException {
        if(entity.getCandrive().equals(0) && entity.getRtstatus() >= User.REALTIME_STATUS_DRIVER_IDLE) {
            entity.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            entity.setUmode(User.UMODE_RIDER);
        }
        return super.updateRecord(entity);
    }
}
