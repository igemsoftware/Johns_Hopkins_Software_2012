/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.managers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.FeatureType;
import org.autogene.core.bio.entities.Pathogen;

/**
 *
 * @author georgechen
 */
public class PathogenManager {
    
    private EntityManagerFactory emf;
    
    public PathogenManager() {
       //  emf = Persistence.createEntityManagerFactory("autogenePU");
                 emf = Master.getEntityManager();

    }
    public List<Pathogen> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from Pathogen f");
        System.out.println("created query: from Pathogen p....getting result list");
        List<Pathogen> pathogens = query.getResultList();
        System.out.println("getting result list");
        em.close();
        return pathogens;
    }
    
    public Pathogen findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByPathogenName");
        query.setParameter("name", name);
        Pathogen p = (Pathogen)query.getResultList();
        em.close();
        return p;
    }
    
    public List<Pathogen> findByType(String type){
        FeatureTypeManager typeManager = new FeatureTypeManager();
        FeatureType newType = typeManager.findByName(type);
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByPathogenType");
        query.setParameter("type_id", newType);
        List<Pathogen> p = query.getResultList();
        em.close();
        return p;
    }
    public Pathogen findBySequence(String sequence) {
        EntityManager em = emf.createEntityManager();
        System.out.print("Trying to make query....");
        Query query = em.createNamedQuery("findByPathogenSequence");
        System.out.println("Made Query!");
        query.setParameter("sequence", sequence.toUpperCase());
        System.out.println("parameters Set!");
        Pathogen p = (Pathogen)query.getSingleResult();
        em.close();
        return p;
    }
}
