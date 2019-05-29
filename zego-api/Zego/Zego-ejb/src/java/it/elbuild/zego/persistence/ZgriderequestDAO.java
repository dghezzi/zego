package it.elbuild.zego.persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.legacy.Zgriderequest;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ZgriderequestDAO extends JpaDAO<Integer, Zgriderequest>{

    public ZgriderequestDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}