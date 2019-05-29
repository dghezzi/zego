package it.elbuild.zego.persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.legacy.Zgcity;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ZgcityDAO extends JpaDAO<Integer, Zgcity>{

    public ZgcityDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}