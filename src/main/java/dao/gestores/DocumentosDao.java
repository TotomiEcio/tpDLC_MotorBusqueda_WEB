/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.gestores;

import dao.commons.DaoEclipseLink;
import persistencia.Documentos_EC;

/**
 *
 * @author tecio
 */
public class DocumentosDao extends DaoEclipseLink<Documentos_EC, Integer>{


    public DocumentosDao() {
        super(Documentos_EC.class);
    }
}
