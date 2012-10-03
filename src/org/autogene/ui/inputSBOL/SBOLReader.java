/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputSBOL;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.FeatureType;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.core.bio.managers.FeatureTypeManager;
import org.autogene.ui.cgview.ColorConstants;
import org.sbolstandard.core.DnaComponent;
import org.sbolstandard.core.SBOLDocument;
import org.sbolstandard.core.SBOLFactory;
import org.sbolstandard.core.SequenceAnnotation;
import org.sbolstandard.core.StrandType;

/**
 *
 * @author georgechen
 */
public class SBOLReader {
    public boolean alreadyHasAnnotations;
    private static FeatureTypeManager ftm;
    
    public SBOLReader() {
        alreadyHasAnnotations = false;
        ftm = new FeatureTypeManager();
    }
                  
    public Plasmid read(File file) throws IOException {
        Plasmid plasmid = new Plasmid();
            SBOLDocument document = SBOLFactory.read(new FileInputStream(file));
            //get the plasmid sequence
            DnaComponent dnaComponent = (DnaComponent) document.getContents().iterator().next();
            plasmid.setSequence(dnaComponent.getDnaSequence().getNucleotides());
            plasmid.setName(dnaComponent.getName());
            
            plasmid.setAnnotations(new ArrayList<Annotation>());
            //fill annotations
            List<SequenceAnnotation> annotations = dnaComponent.getAnnotations();
            
            for(SequenceAnnotation a: annotations) {
                Annotation plasmidAnnotate = new Annotation();
                plasmidAnnotate.setFeature(new Feature());
                //fill up the Feature data members
                plasmidAnnotate.getFeature().setName(a.getSubComponent().getName());
                plasmidAnnotate.getFeature().setDisplayName(a.getSubComponent().getDisplayId());
                plasmidAnnotate.getFeature().setDescription(a.getSubComponent().getDescription());
                plasmidAnnotate.getFeature().setSequence(plasmid.getSequence().substring(a.getBioStart(), a.getBioEnd()+1));
                plasmidAnnotate.getFeature().setType(convertToFeatureTypes(a.getSubComponent().getTypes()));
                //full up other Annotation data members
                plasmidAnnotate.setStart(a.getBioStart());
                plasmidAnnotate.setEnd(a.getBioEnd());
                plasmidAnnotate.setStrand(convertToStrandType(a.getStrand()));
                //set a default score
                plasmidAnnotate.setScore(1.0);
                plasmidAnnotate.setColor(ColorConstants.getColorForAnnotation(plasmidAnnotate));
                plasmid.getAnnotations().add(plasmidAnnotate);
            }
        alreadyHasAnnotations = true;    
        return plasmid;
    }
    //TODO: manage entities that have more than one featureType.
    //helps to convert sbol sofa types to FeatureType. Manages only one featureType at the moment though
    private static FeatureType convertToFeatureTypes(Collection<URI> featureTypes) {
        FeatureType featureType = new FeatureType();
        
        for(URI c: featureTypes) {
            String sofa = c.getPath().substring(c.getPath().lastIndexOf("/") + 1);
            sofa = sofa.substring(0, sofa.indexOf("_")) + ":" + sofa.substring(sofa.indexOf("_")+1);
            featureType = ftm.findBysofa_id(sofa);
        }
        return featureType;
    }
    //Helps to convert the strandtype of SBOL to strandtyp of annotation
    private static Annotation.StrandType convertToStrandType(StrandType st) {
        if (st.equals(StrandType.NEGATIVE)) {
            return Annotation.StrandType.COMPLEMENT;
        }
        return Annotation.StrandType.ORIGINAL;
    }
}
