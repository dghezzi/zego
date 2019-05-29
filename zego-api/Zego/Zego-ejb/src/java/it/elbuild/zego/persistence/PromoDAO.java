/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Promo;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class PromoDAO extends JpaDAO<Integer, Promo>{

    public PromoDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}