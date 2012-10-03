/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities;

import java.awt.Color;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author giovanni
 */
@Entity
public class Annotation implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Plasmid plasmid;
      
    @ManyToOne
    private Feature feature;
    
    private Integer start;
    
    private Integer end;
    
    private Double score;
    
    private String report;
    
    private StrandType strand;
  
    public enum StrandType {
        ORIGINAL, COMPLEMENT;
        
    }
    
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
        
        /*if(score == 1.0) {
            String out = "plasmid\t\t" + start + " " + feature.getSequence() + " " + end + "\n\t\t";
            for(int i = 0; i < feature.getSequence().length(); i++) {
                out += "|";
            }
            out += "\nfeature\t\t1 " + feature.getSequence() + "";
        }*/
    }
    

    public StrandType getStrand() {
        return strand;
    }

    public void setStrand(StrandType strand) {
        this.strand = strand;
    }
    
    
            

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    private String comment;
    
    private Color color;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Plasmid getPlasmid() {
        return plasmid;
    }

    public void setPlasmid(Plasmid plasmid) {
        this.plasmid = plasmid;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }


    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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
        if (!(object instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.bio.entities.Annotation[ id=" + id + " ]";
    }
    

    @Override
    public int compareTo(Object o) {
        Annotation a = (Annotation)o;
        
        if(a.getScore() > score)
            return 1;
        if(a.getScore() < score)
            return -1;
        return 0;
            
    }
}
