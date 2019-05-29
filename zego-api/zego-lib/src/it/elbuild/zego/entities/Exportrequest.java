/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "exportrequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exportrequest.findAll", query = "SELECT e FROM Exportrequest e"),
    @NamedQuery(name = "Exportrequest.findByPaymentdate", query = "SELECT e FROM Exportrequest e WHERE e.paymentdate = :paymentdate"),
    @NamedQuery(name = "Exportrequest.findByDfname", query = "SELECT e FROM Exportrequest e WHERE e.dfname = :dfname"),
    @NamedQuery(name = "Exportrequest.findByDemail", query = "SELECT e FROM Exportrequest e WHERE e.demail = :demail"),
    @NamedQuery(name = "Exportrequest.findByDlname", query = "SELECT e FROM Exportrequest e WHERE e.dlname = :dlname"),
    @NamedQuery(name = "Exportrequest.findByRfname", query = "SELECT e FROM Exportrequest e WHERE e.rfname = :rfname"),
    @NamedQuery(name = "Exportrequest.findByRemail", query = "SELECT e FROM Exportrequest e WHERE e.remail = :remail"),
    @NamedQuery(name = "Exportrequest.findByRlname", query = "SELECT e FROM Exportrequest e WHERE e.rlname = :rlname"),
    @NamedQuery(name = "Exportrequest.findById", query = "SELECT e FROM Exportrequest e WHERE e.id = :id"),
    @NamedQuery(name = "Exportrequest.findByPid", query = "SELECT e FROM Exportrequest e WHERE e.pid = :pid"),
    @NamedQuery(name = "Exportrequest.findByDid", query = "SELECT e FROM Exportrequest e WHERE e.did = :did"),
    @NamedQuery(name = "Exportrequest.findByStatus", query = "SELECT e FROM Exportrequest e WHERE e.status = :status"),
    @NamedQuery(name = "Exportrequest.findByPulat", query = "SELECT e FROM Exportrequest e WHERE e.pulat = :pulat"),
    @NamedQuery(name = "Exportrequest.findByPulng", query = "SELECT e FROM Exportrequest e WHERE e.pulng = :pulng"),
    @NamedQuery(name = "Exportrequest.findByPuaddr", query = "SELECT e FROM Exportrequest e WHERE e.puaddr = :puaddr"),
    @NamedQuery(name = "Exportrequest.findByDolat", query = "SELECT e FROM Exportrequest e WHERE e.dolat = :dolat"),
    @NamedQuery(name = "Exportrequest.findByDolng", query = "SELECT e FROM Exportrequest e WHERE e.dolng = :dolng"),
    @NamedQuery(name = "Exportrequest.findByDoaddr", query = "SELECT e FROM Exportrequest e WHERE e.doaddr = :doaddr"),
    @NamedQuery(name = "Exportrequest.findByReqdate", query = "SELECT e FROM Exportrequest e WHERE e.reqdate = :reqdate"),
    @NamedQuery(name = "Exportrequest.findBetween", query = "SELECT e FROM Exportrequest e WHERE e.reqdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportrequest.findUnpaidBetween", query = "SELECT e FROM Exportrequest e WHERE e.status = 10 and e.paymentdate is not null and e.paymentdate BETWEEN :start and :stop and (CAST(e.paymentdate as SIGNED) - CAST(e.enddate as SIGNED)) > 600"),
    @NamedQuery(name = "Exportrequest.findFeeBetween", query = "SELECT e FROM Exportrequest e WHERE e.zegofee > 0 and e.paymentdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportrequest.findPromoBetween", query = "SELECT e FROM Exportrequest e WHERE e.promoid is not null and e.discount > 0 and e.paymentdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportrequest.findByAssigndate", query = "SELECT e FROM Exportrequest e WHERE e.assigndate = :assigndate"),
    @NamedQuery(name = "Exportrequest.findByCanceldate", query = "SELECT e FROM Exportrequest e WHERE e.canceldate = :canceldate"),
    @NamedQuery(name = "Exportrequest.findByAbortdate", query = "SELECT e FROM Exportrequest e WHERE e.abortdate = :abortdate"),
    @NamedQuery(name = "Exportrequest.findByStartdate", query = "SELECT e FROM Exportrequest e WHERE e.startdate = :startdate"),
    @NamedQuery(name = "Exportrequest.findByEnddate", query = "SELECT e FROM Exportrequest e WHERE e.enddate = :enddate"),
    @NamedQuery(name = "Exportrequest.findByExtmeters", query = "SELECT e FROM Exportrequest e WHERE e.extmeters = :extmeters"),
    @NamedQuery(name = "Exportrequest.findByExtsecond", query = "SELECT e FROM Exportrequest e WHERE e.extsecond = :extsecond"),
    @NamedQuery(name = "Exportrequest.findByExtshort", query = "SELECT e FROM Exportrequest e WHERE e.extshort = :extshort"),
    @NamedQuery(name = "Exportrequest.findByDrivereta", query = "SELECT e FROM Exportrequest e WHERE e.drivereta = :drivereta"),
    @NamedQuery(name = "Exportrequest.findByRealpulat", query = "SELECT e FROM Exportrequest e WHERE e.realpulat = :realpulat"),
    @NamedQuery(name = "Exportrequest.findByRealpulng", query = "SELECT e FROM Exportrequest e WHERE e.realpulng = :realpulng"),
    @NamedQuery(name = "Exportrequest.findByRealpuaddr", query = "SELECT e FROM Exportrequest e WHERE e.realpuaddr = :realpuaddr"),
    @NamedQuery(name = "Exportrequest.findByRealdolat", query = "SELECT e FROM Exportrequest e WHERE e.realdolat = :realdolat"),
    @NamedQuery(name = "Exportrequest.findByRealdolng", query = "SELECT e FROM Exportrequest e WHERE e.realdolng = :realdolng"),
    @NamedQuery(name = "Exportrequest.findByRealdoaddr", query = "SELECT e FROM Exportrequest e WHERE e.realdoaddr = :realdoaddr"),
    @NamedQuery(name = "Exportrequest.findByDriverfee", query = "SELECT e FROM Exportrequest e WHERE e.driverfee = :driverfee"),
    @NamedQuery(name = "Exportrequest.findByZegofee", query = "SELECT e FROM Exportrequest e WHERE e.zegofee = :zegofee"),
    @NamedQuery(name = "Exportrequest.findByPassprice", query = "SELECT e FROM Exportrequest e WHERE e.passprice = :passprice"),
    @NamedQuery(name = "Exportrequest.findByDriverprice", query = "SELECT e FROM Exportrequest e WHERE e.driverprice = :driverprice"),
    @NamedQuery(name = "Exportrequest.findByExtprice", query = "SELECT e FROM Exportrequest e WHERE e.extprice = :extprice"),
    @NamedQuery(name = "Exportrequest.findByNumpass", query = "SELECT e FROM Exportrequest e WHERE e.numpass = :numpass"),
    @NamedQuery(name = "Exportrequest.findByOptions", query = "SELECT e FROM Exportrequest e WHERE e.options = :options"),
    @NamedQuery(name = "Exportrequest.findByPassrating", query = "SELECT e FROM Exportrequest e WHERE e.passrating = :passrating"),
    @NamedQuery(name = "Exportrequest.findByDrivrating", query = "SELECT e FROM Exportrequest e WHERE e.drivrating = :drivrating"),
    @NamedQuery(name = "Exportrequest.findByDiscount", query = "SELECT e FROM Exportrequest e WHERE e.discount = :discount"),
    @NamedQuery(name = "Exportrequest.findByPromoid", query = "SELECT e FROM Exportrequest e WHERE e.promoid = :promoid"),
    @NamedQuery(name = "Exportrequest.findByShid", query = "SELECT e FROM Exportrequest e WHERE e.shid = :shid"),
    @NamedQuery(name = "Exportrequest.findByAbortreason", query = "SELECT e FROM Exportrequest e WHERE e.abortreason = :abortreason"),
    @NamedQuery(name = "Exportrequest.findByCancelreason", query = "SELECT e FROM Exportrequest e WHERE e.cancelreason = :cancelreason"),
    @NamedQuery(name = "Exportrequest.findByShortid", query = "SELECT e FROM Exportrequest e WHERE e.shortid = :shortid"),
    @NamedQuery(name = "Exportrequest.findByCharge", query = "SELECT e FROM Exportrequest e WHERE e.charge = :charge"),
    @NamedQuery(name = "Exportrequest.findByChargestatus", query = "SELECT e FROM Exportrequest e WHERE e.chargestatus = :chargestatus"),
    @NamedQuery(name = "Exportrequest.findByChargeerror", query = "SELECT e FROM Exportrequest e WHERE e.chargeerror = :chargeerror"),
    @NamedQuery(name = "Exportrequest.findByStripezegofee", query = "SELECT e FROM Exportrequest e WHERE e.stripezegofee = :stripezegofee"),
    @NamedQuery(name = "Exportrequest.findByStripedriverfee", query = "SELECT e FROM Exportrequest e WHERE e.stripedriverfee = :stripedriverfee"),
    @NamedQuery(name = "Exportrequest.findByZonename", query = "SELECT e FROM Exportrequest e WHERE e.zonename = :zonename"),
    @NamedQuery(name = "Exportrequest.findByZid", query = "SELECT e FROM Exportrequest e WHERE e.zid = :zid"),
    @NamedQuery(name = "Exportrequest.findByFreecanceldate", query = "SELECT e FROM Exportrequest e WHERE e.freecanceldate = :freecanceldate"),
    @NamedQuery(name = "Exportrequest.findByZgid", query = "SELECT e FROM Exportrequest e WHERE e.zgid = :zgid")})
public class Exportrequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "paymentdate")
    private String paymentdate;
    @Column(name = "dfname")
    private String dfname;
    @Column(name = "demail")
    private String demail;
    @Column(name = "dlname")
    private String dlname;
    @Column(name = "rfname")
    private String rfname;
    @Column(name = "remail")
    private String remail;
    @Column(name = "rlname")
    private String rlname;
    @Basic(optional = false)
    @Column(name = "id")
    @Id
    private Integer id;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "did")
    private Integer did;
    @Column(name = "status")
    private Integer status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "iban")
    private String iban;
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
    @Column(name = "driverfee")
    private Integer driverfee;
    @Column(name = "zegofee")
    private Integer zegofee;
    @Column(name = "passprice")
    private Integer passprice;
    @Column(name = "driverprice")
    private Integer driverprice;
    @Column(name = "extprice")
    private Integer extprice;
    @Column(name = "refund")
    private Integer refund;
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
    @Column(name = "stripezegofee")
    private Integer stripezegofee;
    @Column(name = "stripedriverfee")
    private Integer stripedriverfee;
    @Column(name = "zonename")
    private String zonename;
    @Column(name = "zid")
    private Integer zid;
    @Column(name = "freecanceldate")
    private String freecanceldate;
    @Column(name = "zgid")
    private Integer zgid;
    @Column(name = "oldid")
    private Integer oldid;
    @Column(name = "oldpid")
    private Integer oldpid;
        @Column(name = "method")
    private String method;
        @Column(name = "reqlevel")
    private Integer reqlevel;

    @Transient
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    
    @Transient
    private DecimalFormat NF = new DecimalFormat("0.00");
    
    public Exportrequest() {
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getDfname() {
        return dfname;
    }

    public void setDfname(String dfname) {
        this.dfname = dfname;
    }

    public String getDemail() {
        return demail;
    }

    public void setDemail(String demail) {
        this.demail = demail;
    }

    public String getDlname() {
        return dlname;
    }

    public void setDlname(String dlname) {
        this.dlname = dlname;
    }

    public String getRfname() {
        return rfname;
    }

    public void setRfname(String rfname) {
        this.rfname = rfname;
    }

    public String getRemail() {
        return remail;
    }

    public void setRemail(String remail) {
        this.remail = remail;
    }

    public String getRlname() {
        return rlname;
    }

    public void setRlname(String rlname) {
        this.rlname = rlname;
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
        return drivereta;
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

    public Integer getDriverfee() {
        return driverfee == null ? 0 : driverfee;
    }

    public void setDriverfee(Integer driverfee) {
        this.driverfee = driverfee;
    }

    public Integer getZegofee() {
        return zegofee == null ? 0 : zegofee;
    }

    public void setZegofee(Integer zegofee) {
        this.zegofee = zegofee;
    }

    public Integer getPassprice() {
        return passprice;
    }

    public void setPassprice(Integer passprice) {
        this.passprice = passprice;
    }

    public Integer getDriverprice() {
        return driverprice;
    }

    public void setDriverprice(Integer driverprice) {
        this.driverprice = driverprice;
    }

    public Integer getExtprice() {
        return extprice == null ? 0 : extprice;
    }

    public void setExtprice(Integer extprice) {
        this.extprice = extprice;
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

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
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

    public Integer getStripezegofee() {
        return stripezegofee == null ? 0 : stripezegofee;
    }

    public void setStripezegofee(Integer stripezegofee) {
        this.stripezegofee = stripezegofee;
    }

    public Integer getStripedriverfee() {
        return stripedriverfee  == null ? 0 : stripedriverfee;
    }

    public void setStripedriverfee(Integer stripedriverfee) {
        this.stripedriverfee = stripedriverfee;
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
        return freecanceldate;
    }

    public void setFreecanceldate(String freecanceldate) {
        this.freecanceldate = freecanceldate;
    }

    public Integer getZgid() {
        return zgid;
    }

    public void setZgid(Integer zgid) {
        this.zgid = zgid;
    }
    
    public static String header() {
        return "ID Ride;ID Stripe;Ride date;Ride payment date;Ride amount ;Ride amount paid;Promo ;Payment method;Commission ;Driver reimbursement;Pickup address;Dropoff address;Passenger ID;Passenger first ride;Passenger email;Driver ID;Driver first ride;Driver firstname;Driver lastname;Driver email;Driver IBAN;Driver Paypal;Request aborted;Pickup (request);Dropoff (request);Pickup (real);Dropoff (real);Total amount ;Extimate price;Passenger price;Driver total price;Fee lorda zego;Fee lorda driver;Fee discount;Reimb driver discount;Total discount;Fee zego netta;Fee driver netta;Stato ride;Refund;Charge Status;Zego fee incassata;Driver fee incassata\n";
    }
    
    public static String feeheader() {
        return "Short ID;Driver ID;Passenger ID;Data Ride;Data Pagamento Ride;Zego Fee Lorda;Zego Fee Incassata;Payment Method;Status Ride\n";
    }
    
    
    
    public static String promoheader() {
        return "Short ID;Driver ID;Passenger ID;Data Pagamento Ride;Zego Fee Lorda;Discount Zego Fee;Zego Fee Incassata;Driver Fee Lorda;Discount Driver Fee;Driver Fee Incassata;Importo Totale Promo;ID Promo;Payment Method;Ride Paid;Status Ride\n";
    }
    
     public String feedata() {
                 long offs =  TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis())/1000;

        StringBuilder sb = new StringBuilder();
        sb.append(n(shortid)).append(";");
        sb.append(did == null ?"":did).append(";");
        sb.append(pid == null ?"":pid).append(";");
        sb.append(reqdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(reqdate)+offs)))).append(";");
        sb.append(paymentdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(paymentdate)+offs)))).append(";");
        sb.append(f(getZegofee())).append(";");
        sb.append(f(getStripezegofee())).append(";");
        sb.append(method == null || !method.equals("cash") ? "Credit Card" : "Cash").append(";");
        sb.append(status).append(";");
        sb.append("\n");
        return sb.toString();
     }
     
     public String promodata() {
                          long offs =  TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis())/1000;

        StringBuilder sb = new StringBuilder();
        sb.append(n(shortid)).append(";");
        sb.append(did == null ?"":did).append(";");
        sb.append(pid == null ?"":pid).append(";");
        sb.append(paymentdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(paymentdate)+offs)))).append(";");
        sb.append(f(getZegofee())).append(";");
        sb.append(f(getZegofee()-getStripezegofee())).append(";");
        sb.append(f(getStripezegofee())).append(";");
        sb.append(f(getDriverfee())).append(";");
        sb.append(f(getDriverfee()-getStripedriverfee())).append(";");
        sb.append(f(getStripedriverfee())).append(";");
        sb.append(f(getDiscount())).append(";");
        sb.append(promoid == null ?"":promoid).append(";");
        sb.append("Credit Card").append(";");
        sb.append(status.equals(Riderequest.REQUEST_STATUS_PAID) ? "true":"false").append(";");
        sb.append(status).append(";");
        sb.append("\n");
        return sb.toString();
     }
     
        
    public String data() {
        long offs =  TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis())/1000;

        // "ID Ride;ID Stripe;Ride date;Ride payment date;Ride amount ;Ride amount paid;Promo ;Payment method;Commission ; [9]
        // Driver reimbursement;Pickup address;Dropoff address;Passenger ID;Passenger first ride;Passenger email;Driver ID; [16]
        // Driver first ride;Driver firstname;Driver lastname;Driver email;Driver IBAN;Driver Paypal;Request aborted;Pickup (request); [24] 
        // Dropoff (request);Pickup (real);Dropoff (real);Total amount ;Extimate price;Passenger price;Driver total price;Fee lorda zego;
        // Fee lorda driver;Fee discount;Reimb driver discount;Total discount;Fee zego netta;Fee driver netta"
        StringBuilder sb = new StringBuilder();
        sb.append(n(shortid)).append(";");
        sb.append(n(charge)).append(";");
        sb.append(reqdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(reqdate)+offs)))).append(";");
        sb.append(paymentdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(paymentdate)+offs)))).append(";");
        sb.append(f(getDriverfee()+(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? 0 : getStripezegofee()))).append(";");
        sb.append(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? "0" : f(getStripezegofee()+getStripedriverfee())).append(";");
        sb.append(pp(getPromoid())).append(";");
        sb.append(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? "Nonpag" : (method == null || !method.equals("cash")?"Credit Card" : "Cash")).append(";");//getCardbrand()).append(" ").append(getLastdigit()).append(";");
        sb.append(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? "0" : f(getStripezegofee())).append(";");
        
        sb.append(f(getDriverfee())).append(";");
        sb.append(getPuaddr()).append(";");
        sb.append(getDoaddr()).append(";");
        sb.append(getPid()).append(";");
        sb.append("").append(";");
        sb.append(getRemail()).append(";");
        sb.append(getOldid()).append(";");
        
        sb.append("").append(";");
        sb.append(getDfname()).append(";");
        sb.append(getDlname()).append(";");
        sb.append(getDemail()).append(";");
        sb.append(n(getIban())).append(";");
        sb.append("").append(";");
        sb.append("").append(";");
        sb.append(getPuaddr()).append(";");
        
        sb.append(getDoaddr()).append(";");
        sb.append(getRealpuaddr()).append(";");
        sb.append(getRealdoaddr()).append(";");
        sb.append(f(getDriverfee()+getZegofee())).append(";");
        sb.append(f(getExtprice())).append(";");
        sb.append(f(getPassprice())).append(";");
        sb.append(f(getDriverfee()+getZegofee())).append(";");
        sb.append(f(getZegofee())).append(";");
        
        sb.append(f(getDriverfee())).append(";");
        sb.append(f(getZegofee()-getStripezegofee())).append(";");
        sb.append(f(getDriverfee()-getStripedriverfee())).append(";");
        sb.append(f(getDiscount())).append(";");
        sb.append(f(getStripezegofee())).append(";");
        sb.append(f(getStripedriverfee())).append(";");
        sb.append(getStatus()).append(";");
        sb.append(f(getRefund())).append(";");
        sb.append(getChargestatus()).append(";");
        sb.append(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? "0" : f(getStripezegofee())).append(";");
        sb.append(status.equals(Riderequest.REQUEST_PAYMENT_FAILED) ? "0" : f(getStripedriverfee())).append("\n");


        
        return sb.toString();
    }
    
    public String pp (Integer pp) {
        return pp == null || pp == 0 ? "" : String.valueOf(pp);
    }
    public String n(String s) {
        return s == null ? "" : s;
    }

    public String f(Integer d) {
        return (d == null ? "0.00" : NF.format(Math.max(0, d)/100.f)).replace(".", ",");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getRefund() {
        return refund == null ? 0 : refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public Integer getOldid() {
        return oldid;
    }

    public void setOldid(Integer oldid) {
        this.oldid = oldid;
    }

    public Integer getOldpid() {
        return oldpid;
    }

    public void setOldpid(Integer oldpid) {
        this.oldpid = oldpid;
    }
    
    public boolean canBeAdded() {
        if(status > 8) {
            return true;
        } else if (status.equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)) {
            
            if(stripedriverfee == 0 && stripezegofee == 0 && discount > 0) {
                return false;
            }
            
            Integer rd = assigndate == null ? 0  : Integer.parseInt(assigndate);
            Integer ad = abortdate == null ? 0  : Integer.parseInt(abortdate);
            Integer fd = freecanceldate == null ? 0  : Integer.parseInt(freecanceldate);
            
            return stripezegofee > 0 || stripedriverfee > 0 || (ad-rd) > 120 || ad < (fd) || charge != null;
        } else if (status.equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)) {
            
            if(stripedriverfee == 0 && stripezegofee == 0 && discount > 0) {
                return false;
            }
            
            return stripezegofee > 0 || stripedriverfee > 0 || charge != null;
        } else {
            return true;
        }
        
        
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getReqlevel() {
        return reqlevel;
    }

    public void setReqlevel(Integer reqlevel) {
        this.reqlevel = reqlevel;
    }
    
}
