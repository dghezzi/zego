/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "exportcash")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exportcash.findAll", query = "SELECT e FROM Exportcash e"),
     @NamedQuery(name = "Exportcash.findBetween", query = "SELECT e FROM Exportcash e WHERE e.collectiondate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportcash.findById", query = "SELECT e FROM Exportcash e WHERE e.id = :id"),
    @NamedQuery(name = "Exportcash.findByCollectiondate", query = "SELECT e FROM Exportcash e WHERE e.collectiondate = :collectiondate"),
    @NamedQuery(name = "Exportcash.findByDfname", query = "SELECT e FROM Exportcash e WHERE e.dfname = :dfname"),
    @NamedQuery(name = "Exportcash.findByDemail", query = "SELECT e FROM Exportcash e WHERE e.demail = :demail"),
    @NamedQuery(name = "Exportcash.findByDlname", query = "SELECT e FROM Exportcash e WHERE e.dlname = :dlname"),
    @NamedQuery(name = "Exportcash.findByOldid", query = "SELECT e FROM Exportcash e WHERE e.oldid = :oldid"),
    @NamedQuery(name = "Exportcash.findByRfname", query = "SELECT e FROM Exportcash e WHERE e.rfname = :rfname"),
    @NamedQuery(name = "Exportcash.findByRemail", query = "SELECT e FROM Exportcash e WHERE e.remail = :remail"),
    @NamedQuery(name = "Exportcash.findByRlname", query = "SELECT e FROM Exportcash e WHERE e.rlname = :rlname"),
    @NamedQuery(name = "Exportcash.findByOldpid", query = "SELECT e FROM Exportcash e WHERE e.oldpid = :oldpid"),
    @NamedQuery(name = "Exportcash.findByRideid", query = "SELECT e FROM Exportcash e WHERE e.rideid = :rideid"),
    @NamedQuery(name = "Exportcash.findByShortid", query = "SELECT e FROM Exportcash e WHERE e.shortid = :shortid"),
    @NamedQuery(name = "Exportcash.findByIncassato", query = "SELECT e FROM Exportcash e WHERE e.incassato = :incassato"),
    @NamedQuery(name = "Exportcash.findByRimanenza", query = "SELECT e FROM Exportcash e WHERE e.rimanenza = :rimanenza"),
    @NamedQuery(name = "Exportcash.findBySaldozego", query = "SELECT e FROM Exportcash e WHERE e.saldozego = :saldozego"),
    @NamedQuery(name = "Exportcash.findByRef", query = "SELECT e FROM Exportcash e WHERE e.ref = :ref")})
public class Exportcash implements Serializable {
    private static final long serialVersionUID = 1L;
    @Transient
    private DecimalFormat NF = new DecimalFormat("0.00");
   
    @Transient
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Column(name = "collectiondate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectiondate;
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
    @Basic(optional = false)
    @Column(name = "rideid")
    private int rideid;
    @Column(name = "shortid")
    private String shortid;
    @Column(name = "incassato")
    private Integer incassato;
    @Column(name = "rimanenza")
    private Integer rimanenza;
    @Column(name = "saldozego")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldozego;
    @Column(name = "ref")
    private String ref;
    @Column(name = "reftype")
    private String reftype;

    public Exportcash() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCollectiondate() {
        return collectiondate;
    }

    public void setCollectiondate(Date collectiondate) {
        this.collectiondate = collectiondate;
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

    public int getRideid() {
        return rideid;
    }

    public void setRideid(int rideid) {
        this.rideid = rideid;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public Integer getIncassato() {
        return incassato;
    }

    public void setIncassato(Integer incassato) {
        this.incassato = incassato;
    }

    public Integer getRimanenza() {
        return rimanenza;
    }

    public void setRimanenza(Integer rimanenza) {
        this.rimanenza = rimanenza;
    }

    public Date getSaldozego() {
        return saldozego;
    }

    public void setSaldozego(Date saldozego) {
        this.saldozego = saldozego;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

  
    public static String header() {
        return "ID;Data incasso;Nome Driver;Email Driver;Cognome Driver;OLD ID Driver;Nome Passeggero;Email Passeggero;Cognome Passeggero;OLD ID Passeggero;ID Corsa;Short ID Corsa;Incassato;Rimanenza;Data Saldo Zego;Riferimento";
    }

    public String data() {
        long offs =  TimeZone.getTimeZone("Europe/Rome").getOffset(Calendar.getInstance().getTimeInMillis())/1000;
         StringBuilder sb = new StringBuilder();
        
         /*"ID;Data incasso;Nome Driver;Email Driver;Cognome Driver;OLD ID Driver;Nome Passeggero;Email Passeggero;Cognome Passeggero;OLD ID Passeggero;"
                 + "ID Corsa;Short ID Corsa;Incassato;Rimanenza;Data Saldo Zego;Riferimento";
                 */
         
         sb.append(id).append(";");
         sb.append(SDF.format(collectiondate)).append(";");
         sb.append(dfname).append(";");
         sb.append(demail).append(";");
         sb.append(dlname).append(";");
         sb.append(oldid).append(";");
         sb.append(rfname).append(";");
         sb.append(remail).append(";");
         sb.append(rlname).append(";");
         sb.append(oldpid).append(";");
         sb.append(rideid).append(";");
         sb.append(shortid).append(";");
         sb.append(f(incassato)).append(";");
         sb.append(f(rimanenza)).append(";");
         sb.append(saldozego == null ? "": SDF.format(saldozego)).append(";");
         sb.append(n(ref)).append("");
         
         return sb.append("\n").toString();
    }
    
    public String f(Integer d) {
        return (d == null ? "0.00" : NF.format(Math.max(0, d)/100.f)).replace(".", ",");
    }
    
    private String n(String s) {
        return s == null ? "" : s;
    }
    
    private String d(String s) {
        return s == null ? "" : s;
    }

    public String getReftype() {
        return reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }
    
    
}
