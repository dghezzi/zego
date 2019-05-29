/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Driveravailability;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Errordata;
import it.elbuild.zego.entities.Exportlink;
import it.elbuild.zego.entities.Feedback;
import it.elbuild.zego.entities.Nextipcall;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.Thirdparty;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userlogin;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MarketingExportController;
import it.elbuild.zego.iface.OperativeExportController;
import it.elbuild.zego.util.PromoReportEntry;
import it.elbuild.zego.util.RESTDateUtil;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class OperationsExportControllerBean implements OperativeExportController {

    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;
    EntityController<Integer, Thirdparty> thirdCtrl;
    EntityController<Integer, Userlogin> loginCtrl;
    EntityController<Integer, Errordata> errorCtrl;
    EntityController<Integer, Driveravailability> avaCtrl;
    

    private SimpleDateFormat DMY;
    private SimpleDateFormat MY;
    private SimpleDateFormat TS;
    private SimpleDateFormat HMS;
    private SimpleDateFormat HH;
    private NumberFormat NF;
    private int offsetMillis;

    @PostConstruct
    private void init() {
        thirdCtrl = provider.provide(Thirdparty.class);
        loginCtrl = provider.provide(Userlogin.class);
        userCtrl = provider.provide(User.class);
        errorCtrl = provider.provide(Errordata.class);
        avaCtrl = provider.provide(Driveravailability.class);
       

        DMY = new SimpleDateFormat("dd/MM/yyyy");
        MY = new SimpleDateFormat("MM/yyyy");
        TS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        HMS = new SimpleDateFormat("HH:mm:ss");
        HH = new SimpleDateFormat("HH");
        NF = new DecimalFormat("0.00");
        offsetMillis = TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis());

    }

    @Override
    public StringBuilder login(StringBuilder sb, String start, String stop) {
        List<Userlogin> ul = loginCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Userlogin.header());
        for(Userlogin e : ul) {
            sb.append(e.data());
        }
        return sb;
    }

    @Override
    public StringBuilder error(StringBuilder sb, String start, String stop) {
        List<Errordata> ul = errorCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Errordata.header());
        for(Errordata e : ul) {
            sb.append(e.data());
        }
        return sb;
    }

    @Override
    public StringBuilder third(StringBuilder sb, String start, String stop) {
        List<Thirdparty> ul = thirdCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        sb.append(Thirdparty.header());
        for(Thirdparty t : ul) {
            sb.append(t.data());
        }
        return sb;
    }

    @Override
    public StringBuilder driver(StringBuilder sb, String start, String stop, Integer city) {
        List<Driveravailability> ul;
        if(city.equals(0)) {
            ul = avaCtrl.findListCustom("findBetween", Arrays.asList(new DBTuple("start", start),new DBTuple("stop", stop)));
        } else {
            ul = avaCtrl.findListCustom("findByZidBetween", Arrays.asList(new DBTuple("zid", city),new DBTuple("start", start),new DBTuple("stop", stop)));
        }
        sb.append(Driveravailability.header());
        for(Driveravailability t : ul) {
            sb.append(t.data());
        }
        return sb;
    }

    private String n(String s) {
        return s == null ? "" : s;
    }
    
    private Integer s(Integer s) {
        return s == null ? 0 : s;
    }

    private String p(Integer p) {
        return p == null ? "0.00" : NF.format(p / 100d);
    }
    
    private String k(Integer p) {
        return (p == null ? "0.00" : NF.format(p / 1000d)).replace(".", ",");
    }

    private String mmss(Integer h) {
        Integer m = h / 60;
        Integer s = h % 60;
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    private String ls(Integer l) {
        if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_REJECTED)) {
            return "REJECT";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER)) {
            return "ACCEPTED_BY_OTHERS";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CANCELED)) {
            return "CANCELED";
        } else if (l.equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED)) {
            return "ACCEPTED";
        } else {
            return "";
        }
    }
    
    private String b(Integer burnt){
        if(burnt == null || burnt.equals(0)) {
            return "NOT USED";
        } else if(burnt.equals(1)) {
            return "USED";
        } else if(burnt.equals(2)) {
          return "UNUSABLE";
        } else {
            return "";
        }
    }
    
    private String tz(String t) {
        return t == null ? "" : String.valueOf(Long.parseLong(t)+(offsetMillis/1000));
    }

    
}   
