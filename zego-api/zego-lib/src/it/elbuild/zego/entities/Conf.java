/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Cacheable(false)
@Entity
@Table(name = "conf")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conf.findAll", query = "SELECT c FROM Conf c"),
    @NamedQuery(name = "Conf.findById", query = "SELECT c FROM Conf c WHERE c.id = :id"),
    @NamedQuery(name = "Conf.findByPub", query = "SELECT c FROM Conf c WHERE c.pub = :pub"),
    @NamedQuery(name = "Conf.findByVal", query = "SELECT c FROM Conf c WHERE c.val = :val"),
    @NamedQuery(name = "Conf.findByK", query = "SELECT c FROM Conf c WHERE c.k = :k"),
    @NamedQuery(name = "Conf.findByDescr", query = "SELECT c FROM Conf c WHERE c.descr = :descr")})
public class Conf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pub")
    private Integer pub;
    @Column(name = "val")
    private String val;
    @Column(name = "k")
    private String k;
    @Column(name = "descr")
    private String descr;

    public static final String MINIMUM_APP = "minimimum.app";
    public static final String NEXMO_API_KEY = "nexmo.api.key";
    public static final String NEXMO_API_SECRET = "nexmo.api.secret";
    public static final String NEXMO_FROM = "nexmo.from";
    public static final String SEND_SMS = "send.sms";
    public static final String NEXMO_TEMPLATE = "nexmo.sms.prefix";
    public static final String NEXMO_TEMPLATE_INTNL = "nexmo.sms.prefix.intnl";
    public static final String RADIUS_MAX_DISTANCE_METERS = "radius.max.distance.meters";
    public static final String CANDIDATES_INTERLEAVING = "candidates.interleaving";
    public static final String RADIUS_MAX_LOC_AGE_SECONDS = "radius.max.location.age.sec";
    public static final String RADIUS_STRATEGY = "radius.strategy";
    public static final String MAX_CANDIDATES = "max.candidates";
    public static final String PRICING_CENT_PER_KM = "pricing.cent.per.km";
    public static final String PRICING_CENT_PER_MIN = "pricing.cent.per.minute";
    public static final String PRICING_CENT_MINIMUM_FEE = "pricing.minimum.fee";
    public static final String PRICING_CENT_MINIMUM_ZEGOFEE = "pricing.minimum.zegofee";
    public static final String PRICING_CENT_MAXIMUM_FEE = "pricing.maximum.fee";
    public static final String PRICING_ZEGO_COMMISSION = "pricing.zego.commission";
    public static final String PRICING_PREAUTH_CENT = "pricing.preauth.cent";
    public static final String METER_THRESHOLD = "pricing.included.meters";
    public static final String ZEGO_SHORTEST_ROUTE = "zego.shortest.route.meters";
    public static final String ZEGO_LONGEST_ROUTE = "zego.longest.route.meters";
    public static final String ZEGO_PASS_PICKUP_MAX_DISTANCE = "zego.max.displacement.meters";
    public static final String ZEGO_MAX_CASH_DUE = "max.cash.due";
    public static final String STRIPE_KEY = "stripe.key";
    public static final String STRIPE_MAX_RETRIES = "max.stripe.attempts";
    public static final String PIN_EXPIRY_MIN = "sms.pin.expirymin";
    public static final String SMS_ENGINE = "sms.engine";
    public static final String SMS_SKEBBY_USER = "sms.skebby.user";
    public static final String SMS_SKEBBY_PASSWORD = "sms.skebby.password";
    public static final String SMS_SKEBBY_SENDER = "sms.skebby.sender";
    public static final String SMS_SKEBBY_SENDER_INTNL = "sms.skebby.sender.intnl";
    public static final String SMS_NEXMO_SENDER_INTNL = "sms.nexmo.sender.intnl";

    public static final String ENVIRONMENT = "environment";
    public static final String SANDBOX_CERT_PATH = "sandbox.cert.path";
    public static final String PRODUCTION_CERT_PATH = "production.cert.path";
    public static final String SANDBOX_CERT_PASSWORD = "sandbox.cert.pass";
    public static final String PRODUCTION_CERT_PASSWORD = "production.cert.pass";

    public static final String WELCOME_MAIL = "welcome.user.template";
    public static final String DRIVER_APPROVED = "driver.approved.template";
    public static final String DRIVER_DOC_EXPIRED = "driver.docexpired.template";
    public static final String RIDEREQUEST_INVOICE = "riderequest.invoice.template";
    public static final String RIDEREQUEST_UNPAID = "riderequest.unpaid.template";
    public static final String RIDEREQUEST_INVOICE_NOPROMO = "riderequest.invoice.nopromo.template";
    public static final String RIDEREQUEST_INVOICE_CASH = "riderequest.invoice.cash.template";
    public static final String RIDEREQUEST_INVOICE_PINK = "riderequest.invoice.pink.template";
    public static final String RIDEREQUEST_INVOICE_CONTROL = "riderequest.invoice.control.template";
    public static final String RIDEREQUEST_TERMINATED = "riderequest.invoice.terminated.template";
    public static final String RIDEREQUEST_INVOICE_CANCFEE = "riderequest.invoice.cancellationfee.template";
    public static final String MANDRILL_KEY = "mandrill.api.key";

    public static final String WELCOME_MAIL_SUBJECT = "welcome.user.subject";
    public static final String DRIVER_APPROVED_SUBJECT = "driver.approved.subject";
    public static final String DRIVER_DOC_EXPIRED_SUBJECT = "driver.docexpired.subject";
    public static final String RIDEREQUEST_INVOICE_SUBJECT = "riderequest.invoice.subject";
    public static final String RIDEREQUEST_UNPAID_SUBJECT = "riderequest.unpaid.subject";

    public static final String RIDEREQUEST_TERMINATED_SUBJECT = "riderequest.invoice.terminated.subject";
    public static final String RIDEREQUEST_INVOICE_NOPROMO_SUBJECT = "riderequest.invoice.nopromo.subject";
    public static final String RIDEREQUEST_INVOICE_CASH_SUBJECT = "riderequest.invoice.cash.subject";
    public static final String RIDEREQUEST_INVOICE_PINK_SUBJECT = "riderequest.invoice.pink.subject";
    public static final String RIDEREQUEST_INVOICE_CONTROL_SUBJECT = "riderequest.invoice.control.subject";
    public static final String RIDEREQUEST_INVOICE_CANCFEE_SUBJECT = "riderequest.invoice.cancellationfee.subject";

    public static final String PROMO_AWA_EXPIRY_DAYS = "promo.awa.expiry.days";
    public static final String PROMO_AWA_VALUE_CENT = "promo.awa.value.cents";
    public static final String PROMO_AWA_MSG_TITLE = "promo.awa.msg.title";
    public static final String PROMO_AWA_MSG_BODY = "promo.awa.msg.body";

    public static final String PROMO_MGM_EXPIRY_DAYS = "promo.mgm.expiry.days";
    public static final String PROMO_MGM_VALUE_CENT = "promo.mgm.value.cents";
    public static final String PROMO_MGM_MSG_TITLE = "promo.mgm.msg.title";
    public static final String PROMO_MGM_MSG_BODY = "promo.mgm.msg.body";

    public static final String PRODUCTION = "production";
    public static final String SANDBOX = "sandbox";
    public static final String APPID = "it.sharethecity.Letzgo";

    public Conf() {
    }

    public Conf(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getK() {
        return k;
    }

    public void setK(String key) {
        this.k = key;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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
        if (!(object instanceof Conf)) {
            return false;
        }
        Conf other = (Conf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.iface.entities.Conf[ id=" + id + " ]";
    }

    /**
     * @return the pub
     */
    public Integer getPub() {
        return pub;
    }

    /**
     * @param pub the pub to set
     */
    public void setPub(Integer pub) {
        this.pub = pub;
    }

    public Integer asInteger(Integer def) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return def;
        }
    }

    public Double asDouble(Double def) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return def;
        }
    }

}
