/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.gestores;

import dao.commons.DaoEclipseLink;
import persistencia.Terminos_EC;

/**
 *
 * @author tecio
 */
public class TerminosDao extends DaoEclipseLink<Terminos_EC, Integer>{
    
    public TerminosDao() {
        super(Terminos_EC.class);
    }
    
}
