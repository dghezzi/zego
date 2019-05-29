/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Table(name = "exportdriver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exportdriver.findAll", query = "SELECT e FROM Exportdriver e"),
    @NamedQuery(name = "Exportdriver.findById", query = "SELECT e FROM Exportdriver e WHERE e.id = :id"),
    @NamedQuery(name = "Exportdriver.findByZgid", query = "SELECT e FROM Exportdriver e WHERE e.zgid = :zgid"),
    @NamedQuery(name = "Exportdriver.findByLname", query = "SELECT e FROM Exportdriver e WHERE e.lname = :lname"),
    @NamedQuery(name = "Exportdriver.findByFname", query = "SELECT e FROM Exportdriver e WHERE e.fname = :fname"),
    @NamedQuery(name = "Exportdriver.findByCf", query = "SELECT e FROM Exportdriver e WHERE e.cf = :cf"),
    @NamedQuery(name = "Exportdriver.findByBirthdate", query = "SELECT e FROM Exportdriver e WHERE e.birthdate = :birthdate"),
    @NamedQuery(name = "Exportdriver.findByBirthcity", query = "SELECT e FROM Exportdriver e WHERE e.birthcity = :birthcity"),
    @NamedQuery(name = "Exportdriver.findByAddress", query = "SELECT e FROM Exportdriver e WHERE e.address = :address"),
    @NamedQuery(name = "Exportdriver.findByCap", query = "SELECT e FROM Exportdriver e WHERE e.cap = :cap"),
    @NamedQuery(name = "Exportdriver.findByCity", query = "SELECT e FROM Exportdriver e WHERE e.city = :city"),
    @NamedQuery(name = "Exportdriver.findByBirthcountry", query = "SELECT e FROM Exportdriver e WHERE e.birthcountry = :birthcountry"),
    @NamedQuery(name = "Exportdriver.findByIban", query = "SELECT e FROM Exportdriver e WHERE e.iban = :iban"),
    @NamedQuery(name = "Exportdriver.findByInsertdate", query = "SELECT e FROM Exportdriver e WHERE e.insertdate = :insertdate"),
    @NamedQuery(name = "Exportdriver.findByEditdate", query = "SELECT e FROM Exportdriver e WHERE e.editdate = :editdate"),
    @NamedQuery(name = "Exportdriver.findByEmail", query = "SELECT e FROM Exportdriver e WHERE e.email = :email")})
public class Exportdriver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "id")
    @Id
    private int id;
    @Column(name = "zgid")
    private Integer zgid;
    @Column(name = "lname")
    private String lname;
    @Column(name = "fname")
    private String fname;
    @Column(name = "cf")
    private String cf;
    @Column(name = "birthdate")
    private String birthdate;
    @Column(name = "birthcity")
    private String birthcity;
    @Column(name = "address")
    private String address;
    @Column(name = "cap")
    private String cap;
    @Column(name = "city")
    private String city;
    @Column(name = "birthcountry")
    private String birthcountry;
    @Column(name = "iban")
    private String iban;
    @Column(name = "insertdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertdate;
    @Column(name = "editdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editdate;
    @Column(name = "email")
    private String email;

    @Transient
    private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Transient
    private DecimalFormat NF = new DecimalFormat("0.00");

    public Exportdriver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getZgid() {
        return zgid;
    }

    public void setZgid(Integer zgid) {
        this.zgid = zgid;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthcity() {
        return birthcity;
    }

    public void setBirthcity(String birthcity) {
        this.birthcity = birthcity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthcountry() {
        return birthcountry;
    }

    public void setBirthcountry(String birthcountry) {
        this.birthcountry = birthcountry;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Date getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(Date insertdate) {
        this.insertdate = insertdate;
    }

    public Date getEditdate() {
        return editdate;
    }

    public void setEditdate(Date editdate) {
        this.editdate = editdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String header() {
        return "ID Driver;Nome;Cognome;Codice fiscale;Data di nascita;Comune di nascita;Indirizzo residenza;"
                + "CAP;Comune residenza;Provincia residenza;Regione residenza;Paese;"
                + "Mail Paypal;IBAN;Metodo di pagamento;Data creazione;Data ultima modifica;Email;NEW ID\n";
    }

    public String data() {
        StringBuilder sb = new StringBuilder();
        sb.append(zgid).append(";");       
        sb.append(n(fname)).append(";");
        sb.append(n(lname)).append(";");
        sb.append(n(cf)).append(";");
        sb.append(n(birthdate)).append(";");
        sb.append(n(birthcity)).append(";");
        sb.append(n(address)).append(";");
        sb.append(n(cap)).append(";");
        sb.append(n(city)).append(";");
        sb.append("-").append(";");
        sb.append("-").append(";");
        sb.append(n(birthcountry)).append(";");
        sb.append("-").append(";");
        sb.append(n(iban)).append(";");
        sb.append("1").append(";");
        sb.append(insertdate).append(";");
        sb.append(editdate).append(";");
        sb.append(email).append(";");
        sb.append(id).append("\n");

        return sb.toString();
    }

    public String n(String s) {
        return s == null ? "" : s;
    }

    public String f(Integer d) {
        return d == null ? "0.00" : NF.format(d / 100.f);
    }
}
