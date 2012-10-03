/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.managers.registry;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.autogene.core.bio.entities.FeatureType;
import org.autogene.core.bio.entities.registry.RegistryType;

/**
 *
 * @author georgechen
 */
public class RegistryTypeManager {
 
    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
*/
    
    private EntityManagerFactory emf;

    public RegistryTypeManager() {
        emf = Persistence.createEntityManagerFactory("autogenePU");
    }
    
    public List<RegistryType> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from FeatureType f");
        List<RegistryType> registryTypes = query.getResultList();
        em.close();
        return registryTypes;
    }
    
    public RegistryType findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByName");
        query.setParameter("name", name);
        RegistryType r = (RegistryType)query.getSingleResult();
        em.close();
        return r;
    }
        
    public RegistryType findById (Long id) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findById");
        query.setParameter("id", id);
        RegistryType r = (RegistryType) query.getSingleResult();
        em.close();
        return r;
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

