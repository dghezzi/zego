/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.rest.response.ride.CompactDriver;
import it.elbuild.zego.rest.response.ride.CompactUser;
import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "riderequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Riderequest.findAll", query = "SELECT r FROM Riderequest r"),
    @NamedQuery(name = "Riderequest.count", query = "SELECT count(r) FROM Riderequest r"),
    @NamedQuery(name = "Riderequest.findById", query = "SELECT r FROM Riderequest r WHERE r.id = :id"),
    @NamedQuery(name = "Riderequest.findByReqdateBetween", query = "SELECT r FROM Riderequest r WHERE r.reqdate BETWEEN :start and  :stop"),
    @NamedQuery(name = "Riderequest.findByPromoAndReqdateBetween", query = "SELECT r FROM Riderequest r WHERE r.promoid is not null and r.promoid> 0 and r.reqdate BETWEEN :start and  :stop"),
    @NamedQuery(name = "Riderequest.findByReqdateBetweenZid", query = "SELECT r FROM Riderequest r WHERE r.zid = :zid and r.reqdate BETWEEN :start and :stop order by r.reqdate asc"),
    @NamedQuery(name = "Riderequest.findUnpaidForUid", query = "SELECT r FROM Riderequest r WHERE r.pid = :pid and r.status = 12 and r.failctr < :failctr order by r.id desc"),
    @NamedQuery(name = "Riderequest.findByZid", query = "SELECT r FROM Riderequest r WHERE r.zid = :zid"),
    @NamedQuery(name = "Riderequest.findByShid", query = "SELECT r FROM Riderequest r WHERE r.shid = :shid"),
    @NamedQuery(name = "Riderequest.findByPid", query = "SELECT r FROM Riderequest r WHERE r.pid = :pid order by r.id desc"),
    @NamedQuery(name = "Riderequest.findByPidTerminated", query = "SELECT r FROM Riderequest r WHERE r.pid = :pid and r.status IN (9,10,11,12) order by r.id desc"),
    @NamedQuery(name = "Riderequest.findByDid", query = "SELECT r FROM Riderequest r WHERE r.did = :did order by r.id desc"),
    @NamedQuery(name = "Riderequest.countByPidBetween", query = "SELECT count(r) FROM Riderequest r WHERE r.pid = :pid and r.reqdate between :start and :stop order by r.id desc"),
    @NamedQuery(name = "Riderequest.countByPidBetweenTerminated", query = "SELECT count(r) FROM Riderequest r WHERE r.pid = :pid and r.reqdate between :start and :stop and r.status IN (9,10,11,12) order by r.id desc"),
    @NamedQuery(name = "Riderequest.countByDidBetween", query = "SELECT count(r) FROM Riderequest r WHERE r.did = :did and r.reqdate between :start and :stop order by r.id desc"),    
    @NamedQuery(name = "Riderequest.findExpired", query = "SELECT r FROM Riderequest r WHERE r.status IN (0,2) and r.reqdate < :now"),
    @NamedQuery(name = "Riderequest.findHistoryByPid", query = "SELECT r FROM Riderequest r WHERE r.pid = :pid and r.status IN (9,10,11,12) order by r.id desc"),
    @NamedQuery(name = "Riderequest.findHistoryByDid", query = "SELECT r FROM Riderequest r WHERE r.did = :did and r.status IN (9,10,11,12) order by r.id desc"),
    @NamedQuery(name = "Riderequest.findWebHistoryByPid", query = "SELECT r FROM Riderequest r WHERE r.pid = :pid and r.status IN (9,10,11,12) and r.reqdate between :start and :stop order by r.id desc"),
    @NamedQuery(name = "Riderequest.findWebHistoryByDid", query = "SELECT r FROM Riderequest r WHERE r.did = :did and r.status IN (9,10,11,12) and r.reqdate between :start and :stop order by r.id desc"),
    @NamedQuery(name = "Riderequest.findByStatus", query = "SELECT r FROM Riderequest r WHERE r.status = :status"),
    @NamedQuery(name = "Riderequest.findByPulat", query = "SELECT r FROM Riderequest r WHERE r.pulat = :pulat"),
    @NamedQuery(name = "Riderequest.findByPulng", query = "SELECT r FROM Riderequest r WHERE r.pulng = :pulng"),
    @NamedQuery(name = "Riderequest.findByPuaddr", query = "SELECT r FROM Riderequest r WHERE r.puaddr = :puaddr"),
    @NamedQuery(name = "Riderequest.findByDolat", query = "SELECT r FROM Riderequest r WHERE r.dolat = :dolat"),
    @NamedQuery(name = "Riderequest.findByDolng", query = "SELECT r FROM Riderequest r WHERE r.dolng = :dolng"),
    @NamedQuery(name = "Riderequest.findByDoaddr", query = "SELECT r FROM Riderequest r WHERE r.doaddr = :doaddr"),
    @NamedQuery(name = "Riderequest.findByReqdate", query = "SELECT r FROM Riderequest r WHERE r.reqdate = :reqdate"),
    @NamedQuery(name = "Riderequest.findByAssigndate", query = "SELECT r FROM Riderequest r WHERE r.assigndate = :assigndate"),
    @NamedQuery(name = "Riderequest.findByCanceldate", query = "SELECT r FROM Riderequest r WHERE r.canceldate = :canceldate"),
    @NamedQuery(name = "Riderequest.findByAbortdate", query = "SELECT r FROM Riderequest r WHERE r.abortdate = :abortdate"),
    @NamedQuery(name = "Riderequest.findByStartdate", query = "SELECT r FROM Riderequest r WHERE r.startdate = :startdate"),
    @NamedQuery(name = "Riderequest.findByEnddate", query = "SELECT r FROM Riderequest r WHERE r.enddate = :enddate"),
    @NamedQuery(name = "Riderequest.findByExtmeters", query = "SELECT r FROM Riderequest r WHERE r.extmeters = :extmeters"),
    @NamedQuery(name = "Riderequest.findByExtsecond", query = "SELECT r FROM Riderequest r WHERE r.extsecond = :extsecond"),
    @NamedQuery(name = "Riderequest.findByExtshort", query = "SELECT r FROM Riderequest r WHERE r.extshort = :extshort"),
    @NamedQuery(name = "Riderequest.findByDrivereta", query = "SELECT r FROM Riderequest r WHERE r.drivereta = :drivereta"),
    @NamedQuery(name = "Riderequest.findByRealpulat", query = "SELECT r FROM Riderequest r WHERE r.realpulat = :realpulat"),
    @NamedQuery(name = "Riderequest.findByRealpulng", query = "SELECT r FROM Riderequest r WHERE r.realpulng = :realpulng"),
    @NamedQuery(name = "Riderequest.findByRealpuaddr", query = "SELECT r FROM Riderequest r WHERE r.realpuaddr = :realpuaddr"),
    @NamedQuery(name = "Riderequest.findByRealdolat", query = "SELECT r FROM Riderequest r WHERE r.realdolat = :realdolat"),
    @NamedQuery(name = "Riderequest.findByRealdolng", query = "SELECT r FROM Riderequest r WHERE r.realdolng = :realdolng"),
    @NamedQuery(name = "Riderequest.findByRealdoaddr", query = "SELECT r FROM Riderequest r WHERE r.realdoaddr = :realdoaddr"),
    @NamedQuery(name = "Riderequest.findByExtprice", query = "SELECT r FROM Riderequest r WHERE r.extprice = :extprice"),
    @NamedQuery(name = "Riderequest.findByDriverfee", query = "SELECT r FROM Riderequest r WHERE r.driverfee = :driverfee"),
    @NamedQuery(name = "Riderequest.findByZegofee", query = "SELECT r FROM Riderequest r WHERE r.zegofee = :zegofee"),
    @NamedQuery(name = "Riderequest.findByPassprice", query = "SELECT r FROM Riderequest r WHERE r.passprice = :passprice"),
    @NamedQuery(name = "Riderequest.findByNumpass", query = "SELECT r FROM Riderequest r WHERE r.numpass = :numpass"),
    @NamedQuery(name = "Riderequest.findByOptions", query = "SELECT r FROM Riderequest r WHERE r.options = :options"),
    @NamedQuery(name = "Riderequest.findByPassrating", query = "SELECT r FROM Riderequest r WHERE r.passrating = :passrating"),
    @NamedQuery(name = "Riderequest.findByDrivrating", query = "SELECT r FROM Riderequest r WHERE r.drivrating = :drivrating"),
    @NamedQuery(name = "Riderequest.findByDiscount", query = "SELECT r FROM Riderequest r WHERE r.discount = :discount"),
    @NamedQuery(name = "Riderequest.findByPromoid", query = "SELECT r FROM Riderequest r WHERE r.promoid = :promoid"),
    @NamedQuery(name = "Riderequest.findByShid", query = "SELECT r FROM Riderequest r WHERE r.shid = :shid"),
    @NamedQuery(name = "Riderequest.findByZgid", query = "SELECT r FROM Riderequest r WHERE r.zgid = :zgid"),
    @NamedQuery(name = "Riderequest.findByAbortreason", query = "SELECT r FROM Riderequest r WHERE r.abortreason = :abortreason"),
    @NamedQuery(name = "Riderequest.findByCancelreason", query = "SELECT r FROM Riderequest r WHERE r.cancelreason = :cancelreason")})
public class Riderequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "did")
    private Integer did;
    @Column(name = "status")
    private Integer status;
    @Column(name = "pulat")
    private Double pulat;
    @Column(name = "pulng")
    private Double pulng;
    @Column(name = "puaddr")
    private String puaddr;
    @Column(name = "dolat")
    private Double dolat;
    @Column(name = "dolng")
    private Double dolng;
    @Column(name = "doaddr")
    private String doaddr;
    @Column(name = "reqdate")
    private String reqdate;
    @Column(name = "assigndate")
    private String assigndate;
    @Column(name = "canceldate")
    private String canceldate;
    @Column(name = "abortdate")
    private String abortdate;
    @Column(name = "startdate")
    private String startdate;
    @Column(name = "enddate")
    private String enddate;
    @Column(name = "extmeters")
    private Integer extmeters;
    @Column(name = "extsecond")
    private Integer extsecond;
    @Column(name = "extshort")
    private Integer extshort;
    @Column(name = "drivereta")
    private Integer drivereta;
    @Column(name = "realpulat")
    private Double realpulat;
    @Column(name = "realpulng")
    private Double realpulng;
    @Column(name = "realpuaddr")
    private String realpuaddr;
    @Column(name = "realdolat")
    private Double realdolat;
    @Column(name = "realdolng")
    private Double realdolng;
    @Column(name = "realdoaddr")
    private String realdoaddr;
    @Column(name = "extprice")
    private Integer extprice;
    @Column(name = "driverfee")
    private Integer driverfee;
    @Column(name = "zegofee")
    private Integer zegofee;
    @Column(name = "passprice")
    private Integer passprice;
    @Column(name = "driverprice")
    private Integer driverprice;
    @Column(name = "numpass")
    private Integer numpass;
    @Column(name = "options")
    private Integer options;
    @Column(name = "passrating")
    private Integer passrating;
    @Column(name = "drivrating")
    private Integer drivrating;
    @Column(name = "discount")
    private Integer discount;
    @Column(name = "promoid")
    private Integer promoid;
    @Column(name = "shid")
    private String shid;
    @Column(name = "abortreason")
    private String abortreason;
    @Column(name = "cancelreason")
    private String cancelreason;
    @Column(name = "shortid")
    private String shortid;
    @Column(name = "charge")
    private String charge;
    @Column(name = "chargestatus")
    private String chargestatus;
    @Column(name = "chargeerror")
    private String chargeerror;
    @Column(name = "zonename")
    private String zonename;
    @Column(name = "zid")
    private Integer zid;
    @Column(name = "refund")
    private Integer refund;
    @Column(name = "freecanceldate")
    private String freecanceldate;
    @Column(name = "paymentdate")
    private String paymentdate;
    @Column(name = "stripezegofee")
    private Integer stripezegofee;
    @Column(name = "stripedriverfee")
    private Integer stripedriverfee;
    @Column(name = "zgid")
    private Integer zgid;
    @Column(name = "failctr")
    private Integer failctr;
    @Column(name = "reqlevel")
    private Integer reqlevel;
    @Column(name = "driverend")
    private String driverend;
    @Column(name = "method")
    private String method;
    
    public static final Integer REQUEST_STATUS_IDLE = 0;
    public static final Integer REQUEST_STATUS_NO_DRIVERS = 1;
    public static final Integer REQUEST_STATUS_SUBMITTED = 2;
    public static final Integer REQUEST_STATUS_DRIVER_ANSWERED = 3;
    public static final Integer REQUEST_STATUS_PASSENGER_CANCELED = 4;
    public static final Integer REQUEST_STATUS_DRIVER_CANCELED = 5;
    public static final Integer REQUEST_STATUS_ON_RIDE = 6;
    public static final Integer REQUEST_STATUS_DRIVER_ABORTED = 7;
    public static final Integer REQUEST_STATUS_PASSENGER_ABORTED = 8;
    public static final Integer REQUEST_STATUS_ENDED = 9;
    public static final Integer REQUEST_STATUS_PAID = 10;
    public static final Integer REQUEST_STATUS_PASSENGER_TERMINATED = 11;
    public static final Integer REQUEST_PAYMENT_FAILED = 12;
    public static final Integer REQUEST_MISSING_FUNDS = 13;
    public static final Integer REQUEST_CANCELLED_BY_SERVER = 14;
    public static final Integer REQUEST_STATUS_REFUNDED = 15;
    public static final Integer REQUEST_STATUS_ABORTED_UNPAID = 16;
    
    public static final String REQUEST_CHARGE_STATUS_AUTHORIZED = "authorized";
    public static final String REQUEST_CHARGE_STATUS_FAILED = "failed";
    public static final String REQUEST_CHARGE_STATUS_CAPTURED = "captured";
    public static final String REQUEST_CHARGE_STATUS_REFUNDED = "refunded";
    
    public static final Integer REQUEST_LEVEL_STANDARD = 0;
    public static final Integer REQUEST_LEVEL_PINK = 2;
    public static final Integer REQUEST_LEVEL_CONTROL = 4;
    
    public static final String REQUEST_METHOD_CASH = "cash";
    public static final String REQUEST_METHOD_CARD = "card";
    @Transient
    private CompactDriver driver;
    
    @Transient
    private CompactUser rider;
    
    public Riderequest() {
    }

    public Riderequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPulat() {
        return pulat;
    }

    public void setPulat(Double pulat) {
        this.pulat = pulat;
    }

    public Double getPulng() {
        return pulng;
    }

    public void setPulng(Double pulng) {
        this.pulng = pulng;
    }

    public String getPuaddr() {
        return puaddr;
    }

    public void setPuaddr(String puaddr) {
        this.puaddr = puaddr;
    }

    public Double getDolat() {
        return dolat;
    }

    public void setDolat(Double dolat) {
        this.dolat = dolat;
    }

    public Double getDolng() {
        return dolng;
    }

    public void setDolng(Double dolng) {
        this.dolng = dolng;
    }

    public String getDoaddr() {
        return doaddr;
    }

    public void setDoaddr(String doaddr) {
        this.doaddr = doaddr;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getAssigndate() {
        return assigndate;
    }

    public void setAssigndate(String assigndate) {
        this.assigndate = assigndate;
    }

    public String getCanceldate() {
        return canceldate;
    }

    public void setCanceldate(String canceldate) {
        this.canceldate = canceldate;
    }

    public String getAbortdate() {
        return abortdate;
    }

    public void setAbortdate(String abortdate) {
        this.abortdate = abortdate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Integer getExtmeters() {
        return extmeters;
    }

    public void setExtmeters(Integer extmeters) {
        this.extmeters = extmeters;
    }

    public Integer getExtsecond() {
        return extsecond;
    }

    public void setExtsecond(Integer extsecond) {
        this.extsecond = extsecond;
    }

    public Integer getExtshort() {
        return extshort;
    }

    public void setExtshort(Integer extshort) {
        this.extshort = extshort;
    }

    public Integer getDrivereta() {
        return drivereta == null ? 0 : drivereta;
    }

    public void setDrivereta(Integer drivereta) {
        this.drivereta = drivereta;
    }

    public Double getRealpulat() {
        return realpulat;
    }

    public void setRealpulat(Double realpulat) {
        this.realpulat = realpulat;
    }

    public Double getRealpulng() {
        return realpulng;
    }

    public void setRealpulng(Double realpulng) {
        this.realpulng = realpulng;
    }

    public String getRealpuaddr() {
        return realpuaddr;
    }

    public void setRealpuaddr(String realpuaddr) {
        this.realpuaddr = realpuaddr;
    }

    public Double getRealdolat() {
        return realdolat;
    }

    public void setRealdolat(Double realdolat) {
        this.realdolat = realdolat;
    }

    public Double getRealdolng() {
        return realdolng;
    }

    public void setRealdolng(Double realdolng) {
        this.realdolng = realdolng;
    }

    public String getRealdoaddr() {
        return realdoaddr;
    }

    public void setRealdoaddr(String realdoaddr) {
        this.realdoaddr = realdoaddr;
    }

    public Integer getExtprice() {
        return extprice == null ? 0 : extprice;
    }

    public void setExtprice(Integer extprice) {
        this.extprice = extprice;
    }

    public Integer getDriverfee() {
        return driverfee == null ? 0 : driverfee;
    }

    public void setDriverfee(Integer driverfee) {
        this.driverfee = Math.max(0,driverfee);
    }

    public Integer getZegofee() {
        return zegofee == null ? 0 : zegofee;
    }

    public void setZegofee(Integer zegofee) {
        this.zegofee = zegofee;
    }

    public Integer getPassprice() {
        return passprice == null ? 0 : passprice;
    }

    public void setPassprice(Integer passprice) {
        this.passprice = passprice;
    }

    public Integer getNumpass() {
        return numpass;
    }

    public void setNumpass(Integer numpass) {
        this.numpass = numpass;
    }

    public Integer getOptions() {
        return options;
    }

    public void setOptions(Integer options) {
        this.options = options;
    }

    public Integer getPassrating() {
        return passrating;
    }

    public void setPassrating(Integer passrating) {
        this.passrating = passrating;
    }

    public Integer getDrivrating() {
        return drivrating;
    }

    public void setDrivrating(Integer drivrating) {
        this.drivrating = drivrating;
    }

    public Integer getDiscount() {
        return discount == null ? 0 : discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPromoid() {
        return promoid;
    }

    public void setPromoid(Integer promoid) {
        this.promoid = promoid;
    }

    public String getShid() {
        return shid;
    }

    public void setShid(String shid) {
        this.shid = shid;
    }

    public String getAbortreason() {
        return abortreason;
    }

    public void setAbortreason(String abortreason) {
        this.abortreason = abortreason;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }

    public CompactDriver getDriver() {
        return driver;
    }

    public void setDriver(CompactDriver driver) {
        this.driver = driver;
    }

    public CompactUser getRider() {
        return rider;
    }

    public void setRider(CompactUser rider) {
        this.rider = rider;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
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
        if (!(object instanceof Riderequest)) {
            return false;
        }
        Riderequest other = (Riderequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Riderequest[ id=" + id + " ]";
    }

    /**
     * @return the driverprice
     */
    public Integer getDriverprice() {
        return driverprice == null ? 0 : driverprice;
    }

    /**
     * @param driverprice the driverprice to set
     */
    public void setDriverprice(Integer driverprice) {
        this.driverprice = driverprice;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getChargestatus() {
        return chargestatus;
    }

    public void setChargestatus(String chargestatus) {
        this.chargestatus = chargestatus;
    }

    public String getChargeerror() {
        return chargeerror;
    }

    public void setChargeerror(String chargeerror) {
        this.chargeerror = chargeerror;
    }
    
    
    public String asNotificationText(Integer type) {
        StringBuilder sb = new StringBuilder();
        switch(type) {
            case Notifications.DRIVER_NEW_REQUEST:
            {
                sb.append(Notifications.NEW_REQUEST_TEXT);
                sb.append("\nDa: ").append(getPuaddr()).append("\n");
                sb.append("A: ").append(getDoaddr()).append("\n");
                sb.append("Rimborso: ").append(new DecimalFormat("0.00").format(driverfee/100f)).append(" €");
                if(getRider()!=null){
                    sb.append("\nPasseggero: ").append(rider.getName());
                }        
                break;
            }
            case Notifications.USER_DRIVER_ACCEPT:
            {
                sb.append(driver.getName()).append(" ha accettato la tua richiesta e sarà al punto di partenza fra ").append((getDrivereta()/60)+1).append(" minuti.\n");
                sb.append("Auto: ").append(driver.getBrand()).append(" ").append(driver.getModel()).append(" ").append(driver.getColor());
                break;
            }
            
            case Notifications.USER_DRIVER_IAMHERE:
            {
                sb.append("Il tuo driver è arrivato al punto di partenza e ti sta aspettando.");
                //sb.append("Auto: ").append(driver.getBrand()).append(" ").append(driver.getModel()).append(" ").append(driver.getColor());
                break;
            }
        }
        return sb.toString();
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public String getFreecanceldate() {
        return freecanceldate == null ? String.valueOf(Integer.MAX_VALUE) : freecanceldate;
    }

    public void setFreecanceldate(String freecanceldate) {
        this.freecanceldate = freecanceldate;
    }        

    public Integer getStripezegofee() {
        return stripezegofee == null ?  0 : stripezegofee;
    }

    public void setStripezegofee(Integer stripezegofee) {
        this.stripezegofee = stripezegofee;
    }

    public Integer getStripedriverfee() {
        return stripedriverfee == null ? 0 : stripedriverfee;
    }

    public void setStripedriverfee(Integer stripedriverfee) {
        this.stripedriverfee = Math.max(0,stripedriverfee);
    }

    public Integer getZgid() {
        return zgid;
    }

    public void setZgid(Integer zgid) {
        this.zgid = zgid;
    }
    
    public Map<String,String> mandrill() {
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat NF = new DecimalFormat("0.00");
        HashMap<String,String> m = new HashMap<>();
        if(enddate != null) {
            m.put("request_enddate_formatted", SDF.format(RESTDateUtil.getInstance().formatDateSeconds(enddate)));
        } else {
            m.put("request_enddate_formatted","");
        }
        m.put("request_driverfee_formatted", NF.format(getDriverfee()/100.f));
        m.put("request_zegofee_formatted", NF.format(getZegofee()/100.f));
        m.put("request_discount_formatted", NF.format(getDiscount()/100.f));
        m.put("request_importoaddebitato", NF.format((getZegofee()+getDriverfee()-getDiscount())/100.f));
        m.put("request_totaleaddebitato", NF.format((getZegofee()+getDriverfee()-getDiscount())/100.f));
        return m;
    }
    
    public boolean hasIssues() {
        return (pulat == null || pulng == null || pulat == 0.0d || pulng == 0.0d) || 
                (dolat == null || dolng == null || dolat == 0.0d || dolng == 0.0d) ||
                (puaddr == null || doaddr == null || Objects.equals(puaddr, doaddr));
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getDriverend() {
        return driverend;
    }

    public void setDriverend(String driverend) {
        this.driverend = driverend;
    }
    
    public void checkRouding() {
        if(discount != null && passprice != null && discount > passprice) {
            discount = passprice;
        }
    }

    public Integer getReqlevel() {
        return reqlevel;
    }

    public void setReqlevel(Integer reqlevel) {
        this.reqlevel = reqlevel;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getFailctr() {
        return failctr;
    }

    public void setFailctr(Integer failctr) {
        this.failctr = failctr;
    }
    
    
    
    
}
