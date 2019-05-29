/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Exportdriver;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ExportdriverDAO extends JpaDAO<Integer, Exportdriver>{

    public ExportdriverDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}