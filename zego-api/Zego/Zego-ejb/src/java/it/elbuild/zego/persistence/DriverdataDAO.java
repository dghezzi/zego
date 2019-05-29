/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Driverdata;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class DriverdataDAO extends JpaDAO<Integer, Driverdata>{

    public DriverdataDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}