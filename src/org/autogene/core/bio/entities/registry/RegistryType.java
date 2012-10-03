/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities.registry;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author georgechen
 */
@Entity
public class RegistryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @OneToMany(targetEntity = RegistryPart.class,mappedBy = "registryType", fetch = FetchType.LAZY)
    private List<RegistryPart> registryParts;
    
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
    
    public List<RegistryPart> getRegistryParts() {
        return registryParts;
    }
    
    public void setRegistryParts(List<RegistryPart> registryParts) {
        this.registryParts = registryParts;
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
        if (!(object instanceof RegistryType)) {
            return false;
        }
        RegistryType other = (RegistryType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.core.bio.entities.registry.RegistryType[ id=" + id + " ]";
    }
    
}
