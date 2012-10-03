/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author georgechen
 */
@Entity
@NamedQueries({
@NamedQuery(name="findByPathogenSequence", query="Select p from Pathogen p WHERE p.sequence = :sequence"),
@NamedQuery(name="findByPathogenType", query = "Select p from Pathogen p WHERE p.type = :type"),
@NamedQuery(name="findByPathogenName", query = "Select p from Pathogen p WHERE p.name = :name")
})
public class Pathogen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @ManyToOne
    private FeatureType type;
    
    @ManyToOne
    private Organism organism;
    
    @Column(length=30000)
    private String sequence;

    @Column(name = "description", length = 30000)
    private String description;
    
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
    
    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }
    
     public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
     public String getSequence() {
        return sequence;
    }
    
    public void setSequence(String sequence) {
        this.sequence = sequence;
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
        if (!(object instanceof Pathogen)) {
            return false;
        }
        Pathogen other = (Pathogen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.core.bio.entities.Pathogen[ id=" + id + " ]";
    }
    
}
