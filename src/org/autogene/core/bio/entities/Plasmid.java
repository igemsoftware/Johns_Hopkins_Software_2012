/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.autogene.algorithms.OligoMatch;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;

/**
 *
 * @author giovanni
 */
@Entity
@NamedQuery(name="findByName", query="Select p from Plasmid p WHERE p.name = :name")
public class Plasmid implements Serializable, Part {

   private HashMap<String, Integer> featureDisplayNamesHashMap;
   private ArrayList<OligoMatch> oligoMatches;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String supplier;
    private String sequence;
    private String comment;
    private String complementSequence;
    
    @OneToMany(targetEntity = Annotation.class, mappedBy = "plasmid", fetch = FetchType.LAZY)
    private List<Annotation> annotations;
    private boolean findingComplementSequence;
    private JLabel setLabelWithComplement;
    private boolean isAnnotating = false, hasBegunAnnotating = false;
    
    
    public Plasmid() {
        featureDisplayNamesHashMap = new HashMap<String, Integer>();
    }

    public Long getId() {
        return id;
    }
    
    public void setOligoMatches(ArrayList<OligoMatch> om) {
        oligoMatches = om;
    }
    
    public ArrayList<OligoMatch> getOligoMatches() {
        return oligoMatches;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public void setHasBegunAnnotating() {
        hasBegunAnnotating = true;
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
        if (!(object instanceof Plasmid)) {
            return false;
        }
        Plasmid other = (Plasmid) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.autogene.bio.entities.Plasmid[ id=" + id + " ]";
    }

    public boolean isFindingComplementSequence() {
        return findingComplementSequence;
    }

    public void setLabelWithComplementWhenDone(JLabel label) {
        setLabelWithComplement = label;
    }

    public void setIsAnnotating(boolean b) {
        isAnnotating = b;
    }

    public boolean isAnnotating() {
        return isAnnotating;
    }

    public HashMap<String, Integer> getFeatureDisplayNamesHashMap() {
        return featureDisplayNamesHashMap;
    }

    public boolean hasBegunAnnotating() {
        return hasBegunAnnotating;
    }
    
    private class FindComplementSequence implements Runnable {

        @Override
        public void run() {
            findingComplementSequence = true;
            complementSequence = findComplementSequence();
            findingComplementSequence = false; 
            fireDoneFindingComplementSequenceEvent();
        }
        
        public String findComplementSequence() {
            String comp = "";
            for(int i = 0; i < sequence.length(); i++) {
                char c = sequence.charAt(i);
                if(c == 'a') 
                    comp += "t";
                else if(c == 't')
                    comp += "a";
                else if(c == 'c')
                    comp += "g";
                else if(c == 'g')
                    comp += "c";
                else {
                    //TODO: other characters?
                    //JOptionPane.showMessageDialog(null, "Unrecognized DNA sequence character " + c + ". Plasmid import failed.");
                    Log.addText("--Import plasmid failed: Unrecognized DNA sequence character " + c + ".", LogEventType.FAILURE);
                    return null;
                }
            }

            return comp;
        }

      
        
    }
    
    private void fireDoneFindingComplementSequenceEvent() {
        if(complementSequence != null) 
            Log.addText("Done Finding Complement!", LogEventType.SUCCESS);
        
        if(setLabelWithComplement != null) {
            setLabelWithComplement.setText(complementSequence);
            setLabelWithComplement = null;
        }
    }
    
    public String getComplementSequence() {
        return complementSequence;
    }
    

    public void initComplementSequence() {
        findingComplementSequence = true;
        new Thread(new FindComplementSequence()).start();
    }
}
