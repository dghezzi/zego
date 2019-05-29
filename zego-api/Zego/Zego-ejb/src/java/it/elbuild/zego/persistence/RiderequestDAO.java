/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Riderequest;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class RiderequestDAO extends JpaDAO<Integer, Riderequest>{

    public RiderequestDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}