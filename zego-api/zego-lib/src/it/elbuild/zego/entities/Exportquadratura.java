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
@Table(name = "exportquadratura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exportquadratura.findAll", query = "SELECT e FROM Exportquadratura e"),
    @NamedQuery(name = "Exportquadratura.findByRefund", query = "SELECT e FROM Exportquadratura e WHERE e.refund = :refund"),
    @NamedQuery(name = "Exportquadratura.findByPaymentdate", query = "SELECT e FROM Exportquadratura e WHERE e.paymentdate = :paymentdate"),
    @NamedQuery(name = "Exportquadratura.findByDfname", query = "SELECT e FROM Exportquadratura e WHERE e.dfname = :dfname"),
    @NamedQuery(name = "Exportquadratura.findByDemail", query = "SELECT e FROM Exportquadratura e WHERE e.demail = :demail"),
    @NamedQuery(name = "Exportquadratura.findByDlname", query = "SELECT e FROM Exportquadratura e WHERE e.dlname = :dlname"),
    @NamedQuery(name = "Exportquadratura.findByOldid", query = "SELECT e FROM Exportquadratura e WHERE e.oldid = :oldid"),
    @NamedQuery(name = "Exportquadratura.findByRfname", query = "SELECT e FROM Exportquadratura e WHERE e.rfname = :rfname"),
    @NamedQuery(name = "Exportquadratura.findBetween", query = "SELECT e FROM Exportquadratura e WHERE e.reqdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportquadratura.findByRemail", query = "SELECT e FROM Exportquadratura e WHERE e.remail = :remail"),
    @NamedQuery(name = "Exportquadratura.findByRlname", query = "SELECT e FROM Exportquadratura e WHERE e.rlname = :rlname"),
    @NamedQuery(name = "Exportquadratura.findByOldpid", query = "SELECT e FROM Exportquadratura e WHERE e.oldpid = :oldpid"),
    @NamedQuery(name = "Exportquadratura.findById", query = "SELECT e FROM Exportquadratura e WHERE e.id = :id"),
    @NamedQuery(name = "Exportquadratura.findByPid", query = "SELECT e FROM Exportquadratura e WHERE e.pid = :pid"),
    @NamedQuery(name = "Exportquadratura.findByDid", query = "SELECT e FROM Exportquadratura e WHERE e.did = :did"),
    @NamedQuery(name = "Exportquadratura.findByStatus", query = "SELECT e FROM Exportquadratura e WHERE e.status = :status"),
    @NamedQuery(name = "Exportquadratura.findByPulat", query = "SELECT e FROM Exportquadratura e WHERE e.pulat = :pulat"),
    @NamedQuery(name = "Exportquadratura.findByPulng", query = "SELECT e FROM Exportquadratura e WHERE e.pulng = :pulng"),
    @NamedQuery(name = "Exportquadratura.findByPuaddr", query = "SELECT e FROM Exportquadratura e WHERE e.puaddr = :puaddr"),
    @NamedQuery(name = "Exportquadratura.findByDolat", query = "SELECT e FROM Exportquadratura e WHERE e.dolat = :dolat"),
    @NamedQuery(name = "Exportquadratura.findByDolng", query = "SELECT e FROM Exportquadratura e WHERE e.dolng = :dolng"),
    @NamedQuery(name = "Exportquadratura.findByDoaddr", query = "SELECT e FROM Exportquadratura e WHERE e.doaddr = :doaddr"),
    @NamedQuery(name = "Exportquadratura.findByReqdate", query = "SELECT e FROM Exportquadratura e WHERE e.reqdate = :reqdate"),
    @NamedQuery(name = "Exportquadratura.findByAssigndate", query = "SELECT e FROM Exportquadratura e WHERE e.assigndate = :assigndate"),
    @NamedQuery(name = "Exportquadratura.findByCanceldate", query = "SELECT e FROM Exportquadratura e WHERE e.canceldate = :canceldate"),
    @NamedQuery(name = "Exportquadratura.findByAbortdate", query = "SELECT e FROM Exportquadratura e WHERE e.abortdate = :abortdate"),
    @NamedQuery(name = "Exportquadratura.findByStartdate", query = "SELECT e FROM Exportquadratura e WHERE e.startdate = :startdate"),
    @NamedQuery(name = "Exportquadratura.findByEnddate", query = "SELECT e FROM Exportquadratura e WHERE e.enddate = :enddate"),
    @NamedQuery(name = "Exportquadratura.findByExtmeters", query = "SELECT e FROM Exportquadratura e WHERE e.extmeters = :extmeters"),
    @NamedQuery(name = "Exportquadratura.findByExtsecond", query = "SELECT e FROM Exportquadratura e WHERE e.extsecond = :extsecond"),
    @NamedQuery(name = "Exportquadratura.findByExtshort", query = "SELECT e FROM Exportquadratura e WHERE e.extshort = :extshort"),
    @NamedQuery(name = "Exportquadratura.findByDrivereta", query = "SELECT e FROM Exportquadratura e WHERE e.drivereta = :drivereta"),
    @NamedQuery(name = "Exportquadratura.findByRealpulat", query = "SELECT e FROM Exportquadratura e WHERE e.realpulat = :realpulat"),
    @NamedQuery(name = "Exportquadratura.findByRealpulng", query = "SELECT e FROM Exportquadratura e WHERE e.realpulng = :realpulng"),
    @NamedQuery(name = "Exportquadratura.findByRealpuaddr", query = "SELECT e FROM Exportquadratura e WHERE e.realpuaddr = :realpuaddr"),
    @NamedQuery(name = "Exportquadratura.findByRealdolat", query = "SELECT e FROM Exportquadratura e WHERE e.realdolat = :realdolat"),
    @NamedQuery(name = "Exportquadratura.findByRealdolng", query = "SELECT e FROM Exportquadratura e WHERE e.realdolng = :realdolng"),
    @NamedQuery(name = "Exportquadratura.findByRealdoaddr", query = "SELECT e FROM Exportquadratura e WHERE e.realdoaddr = :realdoaddr"),
    @NamedQuery(name = "Exportquadratura.findByDriverfee", query = "SELECT e FROM Exportquadratura e WHERE e.driverfee = :driverfee"),
    @NamedQuery(name = "Exportquadratura.findByZegofee", query = "SELECT e FROM Exportquadratura e WHERE e.zegofee = :zegofee"),
    @NamedQuery(name = "Exportquadratura.findByPassprice", query = "SELECT e FROM Exportquadratura e WHERE e.passprice = :passprice"),
    @NamedQuery(name = "Exportquadratura.findByDriverprice", query = "SELECT e FROM Exportquadratura e WHERE e.driverprice = :driverprice"),
    @NamedQuery(name = "Exportquadratura.findByExtprice", query = "SELECT e FROM Exportquadratura e WHERE e.extprice = :extprice"),
    @NamedQuery(name = "Exportquadratura.findByNumpass", query = "SELECT e FROM Exportquadratura e WHERE e.numpass = :numpass"),
    @NamedQuery(name = "Exportquadratura.findByOptions", query = "SELECT e FROM Exportquadratura e WHERE e.options = :options"),
    @NamedQuery(name = "Exportquadratura.findByPassrating", query = "SELECT e FROM Exportquadratura e WHERE e.passrating = :passrating"),
    @NamedQuery(name = "Exportquadratura.findByDrivrating", query = "SELECT e FROM Exportquadratura e WHERE e.drivrating = :drivrating"),
    @NamedQuery(name = "Exportquadratura.findByDiscount", query = "SELECT e FROM Exportquadratura e WHERE e.discount = :discount"),
    @NamedQuery(name = "Exportquadratura.findByPromoid", query = "SELECT e FROM Exportquadratura e WHERE e.promoid = :promoid"),
    @NamedQuery(name = "Exportquadratura.findByShid", query = "SELECT e FROM Exportquadratura e WHERE e.shid = :shid"),
    @NamedQuery(name = "Exportquadratura.findByAbortreason", query = "SELECT e FROM Exportquadratura e WHERE e.abortreason = :abortreason"),
    @NamedQuery(name = "Exportquadratura.findByCancelreason", query = "SELECT e FROM Exportquadratura e WHERE e.cancelreason = :cancelreason"),
    @NamedQuery(name = "Exportquadratura.findByShortid", query = "SELECT e FROM Exportquadratura e WHERE e.shortid = :shortid"),
    @NamedQuery(name = "Exportquadratura.findByCharge", query = "SELECT e FROM Exportquadratura e WHERE e.charge = :charge"),
    @NamedQuery(name = "Exportquadratura.findByChargestatus", query = "SELECT e FROM Exportquadratura e WHERE e.chargestatus = :chargestatus"),
    @NamedQuery(name = "Exportquadratura.findByChargeerror", query = "SELECT e FROM Exportquadratura e WHERE e.chargeerror = :chargeerror"),
    @NamedQuery(name = "Exportquadratura.findByStripezegofee", query = "SELECT e FROM Exportquadratura e WHERE e.stripezegofee = :stripezegofee"),
    @NamedQuery(name = "Exportquadratura.findByStripedriverfee", query = "SELECT e FROM Exportquadratura e WHERE e.stripedriverfee = :stripedriverfee"),
    @NamedQuery(name = "Exportquadratura.findByZonename", query = "SELECT e FROM Exportquadratura e WHERE e.zonename = :zonename"),
    @NamedQuery(name = "Exportquadratura.findByZid", query = "SELECT e FROM Exportquadratura e WHERE e.zid = :zid"),
    @NamedQuery(name = "Exportquadratura.findByFreecanceldate", query = "SELECT e FROM Exportquadratura e WHERE e.freecanceldate = :freecanceldate"),
    @NamedQuery(name = "Exportquadratura.findByZgid", query = "SELECT e FROM Exportquadratura e WHERE e.zgid = :zgid")})
public class Exportquadratura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "refund")
    private Integer refund;
    @Column(name = "paymentdate")
    private String paymentdate;
    @Column(name = "dfname")
    private String dfname;
    @Column(name = "demail")
    private String demail;
    @Column(name = "dlname")
    private String dlname;
    @Column(name = "oldid")
    private Integer oldid;
    @Column(name = "rfname")
    private String rfname;
    @Column(name = "remail")
    private String remail;
    @Column(name = "rlname")
    private String rlname;
    @Column(name = "oldpid")
    private Integer oldpid;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
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

    @Transient
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    
    @Transient
    private DecimalFormat NF = new DecimalFormat("0.00");
    public Exportquadratura() {
    }

    public static String header() {
        return "ID Ride;ID Stripe;ID driver;Nome Driver;Cognome Driver;ID Rider;Nome Rider;Cognome Rider;Data Ride;Data Pagamento;Ride Amount;Promo ID;Importo Promo;Importo Zego Fee Incassato;Importo Driver Fee Incasato;Importo Totale Incasato su Stripe;Importo Rimborso Driver;Payment Method;Ride Paid;Status\n";
    }
    
    public String data() {
        //"ID Ride;ID Stripe;ID driver;Nome Driver;Cognome Driver;ID Rider;Nome Rider;Cognome Rider;Data Ride;Data Pagamento;Ride Amount;
        //Promo ID;Importo Promo;Importo Zego Fee Incasato;Importo Driver Fee Incasato;Importo Totale Incasato su Stripe;Importo Rimborso Driver;//
        //Payment Method;Ride Paid;Status\n"
        StringBuilder sb = new StringBuilder();
        sb.append(n(shortid)).append(";");
        sb.append(n(charge)).append(";");
        sb.append(did == null ? "" : did).append(";");
        sb.append(getDfname()).append(";");
        sb.append(getDlname()).append(";");
        sb.append(pid == 0 ? "" : pid).append(";");
        sb.append(getRfname()).append(";");
        sb.append(getRlname()).append(";");
        sb.append(reqdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(reqdate)+3600)))).append(";");
        sb.append(paymentdate == null ? "" : SDF.format(RESTDateUtil.getInstance().formatDateSeconds(String.valueOf(Integer.valueOf(paymentdate)+3600)))).append(";");
        sb.append(promoid == null ? "" : promoid).append(";");
        sb.append(f(getDiscount())).append(";");
        sb.append(f(getStripezegofee())).append(";");
        sb.append(f(getStripedriverfee())).append(";");
        sb.append(f(getStripezegofee())).append(";");
        sb.append(f(getDriverfee()+getStripezegofee())).append(";");
        sb.append(f(getDriverfee())).append(";");
        sb.append((status.equals(10) || ((status.equals(8) || status.equals(7)) && charge != null)) ? "Credit Card" : "NonPag").append(";");
        sb.append((status.equals(10) || ((status.equals(8) || status.equals(7)) && charge != null)) ? "true" : "false").append(";");
        sb.append(getStatus()).append("\n");
        
        
        return sb.toString();
    }
    
    public String pp (Integer pp) {
        return pp == null || pp == 0 ? "" : String.valueOf(pp);
    }
    public String n(String s) {
        return s == null ? "" : s;
    }

    public String f(Integer d) {
        return d == null ? "0.00" : NF.format(Math.max(0, d)/100.f);
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

    public String getDfname() {
        return dfname == null ? "" : dfname;
    }

    public void setDfname(String dfname) {
        this.dfname = dfname;
    }

    public String getDemail() {
        return demail == null ? "" : demail;
    }

    public void setDemail(String demail) {
        this.demail = demail;
    }

    public String getDlname() {
        return dlname == null ? "" : dlname;
    }

    public void setDlname(String dlname) {
        this.dlname = dlname;
    }

    public Integer getOldid() {
        return oldid;
    }

    public void setOldid(Integer oldid) {
        this.oldid = oldid;
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

    public Integer getOldpid() {
        return oldpid;
    }

    public void setOldpid(Integer oldpid) {
        this.oldpid = oldpid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDid() {
        return did == null ? 0 : did;
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
        return zegofee;
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
        return extprice;
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
        return stripezegofee;
    }

    public void setStripezegofee(Integer stripezegofee) {
        this.stripezegofee = stripezegofee;
    }

    public Integer getStripedriverfee() {
        return stripedriverfee == null ? 0 : stripedriverfee;
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
    
}
