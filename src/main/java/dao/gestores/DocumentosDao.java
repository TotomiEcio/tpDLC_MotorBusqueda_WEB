/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.gestores;

import dao.commons.DaoEclipseLink;
import javax.persistence.TypedQuery;
import persistencia.Documentos_EC;
import persistencia.Posteo_EC;

/**
 *
 * @author tecio
 */
public class DocumentosDao extends DaoEclipseLink<Documentos_EC, Integer>{

    public DocumentosDao(Class<Documentos_EC> entityClass) {
        super(entityClass);
    }
    
    public int cantDocumentos(){
        
        TypedQuery query = entityManager.createNamedQuery("Documentos.cantDocumentos", Documentos_EC.class);
        return query.getFirstResult();
    }

}
