package it.elbuild.zego.persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.legacy.Zgdrivrating;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ZgdrivratingDAO extends JpaDAO<Integer, Zgdrivrating>{

    public ZgdrivratingDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}