/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.RESTDateUtil;
import java.io.Serializable;
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
@Table(name = "promo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promo.findAll", query = "SELECT p FROM Promo p"),
    @NamedQuery(name = "Promo.findById", query = "SELECT p FROM Promo p WHERE p.id = :id"),
    @NamedQuery(name = "Promo.findByCode", query = "SELECT p FROM Promo p WHERE LOWER(p.code) = :code"),
    @NamedQuery(name = "Promo.findByPfx", query = "SELECT p FROM Promo p WHERE LOWER(CONCAT(p.code,' ',p.promotitle)) LIKE :pfx order by p.code asc"),
    @NamedQuery(name = "Promo.findByPromotitle", query = "SELECT p FROM Promo p WHERE p.promotitle = :promotitle"),
    @NamedQuery(name = "Promo.findByPromodesc", query = "SELECT p FROM Promo p WHERE p.promodesc = :promodesc"),
    @NamedQuery(name = "Promo.findByEnablestart", query = "SELECT p FROM Promo p WHERE p.enablestart = :enablestart"),
    @NamedQuery(name = "Promo.findByEnablestop", query = "SELECT p FROM Promo p WHERE p.enablestop = :enablestop"),
    @NamedQuery(name = "Promo.findByValidfrom", query = "SELECT p FROM Promo p WHERE p.validfrom = :validfrom"),
    @NamedQuery(name = "Promo.findByValidto", query = "SELECT p FROM Promo p WHERE p.validto = :validto"),
    @NamedQuery(name = "Promo.findByType", query = "SELECT p FROM Promo p WHERE p.type = :type"),
    @NamedQuery(name = "Promo.findByFeeonly", query = "SELECT p FROM Promo p WHERE p.feeonly = :feeonly"),
    @NamedQuery(name = "Promo.findByMaxusages", query = "SELECT p FROM Promo p WHERE p.maxusages = :maxusages"),
    @NamedQuery(name = "Promo.findByMaxperuser", query = "SELECT p FROM Promo p WHERE p.maxperuser = :maxperuser"),
    @NamedQuery(name = "Promo.findByInsdate", query = "SELECT p FROM Promo p WHERE p.insdate = :insdate"),
    @NamedQuery(name = "Promo.findByModdate", query = "SELECT p FROM Promo p WHERE p.moddate = :moddate"),
    @NamedQuery(name = "Promo.findByValue", query = "SELECT p FROM Promo p WHERE p.value = :value")})
public class Promo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
    private String code;
    @Column(name = "promotitle")
    private String promotitle;
    @Column(name = "promodesc")
    private String promodesc;
    @Column(name = "enablestart")
    private String enablestart;
    @Column(name = "enablestop")
    private String enablestop;
    @Column(name = "validfrom")
    private String validfrom;
    @Column(name = "validto")
    private String validto;
    @Column(name = "type")
    private String type;
    @Column(name = "feeonly")
    private Integer feeonly;
    @Column(name = "firstonly")
    private Integer firstonly;
    @Column(name = "maxusages")
    private Integer maxusages;
    @Column(name = "currentusages")
    private Integer currentusages;
    @Column(name = "maxperuser")
    private Integer maxperuser;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "moddate")
    private String moddate;
    @Column(name = "note")
    private String note;
    @Column(name = "value")
    private Integer value;

    public static final String PERCENT = "percent";
    public static final String FREERIDE = "freeride";
    public static final String WALLET = "wallet";
    public static final String MGM = "mgm";
    public static final String EURO = "euro";
    
    public Promo() {
    }

    public Promo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPromotitle() {
        return promotitle;
    }

    public void setPromotitle(String promotitle) {
        this.promotitle = promotitle;
    }

    public String getPromodesc() {
        return promodesc;
    }

    public void setPromodesc(String promodesc) {
        this.promodesc = promodesc;
    }

    public String getEnablestart() {
        return enablestart;
    }

    public void setEnablestart(String enablestart) {
        this.enablestart = enablestart;
    }

    public String getEnablestop() {
        return enablestop;
    }

    public void setEnablestop(String enablestop) {
        this.enablestop = enablestop;
    }

    public String getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(String validfrom) {
        this.validfrom = validfrom;
    }

    public String getValidto() {
        return validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFeeonly() {
        return feeonly == null ? 0 : feeonly;
    }

    public void setFeeonly(Integer feeonly) {
        this.feeonly = feeonly;
    }

    public Integer getMaxusages() {
        return maxusages;
    }

    public void setMaxusages(Integer maxusages) {
        this.maxusages = maxusages;
    }

    public Integer getMaxperuser() {
        return maxperuser;
    }

    public void setMaxperuser(Integer maxperuser) {
        this.maxperuser = maxperuser;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
        if (!(object instanceof Promo)) {
            return false;
        }
        Promo other = (Promo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Promo[ id=" + id + " ]";
    }
    
    public boolean missingMandatory() {
        return enablestart == null || enablestop == null || code == null || validfrom == null || validto == null || type == null || value == null;
    }
    
    public boolean wrongEnableDates() {
        return Long.parseLong(enablestart) > Long.parseLong(enablestop);
    }
    
    public boolean wrongValidDates() {
        return Long.parseLong(validfrom) > Long.parseLong(validto);
    }
    
    public boolean wrongValue() {
        return value < 0 || ((type.equalsIgnoreCase(PERCENT) || type.equalsIgnoreCase(PERCENT)) && value > 100);
    }

    public Integer getCurrentusages() {
        return currentusages == null ? 0 : currentusages;
    }

    public void setCurrentusages(Integer currentusages) {
        this.currentusages = currentusages;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public boolean isExpired() {
        Long l =  Long.parseLong(RESTDateUtil.getInstance().secondsNow());
        Long stop = Long.parseLong(validto);
        return l > stop;
    }
    
    public boolean isNotUsableYet() {
        Long l =  Long.parseLong(RESTDateUtil.getInstance().secondsNow());
        Long start = Long.parseLong(validfrom);
        return l < start;
    }
    
    public boolean canBeRedeemed() {
        Long l =  Long.parseLong(RESTDateUtil.getInstance().secondsNow());
        Long start = Long.parseLong(enablestart);
        Long stop = Long.parseLong(enablestop);
        return l >= start && l <= stop;
    }

    public Integer getFirstonly() {
        return firstonly == null ? 0 : firstonly;
    }

    public void setFirstonly(Integer firstonly) {
        this.firstonly = firstonly;
    }
    
    
}
