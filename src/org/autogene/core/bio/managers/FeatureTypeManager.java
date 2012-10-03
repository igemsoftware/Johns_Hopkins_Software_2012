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

/**
 *
 * @author Emily
 */
public class FeatureTypeManager {
    
    private EntityManagerFactory emf;

    public FeatureTypeManager() {
        emf = Persistence.createEntityManagerFactory("autogenePU");
    }
    
    public List<FeatureType> findAll(){
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("from FeatureType f");
        List<FeatureType> featureTypes = query.getResultList();
        em.close();
        return featureTypes;
    }
    public List<FeatureType> findGeneral() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("find General features");
        List<FeatureType> featureTypes = query.getResultList();
        em.close();
        return featureTypes;
    }
    
    public FeatureType findByName(String name){
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByName");
        query.setParameter("name", name);
        FeatureType f = (FeatureType)query.getSingleResult();
        em.close();
        return f;
    }
    
    public FeatureType findBysofa_id (String sofa_id) {
        System.out.println("Accessing with String case");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findBysofa_id");
        System.out.println("created query");
        query.setParameter("sofa_id", sofa_id);
        FeatureType  f =  (FeatureType) query.getSingleResult();
        em.close();
        return f;
    }
    
    public FeatureType findBysofa_id(int soIDnum) {
        System.out.println("Accessing with int case");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findBysofa_id");
        System.out.println("created query");
        String sofa_id = String.format("SO:%07d",soIDnum);    
        query.setParameter("sofa_id", sofa_id);
        FeatureType f = (FeatureType) query.getSingleResult();
        em.close();
        return f;
    }
     
    public FeatureType findById (Long id) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findById");
        query.setParameter("id", id);
        FeatureType f = (FeatureType) query.getSingleResult();
        em.close();
        return f;
    }
    public FeatureType findBySofa_name(String sofa_name) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findBySofa_name");
        query.setParameter("sofa_name", sofa_name);
        FeatureType f = (FeatureType) query.getSingleResult();
        em.close();
        return f;
    }
    //finds multiple keys per genbankfeature
    public List<FeatureType> findByGenBankFeature(String genBankFeature) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByGenBankFeature");
        query.setParameter("genBankFeature", genBankFeature);
        List<FeatureType> featureTypes = query.getResultList();
        em.close();
        return featureTypes;
    }
    //finds only one key per genbankfeature
    public FeatureType findSingleGenBankFeature(String genBankFeature) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("findByGenBankFeature");
        query.setParameter("genBankFeature", genBankFeature);
        List<FeatureType> featureTypes = query.getResultList(); 
        FeatureType featureType = featureTypes.get(0);
        em.close();
        return featureType;
        
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
