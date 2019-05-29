/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import it.elbuild.zego.ejb.util.SkebbySender;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Pin;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.SmsSender;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.NexmoRequest;
import it.elbuild.zego.rest.response.NexmoMessage;
import it.elbuild.zego.rest.response.NexmoResponse;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;



/**
 *
 * @author Lu
 */
@Startup
@Singleton
public class SMSSenderBean implements SmsSender {

    private static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    
    private String API_KEY = "";
    private String API_SECRET = "";
    private String FROM = "";
    private String FROM_INTNL = "";
    private String TEMPLATE = "";
    private String TEMPLATE_INTNL = "";
    
    private String SKEBBY_USERNAME = "";
    private String SKEBBY_PASSWORD = "";
    private String SKEBBY_SENDER = "";
    private String SKEBBY_SENDER_INTL = "";
    
    private String ENGINE = "";
    Gson g;
    
    OkHttpClient client;
    
    @EJB
    ConfController confCtrl;
    
    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Pin> pinCtrl;
    
    private SkebbySender sender;
    
    @PostConstruct
    private void init() {
        pinCtrl = provider.provide(Pin.class);
        client = new OkHttpClient();
        client.setReadTimeout(3, TimeUnit.SECONDS);
        client.setConnectTimeout(3, TimeUnit.SECONDS);
        g = new Gson();
        
        API_KEY = confCtrl.getConfByKey(Conf.NEXMO_API_KEY).getVal();
        API_SECRET = confCtrl.getConfByKey(Conf.NEXMO_API_SECRET).getVal();
        FROM = confCtrl.getConfByKey(Conf.NEXMO_FROM).getVal();
        FROM_INTNL = confCtrl.getConfByKey(Conf.SMS_NEXMO_SENDER_INTNL).getVal();
        TEMPLATE = confCtrl.getConfByKey(Conf.NEXMO_TEMPLATE).getVal();
        TEMPLATE_INTNL = confCtrl.getConfByKey(Conf.NEXMO_TEMPLATE_INTNL).getVal();
        
        SKEBBY_USERNAME = confCtrl.getConfByKey(Conf.SMS_SKEBBY_USER).getVal();
        SKEBBY_SENDER =  confCtrl.getConfByKey(Conf.SMS_SKEBBY_SENDER).getVal();
        SKEBBY_PASSWORD = confCtrl.getConfByKey(Conf.SMS_SKEBBY_PASSWORD).getVal();
        SKEBBY_SENDER_INTL = confCtrl.getConfByKey(Conf.SMS_SKEBBY_SENDER_INTNL).getVal();
        
        ENGINE = confCtrl.getConfByKey(Conf.SMS_ENGINE).getVal();
        
        sender = new SkebbySender();
        sender.setASCIISENDER(SKEBBY_SENDER);
        sender.setPASSWORD(SKEBBY_PASSWORD);
        sender.setUSERNAME(SKEBBY_USERNAME);
        
        
    }
    
    @Override
    public NexmoResponse sendSms(Pin p, String target) throws RESTException {
        
        try {
            
            if(ENGINE != null && ENGINE.equalsIgnoreCase("nexmo")){
            
            NexmoRequest nr = new NexmoRequest();
            nr.setApi_key(API_KEY);
            nr.setApi_secret(API_SECRET);
            nr.setFrom(target.startsWith("39") ? FROM : FROM_INTNL);
            nr.setText((target.startsWith("39") ? TEMPLATE : TEMPLATE_INTNL)+" "+p.getPin());
            nr.setTo(target);
            String reqdata = g.toJson(nr);
            System.out.println(reqdata);
            
            Request request = new Request.Builder()
                    .url("https://rest.nexmo.com/sms/json")
                    .post(RequestBody.create(JSON, reqdata))
                    .build();
            Response responses = null;

            responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            NexmoResponse nresp =  g.fromJson(jsonData, NexmoResponse.class);
            
            if(Integer.valueOf(nresp.getMessageCount()) > 0) {
                NexmoMessage ms = nresp.getMessages().get(0);
                
                p.setNexmoid(ms.getMessageId());
                p.setNexmostatus(ms.getStatus());
                p.setRaw(jsonData);
                
                    
                if(ms.getStatus().equalsIgnoreCase("0")) {   
                    p.setSentdate(RESTDateUtil.getInstance().secondsNow());
                    pinCtrl.save(p);
                } else {    
                    pinCtrl.save(p);
                    throw new RESTException(ZegoK.Error.IMPOSSIBLE_TO_SEND_SMS);
                }
            }
            return nresp;
            } else {
                ArrayList<String> arr = new ArrayList<>();
                arr.add(target);
                try {
                    sender.setASCIISENDER(target.startsWith("39") ? SKEBBY_SENDER : SKEBBY_SENDER_INTL);
                    String result = sender.send(arr, (target.startsWith("39") ? TEMPLATE : TEMPLATE_INTNL)+" "+p.getPin());
                     p.setSentdate(RESTDateUtil.getInstance().secondsNow());
                     p.setRaw(result);
                     p.setNexmoid(null);
                     pinCtrl.save(p);
                } catch (Exception ex) {
                    pinCtrl.save(p);
                    throw new RESTException(ZegoK.Error.IMPOSSIBLE_TO_SEND_SMS);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SMSSenderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
