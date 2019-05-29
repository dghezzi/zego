/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Cash;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Payment;
import it.elbuild.zego.entities.Paymentaction;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.CashController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.Pair;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
public class CashControllerBean implements CashController {

    @EJB
    DAOProvider provider;

    @EJB
    MailSender mail;

    EntityController<Integer, Riderequest> requestCtrl;
    EntityController<Integer, Cash> cashCtrl;
    EntityController<Integer, User> userCtrl;
    EntityController<Integer, Payment> paymentCtrl;
    EntityController<Integer, Paymentaction> payActCtrl;

    @PostConstruct
    private void init() {
        requestCtrl = provider.provide(Riderequest.class);
        cashCtrl = provider.provide(Cash.class);
        userCtrl = provider.provide(User.class);
        payActCtrl = provider.provide(Paymentaction.class);
        paymentCtrl = provider.provide(Payment.class);
    }

    @Override
    public Pair<User, Riderequest> updateCashDue(Riderequest req, User driver, User rider) throws RESTException {
        Integer amount = req.getZegofee();// + req.getDriverfee();
        Cash c = new Cash();
        c.setCollected(amount);
        c.setDid(req.getDid());
        c.setDue(amount);
        c.setCollectdate(RESTDateUtil.getInstance().secondsNow());
        c.setPid(req.getPid());
        c.setRid(req.getId());
        c = cashCtrl.save(c);

        driver.setCashdue((driver.getCashdue() == null ? 0 : driver.getCashdue()) + req.getZegofee());
        driver = userCtrl.save(driver);

        // insert a fake payment to keep consistency with the current data structure
        Payment p = new Payment();
        p.setAmount(amount);
        p.setCardbrand(Riderequest.REQUEST_METHOD_CASH);
        p.setCurrency("eur");
        p.setDid(req.getDid());
        p.setDiscount(req.getDiscount());
        p.setDriveraccount(null);
        p.setDriverfee(req.getDriverfee());
        p.setLastdigit("-");
        p.setMode(Payment.MODE_AUTO);
        p.setPaymentdate(RESTDateUtil.getInstance().secondsNow());
        req.setPaymentdate(p.getPaymentdate());
        req.setChargestatus(Riderequest.REQUEST_CHARGE_STATUS_CAPTURED);
        p.setRid(req.getId());
        p.setStripecharge("CASH_TRANSACTION_NO_STRIPE_" + req.getId());
        p.setStripeid(rider.getStripeid());
        p.setStriperror(null);
        p.setTransactionid(null);
        p.setType(Payment.PAYMENT_TYPE_CASH);
        p.setUid(req.getPid());

        p.setZegofee(req.getZegofee());
        p.setStatus(Payment.PAYMENT_STATUS_CAPT);
        p.setRefund(0);

        p = paymentCtrl.save(p);

        Paymentaction pa = new Paymentaction();
        pa.setActiondate(RESTDateUtil.getInstance().secondsNow());
        pa.setActor("system");
        pa.setActiontype("capture");
        pa.setCapture(amount);
        pa.setChargeid("CASH_TRANSACTION_NO_STRIPE_" + req.getId());
        pa.setPid(p.getId());
        pa.setRefund(0);
        payActCtrl.save(pa);

        try {

            if (!req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)) {

                HashMap<String, String> maps = new HashMap<>();
                maps.putAll(MandrillMapper.param(rider, "rider"));
                if (driver != null) {
                    maps.putAll(MandrillMapper.param(driver, "driver"));
                }
                maps.putAll(MandrillMapper.param(req, "request"));
                maps.putAll(req.mandrill());
                mail.sendMail(rider, Conf.RIDEREQUEST_INVOICE_CASH, maps);
            }

        } catch (Exception e) {
            Logger.getLogger(StripeControllerBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return new Pair<>(req, driver);

    }

    @Override
    public User insertUserPayment(Integer amount, String ref, User driver) throws RESTException {

        if (driver.getCashdue() > 0) {
            driver.setCashdue(Math.max(0, driver.getCashdue() - amount));
        }

        List<Cash> drcash = cashCtrl.findListCustom("findToBePaid", Arrays.asList(new DBTuple("did", driver.getId())));
        Integer inta = amount;
        for (Cash cc : drcash) {

            if (inta > 0) {
                if (inta >= cc.getDue()) {
                    inta = inta - cc.getDue();
                    cc.setDue(0);
                    cc.setReftype(Cash.REFTYPE_RIDEPAYMENT);
                    cc.setReference(ref);
                    cc.setZegopaiddate(RESTDateUtil.getInstance().secondsNow());                    
                } else {
                    cc.setDue(cc.getDue() - inta);
                    cc.setReference(ref);
                    cc.setReftype(Cash.REFTYPE_RIDEPAYMENT);
                    cc.setZegopaiddate(RESTDateUtil.getInstance().secondsNow());
                    inta = 0;
                }

                cashCtrl.save(cc);
            }
        }

        return userCtrl.save(driver);
    }

}
