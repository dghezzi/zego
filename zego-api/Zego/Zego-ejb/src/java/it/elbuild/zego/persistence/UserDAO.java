/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.User;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class UserDAO extends JpaDAO<Integer, User>{

    public UserDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}