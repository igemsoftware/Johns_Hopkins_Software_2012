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
import org.autogene.core.bio.entities.Plasmid;

/**
 *
 * @author giovanni
 */
public class PlasmidManager {

    private EntityManagerFactory emf;

    public PlasmidManager() {
        emf = Persistence.createEntityManagerFactory("autogenePU");
    }
    
    public List<Plasmid> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from Plasmid p");
        List<Plasmid> plasmids = query.getResultList();
        em.close();
        return plasmids;
    }
    
    public Plasmid findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByName");
        query.setParameter("name", name);
        Plasmid p = (Plasmid)query.getSingleResult();
        em.close();
        return p;
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
    
    public void remove(Plasmid p){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Plasmid ptmp = em.find(Plasmid.class, p.getId());
            em.remove(ptmp);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    
    
}
