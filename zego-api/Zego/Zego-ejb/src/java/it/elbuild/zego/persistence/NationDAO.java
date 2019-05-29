/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Nation;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class NationDAO extends JpaDAO<Integer, Nation>{

    public NationDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}