/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Refund;
import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Fraud;
import it.elbuild.zego.entities.Payment;
import it.elbuild.zego.entities.Paymentaction;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.AuthController;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.StripeActionRequest;
import it.elbuild.zego.rest.request.StripeCreateCustomerRequest;
import it.elbuild.zego.rest.response.StripeCard;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class StripeControllerBean implements StripeInterface {

    @EJB
    ConfController confCtrl;

    @EJB
    MailSender mail;

    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Userpromo> promoCtrl;

    EntityController<Integer, Promo> promoPromoCtrl;

    EntityController<Integer, Riderequest> reqCtrl;

    EntityController<Integer, Payment> paymentCtrl;

    EntityController<Integer, Paymentaction> payActCtrl;

    EntityController<Integer, Fraud> fraudCtrl;

    private final Integer[] WAITS = {1, 3, 5, 8, 15, 22, 36, 66, 100};

    @EJB
    AuthController auth;

    private Integer preathCent;

    private SimpleDateFormat SDF;

    private Gson g;

    @PostConstruct
    private void init() {
        g = new Gson();
        SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        userCtrl = provider.provide(User.class);
        paymentCtrl = provider.provide(Payment.class);
        promoCtrl = provider.provide(Userpromo.class);
        reqCtrl = provider.provide(Riderequest.class);
        fraudCtrl = provider.provide(Fraud.class);
        payActCtrl = provider.provide(Paymentaction.class);
        promoPromoCtrl = provider.provide(Promo.class);
        preathCent = confCtrl.getConfByKey(Conf.PRICING_PREAUTH_CENT).asInteger(100);
        Stripe.apiKey = confCtrl.getConfByKey(Conf.STRIPE_KEY).getVal();
    }

    @Override
    public User createStripeUser(StripeCreateCustomerRequest req) throws RESTException {
        try {
            User u = userCtrl.findById(req.getUid());

            List<Fraud> userFraud = fraudCtrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid", u.getId())));

            if (userFraud.size() >= 5) {
                u.setStatus(User.STATUS_BANNED);
                u.setBanreason("Massimo numero di FRAUD raggiunto.");
                auth.erase(u.getZegotoken());
                userCtrl.save(u);
                throw new RESTException(ZegoK.Error.TOO_MANY_CARD_ERROR);
            }
            if (u.getStripeid() != null) {
                Customer customer = Customer.retrieve(u.getStripeid());
                Map<String, Object> params = new HashMap<>();
                params.put("source", req.getToken());

                ExternalAccount ea = customer.getSources().create(params);
                Card newcard = (Card) ea;

                if (isCardBillable(u, preathCent, "eur", u.getStripeid(), u.getEmail(), newcard.getId())) {

                    Customer c = Customer.retrieve(u.getStripeid());
                    Map<String, Object> updateParams = new HashMap<>();
                    updateParams.put("default_source", newcard.getId());
                    c.update(updateParams);

                    if (u.getDebt() > 0) {
                        if (paydebt(u, false)) {
                            u.setPayok(1);
                            u.setDebt(0);
                        } else {
                            u.setPayok(0);
                        }
                    } else {
                        u.setPayok(1);
                    }

                    u = userCtrl.save(u);
                    return u;
                } else {
                    throw new RESTException(ZegoK.Error.STRIPE_CARD_ERROR);
                }

            } else {

                Map<String, Object> customerParams = new HashMap<String, Object>();
                customerParams.put("source", req.getToken());
                customerParams.put("email", u.getEmail());
                customerParams.put("description", u.getId() + " - " + u.getFname() + " " + u.getLname());
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Passenger ID", String.valueOf(u.getId()));
                metadata.put("Name", u.getFname() + " " + u.getLname());
                metadata.put("Mobile", u.getPrefix() + " " + u.getMobile());
                customerParams.put("metadata", metadata);
                com.stripe.model.Customer customer = com.stripe.model.Customer.create(customerParams);

                if (customer.getId() != null) {
                    u.setStripeid(customer.getId());
                    u = userCtrl.save(u);

                    if (isCardBillable(u, preathCent, "eur", u.getStripeid(), u.getEmail(), null)) {
                        return u;
                    } else {
                        throw new RESTException(ZegoK.Error.STRIPE_CARD_ERROR);
                    }
                } else {
                    throw new RESTException(ZegoK.Error.STRIPE_PROCESSING_ERROT);
                }
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            if (ex instanceof CardException) {
                Fraud f = new Fraud();
                f.setFrauddate(RESTDateUtil.getInstance().secondsNow());
                f.setStripedata(ex.getRequestId() + " " + ex.getMessage());
                f.setUid(req.getUid());
                fraudCtrl.save(f);
                throw new RESTException(ZegoK.Error.STRIPE_CARD_ERROR);
            } else {
                throw new RESTException(ZegoK.Error.STRIPE_PROCESSING_ERROT);
            }
        }

    }

    private boolean paydebt(User u, boolean fromScheduler) {
        if (u.getDebt() > 0) {
            Integer failctr = fromScheduler ? confCtrl.getConfByKey(Conf.STRIPE_MAX_RETRIES).asInteger(8) : 2000; // 2000 = 5 yrs... MORE than enough for zego
            Riderequest un = null;
            List<Riderequest> unpaids = reqCtrl.findListCustom("findUnpaidForUid", Arrays.asList(new DBTuple("pid", u.getId()), new DBTuple("failctr", failctr)));
            if (unpaids != null && !unpaids.isEmpty()) {
                un = unpaids.get(0);
            } else {
                // give up, reset the user debt and ban him?
                return false;
            }

            // if from scheduler determine if it needs to be retried
            Integer minwait = WAITS[Math.min(un.getFailctr(), WAITS.length - 1)];
            
            Integer daysDelta = (Integer.parseInt(RESTDateUtil.getInstance().secondsNow()) - Integer.parseInt(un.getReqdate())) / (3600 * 24); // very inaccurate but good enough

            if (!fromScheduler || (daysDelta > minwait)) {
                un.setFailctr(un.getFailctr()+1);
                un = reqCtrl.save(un);
                Map<String, Object> otherChargeParams = new HashMap<>();
                otherChargeParams.put("amount", u.getDebt());
                otherChargeParams.put("currency", "eur");
                otherChargeParams.put("capture", "true");
                otherChargeParams.put("description", "Previously unpaid rides payment");
                otherChargeParams.put("customer", u.getStripeid());
                if (un != null) {
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("Passenger ID", String.valueOf(un.getPid()));
                    metadata.put("Passenger Name", u.getFname() + " " + u.getLname());
                    if (un.getDid() != null) {
                        User dr = userCtrl.findById(un.getDid());
                        metadata.put("Driver ID", String.valueOf(un.getDid()));
                        metadata.put("Driver Name", dr.getFname() + " " + dr.getLname());
                    }
                    metadata.put("Ride ID", un.getShortid());
                    metadata.put("Ride Date", SDF.format(RESTDateUtil.getInstance().formatDateSeconds(un.getReqdate())));
                    metadata.put("Tipo addebito", "Ride payment Collection");
                    otherChargeParams.put("metadata", metadata);

                    try {
                        Charge c = Charge.create(otherChargeParams);
                        String st = c.getStatus();
                        Boolean b = c.getPaid();
                        if (b) {
                            un.setStatus(Riderequest.REQUEST_STATUS_PAID);
                            un.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
                            reqCtrl.save(un);
                        }
                        return b;
                    } catch (AuthenticationException ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (InvalidRequestException ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (APIConnectionException ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (CardException ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (APIException ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (Exception ex) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isCardBillable(User u, Integer centAmount, String currency, String stripeId, String email, String source) {
        // return true;

        Customer customer = null;

        Map<String, Object> otherChargeParams = new HashMap<>();
        otherChargeParams.put("amount", centAmount);
        otherChargeParams.put("currency", currency);
        otherChargeParams.put("capture", "false");
        otherChargeParams.put("description", "Test charge authorization for user " + email);
        otherChargeParams.put("customer", stripeId); // Previously stored, then retrieved
        if (source != null) {
            otherChargeParams.put("source", source);
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Passenger ID", String.valueOf(u.getId()));
        metadata.put("Nome", u.getFname() + " " + u.getLname());
        metadata.put("Tipo addebito", "Credit Card verification");
        otherChargeParams.put("metadata", metadata);
        try {
            customer = Customer.retrieve(stripeId);
            Charge c = Charge.create(otherChargeParams);
            String st = c.getStatus();
            Boolean b = c.getPaid();

            try {
                Map<String, Object> refundParams = new HashMap<>();
                refundParams.put("charge", c.getId());
                Refund r = Refund.create(refundParams);
            } catch (Exception e) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, e);
            }

            if (!b && source != null) {
                if (customer != null) {
                    ExternalAccount cc;
                    try {
                        cc = customer.getSources().retrieve(source);
                        if (cc != null) {
                            cc.delete();
                        }
                    } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex1) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex1);
                    }

                }

            }

            return b;
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            if (source != null) {

                if (customer != null) {
                    ExternalAccount cc;
                    try {
                        cc = customer.getSources().retrieve(source);
                        if (cc != null) {
                            cc.delete();
                        }
                    } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex1) {
                        Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex1);
                    }

                }

            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            if (customer != null) {
                ExternalAccount cc;
                try {
                    cc = customer.getSources().retrieve(source);
                    if (cc != null) {
                        cc.delete();
                    }
                } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex1) {
                    Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
            return false;
        }

    }

    @Override
    public List<StripeCard> userCards(Integer uid) throws RESTException {
        User us = userCtrl.findById(uid);
        if (us == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            try {
                if (us.getPayok() == 0 || us.getStripeid() == null) {
                    return new ArrayList<>();
                } else {
                    List<StripeCard> lc = new ArrayList<>();
                    Customer c = Customer.retrieve(us.getStripeid());
                    for (ExternalAccount ea : c.getSources().getData()) {
                        Card cc = (Card) ea;

                        Integer expm = cc.getExpMonth();
                        Integer expy = cc.getExpYear();
                        Integer curry = Calendar.getInstance(TimeZone.getTimeZone("CET")).get(Calendar.YEAR);
                        Integer currm = 1 + Calendar.getInstance(TimeZone.getTimeZone("CET")).get(Calendar.MONTH);
                        if (expy > curry || (expy.equals(curry) && currm <= expm)) {
                            lc.add(new StripeCard((Card) ea, c.getDefaultSource()));
                        }
                    }
                    return lc;
                }

            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<StripeCard> chageDefaultCard(Integer uid, StripeCard card) throws RESTException {
        User us = userCtrl.findById(uid);
        if (us == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            try {
                if (us.getPayok() == 0 || us.getStripeid() == null) {
                    return new ArrayList<>();
                } else {
                    List<StripeCard> lc = new ArrayList<>();
                    Customer c = Customer.retrieve(us.getStripeid());
                    Map<String, Object> updateParams = new HashMap<>();
                    updateParams.put("default_source", card.getCard());
                    c.update(updateParams);

                    // re-read
                    c = Customer.retrieve(us.getStripeid());
                    for (ExternalAccount ea : c.getSources().getData()) {
                        lc.add(new StripeCard((Card) ea, c.getDefaultSource()));
                    }

                    if (us.getDebt() > 0) {
                        if (paydebt(us, false)) {
                            us.setPayok(1);
                            us.setDebt(0);
                            userCtrl.save(us);
                        }
                    }
                    return lc;
                }

            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<StripeCard> deleteCard(Integer uid, StripeCard card) throws RESTException {
        User us = userCtrl.findById(uid);
        if (us == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            try {
                if (us.getPayok() == 0 || us.getStripeid() == null) {
                    return new ArrayList<>();
                } else {
                    List<StripeCard> lc = new ArrayList<>();
                    Customer c = Customer.retrieve(us.getStripeid());
                    c.getSources().retrieve(card.getCard()).delete();

                    // re-read
                    c = Customer.retrieve(us.getStripeid());
                    for (ExternalAccount ea : c.getSources().getData()) {
                        lc.add(new StripeCard((Card) ea, c.getDefaultSource()));
                    }

                    if (c.getSources().getData() == null || c.getSources().getData().isEmpty()) {
                        us.setPayok(0);
                        userCtrl.save(us);
                    }

                    return lc;
                }

            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Riderequest preauthorize(Riderequest req) throws RESTException {
        User u = userCtrl.findById(req.getPid());
        boolean zerotrans = (req.getStripedriverfee() + req.getStripezegofee()) <= 49;
        Map<String, Object> otherChargeParams = new HashMap<>();
        //otherChargeParams.put("amount", req.getDriverfee() + req.getZegofee() - req.getDiscount());
        otherChargeParams.put("amount", req.getStripedriverfee() + req.getStripezegofee());
        otherChargeParams.put("currency", "eur");
        otherChargeParams.put("capture", "false");
        otherChargeParams.put("description", "Preautorizzazione per ride " + req.getShortid());
        otherChargeParams.put("customer", u.getStripeid());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Passenger ID", String.valueOf(req.getPid()));
        metadata.put("Ride ID", req.getShortid());
        metadata.put("Tipo addebito", "Ride credit check");
        otherChargeParams.put("metadata", metadata);
        try {
            Boolean b = false;
            Charge c = null;
            if (!zerotrans) {
                c = Charge.create(otherChargeParams);
                String st = c.getStatus();
                b = c.getPaid();
                req.setCharge(c.getId());
                req.setChargestatus(b ? Riderequest.REQUEST_CHARGE_STATUS_AUTHORIZED : Riderequest.REQUEST_CHARGE_STATUS_FAILED);
                req.setChargeerror(st);
            } else {
                b = true;
                req.setCharge("ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId());
                req.setChargestatus(b ? Riderequest.REQUEST_CHARGE_STATUS_AUTHORIZED : Riderequest.REQUEST_CHARGE_STATUS_FAILED);
                req.setChargeerror("");
            }

            Userpromo promo = null;
            if (req.getPromoid() != null) {
                promo = promoCtrl.findById(req.getPromoid());
            }

            Customer cu = Customer.retrieve(u.getStripeid());
            String last4 = null;
            String brand = null;
            for (ExternalAccount ea : cu.getSources().getData()) {
                if (((Card) ea).getId().equalsIgnoreCase(cu.getDefaultSource())) {
                    last4 = ((Card) ea).getLast4();
                    brand = ((Card) ea).getBrand();
                }
            }

            if (b) {
                Payment p = new Payment();
                p.setAmount(c == null ? 0 : c.getAmount().intValue());
                p.setCardbrand(brand);
                p.setCurrency("eur");
                p.setDid(req.getDid());
                p.setDiscount(req.getDiscount());
                p.setDriveraccount(null);
                p.setDriverfee(req.getDriverfee());
                p.setLastdigit(last4);
                p.setMode(Payment.MODE_AUTO);
                p.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
                req.setPaymentdate(p.getPaymentdate());
                p.setRid(req.getId());
                p.setStripecharge(c == null ? "ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId() : c.getId());
                p.setStripeid(u.getStripeid());
                p.setStriperror(null);
                p.setTransactionid(null);
                p.setType(Payment.PAYMENT_TYPE_PREAUTH);
                p.setUid(req.getPid());
                if (promo != null && promo.getPromo() != null) {
                    p.setUpromocode(promo.getPromo().getCode());
                    p.setUpromoid(promo.getId());
                }

                p.setZegofee(req.getZegofee());
                p.setStatus(Payment.PAYMENT_STATUS_CAPT);
                p.setRefund(0);

                p = paymentCtrl.save(p);

                Paymentaction pa = new Paymentaction();
                pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                pa.setActor("system");
                pa.setActiontype("preauth");
                pa.setCapture(c == null ? 0 : c.getAmount().intValue());
                pa.setChargeid(c == null ? "ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId() : c.getId());
                pa.setPid(p.getId());
                pa.setRefund(0);
                payActCtrl.save(pa);
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            req.setCharge(null);
            req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_FAILED);
            if (ex instanceof StripeException) {
                req.setChargeerror(g.toJson(ex));
            } else {
                req.setChargeerror(ex.getMessage());
            }

        }

        return req;

    }

    @Override
    public Riderequest capture(Riderequest req, boolean penalty) throws RESTException {

        if (req.getCharge() == null) {
            return captureWithoutPreviousCharge(req, penalty);
        } else {
            if (req.getChargestatus().equalsIgnoreCase(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED)) {
                return req;
            } else {
                return captureWithPreviousCharge(req, penalty);
            }
        }
    }

    private Riderequest captureWithoutPreviousCharge(Riderequest req, boolean penalty) {
        User u = userCtrl.findById(req.getPid());
        Userpromo promo = null;
        boolean zerotrans = !penalty && (req.getStripedriverfee() + req.getStripezegofee()) <= 49;
        Map<String, Object> otherChargeParams = new HashMap<>();
        Integer amount = penalty ? 0 : (req.getStripedriverfee() + req.getStripezegofee()); // this variable is used only to increase the debt
        //otherChargeParams.put("amount", req.getDriverfee() + req.getZegofee() - req.getDiscount());
        otherChargeParams.put("amount", penalty ? req.getZegofee() : (req.getStripedriverfee() + req.getStripezegofee()));
        otherChargeParams.put("currency", "eur");
        otherChargeParams.put("capture", "true");
        otherChargeParams.put("description", "Pagamento per ride " + req.getShortid());
        otherChargeParams.put("customer", u.getStripeid());

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Passenger ID", String.valueOf(req.getPid()));
        metadata.put("Passenger Name", u.getFname() + " " + u.getLname());
        if (req.getDid() != null) {
            User dr = userCtrl.findById(req.getDid());
            metadata.put("Driver ID", String.valueOf(req.getDid()));
            metadata.put("Driver Name", dr.getFname() + " " + dr.getLname());
        }
        metadata.put("Ride ID", req.getShortid());
        metadata.put("Ride Date", SDF.format(RESTDateUtil.getInstance().formatDateSeconds(req.getReqdate())));
        metadata.put("Tipo addebito", penalty ? "Penale cancellazione" : "Ride payment");
        otherChargeParams.put("metadata", metadata);
        try {
            if (req.getPromoid() != null) {
                promo = promoCtrl.findById(req.getPromoid());
            }
            Boolean b = false;
            Charge c = null;
            if (!zerotrans) {
                c = Charge.create(otherChargeParams);
                String st = c.getStatus();
                b = c.getPaid();
                req.setCharge(c.getId());
                req.setChargestatus(b ? Riderequest.REQUEST_CHARGE_STATUS_CAPTURED : Riderequest.REQUEST_CHARGE_STATUS_FAILED);
                req.setChargeerror(st);
            } else {
                b = true;
                req.setCharge("ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId());
                req.setChargestatus(b ? Riderequest.REQUEST_CHARGE_STATUS_CAPTURED : Riderequest.REQUEST_CHARGE_STATUS_FAILED);
                req.setChargeerror("");
            }

            Customer cu = Customer.retrieve(u.getStripeid());
            String last4 = null;
            String brand = null;
            for (ExternalAccount ea : cu.getSources().getData()) {
                if (((Card) ea).getId().equalsIgnoreCase(cu.getDefaultSource())) {
                    last4 = ((Card) ea).getLast4();
                    brand = ((Card) ea).getBrand();
                }
            }

            if (b) {
                Payment p = new Payment();
                p.setAmount(c == null ? 0 : c.getAmount().intValue());
                p.setCardbrand(brand);
                p.setCurrency("eur");
                p.setDid(req.getDid());
                p.setDiscount(req.getDiscount());
                p.setDriveraccount(null);
                p.setDriverfee(req.getDriverfee());
                p.setLastdigit(last4);
                p.setMode(Payment.MODE_AUTO);
                p.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
                req.setPaymentdate(p.getPaymentdate());
                p.setRid(req.getId());
                p.setStripecharge(c == null ? "ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId() : c.getId());
                p.setStripeid(u.getStripeid());
                p.setStriperror(null);
                p.setTransactionid(null);
                p.setType(Payment.PAYMENT_TYPE_PREAUTH);
                p.setUid(req.getPid());
                if (promo != null && promo.getPromo() != null) {
                    p.setUpromocode(promo.getPromo().getCode());
                    p.setUpromoid(promo.getId());
                }

                p.setZegofee(req.getZegofee());
                p.setStatus(Payment.PAYMENT_STATUS_CAPT);
                p.setRefund(0);

                p = paymentCtrl.save(p);

                Paymentaction pa = new Paymentaction();
                pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                pa.setActor("system");
                pa.setActiontype("capture");
                pa.setCapture(c == null ? 0 : c.getAmount().intValue());
                pa.setChargeid(c == null ? "ZERO_EURO_TRANSACTION_NO_STRIPE_" + req.getId() : c.getId());
                pa.setPid(p.getId());
                pa.setRefund(0);
                payActCtrl.save(pa);

                try {

                    if (!req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)) {
                        User driver = null;
                        if (req.getDid() != null) {
                            driver = userCtrl.findById(req.getDid());
                        }

                        HashMap<String, String> maps = new HashMap<>();
                        maps.putAll(MandrillMapper.param(u, "rider"));
                        if (driver != null) {
                            maps.putAll(MandrillMapper.param(driver, "driver"));
                        }
                        maps.putAll(MandrillMapper.param(req, "request"));
                        maps.putAll(req.mandrill());
                        if (penalty) {
                            mail.sendMail(u, Conf.RIDEREQUEST_INVOICE_CANCFEE, maps);
                        } else if (promo != null) {
                            maps.putAll(MandrillMapper.param(promo, "promo"));
                            mail.sendMail(u, Conf.RIDEREQUEST_INVOICE, maps);
                        } else {
                            String tem = Conf.RIDEREQUEST_INVOICE_NOPROMO;
                            if (req.getReqlevel() != null) {
                                if (req.getReqlevel().equals(Riderequest.REQUEST_LEVEL_PINK)) {
                                    tem = Conf.RIDEREQUEST_INVOICE_PINK;
                                } else if (req.getReqlevel().equals(Riderequest.REQUEST_LEVEL_CONTROL)) {
                                    tem = Conf.RIDEREQUEST_INVOICE_CONTROL;
                                }
                            }
                            mail.sendMail(u, tem, maps);
                        }
                    }

                } catch (Exception e) {
                    Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, e);
                }
                if (promo != null && !penalty) {
                    burnPromo(promo, amount);
                }
            } else {
                HashMap<String, String> maps = mandrillData(req, u);

                if (!penalty) {
                    req.setStatus(Riderequest.REQUEST_PAYMENT_FAILED);
                    req.setFailctr(req.getFailctr() == null ? 0 : req.getFailctr() + 1);
                    mail.sendMail(u, Conf.RIDEREQUEST_UNPAID, maps);
                } else {
                    if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)) {
                        req.setStatus(Riderequest.REQUEST_STATUS_ABORTED_UNPAID);
                    }
                }
                System.out.println("ADDING " + amount + " to DEBT");
                u.setDebt(u.getDebt() + amount);
                userCtrl.save(u);
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            req.setCharge(null);
            req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_FAILED);
            if (ex instanceof StripeException) {
                if (ex instanceof CardException) {
                    req.setCharge(((CardException) ex).getCharge());
                } else {
                    req.setCharge(((StripeException) ex).getRequestId());
                }

                req.setChargeerror(g.toJson(ex));
            } else {
                req.setChargeerror(ex.getMessage());
            }

            HashMap<String, String> maps = mandrillData(req, u);
            if (!penalty) {
                req.setStatus(Riderequest.REQUEST_PAYMENT_FAILED);
                req.setFailctr(req.getFailctr() == null ? 0 : req.getFailctr() + 1);
                mail.sendMail(u, Conf.RIDEREQUEST_UNPAID, maps);
            } else {
                if (req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)) {
                    req.setStatus(Riderequest.REQUEST_STATUS_ABORTED_UNPAID);
                }
            }
            u.setDebt(u.getDebt() + amount);
            userCtrl.save(u);

            if (promo != null && !penalty) {
                burnPromo(promo, amount);
            }
        }

        return req;
    }

    private void burnPromo(Userpromo promo, Integer amount) {
        Promo ppp = promoPromoCtrl.findById(promo.getPromo().getId());
        if (promo.getPromo() != null && (promo.getPromo().getType().equals(Promo.EURO) || promo.getPromo().getType().equals(Promo.PERCENT))) {
            promo.setBurnt(1);
            promo.setUsagedate(RESTDateUtil.getInstance().secondsNow());
            promoCtrl.save(promo);

            ppp.setCurrentusages(ppp.getCurrentusages() + 1);
            promoPromoCtrl.save(ppp);
        } else if (promo.getPromo() != null && (promo.getPromo().getType().equals(Promo.WALLET) || promo.getPromo().getType().equals(Promo.FREERIDE))) {
            promo.setValueleft(Math.max(0, promo.getValueleft() - (promo.getPromo().getType().equals(Promo.FREERIDE) ? 1 : amount)));

            if (promo.getValueleft() <= 0) {
                promo.setBurnt(1);
                promo.setUsagedate(RESTDateUtil.getInstance().secondsNow());
                ppp.setCurrentusages(ppp.getCurrentusages() + 1);
                promoPromoCtrl.save(ppp);

            }

            promoCtrl.save(promo);
        }

        if (ppp.getCurrentusages() >= ppp.getMaxusages()) {
            List<Userpromo> promos = promoCtrl.findListCustom("findUnusedByPid", Arrays.asList(new DBTuple("pid", ppp.getId()), new DBTuple("now", RESTDateUtil.getInstance().secondsNow())));
            for (Userpromo px : promos) {
                if (px.getId() != promo.getId()) {
                    px.setBurnt(2);
                    promoCtrl.save(px);
                }
            }
        }
    }

    private HashMap<String, String> mandrillData(Riderequest req, User u) {
        User driver = null;
        if (req.getDid() != null) {
            driver = userCtrl.findById(req.getDid());
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.putAll(MandrillMapper.param(u, "rider"));
        if (driver != null) {
            maps.putAll(MandrillMapper.param(driver, "driver"));
        }
        maps.putAll(MandrillMapper.param(req, "request"));
        maps.putAll(req.mandrill());
        return maps;
    }

    private Riderequest captureWithPreviousCharge(Riderequest req, boolean penalty) {
        Integer paid = 0;

        User u = userCtrl.findById(req.getPid());

        Userpromo promo = null;
        if (req.getPromoid() != null) {
            promo = promoCtrl.findById(req.getPromoid());
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Passenger ID", String.valueOf(req.getPid()));
        metadata.put("Driver ID", String.valueOf(req.getDid()));
        metadata.put("Ride ID", req.getShortid());
        metadata.put("Tipo addebito", penalty ? "Penale cancellazione" : "Ride payment");
        boolean zerotransaction = req.getCharge() != null && req.getCharge().startsWith("ZERO_EURO");
        if (!zerotransaction) {
            try {

                Customer c = Customer.retrieve(u.getStripeid());
                String last4 = null;
                String brand = null;
                for (ExternalAccount ea : c.getSources().getData()) {
                    if (((Card) ea).getId().equalsIgnoreCase(c.getDefaultSource())) {
                        last4 = ((Card) ea).getLast4();
                        brand = ((Card) ea).getBrand();
                    }
                }

                Charge ch = Charge.retrieve(req.getCharge());

                Map<String, Object> updateParams = new HashMap<String, Object>();
                updateParams.put("metadata", metadata);
                ch = ch.update(updateParams);

                Integer amount = penalty ? req.getZegofee() : (req.getStripedriverfee() + req.getStripezegofee());
                Integer oldamount = ch.getAmount().intValue();

                Map<String, Object> otherChargeParams = new HashMap<>();

                // it can't be more, driver update can only lower the price
                boolean refundNeeded = false;

                Long refunded = 0l;
                if (penalty) {
                    otherChargeParams.put("amount", req.getZegofee());
                    ch = ch.capture(otherChargeParams);
                    refundNeeded = true;
                    refunded = ch.getAmount() - req.getZegofee();
                    paid = 0;
                } else if (amount < oldamount) {
                    if (amount < 50) {
                        zerotransaction = true;
                    }
                    otherChargeParams.put("amount", amount);
                    if (!zerotransaction) {
                        ch = ch.capture(otherChargeParams);
                        refundNeeded = true;
                        refunded = oldamount - amount + 0l;
                    }
                    paid = amount;
                } else {
                    ch = ch.capture();
                    paid = ch.getAmount().intValue();
                }

                if (ch.getCaptured() && ch.getPaid()) {
                    req.setChargeerror(null);
                    req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED);

                    Payment p = paymentCtrl.findFirst("findByStripecharge", Arrays.asList(new DBTuple("stripecharge", req.getCharge())), false);
                    if (p == null) {
                        p = new Payment();
                    }
                    p.setAmount(ch.getAmount().intValue());
                    p.setCardbrand(brand);
                    p.setCurrency("eur");
                    p.setDid(req.getDid());
                    p.setDiscount(req.getDiscount());
                    p.setDriveraccount(null);
                    p.setDriverfee(req.getDriverfee());
                    p.setLastdigit(last4);
                    p.setMode(Payment.MODE_AUTO);
                    p.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
                    req.setPaymentdate(p.getPaymentdate());
                    p.setRid(req.getId());
                    p.setStripecharge(ch.getId());
                    p.setStripeid(u.getStripeid());
                    p.setStriperror(null);
                    p.setTransactionid(null);
                    p.setType(penalty ? Payment.PAYMENT_TYPE_PENALTY : Payment.PAYMENT_TYPE_STANDARD);
                    p.setUid(req.getPid());
                    if (promo != null && promo.getPromo() != null) {
                        p.setUpromocode(promo.getPromo().getCode());
                        p.setUpromoid(promo.getId());
                    }

                    p.setZegofee(req.getZegofee());
                    p.setStatus(Payment.PAYMENT_STATUS_CAPT);
                    p = paymentCtrl.save(p);
                    if (refundNeeded) {
                        p.setRefund(refunded.intValue());

                        Paymentaction pa = new Paymentaction();
                        pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                        pa.setActor("system");
                        pa.setActiontype("refund");
                        pa.setCapture(0);
                        pa.setChargeid(c.getId());
                        pa.setPid(p.getId());
                        pa.setRefund(refunded.intValue());
                        req.setRefund(pa.getRefund());
                        payActCtrl.save(pa);
                    } else {
                        p.setRefund(0);
                        Paymentaction pa = new Paymentaction();
                        pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                        pa.setActor("system");
                        pa.setActiontype("capture");
                        pa.setCapture(ch.getAmount().intValue());
                        pa.setChargeid(ch.getId());
                        pa.setPid(p.getId());
                        pa.setRefund(0);
                        payActCtrl.save(pa);
                    }

                } else {
                    req.setChargeerror(ch.getFailureMessage());
                    req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_FAILED);
                }
            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                req.setChargeerror(ex.getMessage());
                req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_FAILED);
            }
            req = reqCtrl.save(req);
        } else {
            req.setChargeerror(null);
            req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED);
            req = reqCtrl.save(req);
        }
        try {
            User driver = null;
            if (req.getDid() != null) {
                driver = userCtrl.findById(req.getDid());
            }

            HashMap<String, String> maps = new HashMap<>();
            maps.putAll(MandrillMapper.param(u, "rider"));
            if (driver != null) {
                maps.putAll(MandrillMapper.param(driver, "driver"));
            }
            maps.putAll(MandrillMapper.param(req, "request"));
            maps.putAll(req.mandrill());
            if (penalty) {
                mail.sendMail(u, Conf.RIDEREQUEST_INVOICE_CANCFEE, maps);
            } else if (promo != null) {
                maps.putAll(MandrillMapper.param(promo, "promo"));
                mail.sendMail(u, Conf.RIDEREQUEST_INVOICE, maps);
            } else {
                String tem = Conf.RIDEREQUEST_INVOICE_NOPROMO;
                if (req.getReqlevel() != null) {
                    if (req.getReqlevel().equals(Riderequest.REQUEST_LEVEL_PINK)) {
                        tem = Conf.RIDEREQUEST_INVOICE_PINK;
                    } else if (req.getReqlevel().equals(Riderequest.REQUEST_LEVEL_CONTROL)) {
                        tem = Conf.RIDEREQUEST_INVOICE_CONTROL;
                    }
                }
                mail.sendMail(u, tem, maps);
            }

        } catch (Exception e) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, e);
        }

        if (promo != null && !penalty) {
            burnPromo(promo, req.getDiscount());
        }
        return req;
    }

    @Override
    public Riderequest release(Riderequest req) throws RESTException {

        if (req.getCharge() != null) {
            try {

                if (req.getChargestatus().equalsIgnoreCase(Riderequest.REQUEST_CHARGE_STATUS_REFUNDED)) {
                    return req;
                } else {

                    Map<String, Object> refundParams = new HashMap<String, Object>();
                    refundParams.put("charge", req.getCharge());
                    Refund r = Refund.create(refundParams);
                    if (r != null && r.getStatus().equalsIgnoreCase("succeeded")) {
                        req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_REFUNDED);

                        Payment p = paymentCtrl.findFirst("findByStripecharge", Arrays.asList(new DBTuple("stripecharge", req.getCharge())), false);
                        if (p != null) {
                            p.setStatus(Payment.PAYMENT_STATUS_RELEASED);
                            p = paymentCtrl.save(p);
                            Paymentaction pa = new Paymentaction();
                            pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                            pa.setActor("system");
                            pa.setActiontype("release");
                            pa.setCapture(0);
                            pa.setChargeid(req.getCharge());
                            pa.setPid(p.getId());
                            pa.setRefund(r.getAmount().intValue());
                            req.setRefund(pa.getRefund());
                            payActCtrl.save(pa);
                        }

                    }
                    req = reqCtrl.save(req);
                    return req;
                }
            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
                return req;
            }
        } else {
            return req;
        }
    }

    @Override
    public Payment payExtra(Integer uid, Integer rid, Integer amount, String note) {

        Riderequest req = reqCtrl.findById(rid);
        User u = userCtrl.findById(uid);

        Map<String, Object> otherChargeParams = new HashMap<>();
        otherChargeParams.put("amount", amount);
        otherChargeParams.put("currency", "eur");
        otherChargeParams.put("capture", "true");
        otherChargeParams.put("description", note + " - Ride " + req.getShortid());
        otherChargeParams.put("customer", u.getStripeid());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Passenger ID", String.valueOf(req.getPid()));
        metadata.put("Driver ID", String.valueOf(req.getDid()));
        metadata.put("Ride ID", req.getShortid());
        metadata.put("Tipo addebito", "Extra Backoffice");
        otherChargeParams.put("metadata", metadata);
        try {
            Charge ch = Charge.create(otherChargeParams);
            String st = ch.getStatus();
            Boolean b = ch.getPaid();
            Customer c = Customer.retrieve(u.getStripeid());
            String last4 = null;
            String brand = null;
            for (ExternalAccount ea : c.getSources().getData()) {
                if (((Card) ea).getId().equalsIgnoreCase(c.getDefaultSource())) {
                    last4 = ((Card) ea).getLast4();
                    brand = ((Card) ea).getBrand();
                }
            }

            if (b) {
                Payment p = new Payment();
                p.setAmount(ch.getAmount().intValue());
                p.setCardbrand(brand);
                p.setCurrency("eur");
                p.setDid(req.getDid());
                p.setDiscount(0);
                p.setDriveraccount(null);
                p.setDriverfee(ch.getAmount().intValue());
                p.setLastdigit(last4);
                p.setMode(Payment.MODE_BACKOFFICE);
                p.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
                if (req.getPaymentdate() == null) {
                    req.setPaymentdate(p.getPaymentdate());
                }
                p.setRid(req.getId());
                p.setStripecharge(ch.getId());
                p.setStripeid(u.getStripeid());
                p.setStriperror(null);
                p.setTransactionid(null);
                p.setType(Payment.PAYMENT_TYPE_EXTRA);
                p.setUid(req.getPid());
                p.setUpromocode(null);
                p.setUpromoid(null);
                p.setZegofee(0);
                p.setStatus(Payment.PAYMENT_STATUS_CAPT);
                p = paymentCtrl.save(p);

                Paymentaction pa = new Paymentaction();
                pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
                pa.setActor("backoffice");
                pa.setActiontype("capture");
                pa.setCapture(ch.getAmount().intValue());
                pa.setChargeid(c.getId());
                pa.setPid(p.getId());
                pa.setRefund(0);
                payActCtrl.save(pa);

                if (req.getStatus().equals(Riderequest.REQUEST_STATUS_REFUNDED)) {
                    req.setStatus(Riderequest.REQUEST_STATUS_PAID);
                    reqCtrl.save(req);
                }
                return p;
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    @Override
    public Payment refund(Integer pid, Integer amount) {
        Payment p = paymentCtrl.findById(pid);
        try {
            Map<String, Object> refundParams = new HashMap<String, Object>();
            refundParams.put("charge", p.getStripecharge());
            refundParams.put("amount", amount);
            Refund r = Refund.create(refundParams);
            p.setStatus(Payment.PAYMENT_STATUS_REFUND);
            p.setRefund(p.getRefund() + amount);
            p = paymentCtrl.save(p);
            Paymentaction pa = new Paymentaction();
            pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
            pa.setActor("backoffice");
            pa.setActiontype("refund");
            pa.setCapture(0);
            pa.setChargeid(p.getStripecharge());
            pa.setPid(p.getId());
            pa.setRefund(amount);
            payActCtrl.save(pa);
            return p;
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            return p;
        }
    }

    @Override
    public Riderequest action(StripeActionRequest act) throws RESTException {
        Riderequest r = reqCtrl.findById(act.getRid());
        User u = userCtrl.findById(act.getUid());

        switch (act.getAction()) {
            case "capture": {
                r = capture(r, false);
                break;
            }
            case "refund": {
                r = release(r);
                r.setStatus(Riderequest.REQUEST_STATUS_REFUNDED);
                r = reqCtrl.save(r);
                break;
            }

        }
        return r;
    }

    @Override
    public boolean attemptPayUserDebt(User u, boolean fromscheduler) throws RESTException {
        return paydebt(u, fromscheduler);
    }

}
