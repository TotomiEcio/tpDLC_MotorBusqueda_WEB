/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.gestores;

import dao.commons.DaoEclipseLink;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import logicaHash.Termino;
import persistencia.PosteoPK;
import persistencia.Posteo_EC;

/**
 *
 * @author tecio
 */
public class PosteoDao extends DaoEclipseLink<Posteo_EC, PosteoPK>{
    
    public PosteoDao(Class<Posteo_EC> entityClass) {
        super(entityClass);
    }
    
    public List<Posteo_EC> findPosteoForTermino(Termino t, int r){
        
        TypedQuery<Posteo_EC> query = entityManager.createNamedQuery("Posteo.findByHashTerMTF", Posteo_EC.class);
        query.setParameter("hashTer", t.hashCode());
        List<Posteo_EC> posteo = query.setMaxResults(r).getResultList();
        return posteo;
    }
}
