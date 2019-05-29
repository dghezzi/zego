/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import el.persistence.db.DBTuple;
import elbuild.utils.RandomPassword;
import it.elbuild.zego.entities.Appversion;
import it.elbuild.zego.entities.Banhistory;
import it.elbuild.zego.entities.Blacklist;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Dialog;
import it.elbuild.zego.entities.Pin;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.iface.AuthController;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.iface.SignupFunnelController;
import it.elbuild.zego.iface.SmsSender;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.persistence.DAOFactory;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.BootRequest;
import it.elbuild.zego.rest.request.PinRequest;
import it.elbuild.zego.rest.request.PinResendRequest;
import it.elbuild.zego.rest.request.ReferralRequest;
import it.elbuild.zego.rest.request.StripeCreateCustomerRequest;
import it.elbuild.zego.util.LoginRequest;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
public class SignupControllerBean implements SignupFunnelController {

    @EJB
    DAOFactory factory;

    @EJB
    AuthController auth;

    @EJB
    ConfController confCtrl;

    @EJB
    StripeInterface stripe;
    
    @EJB
    DAOProvider provider;

    EntityController<Integer, User> userCtrl;

    EntityController<Integer, Userpromo> upromoCtrl;

    EntityController<Integer, Promo> promoCtrl;

    EntityController<Integer, Pin> pinCtrl;

    EntityController<Integer, Conf> confEntityCtrl;

    EntityController<Integer, Blacklist> blacklistCtrl;

    EntityController<Integer, Appversion> versionCtrl;

    EntityController<Integer, Banhistory> banhistoryCtrl;

    EntityController<Integer, Dialog> dialogCtrl;

    @EJB
    SmsSender smsSender;

    @EJB
    MailSender mail;

    private Integer pinSecValidity;

    @PostConstruct
    private void init() {
        userCtrl = provider.provide(User.class);
        blacklistCtrl = provider.provide(Blacklist.class);
        versionCtrl = provider.provide(Appversion.class);
        pinCtrl = provider.provide(Pin.class);
        confEntityCtrl = provider.provide(Conf.class);
        banhistoryCtrl = provider.provide(Banhistory.class);
        dialogCtrl = provider.provide(Dialog.class);
        upromoCtrl = provider.provide(Userpromo.class);
        promoCtrl = provider.provide(Promo.class);
        pinSecValidity = 60 * confCtrl.getConfByKey(Conf.PIN_EXPIRY_MIN).asInteger(60);
    }
    /*
     * 0. Check blacklist, if blacklist, kill the request
     * 1. If there's already an user in ACTIVE/BANNED, kill the request.
     * 2. If there's already an user in IDLE state, update it and go on.
     * 3. If it doesn't, create a user in IDLE state and go on.
     * 
     */

    @Override
    public User signup(BootRequest lr) throws RESTException {

        preliminaryChecks(lr);

        List<User> oldus = getUserByPrefixAndMobile(lr.getPrefix(), lr.getMobile());
        User old = oldus.isEmpty() ? null : oldus.get(0);

        if (old == null) {
            User u = new User();
            u.defaultInit();
            u.updateFromBoot(lr);
            u.setTcok(1);
            u.setRefawarded(1);
            u.setCardonly(0);
            u.setCashused(0);
            u.setCashdue(0);
            u.setZegotoken("us_" + UUID.randomUUID().toString());
            u = userCtrl.save(u);
            u.setZgid(100000+u.getId());
            u = userCtrl.save(u);
            auth.addOrUpdate(u);
            sendSms(u);
            return u;
        } else {
            if (old.getStatus().equals(User.STATUS_IDLE)) {
                old.defaultInit();
                old.updateFromBoot(lr);
                old.setTcok(1);
                auth.erase(old.getZegotoken());
                old.setZegotoken("us_" + UUID.randomUUID().toString());
                old = userCtrl.save(old);
                auth.addOrUpdate(old);
                sendSms(old);
                return old;
            } else {
                throw new RESTException(ZegoK.Error.DUPLICATE_USERNAME);
            }
        }
    }

    @Override
    public User login(BootRequest lr) throws RESTException {
        preliminaryChecks(lr);
        List<User> oldus = null;
        User old = null;
        boolean isfacebook = false;
        if (lr.getFbid() != null) {
            old = getUserByFBId(lr.getFbid());
            isfacebook = true;
        } else {
            oldus = getUserByPrefixAndMobile(lr.getPrefix(), lr.getMobile());
            old = oldus.isEmpty() ? null : oldus.get(0);
        }

        if (old == null) {
            throw new RESTException(lr.getFbid() != null ? ZegoK.Error.USER_FB_NOT_FOUND : ZegoK.Error.USER_NOT_FOUND);
        } else if (old.getStatus().equals(User.STATUS_BANNED)) {
            throw new RESTException(ZegoK.Error.USER_BANNED);
        } else if (old.getStatus().equals(User.STATUS_IDLE)) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            if (!lr.getVos().equalsIgnoreCase("web")) {
                old.updateFromBoot(lr);
                old = tc(old);
                auth.erase(old.getZegotoken());
                old.setZegotoken("us_" + UUID.randomUUID().toString());
                old.setLastseen(RESTDateUtil.getInstance().secondsNow());

                try {
                    if (old.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_IDLE) && old.getUmode().equalsIgnoreCase("rider")) {
                        old.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
                    }
                } catch (Exception e) {
                }

                old = userCtrl.save(old);

                auth.addOrUpdate(old);
            }

            if (!isfacebook || old.getMobok() == null || old.getMobok() == 0) {
                sendSms(old);
            }

            return old;
        }
    }

    private User tc(User old) {
        Dialog d = dialogCtrl.findFirst("findByType",
                Arrays.asList(new DBTuple("type", Dialog.DIALOG_TYPE_TC)),
                false);

        if (d != null) {
            Integer valid = Integer.valueOf(d.getValidfrom());
            Integer last = Integer.valueOf(old.getLastseen());
            Integer now = Integer.valueOf(RESTDateUtil.getInstance().secondsNow());
            System.out.println(valid + " " + last + " " + now);
            if (now > valid && valid > last) {
                old.setTcok(0);
            } else {
                old.setTcok(1);
            }
        }
        return old;
    }

    @Override
    public User silentLogin(BootRequest lr) throws RESTException {
        preliminaryChecks(lr);
        User old = null;
        Boolean isFb = lr.getFbid() != null && lr.getFbid().length() > 4;
        if (isFb) {
            old = getUserByFBId(lr.getFbid());
        } else {
            List<User> oldus = getUserByPrefixAndMobile(lr.getPrefix(), lr.getMobile());
            old = oldus.isEmpty() ? null : oldus.get(0);
            if (old == null && lr.getUid() != null) {
                old = userCtrl.findById(lr.getUid());
            }
        }

        if (old == null) {
            throw new RESTException(lr.getFbid() != null ? ZegoK.Error.USER_FB_NOT_FOUND : ZegoK.Error.USER_NOT_FOUND);
        }
        if (old == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else if (old.getStatus().equals(User.STATUS_BANNED)) {
            throw new RESTException(ZegoK.Error.USER_BANNED);
        } else if (old.getStatus().equals(User.STATUS_IDLE)) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else if (!isFb && (old.getMobok() == null || old.getMobok() == 0)) {
            throw new RESTException(ZegoK.Error.USER_SHOULD_REVALIDATE_MOBILE);
        } else {
            old.updateFromBoot(lr);
            old = tc(old);
            auth.erase(old.getZegotoken());
            old.setZegotoken("us_" + UUID.randomUUID().toString());
            old.setLastseen(RESTDateUtil.getInstance().secondsNow());
            try {
                if (old.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_IDLE) && old.getUmode().equalsIgnoreCase("rider")) {
                    old.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
                }
            } catch (Exception e) {
            }
            old = userCtrl.save(old);
            auth.addOrUpdate(old);
            return old;
        }
    }

    @Override
    public User validatePin(PinRequest pinreq) throws RESTException {
        User us = userCtrl.findById(pinreq.getUid());
        if (us != null) {
            List<Pin> validPins = pinCtrl.findListCustom("findByUidValid", Arrays.asList(new DBTuple("uid", pinreq.getUid()), new DBTuple("expirydate", RESTDateUtil.getInstance().secondsNow())));
            boolean ok = false;
            for (Pin p : validPins) {
                ok = ok || p.getPin().equalsIgnoreCase(pinreq.getPin());
            }

            if (ok || (ZegoK.Tuning.TEST && pinreq.getPin().equals("1144"))) {
                us.setMobok(1);
                us.setLastseen(RESTDateUtil.getInstance().secondsNow());
                us = userCtrl.save(us);
                auth.addOrUpdate(us);
                return us;
            } else {
                throw new RESTException(ZegoK.Error.PIN_INVALID);
            }
        } else {
            throw new RESTException(ZegoK.Error.PIN_INVALID);
        }
    }

    @Override
    public User resendPin(PinResendRequest pinreq) throws RESTException {
        User us = userCtrl.findById(pinreq.getUid());
        if (us != null) {
            List<Pin> validPins = pinCtrl.findListCustom("findByUidValid", Arrays.asList(new DBTuple("uid", pinreq.getUid()), new DBTuple("expirydate", RESTDateUtil.getInstance().secondsNow())));
            if (validPins.size() >= ZegoK.Tuning.THROTTLING_SMS_THRESHOLD) {
                throw new RESTException(ZegoK.Error.TOO_MANY_SMS);
            } else {
                sendSms(us);
            }
            return us;
        } else {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        }

    }

    @Override
    public User checkReferral(ReferralRequest refreq) throws RESTException {
        User referral = userCtrl.findFirst("findByRefcode", Arrays.asList(new DBTuple("refcode", refreq.getReferral())), true);
        User redeemer = userCtrl.findById(refreq.getUid());

        if (referral == null) {
            try {
                List<Userpromo> up = standardRedeem(refreq.getUid(), refreq.getReferral());
                return redeemer;
            } catch (Exception e) {
                if (e instanceof RESTException) {
                    throw e;
                }
            }

            throw new RESTException(ZegoK.Error.REFERRALCODE_INVALID);
        } else if (referral.getStatus().equals(User.STATUS_BANNED)) {
            throw new RESTException(ZegoK.Error.REFERRALCODE_BANNED);
        } else {
            redeemer.setRefuid(referral.getId());
            redeemer.setRefawarded(0);
            redeemer = userCtrl.save(redeemer);
            auth.addOrUpdate(redeemer);
            try {
                if (redeemer != null) {
                    Promo p = new Promo();
                    p.setType(Promo.EURO);
                    p.setCode("MGM-" + RandomPassword.generate(5).toUpperCase());
                    p.setCurrentusages(0);
                    p.setEnablestart(RESTDateUtil.getInstance().secondsNow());
                    Integer days = confCtrl.getConfByKey(Conf.PROMO_MGM_EXPIRY_DAYS).asInteger(30);                            
                    p.setEnablestop(RESTDateUtil.getInstance().secondsMillisAgo(-1 * days * 24 * 3600));
                    p.setFeeonly(0);
                    p.setFirstonly(1);
                    p.setInsdate(RESTDateUtil.getInstance().secondsNow());
                    p.setMaxperuser(1);
                    p.setMaxusages(1);
                    p.setModdate(RESTDateUtil.getInstance().secondsNow());
                    p.setNote("");
                    p.setPromodesc(confCtrl.getConfByKey(Conf.PROMO_MGM_MSG_BODY).getVal().replace("{fname}", referral.getFname()));
                    p.setPromotitle(confCtrl.getConfByKey(Conf.PROMO_MGM_MSG_TITLE).getVal());
                    p.setValidfrom(RESTDateUtil.getInstance().secondsNow());
                    p.setValidto(RESTDateUtil.getInstance().secondsMillisAgo(-1 * days * 24 * 3600));
                    p.setValue(confCtrl.getConfByKey(Conf.PROMO_MGM_VALUE_CENT).asInteger(500));
                    p = promoCtrl.save(p);
                    if (p != null) {
                        Userpromo up = new Userpromo();
                        up.setBurnt(0);
                        up.setExpirydate(p.getValidto());
                        up.setPid(p.getId());
                        up.setRedeemdate(RESTDateUtil.getInstance().secondsNow());
                        up.setUid(redeemer.getId());
                        up.setValueleft(p.getType().equalsIgnoreCase(Promo.WALLET) ? p.getValue() : 0);
                        upromoCtrl.save(up);
                    }
                }

            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }

            return redeemer;
        }
    }

    
    @Override
    public User updateUserData(User u, Integer phase) throws RESTException {
        User old = userCtrl.findById(u.getId());

        if (old == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        }

        if (phase.equals(User.UPDATE_PHASE_SIGNUP)) {

            // check if fbid is already used (if any is specified)
            if (u.getFbid() != null && u.getFbid().length() > 4) {
                User prevfbid = getUserByFBId(u.getFbid());
                if (prevfbid != null && !prevfbid.getStatus().equals(User.STATUS_IDLE)) {
                    throw new RESTException(ZegoK.Error.DUPLICATE_FACEBOOKID);
                }
            }

            // check if email is already used
            List<User> emprevus = userCtrl.findListCustom("findByEmail", Arrays.asList(new DBTuple("email", u.getEmail())));
            for (User usx : emprevus) {
                if (!usx.getStatus().equals(User.STATUS_IDLE) && (u.getId() == null || !usx.getId().equals(u.getId()))) {
                    throw new RESTException(ZegoK.Error.DUPLICATE_EMAIL);
                }
            }

            old.updateWithUser(u);
            old.setStatus(User.STATUS_ACTIVE);
            old.generateRefCore();

        } else {

            // check if fbid is already used (if any is specified)
            if (u.getFbid() != null && u.getFbid().length() > 4) {
                User prevfbid = getUserByFBId(u.getFbid());
                if (prevfbid != null && !prevfbid.getStatus().equals(User.STATUS_IDLE) && !prevfbid.getId().equals(u.getId())) {
                    throw new RESTException(ZegoK.Error.DUPLICATE_FACEBOOKID);
                }
            }

            // check if email is already used
            List<User> emprevus = userCtrl.findListCustom("findByEmail", Arrays.asList(new DBTuple("email", u.getEmail())));
            for (User usx : emprevus) {
                if (!usx.getStatus().equals(User.STATUS_IDLE) && !usx.getId().equals(u.getId())) {
                    throw new RESTException(ZegoK.Error.DUPLICATE_EMAIL);
                }
            }

            // check if mobile is already used
            List<User> oldus = getUserByPrefixAndMobile(u.getPrefix(), u.getMobile());
            for (User usx : oldus) {
                if (!usx.getStatus().equals(User.STATUS_IDLE) && !usx.getId().equals(u.getId())) {
                    throw new RESTException(ZegoK.Error.DUPLICATE_USERNAME);
                }
            }

        }
        old.updateWithUser(u);

        if (old.getFbid() != null && old.getImg() == null) {
            old.setImg("https://graph.facebook.com/" + old.getFbid() + "/picture?type=normal");
        }

        old = userCtrl.save(old);
        auth.addOrUpdate(old);

        if (phase.equals(User.UPDATE_PHASE_SIGNUP)) {
            mail.sendMail(u, Conf.WELCOME_MAIL, old);
        }
        return old;
    }

    @Override
    public User createStripeCustomer(StripeCreateCustomerRequest stripeReq) throws RESTException {
        User old = userCtrl.findById(stripeReq.getUid());

        if (old == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        }

        old = stripe.createStripeUser(stripeReq);
        old.setPayok(1);
        old = userCtrl.save(old);
        auth.addOrUpdate(old);
        return old;
    }

    @Override
    public List<Conf> globalConf(BootRequest lr) throws RESTException {
        preliminaryChecks(lr);
        List<Conf> cc = confEntityCtrl.findListCustom("findByPub", Arrays.asList(new DBTuple("pub", 1)));
        return cc == null ? new ArrayList<Conf>() : cc;
    }

    private void preliminaryChecks(BootRequest lr) throws RESTException {

        Integer osInt = lr.getOs().equalsIgnoreCase("ios") ? 0 : 1;
        if (isDeviceBlacklisted(lr.getDeviceId())) {
            throw new RESTException(ZegoK.Error.DEVICE_BLACKLISTED);
        }

        Appversion appv = versionCtrl.findFirst("findByVers", Arrays.asList(new DBTuple("vers", lr.getVapp()), new DBTuple("os", osInt)), false);

        // if missing, add a version with a review note
        if (appv == null) {
            Appversion uk = new Appversion();
            uk.setNote("Unknown release added as a result of a user login.");
            uk.setReleasedate(RESTDateUtil.getInstance().secondsNow());
            uk.setStatus(Appversion.STATUS_CURRENT);
            uk.setVers(lr.getVapp());
            uk.setOs(osInt);
            versionCtrl.save(uk);
        } else {

            if (appv.getStatus().equalsIgnoreCase(Appversion.STATUS_CURRENT)) {
                // DO NOTHING
            } else {
                throw new RESTException(ZegoK.Error.APP_OUTDATED);
            }
        }

    }

    private boolean isDeviceBlacklisted(String deviceid) throws RESTException {
        return deviceid != null && !blacklistCtrl.findListCustom("findByDeviceid", Arrays.asList(new DBTuple("deviceid", deviceid))).isEmpty();
    }

    private List<User> getUserByPrefixAndMobile(String pfx, String mobile) throws RESTException {
        return pfx == null || mobile == null ? new ArrayList<User>() : userCtrl.findListCustom("findByPrefixAndMobile", Arrays.asList(new DBTuple("prefix", pfx), new DBTuple("mobile", mobile.replace(" ", ""))));
    }

    private User getUserByFBId(String fbid) {
        return userCtrl.findFirst("findByFbid", Arrays.asList(new DBTuple("fbid", fbid)), false);
    }

    private void sendSms(User u) throws RESTException {
        Pin p = new Pin();
        p.setSentdate(null);
        p.setUid(u.getId());
        // TODO expirydate configurable
        p.setExpirydate(RESTDateUtil.getInstance().secondsMillisAgo(-pinSecValidity));
        p.setPin(String.valueOf(1000 + new Random(System.currentTimeMillis()).nextInt(8999)));

        p = pinCtrl.save(p);

        if (confCtrl.getConfByKey(Conf.SEND_SMS).getVal().equals("1")) {
            smsSender.sendSms(p, u.asMobileTarget());
        }

    }

    @Override
    public User ban(User u) throws RESTException {
        u.setStatus(User.STATUS_BANNED);

        Banhistory bh = new Banhistory();
        bh.setBanreason(u.getBanreason());
        bh.setBanexpiry(RESTDateUtil.getInstance().secondsMillisAgo(-365 * 24 * 3600));
        bh.setBandate(RESTDateUtil.getInstance().secondsNow());
        bh.setUid(u.getId());
        banhistoryCtrl.save(bh);
        u.setBanexpdate(RESTDateUtil.getInstance().secondsMillisAgo(-365 * 24 * 3600));
        u.setBanreason(u.getBanreason());
        auth.erase(u.getZegotoken());
        return userCtrl.save(u);
    }

    @Override
    public User unban(User u) throws RESTException {
        u.setStatus(User.STATUS_ACTIVE);
        u.setBanexpdate(null);
        u.setBanreason(null);
        u = userCtrl.save(u);
        auth.addOrUpdate(u);
        return u;
    }

    @Override
    public User adminlogin(LoginRequest lr) throws RESTException {
        User u = userCtrl.findFirst("findByAdminLogin", Arrays.asList(new DBTuple("username", lr.getUsername()), new DBTuple("password", lr.getPassword())), true);
        if (u == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else {
            return u;
        }
    }

    public List<Userpromo> standardRedeem(Integer uid, String code) throws RESTException {
        User u = userCtrl.findById(uid);
        Promo p = promoCtrl.findFirst("findByCode", Arrays.asList(new DBTuple("code", code.toLowerCase())), true);

        if (u == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        } else if (p == null) {
            throw new RESTException(ZegoK.Error.PROMO_DOESNT_EXIST);
        } else if (p.getMaxusages() <= p.getCurrentusages()) {
            throw new RESTException(ZegoK.Error.PROMO_IS_OVERUSED);
        } else if (!p.canBeRedeemed()) {
            throw new RESTException(ZegoK.Error.PROMO_CANNOT_BE_REEDEMED_NOW);
        } else if (p.isExpired()) {
            throw new RESTException(ZegoK.Error.PROMO_IS_EXPIRED);
        }

        List<Userpromo> prev = upromoCtrl.findListCustom("findByUidAndPid", Arrays.asList(new DBTuple("pid", p.getId()), new DBTuple("uid", uid)));
        if (!prev.isEmpty() && prev.size() >= p.getMaxperuser()) {
            throw new RESTException(ZegoK.Error.PROMO_ALREADY_USED_BY_THIS_USER);
        }

        Userpromo red = new Userpromo();
        red.setExpirydate(p.getValidto());
        red.setPid(p.getId());
        red.setRedeemdate(RESTDateUtil.getInstance().secondsNow());
        red.setUid(uid);
        red.setValueleft(p.getType().equalsIgnoreCase(Promo.WALLET) || p.getType().equalsIgnoreCase(Promo.FREERIDE) ? p.getValue() : 0);
        red.setBurnt(0);
        upromoCtrl.save(red);

        return upromoCtrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid", uid)));
    }

    @Override
    public User updateFinalizedUser(User u) {
        User old = userCtrl.findById(u.getId());
        old.updateWithUser(u);
        return userCtrl.save(old);
    }
}
