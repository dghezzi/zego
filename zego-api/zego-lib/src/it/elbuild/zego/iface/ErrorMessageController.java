/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Errormsg;



/**
 *
 * @author Lu
 */
public interface ErrorMessageController {    
    public void refreshAll();
    public Errormsg getErrorByCodeAndLang(Integer code, String lang);
}
