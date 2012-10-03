/*
 * Entity responsible for handling the species
 */
package org.autogene.core.bio.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author giovanni
 */
@Entity
@NamedQueries({
@NamedQuery(name = "findByTaxId",query = "SELECT o from Organism o WHERE o.taxid = :taxid"),
@NamedQuery(name = "findByOrganismName", query = "SELECT o from Organism o WHERE o.name = :name")

})
public class Organism implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private Long taxid;
    
    private String name;
    
    @OneToMany(targetEntity=Feature.class,mappedBy="organism",fetch=FetchType.LAZY)
    private List<Feature> features;
    
    @OneToMany(targetEntity=Pathogen.class,mappedBy="organism",fetch=FetchType.LAZY)
    private List<Pathogen> pathogens;

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

    public Long getTaxid() {
        return taxid;
    }

    public void setTaxid(Long taxid) {
        this.taxid = taxid;
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
        if (!(object instanceof Organism)) {
            return false;
        }
        Organism other = (Organism) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.bio.entities.Organism[ id=" + id + " ]";
    }
    
}
