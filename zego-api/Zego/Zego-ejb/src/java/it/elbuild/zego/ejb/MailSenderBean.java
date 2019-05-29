/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import it.elbuild.mandrilllib.send.MandrillIdentity;
import it.elbuild.mandrilllib.send.MandrillMailSender;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.MailSender;
import it.elbuild.zego.util.MandrillMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class MailSenderBean implements MailSender {

    private MandrillMailSender sender;

    @EJB
    ConfController confManager;

    @PostConstruct
    private void init() {
        sender = MandrillMailSender.getInstance(confManager.getConfByKey(Conf.MANDRILL_KEY).getVal());
    }

    @Override
    public void sendMail(User to, String template, Object... params) {

        MandrillIdentity d = new MandrillIdentity(to.getFname() + " " + to.getLname(), to.getEmail());
        MandrillIdentity s = new MandrillIdentity("Zego", "support@zegoapp.com");
        HashMap<String, String> maps = new HashMap<>();
        for (Object o : params) {
            Map<String, String> pp = MandrillMapper.param(o);
            maps.putAll(pp);
        }
        
        sendMail(to, template, maps);

    }

    @Override
    public void sendMail(User to, String template, Map<String, String> maps) {
         MandrillIdentity d = new MandrillIdentity(to.getFname() + " " + to.getLname(), to.getEmail());
        MandrillIdentity s = new MandrillIdentity("Zego", "support@zegoapp.com");
        try {
            switch (template) {
                case Conf.WELCOME_MAIL: {
                    String tem = confManager.getConfByKey(Conf.WELCOME_MAIL).getVal();
                    String sub = confManager.getConfByKey(Conf.WELCOME_MAIL_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                case Conf.DRIVER_APPROVED: {
                    String tem = confManager.getConfByKey(Conf.DRIVER_APPROVED).getVal();
                    String sub = confManager.getConfByKey(Conf.DRIVER_APPROVED_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                case Conf.DRIVER_DOC_EXPIRED: {
                    String tem = confManager.getConfByKey(Conf.DRIVER_APPROVED).getVal();
                    String sub = confManager.getConfByKey(Conf.DRIVER_APPROVED_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }

                case Conf.RIDEREQUEST_INVOICE: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_UNPAID: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_UNPAID).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_UNPAID_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }

                case Conf.RIDEREQUEST_INVOICE_NOPROMO: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_NOPROMO).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_NOPROMO_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_INVOICE_CASH: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CASH).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CASH_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_INVOICE_PINK: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_PINK).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_PINK_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_INVOICE_CONTROL: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CONTROL).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CONTROL_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_INVOICE_CANCFEE: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CANCFEE).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_INVOICE_CANCFEE_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
                
                case Conf.RIDEREQUEST_TERMINATED: {
                    String tem = confManager.getConfByKey(Conf.RIDEREQUEST_TERMINATED).getVal();
                    String sub = confManager.getConfByKey(Conf.RIDEREQUEST_TERMINATED_SUBJECT).getVal();
                    sender.sendEmail(s, d, sub, tem, maps);
                    break;
                }
            }
        } catch (MandrillApiError ex) {
            Logger.getLogger(MailSenderBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MailSenderBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MailSenderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
