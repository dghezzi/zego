/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.User;

/**
 *
 * @author Lu
 */
public interface AuthController {
    
    public User get(String token);
    public void erase(String token);
    public void addOrUpdate(User p);
}
