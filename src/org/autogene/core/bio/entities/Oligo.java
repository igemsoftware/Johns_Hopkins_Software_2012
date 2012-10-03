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
@NamedQuery(name="findByOligoSequence", query="Select o from Oligo o WHERE o.sequence = :sequence"),
@NamedQuery(name="findByOligoDescription", query = "Select o from Oligo o WHERE o.description = :description"),
@NamedQuery(name="findByOligoId", query = "Select o from Oligo o WHERE o.id = :id")        
})
public class Oligo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    @Column (length = 10000)
    private String description;
    
    private String sequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(object instanceof Oligo)) {
            return false;
        }
        Oligo other = (Oligo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.core.bio.entities.Oligo[ id=" + id + " ]";
    }
    
}
