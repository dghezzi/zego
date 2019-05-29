/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Cash;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.response.BaseResponse;
import it.elbuild.zego.rest.response.CountResponse;
import it.elbuild.zego.util.CashPaymentRequest;
import it.elbuild.zego.util.GenericSearch;
import it.elbuild.zego.util.ImportBankFile;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
@Path("driverdata")
public class DriverdataFacadeREST extends BaseFacadeREST<Integer, Driverdata> {
        
    EntityController<Integer,User> userCtrl;
     EntityController<Integer,Cash> cashCtrl;
    
    @EJB
    MailSender mail;
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Driverdata.class);
        userCtrl = provider.provide(User.class);
        cashCtrl = provider.provide(Cash.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
    
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Driverdata createRecord(Driverdata entity) throws RESTException {
        authenticate(entity.getUid());
        if(entity.getStatus() == null || entity.getArea() == null) {
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
        }
        entity.setInsdate(RESTDateUtil.getInstance().secondsNow());
        entity.setModdate(RESTDateUtil.getInstance().secondsNow());
        
        
        return ctrl.save(entity);
    }

    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Driverdata updateRecord(Driverdata entity) throws RESTException {
        authenticate(entity.getUid());
        if(entity.getStatus() == null || entity.getArea() == null) {
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
        }
        entity.setModdate(RESTDateUtil.getInstance().secondsNow());
        User u = userCtrl.findById(entity.getUid());
        Driverdata prev = ctrl.findById(entity.getId());
        entity = ctrl.save(entity);
        if(prev!=null && entity.getStatus() == 2 && prev.getStatus()!=2) {
           HashMap<String, String> maps = new HashMap<>();            
           maps.putAll(MandrillMapper.param(u,"user"));
           maps.putAll(MandrillMapper.param(entity,"data"));
           mail.sendMail(u, Conf.DRIVER_APPROVED, maps);
        } 
        return entity;
    }
   
    @GET
    @Path("user/{uid}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Driverdata getByUId(@PathParam("uid") Integer uid) throws RESTException {
        authenticate(uid);        
        return ctrl.findFirst("findByUid", Arrays.asList(new DBTuple("uid", uid)), true);
    }
    
    @GET
    @Path("withstatus/{status}/range/{start}/{stop}/sorted/{sort}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<Driverdata> statusFilter(@PathParam("status") String status, @PathParam("start") String start, @PathParam("stop") String stop, @PathParam("sort") String sort) throws RESTException {
        authenticate();        
        if(sort == null || !(sort.equals("datedesc") || sort.equals("dateasc"))) {
            sort = "dateasc";
        }
        List<Driverdata> dd = ctrl.findListCustom("findByStatusBetween"+sort, Arrays.asList(new DBTuple("status", Integer.valueOf(status))), Integer.valueOf(start), Integer.valueOf(stop));
        for(Driverdata d : dd) {
            d.setUser(userCtrl.findById(d.getUid()));
        }
        return dd;
    }
    
    @GET
    @Path("countwithstatus/{status}/range/{start}/{stop}/sorted/{sort}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public CountResponse statusCountFilter(@PathParam("status") String status, @PathParam("start") String start, @PathParam("stop") String stop, @PathParam("sort") String sort) throws RESTException {
        authenticate();        
        if(sort == null || !(sort.equals("datedesc") || sort.equals("dateasc"))) {
            sort = "dateasc";
        }
        CountResponse cr = new CountResponse();
        cr.setCt(ctrl.findListCustom("findByStatusBetween"+sort, Arrays.asList(new DBTuple("status", Integer.valueOf(status))), 0, 10000000).size() +  0l);
        cr.setEntity("DriverData");
        return cr;
    }
    
    @POST
    @Path("advancedsearch/{start}/{stop}")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    @Override
    public List<Driverdata> advancedSearch(@PathParam("start") Integer start, @PathParam("stop") Integer stop, GenericSearch gs) throws RESTException {
        authenticateAdmin();
        gs.setEntity(ctrl.type());
        List<Object> ll = factory.getNativeSQLDAO().searchByGenericSearch(gs, start, stop, ctrl.type());
        List<Driverdata> dd = new ArrayList<>();
        for(Object o : ll) {
           Driverdata d = (Driverdata)o;           
           d.setUser(userCtrl.findById(d.getUid()));
           dd.add(d);
        }
        return dd;
    }
    
    @POST
    @Path("processbankfile")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public BaseResponse  processRawBankFile(ImportBankFile raw) throws RESTException {
        String lines[] = raw.getContent().split("(\r\n|\r|\n)", -1);
        System.out.println(lines);
        return new BaseResponse("File importato correttamente.");
    }
    
    
    @POST
    @Path("processpayment")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public BaseResponse  processpayments(List<CashPaymentRequest> arr) throws RESTException {
        authenticateAdmin();
        if(arr == null) {
            throw new RESTException(ZegoK.Error.WRONG_CASHREQ_FORMAT, "Il file non ha un formato valido.");
        }
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
        for(CashPaymentRequest cpr : arr) {
            if(cpr.isValid()) {
                User dr = userCtrl.findById(cpr.getIdAsInt());
                if(dr == null) {
                    throw new RESTException(ZegoK.Error.WRONG_CASHREQ_FORMAT, "Riga "+(arr.indexOf(cpr)+1)+". Driver ID non corrispondente ad alcun driver.");                    
                } else {
                    Date d = null;
                    try {
                        d = SDF.parse(cpr.getDate());
                    } catch (Exception e) {
                        throw new RESTException(ZegoK.Error.WRONG_CASHREQ_FORMAT, "Riga "+(arr.indexOf(cpr)+1)+". Data in formato non valido.");
                    }
                    
                    List<Cash> drcash = cashCtrl.findListCustom("findToBePaid", Arrays.asList(new DBTuple("did", dr.getId())));
                    Double amount = cpr.getAmountAsDouble()*100;
                    Integer inta = amount.intValue();
                    for(Cash cc : drcash) {
                                                
                        if(inta > 0) {
                            if(inta >= cc.getDue()) {
                                inta = inta - cc.getDue();
                                cc.setDue(0);
                                cc.setReftype(Cash.REFTYPE_BANKTRANSFER);
                                cc.setReference(cpr.getCro());
                                cc.setZegopaiddate(RESTDateUtil.getInstance().formatSecondsDate(d, false));                                
                            } else {
                                cc.setDue(cc.getDue() - inta);
                                cc.setReference(cpr.getCro());
                                cc.setReftype(Cash.REFTYPE_BANKTRANSFER);
                                cc.setZegopaiddate(RESTDateUtil.getInstance().formatSecondsDate(d, false));
                                inta = 0;
                            } 
                            
                            cashCtrl.save(cc);
                        }
                    }
                    
                    drcash = cashCtrl.findListCustom("findToBePaid", Arrays.asList(new DBTuple("did", dr.getId())));
                    Integer debt = 0;
                    for(Cash cc : drcash) {
                        debt = debt + cc.getDue();
                    }
                    
                    dr.setCashdue(debt);
                    userCtrl.save(dr);
                                        
                }
                        
            } else {
                throw new RESTException(ZegoK.Error.WRONG_CASHREQ_FORMAT, "Dati errati (mancanti id, amount, o date) alla linea: "+(arr.indexOf(cpr)+1));
            }
        }
        return new BaseResponse("File importato correttamente.");
    }
    
    
}
