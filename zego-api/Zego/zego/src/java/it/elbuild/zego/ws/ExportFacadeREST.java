/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import elbuild.file.FileTextUtil;
import it.elbuild.zego.entities.Exportcash;
import it.elbuild.zego.entities.Exportdriver;
import it.elbuild.zego.entities.Exportquadratura;
import it.elbuild.zego.entities.Exportrequest;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MarketingExportController;
import it.elbuild.zego.iface.OperativeExportController;
import it.elbuild.zego.rest.exception.RESTException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lu
 */
@Stateless
@Path("export")
public class ExportFacadeREST  {

    @EJB
    DAOProvider provider;
    
    EntityController<Integer,Exportrequest> expCtrl;
    
    EntityController<Integer,Exportcash> expCashCtrl;
    
    EntityController<Integer,Exportquadratura> expQuadra;
    
    EntityController<Integer,Exportdriver> driCtrl;
    
    private SimpleDateFormat SDF;
    
    @EJB
    MarketingExportController mktCtrl;
    
    @EJB
    OperativeExportController opsCtrl;
    
    @PostConstruct
    private void init() {
        expCtrl = provider.provide(Exportrequest.class);
        expCashCtrl = provider.provide(Exportcash.class);
        driCtrl = provider.provide(Exportdriver.class);
        expQuadra = provider.provide(Exportquadratura.class);
        SDF = new SimpleDateFormat("yyyyMMdd");
    }
    
    
    
    @GET
    @Path("payment/{start}/{stop}")    
    @Produces("text/csv")
    public Response payment(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportrequest> req = expCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportrequest.header());
        for(Exportrequest e : req) {
            if((e.getStatus().equals(8) || e.getStatus().equals(7)) && e.getStripedriverfee().equals(0)){
                e.setDriverfee(0);
            }
            
            if(e.canBeAdded()){
                sb.append(e.data());
            }
        }
        return fileize(sb, SDF.format(new Date())+"_report_payment.csv");
    }
    
    @GET
    @Path("saldodriver/{start}/{stop}")    
    @Produces("text/csv")
    public Response F(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportcash> req = expCashCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", new Date(Integer.parseInt(start)*1000l)),new DBTuple("stop", new Date(Integer.parseInt(stop)*1000l))));
        sb.append(Exportcash.header());
        for(Exportcash e : req) {
            sb.append(e.data());            
        }
        return fileize(sb, SDF.format(new Date())+"_report_saldo_driver.csv");
    }
    
    @GET
    @Path("unpaid/{start}/{stop}")    
    @Produces("text/csv")
    public Response unpaid(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportrequest> req = expCtrl.findListCustom("findUnpaidBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportrequest.header());
        for(Exportrequest e : req) {
            if((e.getStatus().equals(8) || e.getStatus().equals(7)) && e.getStripedriverfee().equals(0)){
                e.setDriverfee(0);
            }
            if(e.canBeAdded()) {
                sb.append(e.data());
            }
        }
        return fileize(sb, SDF.format(new Date())+"_report_unpaid.csv");
    }
    
    @GET
    @Path("fee/{start}/{stop}")    
    @Produces("text/csv")
    public Response fee(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportrequest> req = expCtrl.findListCustom("findFeeBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportrequest.feeheader());
        for(Exportrequest e : req) {
            if((e.getStatus().equals(8) || e.getStatus().equals(7)) && e.getStripedriverfee().equals(0)){
                e.setDriverfee(0);
            }
            if(e.canBeAdded()){
                sb.append(e.feedata());
            }
        }
        return fileize(sb, SDF.format(new Date())+"_report_fee.csv");
    }
    
    @GET
    @Path("ridepromo/{start}/{stop}")    
    @Produces("text/csv")
    public Response promo(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportrequest> req = expCtrl.findListCustom("findPromoBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportrequest.promoheader());
        for(Exportrequest e : req) {
            if((e.getStatus().equals(8) || e.getStatus().equals(7)) && e.getStripedriverfee().equals(0)){
                e.setDriverfee(0);
            }
            
            if(e.canBeAdded()) {
                sb.append(e.promodata());
            }
        }
        return fileize(sb, SDF.format(new Date())+"_report_promo.csv");
    }
    
    @GET
    @Path("quadratura/{start}/{stop}")    
    @Produces("text/csv")
    public Response qudratura(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportquadratura> req = expQuadra.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportquadratura.header());
        for(Exportquadratura e : req) {
            if((e.getStatus().equals(8) || e.getStatus().equals(7)) && e.getStripedriverfee().equals(0)){
                e.setDriverfee(0);
            }
            
            sb.append(e.data());
        }
        return fileize(sb, SDF.format(new Date())+"_report_quadratura.csv");
    }
    
    
    @GET
    @Path("driver/{start}/{stop}")
    @Produces("text/csv")
    public Response driver(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        List<Exportdriver> req = driCtrl.findAll();//driCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Exportdriver.header());
        for(Exportdriver e : req) {
            
            sb.append(e.data());
        }
        return fileize(sb, SDF.format(new Date())+"_report_driver.csv");
    }
    
    @GET
    @Path("mkt/user/{start}/{stop}")
    @Produces("text/csv")
    public Response mktuser(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = mktCtrl.user(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_mktuser.csv");
    }
    
    @GET
    @Path("mkt/ride/{start}/{stop}")
    @Produces("text/csv")
    public Response mktride(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
         sb = mktCtrl.ride(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_mktride.csv");
    }
    
    @GET
    @Path("mkt/link/{start}/{stop}")
    @Produces("text/csv")
    public Response mktlink(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
         sb = mktCtrl.link(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_mktingaggi.csv");
    }
    
    @GET
    @Path("mkt/promo/{start}/{stop}")
    @Produces("text/csv")
    public Response mktpromo(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
         sb = mktCtrl.promo(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_mktingaggi.csv");
    }
    
    @GET
    @Path("mkt/promocomplex/{start}/{stop}")
    @Produces("text/csv")
    public Response promocomplex(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
         sb = mktCtrl.fullpromo(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_actpromo.csv");
    }
    
    
    @GET
    @Path("mkt/signup/{start}/{stop}")
    @Produces("text/csv")
    public Response promosignup(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = mktCtrl.signup(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_signup.csv");
    }
    
    @GET
    @Path("mkt/ridecomplex/{city}/{start}/{stop}")
    @Produces("text/csv")
    public Response ridecomplex(@PathParam("city") Integer city, @PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = mktCtrl.fullride(sb,start,stop,city);
        return fileize(sb, SDF.format(new Date())+"_report_ridefull.csv");
    }
    
    @GET
    @Path("ops/error/{start}/{stop}")
    @Produces("text/csv")
    public Response opserror(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = opsCtrl.error(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_error.csv");
    }
    
    @GET
    @Path("ops/third/{start}/{stop}")
    @Produces("text/csv")
    public Response third(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = opsCtrl.third(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_thirdparty.csv");
    }
    
    @GET
    @Path("ops/login/{start}/{stop}")
    @Produces("text/csv")
    public Response login(@PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = opsCtrl.login(sb,start,stop);
        return fileize(sb, SDF.format(new Date())+"_report_login.csv");
    }
    
    @GET
    @Path("ops/driver/{city}/{start}/{stop}")
    @Produces("text/csv")
    public Response drivercity(@PathParam("city") Integer city, @PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {        
        StringBuilder sb = new StringBuilder();
        sb = opsCtrl.driver(sb,start,stop,city);
        return fileize(sb, SDF.format(new Date())+"_report_driveravail.csv");
    }
    
    public Response fileize(StringBuilder sb, String fname) {
        File file = new File("/tmp/"+fname);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(PaymentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                FileTextUtil.setContents(file, sb.toString());
            } catch (IOException ex) {
                Logger.getLogger(PaymentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition",
                    "attachment; filename="+fname);
            return response.build();
    }
}
