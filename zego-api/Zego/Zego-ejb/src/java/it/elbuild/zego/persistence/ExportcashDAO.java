/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Exportcash;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
public class ExportcashDAO extends JpaDAO<Integer, Exportcash>{

    public ExportcashDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
}