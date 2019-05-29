/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.rest.request.BootRequest;
import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
import java.util.Random;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.count", query = "SELECT count(u) FROM User u"),
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.id desc"),
    @NamedQuery(name = "User.findWithDebt", query = "SELECT u FROM User u where u.debt > 0 ORDER BY u.id desc"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByCandrive", query = "SELECT u FROM User u WHERE u.candrive = :candrive"),
    @NamedQuery(name = "User.findByUtype", query = "SELECT u FROM User u WHERE u.utype = :utype ORDER BY u.id desc"),
    @NamedQuery(name = "User.findByAdminLogin", query = "SELECT u FROM User u WHERE u.email = :username and u.password = :password"),
    @NamedQuery(name = "User.findByFname", query = "SELECT u FROM User u WHERE u.fname = :fname"),
    @NamedQuery(name = "User.findByLname", query = "SELECT u FROM User u WHERE u.lname = :lname"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByWemail", query = "SELECT u FROM User u WHERE u.wemail = :wemail"),
    @NamedQuery(name = "User.findByCountry", query = "SELECT u FROM User u WHERE u.country = :country"),
    @NamedQuery(name = "User.findByPrefix", query = "SELECT u FROM User u WHERE u.prefix = :prefix"),
    @NamedQuery(name = "User.findByPrefixAndMobile", query = "SELECT u FROM User u WHERE u.prefix = :prefix and u.mobile = :mobile"),
    @NamedQuery(name = "User.findByMobile", query = "SELECT u FROM User u WHERE u.mobile = :mobile"),
    @NamedQuery(name = "User.findByInsdate", query = "SELECT u FROM User u WHERE u.insdate = :insdate"),
    @NamedQuery(name = "User.findByInsdateBetween", query = "SELECT u FROM User u WHERE u.insdate BETWEEN :start and :stop"),
    @NamedQuery(name = "User.findByLastseenBetween", query = "SELECT u FROM User u WHERE u.lastseen BETWEEN :start and :stop"),
    @NamedQuery(name = "User.findByModdate", query = "SELECT u FROM User u WHERE u.moddate = :moddate"),
    @NamedQuery(name = "User.findByLastseen", query = "SELECT u FROM User u WHERE u.lastseen = :lastseen"),
    @NamedQuery(name = "User.findByImg", query = "SELECT u FROM User u WHERE u.img = :img"),
    @NamedQuery(name = "User.findByFbid", query = "SELECT u FROM User u WHERE u.fbid = :fbid"),
    @NamedQuery(name = "User.findByPayok", query = "SELECT u FROM User u WHERE u.payok = :payok"),
    @NamedQuery(name = "User.findByMobok", query = "SELECT u FROM User u WHERE u.mobok = :mobok"),
    @NamedQuery(name = "User.findByRefcode", query = "SELECT u FROM User u WHERE u.refcode = :refcode"),
    @NamedQuery(name = "User.findByRefuid", query = "SELECT u FROM User u WHERE u.refuid = :refuid"),
    @NamedQuery(name = "User.findByDevice", query = "SELECT u FROM User u WHERE u.device = :device"),
    @NamedQuery(name = "User.findByVos", query = "SELECT u FROM User u WHERE u.vos = :vos"),
    @NamedQuery(name = "User.findByOs", query = "SELECT u FROM User u WHERE u.os = :os"),
    @NamedQuery(name = "User.findByPushid", query = "SELECT u FROM User u WHERE u.pushid = :pushid"),
    @NamedQuery(name = "User.findByVapp", query = "SELECT u FROM User u WHERE u.vapp = :vapp"),
    @NamedQuery(name = "User.findByGender", query = "SELECT u FROM User u WHERE u.gender = :gender"),
    @NamedQuery(name = "User.findByBirthdate", query = "SELECT u FROM User u WHERE u.birthdate = :birthdate"),
    @NamedQuery(name = "User.findByDeviceid", query = "SELECT u FROM User u WHERE u.deviceid = :deviceid"),
    @NamedQuery(name = "User.findByLlat", query = "SELECT u FROM User u WHERE u.llat = :llat"),
    @NamedQuery(name = "User.findByLlon", query = "SELECT u FROM User u WHERE u.llon = :llon"),
    @NamedQuery(name = "User.findByLlocdate", query = "SELECT u FROM User u WHERE u.llocdate = :llocdate"),
    @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status ORDER BY u.id desc"),
    @NamedQuery(name = "User.findByRtstatus", query = "SELECT u FROM User u WHERE u.rtstatus = :rtstatus"),
    @NamedQuery(name = "User.findByBitmask", query = "SELECT u FROM User u WHERE u.bitmask = :bitmask"),
    @NamedQuery(name = "User.findByBanexpdate", query = "SELECT u FROM User u WHERE u.banexpdate = :banexpdate"),
    @NamedQuery(name = "User.findByBanreason", query = "SELECT u FROM User u WHERE u.banreason = :banreason"),
    @NamedQuery(name = "User.findByStripeid", query = "SELECT u FROM User u WHERE u.stripeid = :stripeid"),
    @NamedQuery(name = "User.findDriving", query = "SELECT u FROM User u WHERE u.llocdate > :locdate and u.status = 1 and u.umode = 'driver'"),
    @NamedQuery(name = "User.findIosDrivers", query = "SELECT u FROM User u WHERE u.llocdate > :locdate and u.status = 1 and u.umode = 'driver' and u.os = 'iOS'"),
    @NamedQuery(name = "User.findByZegotoken", query = "SELECT u FROM User u WHERE u.zegotoken = :zegotoken"),
    @NamedQuery(name = "User.countBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop"),
    @NamedQuery(name = "User.countByCityBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and u.area = :area"),
    @NamedQuery(name = "User.countInprogressBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and u.status = 0"),
    @NamedQuery(name = "User.countPayOkBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and  u.payok = 1"),
    @NamedQuery(name = "User.countAllOkBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and u.img is not null and u.payok = 1"),
    @NamedQuery(name = "User.countCanDriveBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and u.candrive = 1"),
    @NamedQuery(name = "User.countCanDriveByCityBetween", query = "SELECT count(u) FROM User u WHERE u.insdate BETWEEN :start and :stop and u.candrive = 1 and u.area = :area"),
    @NamedQuery(name = "User.findByZgid", query = "SELECT u FROM User u WHERE u.zgid = :zgid")})
public class User implements Serializable {
    
    public static final Integer STATUS_IDLE = 0;
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_BANNED = 2;
    
    public static final Integer UPDATE_PHASE_SIGNUP = 0;
    public static final Integer UPDATE_PHASE_REGULAR = 1;
    
    public static final Integer REALTIME_STATUS_PASSENGER_IDLE = 100;
    public static final Integer REALTIME_STATUS_PASSENGER_REQUEST_SENT = 101;
    public static final Integer REALTIME_STATUS_PASSENGER_WAITING_DRIVER = 102;
    public static final Integer REALTIME_STATUS_PASSENGER_ONRIDE = 103;
    public static final Integer REALTIME_STATUS_PASSENGER_PAYMENT_DUE = 104;
    public static final Integer REALTIME_STATUS_PASSENGER_FEEDBACK_DUE = 105;
    
    public static final Integer REALTIME_STATUS_DRIVER_IDLE = 200;
    public static final Integer REALTIME_STATUS_DRIVER_ANSWERING = 201;
    public static final Integer REALTIME_STATUS_DRIVER_PICKINGUP = 202;
    public static final Integer REALTIME_STATUS_DRIVER_ONRIDE = 203;
    public static final Integer REALTIME_STATUS_DRIVER_FEEDBACKDUE = 204;
    
    public static final String UTYPE_ADMIN = "admin";
    public static final String UTYPE_USER = "user";
    
    public static final String UMODE_RIDER = "rider";
    public static final String UMODE_DRIVER = "driver";
    
    private static final long serialVersionUID = 1L;    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "utype")
    private String utype;
    @Column(name = "umode")
    private String umode;
    @Column(name = "candrive")
    private Integer candrive;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "email")
    private String email;
    @Column(name = "wemail")
    private String wemail;
    @Column(name = "country")
    private String country;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "moddate")
    private String moddate;
    @Column(name = "lastseen")
    private String lastseen;
    @Column(name = "img")
    private String img;
    @Column(name = "fbid")
    private String fbid;
    @Column(name = "payok")
    private Integer payok;
    @Column(name = "mobok")
    private Integer mobok;
    @Column(name = "refcode")
    private String refcode;
    @Column(name = "refuid")
    private Integer refuid;
    @Column(name = "device")
    private String device;
    @Column(name = "vos")
    private String vos;
    @Column(name = "os")
    private String os;
    @Column(name = "pushid")
    private String pushid;    
    @Column(name = "vapp")
    private String vapp;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birthdate")
    private String birthdate;
    @Column(name = "deviceid")
    private String deviceid;
    @Column(name = "llat")
    private Double llat;
    @Column(name = "llon")
    private Double llon;
    @Column(name = "llocdate")
    private String llocdate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "rtstatus")
    private Integer rtstatus;
    @Column(name = "bitmask")
    private Integer bitmask;
    @Column(name = "banexpdate")
    private String banexpdate;
    @Column(name = "banreason")
    private String banreason;
    @Column(name = "stripeid")
    private String stripeid;
    @Column(name = "zegotoken")
    private String zegotoken;
    @Column(name = "tcok")
    private Integer tcok;
    @Column(name = "pavg")
    private Double pavg;
    @Column(name = "davg")
    private Double davg;
    @Column(name = "current")
    private Integer current;
    @Column(name = "debt")
    private Integer debt;
    @Column(name = "channel")
    private String channel;
    @Column(name = "password")
    private String password;
    @Column(name = "note")
    private String note;
    @Column(name = "area")
    private String area;
    @Column(name = "zgid")
    private Integer zgid;
    @Column(name = "refawarded")
    private Integer refawarded;
    @Column(name = "cashdue")
    private Integer cashdue;
    @Column(name = "cardonly")
    private Integer cardonly;
    @Column(name = "cashused")
    private Integer cashused;
        
    
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWemail() {
        return wemail;
    }

    public void setWemail(String wemail) {
        this.wemail = wemail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public String getModdate() {
        return moddate;
    }

    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public Integer getPayok() {
        return payok;
    }

    public void setPayok(Integer payok) {
        this.payok = payok;
    }

    public Integer getMobok() {
        return mobok;
    }

    public void setMobok(Integer mobok) {
        this.mobok = mobok;
    }

    public String getRefcode() {
        return refcode;
    }

    public void setRefcode(String refcode) {
        this.refcode = refcode;
    }

    public Integer getRefuid() {
        return refuid;
    }

    public void setRefuid(Integer refuid) {
        this.refuid = refuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getVos() {
        return vos;
    }

    public void setVos(String vos) {
        this.vos = vos;
    }

    public String getVapp() {
        return vapp;
    }

    public void setVapp(String vapp) {
        this.vapp = vapp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public Double getLlat() {
        return llat;
    }

    public void setLlat(Double llat) {
        this.llat = llat;
    }

    public Double getLlon() {
        return llon;
    }

    public void setLlon(Double llon) {
        this.llon = llon;
    }

    public String getLlocdate() {
        return llocdate;
    }

    public void setLlocdate(String llocdate) {
        this.llocdate = llocdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRtstatus() {
        return rtstatus;
    }

    public void setRtstatus(Integer rtstatus) {
        this.rtstatus = rtstatus;
    }

    public Integer getBitmask() {
        return bitmask;
    }

    public void setBitmask(Integer bitmask) {
        this.bitmask = bitmask;
    }

    public String getBanexpdate() {
        return banexpdate;
    }

    public void setBanexpdate(String banexpdate) {
        this.banexpdate = banexpdate;
    }

    public String getBanreason() {
        return banreason;
    }

    public void setBanreason(String banreason) {
        this.banreason = banreason;
    }

    public String getStripeid() {
        return stripeid;
    }

    public void setStripeid(String stripeid) {
        this.stripeid = stripeid;
    }

    public String getZegotoken() {
        return zegotoken;
    }

    public void setZegotoken(String zegotoken) {
        this.zegotoken = zegotoken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.User[ id=" + id + " ]";
    }

    /**
     * @return the umode
     */
    public String getUmode() {
        return umode;
    }

    /**
     * @param umode the umode to set
     */
    public void setUmode(String umode) {
        this.umode = umode;
    }

    /**
     * @return the candrive
     */
    public Integer getCandrive() {
        return candrive;
    }

    /**
     * @param candrive the candrive to set
     */
    public void setCandrive(Integer candrive) {
        this.candrive = candrive;
    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return the pushid
     */
    public String getPushid() {
        return pushid;
    }

    /**
     * @param pushid the pushid to set
     */
    public void setPushid(String pushid) {
        this.pushid = pushid;
    }
    
    public void defaultInit() {
        this.candrive = 0;
        this.insdate = RESTDateUtil.getInstance().secondsNow();
        this.lastseen = insdate;
        this.mobok = 0;
        this.moddate = insdate;
        this.payok = 0;
        this.rtstatus = 0;
        this.tcok = 1;
        this.status = STATUS_IDLE;
        this.rtstatus = REALTIME_STATUS_PASSENGER_IDLE;
        this.umode = UMODE_RIDER;
        this.utype = UTYPE_USER; 
        this.debt = 0;
        this.cashused = 0;
    }
    
    public void updateFromBoot(BootRequest req) {
        this.country = req.getCountry() == null ? country : req.getCountry();
        this.device = req.getDevice();
        this.deviceid = req.getDeviceId();
        this.fbid = req.getFbid() == null ? fbid : req.getFbid();
        this.mobile = req.getMobile() == null ? mobile : req.getMobile().trim().replace(" ", "");
        this.os = req.getOs();
        this.vos = req.getVos();
        this.vapp = req.getVapp();
        this.prefix = req.getPrefix()== null ? prefix : req.getPrefix().trim().replace(" ", "");
        this.pushid = req.getPushid();
    }
    
    public void updateWithUser(User up) {
        this.birthdate = up.getBirthdate();
        this.email = up.getEmail();
        this.fname = up.getFname();
        this.gender = up.getGender();
        this.img = up.getImg();
        this.country = up.getCountry();
        if(up.getFbid()!=null) {
            this.fbid = up.getFbid();
        }
        String fulltel = prefix+mobile;
        this.mobok = fulltel.toLowerCase().equalsIgnoreCase(up.getPrefix()+up.getMobile().trim().replace(" ", "")) ? 1 : 0;
        this.mobile = up.getMobile().trim().replace(" ", "");
        this.prefix = up.getPrefix();
        this.wemail = up.getWemail();
        this.lname = up.getLname();
        this.tcok = up.getTcok();
        this.candrive = up.getCandrive();
        if(up.getPassword()!=null) {
            this.password = up.getPassword();
        }
    }
    
    public void generateRefCore() {
        String fn = fname == null ? "ZUSR" : fname.toUpperCase();
        if(fn.length()>4){
            fn = fn.substring(0, 4);
        }
        refcode = fn+String.valueOf(1000+new Random(System.currentTimeMillis()).nextInt(8999));
    }
    
    public String asMobileTarget() {
        return prefix == null || mobile == null ? "" : prefix.trim().replace("+", "").concat(mobile.trim().replace(" ", ""));
    }

    /**
     * @return the tcok
     */
    public Integer getTcok() {
        return tcok;
    }

    /**
     * @param tcok the tcok to set
     */
    public void setTcok(Integer tcok) {
        this.tcok = tcok;
    }

    /**
     * @return the pavg
     */
    public Double getPavg() {
        return pavg == null ? 0 : pavg;
    }

    /**
     * @param pavg the pavg to set
     */
    public void setPavg(Double pavg) {
        this.pavg = pavg;
    }

    /**
     * @return the davg
     */
    public Double getDavg() {
        return davg == null ? 0 : davg;
    }

    /**
     * @param davg the davg to set
     */
    public void setDavg(Double davg) {
        this.davg = davg;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getZgid() {
        return zgid;
    }

    public void setZgid(Integer zgid) {
        this.zgid = zgid;
    }

    public Integer getRefawarded() {
        return refawarded;
    }

    public void setRefawarded(Integer refawarded) {
        this.refawarded = refawarded;
    }

    public Integer getDebt() {
        return debt == null ? 0 : debt;
    }

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getCashdue() {
        return cashdue;
    }

    public void setCashdue(Integer cashdue) {
        this.cashdue = cashdue;
    }

    public Integer getCardonly() {
        return cardonly;
    }

    public void setCardonly(Integer cardonly) {
        this.cardonly = cardonly;
    }

    public Integer getCashused() {
        return cashused;
    }

    public void setCashused(Integer cashused) {
        this.cashused = cashused;
    }

    
    
    
}
