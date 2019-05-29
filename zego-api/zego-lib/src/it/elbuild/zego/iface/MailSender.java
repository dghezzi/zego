/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.User;
import java.util.Map;

/**
 *
 * @author Lu
 */
public interface MailSender {
    
    public void sendMail(User to, String template, Object... params);
    public void sendMail(User to, String template, Map<String, String> maps);
}
