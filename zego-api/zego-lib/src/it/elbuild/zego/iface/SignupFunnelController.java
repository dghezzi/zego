/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.BootRequest;
import it.elbuild.zego.rest.request.PinRequest;
import it.elbuild.zego.rest.request.PinResendRequest;
import it.elbuild.zego.rest.request.ReferralRequest;
import it.elbuild.zego.rest.request.StripeCreateCustomerRequest;
import it.elbuild.zego.util.LoginRequest;
import java.util.List;

/**
 *
 * @author Lu
 */
public interface SignupFunnelController {
   
    public List<Conf> globalConf(BootRequest lr) throws RESTException;
    public User signup(BootRequest lr) throws RESTException;
    public User login(BootRequest lr) throws RESTException;
    public User adminlogin(LoginRequest lr) throws RESTException;
    public User silentLogin(BootRequest lr) throws RESTException;
    public User validatePin(PinRequest pinreq) throws RESTException;
    public User resendPin(PinResendRequest pinreq) throws RESTException;
    public User ban(User u) throws RESTException;
    public User unban(User u) throws RESTException;
    public User checkReferral(ReferralRequest refreq) throws RESTException;
    public User updateUserData(User u, Integer phase) throws RESTException;
    public User updateFinalizedUser(User u);
    public User createStripeCustomer(StripeCreateCustomerRequest stripeReq) throws RESTException;
    
}
