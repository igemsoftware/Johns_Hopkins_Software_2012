/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author giovanni
 */
@Entity
@NamedQueries({
@NamedQuery(name="findBySequence", query="Select f from Feature f WHERE f.sequence = :sequence"),
@NamedQuery(name="findBySourceType", query = "Select f from Feature f WHERE f.source = :source")        
})
public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Organism organism;
    
    @ManyToOne
    private FeatureType type;
    
    private String name;
    
    @Column(length=15000)
    private String sequence;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    private sourceType source;
    
    private String displayName;
    
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDisplayName(String s) {
        displayName = s;
    }
    
    public String getSequence() {
        return sequence;
    }
    
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    
    public sourceType getPartType() {
        return source;
    }
    public void setPartType(sourceType sourceType) {
        this.source = sourceType;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public static enum sourceType {
     MANUAL,
     AUTO,
     BIOBRICK;
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
        if (!(object instanceof Feature)) {
            return false;
        }
        Feature other = (Feature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.bio.entities.Feature[ id=" + id + " ]";
    }
    
}
