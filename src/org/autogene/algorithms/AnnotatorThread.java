/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.algorithms;

import jaligner.matrix.MatrixLoaderException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.inputframe.AnnotationOptionsFrame;
import org.autogene.ui.inputframe.ImportPlasmidFrame;
import org.autogene.ui.inputframe.NewImportPlasmidFrame;
import org.autogene.ui.loggerframe.Log;
import org.autogene.ui.main.AutogeneFrame;

/**
 *
 * @author Robert
 */
public class AnnotatorThread implements Runnable {
    private final Plasmid plasmid;
    private final AutogeneFrame autogeneFrame;
    private final AnnotationOptionsFrame annotationOptions;

    public AnnotatorThread(Plasmid p, AutogeneFrame af, AnnotationOptionsFrame as) {
        
        plasmid = p;
        autogeneFrame = af;
        annotationOptions = as;
        
        this.start();
        
    }
    
    public void start() {
        new Thread(this).start();
    }

    
    
    @Override
    public void run() {
       
        
        
        Log.addText("Annotating plasmid \"" + plasmid.getName() + "\"...");
        long T1 = System.currentTimeMillis();
        Annotator a = new Annotator(plasmid);
        
        plasmid.setHasBegunAnnotating();
        plasmid.setIsAnnotating(true);
        autogeneFrame.updatePlasmidList();
        
        
        //annotate
        if(annotationOptions.perfectMatchingIsSelected())
            
        
            plasmid.setAnnotations(new Annotator(plasmid).findPerfectMatches(annotationOptions.getTypes()));
        else {
            if(annotationOptions.dnaIsSelected())
                try {
                plasmid.setAnnotations(new Annotator(plasmid).findImperfectMatches(1, (double)(annotationOptions.getMatchingThresholdValue())/100, annotationOptions.getTypes()));
            } catch (MatrixLoaderException ex) {
                Logger.getLogger(AnnotatorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            else
                try {
                plasmid.setAnnotations(new Annotator(plasmid).findImperfectMatches(2, (double)annotationOptions.getMatchingThresholdValue()/100, annotationOptions.getTypes()));
            } catch (MatrixLoaderException ex) {
                Logger.getLogger(AnnotatorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
       
        
        //find oligo matches
        OligoMatcher om = new OligoMatcher(plasmid.getSequence());
        //ArrayList<OligoMatch> omm = om.findOligos();
        om.findOligos();
                ArrayList<OligoMatch> omm = om.matches;

        System.out.println("FOUND OLIGOS: " + omm);

        plasmid.setOligoMatches(omm);
         
        plasmid.setIsAnnotating(false);
        autogeneFrame.updatePlasmidList();

        long diff = System.currentTimeMillis() - T1;
        
        Log.addText("Done annotating \"" + plasmid.getName() + ".\" (Process took " + new DecimalFormat("#.##").format((diff/1000.0)) + " seconds)", LogEventType.SUCCESS);
   
        
        
    }
    
}
