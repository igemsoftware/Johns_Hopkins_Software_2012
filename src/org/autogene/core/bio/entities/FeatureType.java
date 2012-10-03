/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author giovanni
 */
@Entity
@NamedQueries({
@NamedQuery(name="findBysofa_id", query="SELECT f FROM FeatureType f WHERE f.sofa_id = :sofa_id"),
@NamedQuery(name="findByGenBankFeature", query = "SELECT f FROM FeatureType f WHERE f.genBankFeature = :genBankFeature"), 
@NamedQuery(name = "findBySofa_name", query = "SELECt f FROM FeatureType f WHERE f.sofa_name = :sofa_name")      
//@NamedQuery(name = "find General features", query = "SELECT f FROM FeatureType f Where")
})
public class FeatureType implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String CUSTOM_TYPE = "UserDefined";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
   
    private String genBankFeature;
    
    private String sofa_id;
    
    @Column (length =100)
    private String sofa_name;
    
    @OneToMany(targetEntity=Feature.class,mappedBy="type",fetch= FetchType.LAZY)
    private List<Feature> features;
    
    @OneToMany(targetEntity=Pathogen.class,mappedBy="type",fetch= FetchType.LAZY)
    private List<Pathogen> pathogens;
    
    public FeatureType() {}
    
    public FeatureType(String n) {
        name = n;
    }
    public FeatureType(Long i) {
        id = i;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public void setGenBankFeature(String genBankFeature) {
        this.genBankFeature = genBankFeature;
    }
    public String getGenBankFeature() {
        return genBankFeature;
    }
    
    public void setSofa_id(String sofa_id) {
        this.sofa_id = sofa_id;
    }
    public String getSofa_id() {
        return sofa_id;
    }
    
    public String getSofa_name() {
        return sofa_name;
    }
    public void setSofa_name(String sofa_name) {
        this.sofa_name = sofa_name;
    }
    
    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
    
    public List<Pathogen> getPathogens() {
        return pathogens;
    } 
    
    public void setPathogens(List<Pathogen> pathogens) {
        this.pathogens = pathogens;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FeatureType)) {
            return false;
        }
        FeatureType other = (FeatureType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.bio.entities.FeatureType[ id=" + id + " ]";
    }
    
}
