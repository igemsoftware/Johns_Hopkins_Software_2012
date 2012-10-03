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
import org.autogene.core.bio.entities.Oligo;

/**
 *
 * @author georgechen
 */
public class OligoManager {
    private EntityManagerFactory emf;
    public OligoManager() {
        emf = Persistence.createEntityManagerFactory("autogenePU");
    }
    
    public List<Oligo> findAll() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from Oligo o");
        List<Oligo> oligos = query.getResultList();
        em.close();
        return oligos;
        
    }
      public Oligo findByDescription(String description){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByOligoDescription");
        query.setParameter("description", description);
        Oligo o = (Oligo)query.getResultList();
        em.close();
        return o;
    }
    
      public Oligo findById(String id) {
       EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByOligoId");
        query.setParameter("id", id);
        Oligo o = (Oligo)query.getResultList();
        em.close();
        return o;   
      }
      
    public Oligo findBySequence(String sequence) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByOligoSequence");
        query.setParameter("sequence", sequence);
        Oligo o = (Oligo)query.getResultList();
        em.close();
        return o;
    }
    
    public void persist(Object object) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
