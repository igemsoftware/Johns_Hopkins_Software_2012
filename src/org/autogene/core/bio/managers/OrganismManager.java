/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.managers;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.autogene.core.bio.entities.Organism;

/**
 *
 * @author georgechen
 */
public class OrganismManager {
    
    private EntityManagerFactory emf;
    
    public OrganismManager() {
        //emf = Persistence.createEntityManagerFactory("autogenePU");
                emf = Master.getEntityManager();

    }
    
    public List<Organism> findAll() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from Organism o");
        List<Organism> organisms = query.getResultList();
        em.close();
        return organisms;
    }
    public Organism findByName(String name) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByOrganismName");
        query.setParameter("name", name);
        Organism o = (Organism)query.getSingleResult();
        return o;
    }
    
    public Organism findByTaxId(Long taxId) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByTaxId");
        query.setParameter("taxid", taxId);
        List<Organism> organisms =query.getResultList();
        Organism o = organisms.get(0);
        return o;
    }
}
