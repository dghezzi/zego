/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Apitest;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ApitestDAO extends JpaDAO<Integer, Apitest>{

    public ApitestDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}