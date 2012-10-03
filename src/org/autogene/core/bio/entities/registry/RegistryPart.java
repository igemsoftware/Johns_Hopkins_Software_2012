/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities.registry;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author georgechen
 */
@Entity
public class RegistryPart implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    private Long partId;
    
    @ManyToOne
    private RegistryType registryType;
    
    private String description;
    
    private Character status;
    
    @Column(length=90000)
    private String sequence;
    
    
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
    
    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }
    
    public RegistryType getRegistryType() {
        return registryType;
    }

    public void setRegistryType(RegistryType registryType) {
        this.registryType = registryType;
    }
     
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Character getStatus() {
        return status;
    }
    
    public void setStatus(Character status) {
        this.status = status;
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
        if (!(object instanceof RegistryPart)) {
            return false;
        }
        RegistryPart other = (RegistryPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.core.bio.entities.registry.RegistryPart[ id=" + id + " ]";
    }
    
}
