/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.legacy;

import el.persistence.db.DBTuple;
import elbuild.utils.RandomPassword;
import it.elbuild.zego.entities.Driverdata;
import it.elbuild.zego.entities.Feedback;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.legacy.Manufacturer;
import it.elbuild.zego.entities.legacy.Zgcar;
import it.elbuild.zego.entities.legacy.Zgcity;
import it.elbuild.zego.entities.legacy.Zgcountry;
import it.elbuild.zego.entities.legacy.Zgdriver;
import it.elbuild.zego.entities.legacy.Zgdrivrating;
import it.elbuild.zego.entities.legacy.Zgfeedback;
import it.elbuild.zego.entities.legacy.Zgpassrating;
import it.elbuild.zego.entities.legacy.Zgride;
import it.elbuild.zego.entities.legacy.Zgridepayment;
import it.elbuild.zego.entities.legacy.Zgriderequest;
import it.elbuild.zego.entities.legacy.ZgriderequestDrivers;
import it.elbuild.zego.entities.legacy.Zguser;
import it.elbuild.zego.entities.legacy.ZguserProfile;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.LegacyMapper;
import it.elbuild.zego.util.RESTDateUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class LegacyImporterBean implements LegacyMapper {

    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Zguser> zguserCtrl;
    
    EntityController<Integer, Zgdrivrating> zgdrRatCtrl;

    EntityController<Integer, Zgpassrating> zgpsRatCtrl;

    EntityController<Integer, Zgcountry> zgCountryCtrl;

    EntityController<Integer, ZguserProfile> zguserProfileCtrl;

    EntityController<Integer, Zgdriver> zgdriverCtrl;

    EntityController<Integer, Zgcity> zgcityCtrl;

    EntityController<Integer, Zgcar> zgcarCtrl;
    
    EntityController<Integer, Zgride> zgrideCtrl;
    
    EntityController<Integer, Zgriderequest> zgreqCtrl;
    
    EntityController<Integer, Zgridepayment> zgpayCtrl;
    
    EntityController<Integer, ZgriderequestDrivers> linkCtrl;
        
    EntityController<Integer, User> userCtrl;
    
    EntityController<Integer, Riderequest> reqCtrl;
    
    EntityController<Integer, RiderequestDrivers> reqDrivCtrl;
    
    EntityController<Integer, Manufacturer> manCtrl;

    EntityController<Integer, Driverdata> drDataCtrl;
    
    EntityController<Integer, Zgfeedback> feedbackCtrl;
    
    EntityController<Integer, Feedback> ffCtrl;

    private final SimpleDateFormat BDF = new SimpleDateFormat("dd/MM/yyyy");

    private HashMap<Integer,User> usercache;
    private HashMap<String,String> nation;
    private HashMap<Integer,Zgride> ridecache;
    private HashMap<Integer,Zgridepayment> paycache;
    @PostConstruct
    private void init() {
        zgdriverCtrl = provider.provide(Zgdriver.class);
        zguserProfileCtrl = provider.provide(ZguserProfile.class);
        zguserCtrl = provider.provide(Zguser.class);
        zgCountryCtrl = provider.provide(Zgcountry.class);
        userCtrl = provider.provide(User.class);
        zgdrRatCtrl = provider.provide(Zgdrivrating.class);
        zgpsRatCtrl = provider.provide(Zgpassrating.class);
        drDataCtrl = provider.provide(Driverdata.class);
        manCtrl = provider.provide(Manufacturer.class);
        zgcityCtrl = provider.provide(Zgcity.class);
        zgcarCtrl = provider.provide(Zgcar.class);
        zgrideCtrl = provider.provide(Zgride.class);
        zgreqCtrl = provider.provide(Zgriderequest.class);
        linkCtrl = provider.provide(ZgriderequestDrivers.class);
        zgpayCtrl = provider.provide(Zgridepayment.class);
        reqCtrl = provider.provide(Riderequest.class);
        reqDrivCtrl = provider.provide(RiderequestDrivers.class);
        feedbackCtrl = provider.provide(Zgfeedback.class);
        ffCtrl = provider.provide(Feedback.class);
    }

    @Override
    public void createUsers() {

        List<Zguser> zgusers = zguserCtrl.findAll();

        for (Zguser zu : zgusers) {
            ZguserProfile zgp = zguserProfileCtrl.findFirst("findByUserId", Arrays.asList(new DBTuple("userId", zu.getUserId())), false);
            Zgdriver zgd = zgdriverCtrl.findFirst("findByUserId", Arrays.asList(new DBTuple("userId", zu.getUserId())), false);

            User imp = usermap(zu, zgp, zgd);
            if (imp != null) {
                imp = userCtrl.save(imp);
                System.out.println("Imported USER with ID: " + imp.getId());
                if (zgd != null) {
                    
                    driver(imp.getId(), zu, zgp, zgd, zgcarCtrl.findFirst("findByUserId", Arrays.asList(new DBTuple("userId", zu.getUserId())), false));
                }
            }
        }
    }

    @Override
    public void createRides() {
        
        usercache = new HashMap<>();
        paycache = new HashMap<>();
        ridecache = new HashMap<>();
        
        List<User> uu = userCtrl.findAll();
        for(User u : uu) {
            if(u.getZgid() != null) {
                usercache.put(u.getZgid(), u);
            }
        }
        
        List<Zgride> rr = zgrideCtrl.findAll();
        for(Zgride r : rr) {
            if(r.getRiderequestId() != null) {
                ridecache.put(r.getRiderequestId(), r);
            }
        }
        
         List<Zgridepayment> rp = zgpayCtrl.findAll();
        for(Zgridepayment p : rp) {
            if(p.getRiderequestId() != null) {
                paycache.put(p.getRiderequestId(), p);
            }
        }
        List<Zgriderequest> req = zgreqCtrl.findAll();
        for(Zgriderequest zr : req) {
            
            
            
            Zgride zd = ridecache.get(zr.getRiderequestId());//zgrideCtrl.findById(zr.getRiderequestId());
            Zgridepayment zp = paycache.get(zr.getRiderequestId());//zgpayCtrl.findById(zr.getRiderequestId());
            ride(zr, zd, zp);
            
        }
        
    }

    @Override
    public void thirdParty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void ride(Zgriderequest zr, Zgride zd, Zgridepayment zp) {
        User pass = usercache.get(zr.getPassenger());//userCtrl.findFirst("findByZgid", Arrays.asList(new DBTuple("zgid", zr.getPassenger())), false);
        if(pass!=null) {
        List<ZgriderequestDrivers> links = linkCtrl.findListCustom("findByRiderequestId", Arrays.asList(new DBTuple("riderequestId", zr.getRiderequestId())));
        
        Zgfeedback dfe = null;
        Zgfeedback pfe = null;
        
        if(zd != null && ((zd.getEnded() != null && zd.getEnded().intValue() == 1) || (zd.getAborted()!= null && zd.getAborted().intValue() == 1))) {
            dfe = feedbackCtrl.findFirst("findDriverFeedback", Arrays.asList(new DBTuple("riderequestId", zr.getRiderequestId())), false);
            pfe = feedbackCtrl.findFirst("findPassengerFeedback", Arrays.asList(new DBTuple("riderequestId", zr.getRiderequestId())), false);
        }
        User driver = null;
        ZgriderequestDrivers drLink = null;
        for(ZgriderequestDrivers l : links) {
            if(l.getStatus() == 2) {
                driver = usercache.get(l.getUserId());//userCtrl.findFirst("findByZgid", Arrays.asList(new DBTuple("zgid", l.getUserId())), false);
                drLink = l;
            }
        }
        
        Integer status = Riderequest.REQUEST_STATUS_IDLE;
        if(zr.getCancellationDate() == null && zr.getConfirmationDate() == null) {
            status = Riderequest.REQUEST_STATUS_PASSENGER_CANCELED;
        } else if(zr.getCancellationDate() != null) {
            if(zr.getCancelledByUser() == null || zr.getCancelledByUser().intValue() != 1) {
                status = Riderequest.REQUEST_STATUS_DRIVER_CANCELED;
            } else {
                if(zr.getConfirmationDate()!=null) {
                    status = Riderequest.REQUEST_STATUS_PASSENGER_ABORTED;
                } else {
                    status = Riderequest.REQUEST_STATUS_PASSENGER_CANCELED;
                }                
            }
        } else if(zr.getConfirmationDate() != null && zd != null) {
            if(zd.getRiderequestAborted() != null && zd.getRiderequestAborted().intValue() == 1) {
                status = Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED;
            } else if (zd.getAborted() != null && zd.getAborted().intValue() == 1) {
                status = Riderequest.REQUEST_STATUS_DRIVER_ABORTED;
            } else if (zd.getEnded() != null && zd.getEnded().intValue() == 1) {
                if(zp != null) {
                    if(zp.getPaymentType() != null && zp.getPaymentType().toLowerCase().equals("unpaid")) {
                        status = Riderequest.REQUEST_PAYMENT_FAILED;
                    } else {
                        status = Riderequest.REQUEST_STATUS_PAID;
                    }
                } else {
                    status = Riderequest.REQUEST_STATUS_ENDED;
                }
            }
        }
        
        Integer driverfee = 0;
        Integer zegofee = 0;        
        Integer discount = 0;
        if(zp!=null) {
            driverfee = zp.getFullAmount();
            zegofee = zp.getFeeAmount();
            discount = zp.getFullAmount()+zp.getFeeAmount()-zp.getAmount();
        }
        
        
        Riderequest r = new Riderequest();
        r.setAbortdate(zd == null ? null : (zd.getAborted() != null && zd.getAborted().intValue() == 1) ? zd.getLastmoddate() : null);
        r.setAbortreason(zd == null ? null : zd.getAbortReason());
        r.setAssigndate(zr.getConfirmationDate());
        r.setCanceldate(zr.getCancellationDate());
        r.setCancelreason(null);
        r.setCharge(zp == null ? null : zp.getTransactionId());
        r.setChargeerror(null);
        r.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED);
        r.setDid(driver == null ? null : driver.getId());
        r.setDiscount(zp == null ? 0 : zp.getFullAmount() - zp.getAmount());
       
        r.setDoaddr(zr.getDropoffAddress());
        r.setDolat(zr.getDropoffLocationLat());
        r.setDolng(zr.getDropoffLocationLon());
        r.setDrivereta(zr.getDriverEta());
        r.setDriverfee(driverfee);
        r.setDriverprice(zr.getSuggestedPrice());
        r.setDrivrating(dfe == null ? null : dfe.getRating());
        r.setEnddate(zd == null ? null : (zd.getEnded() == null ? null : (zd.getEnded() == 1 ? zd.getLastmoddate() : null)));
        r.setExtmeters(0);
        r.setExtprice(zr.getSuggestedPrice());
        r.setExtsecond(0);
        r.setExtshort(0);
        r.setFreecanceldate(null);
        r.setNumpass(1);
        r.setOptions(0);
        r.setPassprice(zp == null ? zr.getSuggestedPrice() : Math.max(zp.getAmount(), zr.getSuggestedPrice()));
        r.setPassrating(pfe == null ? null : pfe.getRating());
        r.setPid(pass.getId());
        r.setPromoid(null);
        r.setPuaddr(zr.getPickupAddress());
        r.setPulat(zr.getPickupLocationLat());
        r.setPulng(zr.getPickupLocationLon());
        r.setRealdoaddr(zd == null ?  null : zd.getRealDropoffAddress());
        r.setRealdolat(zd == null ?  null : zd.getRealDropoffLat());
        r.setRealdolng(zd == null ?  null : zd.getRealDropoffLon());
        r.setRealpulat(zd == null ?  null : zd.getRealPickupLat());
        r.setRealpuaddr(zd == null ?  null : zd.getRealPickupAddress());
        r.setRealpulng(zd == null ?  null : zd.getRealDropoffLon());
        r.setReqdate(zr.getCreatedate());
        r.setShid("sh_"+UUID.randomUUID().toString());
        r.setShortid(RandomPassword.generate(6).toUpperCase());
        r.setStartdate(zd == null ?  null : zd.getCreatedate());
        r.setStatus(status);
        r.setStripedriverfee(driverfee);
        r.setStripezegofee(Math.max(0, zegofee-discount));
        r.setZegofee(zegofee);
        r.setZgid(zr.getRiderequestId());
        r.setZid(0);
        r.setZonename("Legacy");
        
        r = reqCtrl.save(r);
        
        for(ZgriderequestDrivers l : links) {
            User ddd = usercache.get(l.getUserId());//userCtrl.findFirst("findByZgid", Arrays.asList(new DBTuple("zgid", l.getUserId())), false);
            if(ddd!=null) {
            RiderequestDrivers rrd = new RiderequestDrivers();
            rrd.setAccepted(l.getStatus() == 2 ? l.getLastmoddate() : null);
            rrd.setAddr(null);
            rrd.setCanceled(null);
            rrd.setCreated(l.getCreatedate());
            rrd.setDid(ddd.getId());
            rrd.setDmeters(l.getDistance() == null ? 0  : l.getDistance().intValue());
            rrd.setDsec(0);
            rrd.setRejected(null);
            rrd.setRid(r.getId());
            rrd.setStatus(l.getStatus() == 2 ? RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED : (l.getStatus() == 16 ? RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED_BY_OTHER : l.getStatus() == 1 ? RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED : RiderequestDrivers.RIDEREQUESTDRIVERS_REJECTED));
            rrd.setValidfrom(Integer.valueOf(l.getValidfromdate() ==  null || l.getValidfromdate().isEmpty() ? "0" : l.getValidfromdate()));
            reqDrivCtrl.save(rrd);
            }
        }
        
        if(dfe != null && driver!=null && pass!=null) {
            Feedback f = new Feedback();
            f.setInsdate(RESTDateUtil.getInstance().secondsNow());
            f.setRating(dfe.getRating());
            f.setRid(r.getId());
            f.setSender(driver.getId());
            f.setUid(pass.getId());
            ffCtrl.save(f);
            
            Feedback fs = new Feedback();
            fs.setInsdate(RESTDateUtil.getInstance().secondsNow());
            fs.setRating(dfe.getRating());
            fs.setRid(r.getId());
            fs.setSender(pass.getId());
            fs.setUid(driver.getId());
            ffCtrl.save(fs);
        }
        }
        
    }
    
    
    private void driver(Integer uid, Zguser zu, ZguserProfile zp, Zgdriver zd, Zgcar zc) {
        if(zu != null && zp != null && zd != null && zc != null) {
        Zgcity zt = zgcityCtrl.findById(zp.getBirthCity());
        Driverdata dd = new Driverdata();
        dd.setAddress(zp.getHomeAddress());
        dd.setArea("TOFIX");
        dd.setBirthcity("");
        Zgcountry cc = zgCountryCtrl.findById(zp.getCountryId());
        dd.setBirthcountry(cc == null ? null : cc.getCountry());
        Manufacturer m = null;
        if(zc.getManufacturerId()!=null) {
           m =  manCtrl.findById(zc.getManufacturerId());
        }
        dd.setBrand(m == null ? "" : m.getManufacturer());
        dd.setCap(zp.getZipCode());
        dd.setCarimg(null);
        dd.setCf(zp.getFiscalCode());
        dd.setCity(zt == null ? "" : zt.getCity());
        dd.setColor("");
        dd.setDocexpdate(zd.getLicenseExpiringDate()== null ? "0" : String.valueOf(zd.getLicenseExpiringDate().getTime()/1000));
        dd.setDocimg(zd.getLicenseFilename()== null ? null : "https://s3-eu-west-1.amazonaws.com/zegoapp/driverdata/"+zd.getLicenseFilename());
        dd.setDocok(zd.getDocumentStatus() != null && zd.getDocumentStatus().intValue() == 2 ? 1 : 0);
        dd.setExpdate(zd.getLicenseExpiringDate()== null ? "0" : String.valueOf(zd.getLicenseExpiringDate().getTime()/1000));
        dd.setIban(zd.getIban());
        dd.setInsdate(zd.getCreatedate());
        dd.setInsuranceexpdate(zd.getInsuranceExpiringDate() == null ? "0" : String.valueOf(zd.getInsuranceExpiringDate().getTime()/1000));
        // https://s3-eu-west-1.amazonaws.com/zegoapp%2Fdriverdata/1485187858253Schermata%202016-12-01%20alle%2011.36.16.png
        dd.setInsuranceimg(zd.getInsuranceFilename() == null ? null : "https://s3-eu-west-1.amazonaws.com/zegoapp/driverdata/"+zd.getInsuranceFilename());
        dd.setModdate(zd.getLastmoddate());
        dd.setModel(zc.getModel());
        dd.setPlate(zc.getPlate());
        dd.setSeat(zc.getSeats());
        Integer ns;
        if(zd.getDocumentStatus() == null || zd.getAvailable() == null || zd.getDocumentStatus().intValue() == 0) {
            ns = Driverdata.STATUS_CANDIDATE;
        } else if(zd.getDocumentStatus().intValue() == 2 && zd.getAvailable().intValue() != 2) {
            ns = Driverdata.STATUS_ACCEPTED;
        } else {
            ns = Driverdata.STATUS_DOC_EXPIRED;
        }
        dd.setStatus(ns);
        dd.setUid(uid);
        dd.setYear(zc.getYear());
        drDataCtrl.save(dd);
        }
    }

    private User usermap(Zguser zu, ZguserProfile zp, Zgdriver zd) {

        if (zp == null || zp.notImportable()) {
            return null;
        }

        User u = new User();
        u.setBanexpdate(null);
        u.setBanreason(null);
        u.setBirthdate(zp.getBirthday() != null ? BDF.format(zp.getBirthday()) : null);
        u.setBitmask(0);
        u.setCandrive(zd != null && zd.getDocumentStatus() != null && zd.getDocumentStatus().intValue() == 2 && zd.getAvailable() != null && zd.getAvailable().intValue() < 2 ? 1 : 0);
        u.setChannel("legacy");
        u.setCountry(zp.getCountryId() == null ? "it" : zgCountryCtrl.findById(zp.getCountryId()).getCountryCode().toLowerCase());
        u.setCurrent(null);
        Zgdrivrating dr = zgdrRatCtrl.findById(zu.getUserId());
        u.setDavg(dr == null ? 0 : dr.getDriverRating());
        u.setDevice(null);
        u.setDeviceid(null);
        u.setEmail(zp.getEmail());
        u.setFbid(zu.getFacebookId());
        u.setFname(zp.getFirstName());
        u.setGender(zp.getGender() == null || zp.getGender().equals(1) ? "male" : "female");
        u.setImg(null);
        u.setInsdate(zu.getCreatedate());
        u.setLastseen(zu.getLastmoddate());
        u.setLlat(zu.getLastLat());
        u.setLlocdate("0");
        u.setLlon(zu.getLastLon());
        u.setLname(zp.getLastName());
        u.setMobile(zp.getPhoneNumberLocal());
        u.setMobok(zp.getPhoneNumberValidated() == null ? 0 : zp.getPhoneNumberValidated().intValue());
        u.setModdate(zu.getLastmoddate());
        u.setNote(null);
        u.setOs(zu.getOs() == null ? null : zu.getOs().equals(1) ? "iOS" : "Android");
        u.setPassword(zu.getPassword());
        Zgpassrating pr = zgpsRatCtrl.findById(zu.getUserId());
        u.setPavg(pr == null ? 0 : pr.getPassengerRating());
        u.setPayok(zp.getHasPaymentInfo() == null ? 0 : zp.getHasPaymentInfo().intValue());
        u.setPrefix(zp.getPhoneNumberPrefix());
        u.setPushid(zu.getPushId());
        u.setRefcode(zp.getUserCode());
        u.setRefuid(null);
        u.setRtstatus(100);
        u.setStatus(zu.getBanPending() != null && zu.getBanPending().equals(1) ? User.STATUS_BANNED : User.STATUS_ACTIVE);
        u.setStripeid(null);
        u.setTcok(1);
        u.setUmode("rider");
        u.setUtype("user");
        u.setVapp(zu.getVv());
        u.setVos(zu.getVos());
        u.setWemail(null);
        u.setZegotoken("us_" + UUID.randomUUID().toString());
        u.setZgid(zu.getUserId());

        return u;
    }
@Override
    public void bday() {
                SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

        List<User> uu = userCtrl.findAll();
        for(User u : uu) {
            if(u.getZgid() != null) {
                ZguserProfile zgp = zguserProfileCtrl.findFirst("findByUserId", Arrays.asList(new DBTuple("userId", u.getZgid())), false);
                if(zgp != null && zgp.getBirthday()!=null && (u.getBirthdate() == null || !u.getBirthdate().contains("/"))) {
                    u.setBirthdate(SDF.format(zgp.getBirthday()));
                    userCtrl.save(u);
                }
            }
            
        }
    
    }
    @Override
    public void driver() {
        nation = new HashMap<>();
        nation.put("ITALY", "Italia");
        nation.put("UNITED STATES", "Stati Uniti");
        nation.put("ALBANIA", "Albania");
        nation.put("TURKEY", "Tuchia");
        nation.put("TUNISIA", "Tunisia");
        nation.put("BANGLADESH", "Bangladesh");
        nation.put("SWITZERLAND", "Svizzera");
        nation.put("UNITED KINGDOM", "Regno Unito");
        nation.put("ARGENTINA", "Argentina");
        nation.put("IRELAND", "Irlanda");
        nation.put("SPAIN", "Spagna");
        nation.put("BURKINA FASO", "Burkina Faso");
        nation.put("ESTONIA", "Estonia");
        nation.put("BOLIVIA, PLURINATIONAL STATE OF", "Bolivia");
        nation.put("FRANCE", "Francia");
                nation.put("PERU", "Perù");
        nation.put("BRAZIL", "Brasile");
                nation.put("ROMANIA", "Romania");
        nation.put("MOLDOVA, REPUBLIC OF", "Moldova");
                nation.put("EGYPT", "Egitto");
        nation.put("GERMANY", "Germania");
                nation.put("MOROCCO", "Marocco");
        nation.put("PORTUGAL", "Portogallo");
                nation.put("POLAND", "Polonia");
        nation.put("LITHUANIA", "Lituania");
        nation.put("BELARUS", "Bielorussia");
        nation.put("EL SALVADOR", "El Salvador");
       
        

        List<Driverdata> dd = drDataCtrl.findAll();
        for(Driverdata d : dd) {
            User u = userCtrl.findById(d.getUid());
            boolean update = false;
            if(u.getZgid() != null) {
                ZguserProfile zgp = zguserProfileCtrl.findFirst("findByUserId", Arrays.asList(new DBTuple("userId", u.getZgid())), false);
                if(zgp!=null){
                Zgcity home = null;
                Zgcity birth = null;
                if(zgp.getBirthCity()>0) {
                    birth = zgcityCtrl.findFirst("findByCityId",  Arrays.asList(new DBTuple("cityId", zgp.getBirthCity())), false);
                    if(birth!=null){
                        d.setBirthcity(birth.getCity());
                        update = true;
                    }
                }
                
                if(zgp.getHomeCity()>0) {
                    home = zgcityCtrl.findFirst("findByCityId",  Arrays.asList(new DBTuple("cityId", zgp.getHomeCity())), false);
                    if(home!=null){
                        d.setCity(home.getCity());
                        update = true;
                    }
                }
                
                if(d.getBirthcountry()!=null) {
                    String mappedCtr = nation.get(d.getBirthcountry());
                    if(mappedCtr!=null) {
                        d.setBirthcountry(mappedCtr);
                        update = true;
                    }
                }
                
                if(d.getArea()!= null && d.getArea().equalsIgnoreCase("tofix")) {
                    Double llat = u.getLlat();
                    Double llon = u.getLlon();
                    if(llon == null) {
                        d.setArea("Torino");
                    } else if (llon > 9) {
                        d.setArea("Milano");
                    } else if (llon > 8) {
                        d.setArea("Genova");
                    } else {
                        d.setArea("Torino");
                    }
                    update = true;
                }
                
                
                if(update){
                    drDataCtrl.save(d);
                    System.out.println("FIXED "+d.getId());
                }
                }
                
            }
        }
    }

    @Override
    public void recomputeAvg() {
        List<User> tofx = userCtrl.findListCustom("findByCandrive", Arrays.asList(new DBTuple("candrive", 0)));
        for(User u : tofx) {
            List<Feedback> uf = ffCtrl.findListCustom("findByUidDriver", Arrays.asList(new DBTuple("uid", u.getId())));
            if(uf == null || uf.isEmpty()) {
                u.setPavg(0d);
            } else {
                Double ctr = 0.0d;
                for (Feedback f : uf) {
                    ctr = ctr + f.getRating();
                }
                u.setPavg(ctr/(uf.size()+0.0d));
            }
            userCtrl.save(u);
            System.out.println("FIXED "+u.getId());
        }
    }
}
