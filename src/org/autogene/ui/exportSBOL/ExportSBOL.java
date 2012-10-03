/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.exportSBOL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.FeatureType;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;
import org.sbolstandard.core.*;
import org.sbolstandard.core.util.SequenceOntology;

/**
 *
 * @author georgechen
 */
public class ExportSBOL {
    
    //todo: figure out what to input as a URI
    public static int i = 0;
    
    public static void export(Plasmid plasmid, File file) {
        SBOLDocument document = SBOLFactory.createDocument();
        //fill up the annotations
        List<SequenceAnnotation> seqAnnotation = new LinkedList<SequenceAnnotation>();
        int i = 0;
        for(Annotation a:plasmid.getAnnotations()) {
            URI sofa = convertSofaTypeToSBOL(a.getFeature().getType());
            //TODO: create different URI's for each annotation object.
            URI data = URI.create("http://igem" + i + ".org");
            DnaComponent dnaComponent = createAnnotationDnaComponent(data,a.getFeature().getDisplayName(),a.getFeature().getName(),sofa);
            
            StrandType st = convertStrandToSBOL(a.getStrand());
            URI annotation = URI.create("http://sbols.org/anot#" + i);
            SequenceAnnotation sa = createAnnotation(annotation, a.getStart(), a.getEnd(), st , dnaComponent);
            seqAnnotation.add(seqAnnotation.size(), sa);
            i++;
        }
        //create the sequence
        URI site = URI.create("http://www.autogene.org");
        DnaSequence plasmidSequence = createDNASequence(plasmid.getSequence(), URI.create("http://www.autogene2.org"));
        DnaComponent dnaComponent = createPlasmidDnaComponent(site, plasmid.getName(), plasmid.getName(), plasmidSequence, seqAnnotation);
        document.addContent(dnaComponent);
 
        //now export it out
        try {     
            System.out.println(document.toString());
            SBOLFactory.write(document, new FileOutputStream(file));
        }
        catch(IOException e) {
            Log.addText("Failed to Export to SBOL", LogEventType.FAILURE);
        }
        
    }
    private static DnaSequence createDNASequence(String sequence, URI uri){
        DnaSequence dnaSequence = SBOLFactory.createDnaSequence();
        dnaSequence.setNucleotides(sequence);
        dnaSequence.setURI(uri);
        return dnaSequence;
    }
    private static DnaComponent createDnaComponent(URI uri, String displayId, String name, String description, DnaSequence dnaSequence,URI sofaType,List<SequenceAnnotation> seqAnnotation) {
        DnaComponent dnaComponent = SBOLFactory.createDnaComponent();
        dnaComponent.setURI(uri);
	dnaComponent.setDisplayId(displayId);
	dnaComponent.setName(name);
	dnaComponent.setDescription(description);
	dnaComponent.setDnaSequence(dnaSequence);
        if(sofaType != null)
            dnaComponent.addType(sofaType);
        if(seqAnnotation.size() > 0) {
            for(SequenceAnnotation sa: seqAnnotation) {
                dnaComponent.addAnnotation(sa);
            }
        }
        return dnaComponent;
    }
    
    private static DnaComponent createPlasmidDnaComponent(URI uri, String displayId, String name, DnaSequence dnaSequence,List<SequenceAnnotation> seqAnnotation) {
        return createDnaComponent(uri, displayId, name, null, dnaSequence, null, seqAnnotation);
    }
    
    private static DnaComponent createAnnotationDnaComponent(URI uri, String displayId, String name, URI sofaId) {
        DnaComponent dnaComponent = SBOLFactory.createDnaComponent();
        dnaComponent.setURI(uri);
	dnaComponent.setDisplayId(displayId);
	dnaComponent.setName(name);
        dnaComponent.addType(sofaId);
        return dnaComponent;
    }
    
    private static SequenceAnnotation createAnnotation(URI uri, int start, int end, StrandType type, DnaComponent dnaComponent) {
        SequenceAnnotation seqAnnotation = SBOLFactory.createSequenceAnnotation();
        seqAnnotation.setURI(uri);
        seqAnnotation.setBioStart(start);
        seqAnnotation.setBioEnd(end);
        seqAnnotation.setStrand(type);
        seqAnnotation.setSubComponent(dnaComponent);
        return seqAnnotation;
    }
    
    /**Helps to convert the Autogene enumType StrandType to an accept SBOL StrandType
     * 
     */
    private static StrandType convertStrandToSBOL(Annotation.StrandType strandType) {
        if(strandType == Annotation.StrandType.COMPLEMENT)
            return StrandType.NEGATIVE;
        else if(strandType == Annotation.StrandType.ORIGINAL) 
            return StrandType.POSITIVE;
        return null;
    }
    /**
     * Helps to convert SofaId in autogene to URI for sofa types in SBOL
     */
    private static URI convertSofaTypeToSBOL(FeatureType featureType) {
        String sofaId = featureType.getSofa_id();
        sofaId = sofaId.substring(0, sofaId.indexOf(":")) + "_" + sofaId.substring(sofaId.indexOf(":")+1);
        System.out.println(SequenceOntology.type(sofaId));
        i++;
        return SequenceOntology.type(sofaId); 
    }
}
