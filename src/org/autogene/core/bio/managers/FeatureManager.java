/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.Feature.sourceType;
import org.autogene.core.bio.entities.FeatureType;

/**
 *
 * @author Emily
 */
public class FeatureManager {

    private EntityManagerFactory emf;
    
    

    public FeatureManager() {
        System.out.println("OMGGin");
        //emf = Persistence.createEntityManagerFactory("autogenePU");
        emf = Master.getEntityManager();
    }
    
    public List<Feature> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from Feature f");
        System.out.println("created query: from Feature f....getting result list");
        List<Feature> features = query.getResultList();
        System.out.println("getting result list");
        em.close();
        return features;
    }
    
    public Feature findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByName");
        query.setParameter("name", name);
        Feature f = (Feature)query.getResultList();
        em.close();
        return f;
    }
    
    public List<Feature> findByType(String type){
        FeatureTypeManager typeManager = new FeatureTypeManager();
        FeatureType newType = typeManager.findByName(type);
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByType");
        query.setParameter("type_id", newType);
        List<Feature> f = query.getResultList();
        em.close();
        return f;
    }
    public Feature findBySequence(String sequence) {
        EntityManager em = emf.createEntityManager();
        System.out.print("Trying to make query....");
        Query query = em.createNamedQuery("findBySequence");
        System.out.println("Made Query!");
        query.setParameter("sequence", sequence.toUpperCase());
        System.out.println("parameters Set!");
        Feature f = (Feature)query.getSingleResult();
        em.close();
        return f;
    }

    public List<Feature> findBySourceType(sourceType source) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findBySourceType");
        query.setParameter("source", source);
        List<Feature> f = query.getResultList();
        em.close();
        return f;
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
    
    public void remove(Feature f){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Feature ftmp = em.find(Feature.class, f.getId());
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
