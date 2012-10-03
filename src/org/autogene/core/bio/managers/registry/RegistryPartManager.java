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
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.registry.RegistryType;
import org.autogene.core.bio.entities.registry.RegistryPart;

/**
 *
 * @author georgechen
 */
public class RegistryPartManager {
    
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
    private EntityManagerFactory emf;

    public RegistryPartManager() {
        emf = Persistence.createEntityManagerFactory("autogenePU");
    }
    
    public List<RegistryPart> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from RegistryPart r");
        List<RegistryPart> registryParts = query.getResultList();
        em.close();
        return registryParts;
    }
    
    public RegistryPart findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByName");
        query.setParameter("name", name);
        RegistryPart r = (RegistryPart)query.getResultList();
        em.close();
        return r;
    }
  //change this  
    public List<RegistryPart> findByType(String type){
        RegistryTypeManager typeManager = new RegistryTypeManager();
        RegistryType newType = typeManager.findByName(type);
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByType");
        query.setParameter("registryFeature_id", newType);
        List<RegistryPart> r = query.getResultList();
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
    
    public void remove(RegistryPart r){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            RegistryPart ftmp = em.find(RegistryPart.class, r.getId());
            em.remove(ftmp);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    
}
}
