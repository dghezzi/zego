/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsClientBuilder;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import el.persistence.db.DBTuple;
import io.netty.util.concurrent.Future;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Notifications;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.gcm.Message;
import it.elbuild.zego.gcm.Sender;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Lu
 */
@Startup
@Singleton
public class APNSPusherEJB implements APNSPusher {

    private Conf certPath;
    private Conf certPass;
    private Sender gcm;
    private ApnsClient apnsClient;

    ApnsPayloadBuilder payloadBuilder;

    @EJB
    ConfController confMng;

    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Notifications> notCtrl;

    private Boolean SANDBOXMODE = false;

    @PostConstruct
    public void postconstruct() {

        notCtrl = provider.provide(Notifications.class);
        try {
            gcm = new Sender(ZegoK.GCM.APIKEY);

            Conf env = confMng.getConfByKey(Conf.ENVIRONMENT);

            if (env.getVal().equalsIgnoreCase(Conf.PRODUCTION)) {
                try {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "APNS Running in PRODUCTION MODE");
                    certPath = confMng.getConfByKey(Conf.PRODUCTION_CERT_PATH);
                    certPass = confMng.getConfByKey(Conf.PRODUCTION_CERT_PASSWORD);
                    apnsClient = new ApnsClientBuilder().setClientCredentials(new File(certPath.getVal()), certPass.getVal()).build();
                    SANDBOXMODE = false;
                } catch (IOException ex) {
                    Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "APNS Running in SANDBOX MODE");
                    certPath = confMng.getConfByKey(Conf.SANDBOX_CERT_PATH);
                    certPass = confMng.getConfByKey(Conf.SANDBOX_CERT_PASSWORD);
                    apnsClient = new ApnsClientBuilder().setClientCredentials(new File(certPath.getVal()), certPass.getVal()).build();
                    SANDBOXMODE = true;
                } catch (IOException ex) {
                    Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            final Future<Void> connectFuture = apnsClient.connect(SANDBOXMODE ? ApnsClient.DEVELOPMENT_APNS_HOST : ApnsClient.PRODUCTION_APNS_HOST);
            connectFuture.await();
            payloadBuilder = new ApnsPayloadBuilder();
        } catch (InterruptedException ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @PreDestroy
    public void predestroy() {
        try {
            final Future<Void> disconnectFuture = apnsClient.disconnect();
            disconnectFuture.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notifyUser(String message, String token, Integer iconBadgeNumber) {

        if (token != null) {
            if (token.startsWith("APA")) {
                androidNotification("STANDARD", message, token);
            } else {
                iosNotification(message, token, iconBadgeNumber);
            }
        }

    }

    @Override
    public void notifyUser(String message, String token) {
        notifyUser(message, token, 1);
    }

    @Override
    public void factoryReset() {
        payloadBuilder = new ApnsPayloadBuilder();
    }

    private boolean androidNotification(String type, String msg, String gcmid) {
        boolean result;
        try {
            Message message = new Message.Builder()
                    .collapseKey(String.valueOf(type))
                    .restrictedPackageName(ZegoK.GCM.PACKAGE)
                    .timeToLive(ZegoK.GCM.PUSH_TTL)
                    .addData("zegodata", msg)
                    .addData("zegocode", String.valueOf(type))
                    .build();

            gcm.sendNoRetry(message, gcmid);
            result = true;
        } catch (Exception ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        }
        return result;
    }

    private boolean iosNotification(String message, String tk, Integer iconBadgeNumber) {
        try {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.INFO, "Sending push notification ''{0}'' to token ''{1}''", new Object[]{message, tk});
            try {
                SimpleApnsPushNotification pushNotification;
                payloadBuilder.setAlertBody(message);                
                String payload = payloadBuilder.setBadgeNumber(message.startsWith(Notifications.NEW_REQUEST_TEXT)?1:0).setSoundFileName(message.startsWith(Notifications.NEW_REQUEST_TEXT) ? "long.aiff" : "short.aiff").buildWithDefaultMaximumLength();
                
                String token = TokenUtil.sanitizeTokenString(tk);

                pushNotification = new SimpleApnsPushNotification(token, Conf.APPID, payload);
                Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);
                PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

                if (pushNotificationResponse.isAccepted()) {
                    System.out.println("Push notification accepted by APNs gateway.");
                    return true;
                } else {
                    System.out.println("Notification rejected by the APNs gateway: "
                            + pushNotificationResponse.getRejectionReason());

                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        System.out.println("\t…and the token is invalid as of "
                                + pushNotificationResponse.getTokenInvalidationTimestamp());
                    }
                    return false;
                }
            } catch (final ExecutionException e) {
                System.err.println("Failed to send push notification.");

                if (e.getCause() instanceof ClientNotConnectedException) {
                    System.out.println("Waiting for client to reconnect…");
                    apnsClient.getReconnectionFuture().await();
                    System.out.println("Reconnected.");
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Failed to send push notification.");
            return false;
        }

        return false;
    }

    @Override
    public void descheduleNotificationForRide(Integer rid, boolean notify, List<RiderequestDrivers> links) {
        List<Notifications> nots = notCtrl.findListCustom("findByRid", Arrays.asList(new DBTuple("rid", rid)));
        ConcurrentHashMap<Integer,RiderequestDrivers> cache =  new ConcurrentHashMap<>();
        if(links != null) {
            for(RiderequestDrivers l : links) {
                cache.put(l.getDid(),l);
            }            
        }
        for (Notifications n : nots) {
            if(n.getStatus().equals(Notifications.STATUS_SCHEDULED)){
                n.setStatus(Notifications.STATUS_CANCELED);
                notCtrl.save(n);                                                 
            } else if (n.getStatus().equals(Notifications.STATUS_SENT)) {
                if(notify) {
                    RiderequestDrivers rr = cache.get(n.getUid());
                    if(rr != null && (rr.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_CREATED) || rr.getStatus().equals(RiderequestDrivers.RIDEREQUESTDRIVERS_ACCEPTED))) {
                        notifyUser(Notifications.PASSENGER_CANCELED_TEXT, n.getToken());    
                    }                    
                }
            }            
        }

    }

    @Override
    public void scheduleMessage(String message, User u, Integer rid, String when) {
        Notifications n = new Notifications();
        n.setMessage(message);
        n.setNotbefore(when);
        n.setRid(rid);
        n.setToken(u.getPushid());
        n.setUid(u.getId());
        n.setStatus(Notifications.STATUS_SCHEDULED);
        notCtrl.save(n);
    }

    @Override
    public void sendMessageNow(String message, User u, Integer rid) {

        if (u.getPushid() != null) {
            notifyUser(message, u.getPushid());

            Notifications n = new Notifications();
            n.setMessage(message);
            n.setNotbefore("0");
            n.setRid(rid);
            n.setToken(u.getPushid());
            n.setUid(u.getId());
            n.setStatus(Notifications.STATUS_SENT);
            n.setSentdate(RESTDateUtil.getInstance().secondsNow());
            
            notCtrl.save(n);
        }

    }
    
    public void silentPush(User u) {
        try {
            String payload  = payloadBuilder.setBadgeNumber(0).setContentAvailable(true).setSoundFileName("").setAlertBody("").buildWithDefaultMaximumLength();
            String token = TokenUtil.sanitizeTokenString(u.getPushid());
            
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, Conf.APPID, payload);
            Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
            
            if (pushNotificationResponse.isAccepted()) {
                
            } else {
                System.out.println("Notification rejected by the APNs gateway: "
                        + pushNotificationResponse.getRejectionReason());
                
                if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                    System.out.println("\t…and the token is invalid as of "
                            + pushNotificationResponse.getTokenInvalidationTimestamp());                    
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(APNSPusherEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void keepAlive(User u) {
        silentPush(u);
    }
}
