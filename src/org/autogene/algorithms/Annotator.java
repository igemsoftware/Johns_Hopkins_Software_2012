

/*
 * test
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.algorithms;


import antlr.StringUtils;
import jaligner.Alignment;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.MatrixLoader;
import jaligner.matrix.MatrixLoaderException;
import java.awt.Color;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.core.bio.managers.FeatureManager;
import org.autogene.ui.cgview.ColorConstants;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.template.Sequence;
import org.biojava3.core.sequence.transcription.Frame;
import org.biojava3.core.sequence.transcription.TranscriptionEngine;


/**
 *
 * @author Emily
 */


public class Annotator 
{
 
    private Plasmid plasmid;
    private String sequence;
    private ArrayList<Annotation> matches;
    private int[] regions;
    private int[] complementRegions; 
    private ArrayList<Coordinates> tempFound;
    

    
    public HashMap<Annotation, PriorityQueue<Annotation>> collisions;
   
    
    public Annotator(Plasmid p)
    {
        Logger globalLogger = Logger.getLogger("global");
        Handler[] handlers = globalLogger.getHandlers();
        for(Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }

        matches = new ArrayList<Annotation>();
        plasmid = p;
        sequence = p.getSequence().toUpperCase();
        regions = new int[(sequence.length()/100)+1];
        complementRegions = new int[((new DNASequence(sequence).getReverseComplement().getSequenceAsString().length())/100)+1];
    }
    
    public static String invertDNA(String sequence) {
        System.out.println("invert dna " + sequence + " gives " + new DNASequence(sequence).getReverseComplement().getSequenceAsString());
        return new DNASequence(sequence).getReverseComplement().getSequenceAsString();
    }
    
    public ArrayList<Annotation> annotatePlasmid(double percentCovered, int alignmentType, double threshold, ArrayList<String> featureTypes) throws Exception 
    {   
        
        long start = System.currentTimeMillis();
        
        
        findPerfectMatches(featureTypes);
        
        int x = 0;
        
        int empty = 0;
        
        for(int i = complementRegions.length-1; i>=0; i--)
        {
            regions[x]+=complementRegions[i];
            
            if(regions[x]==0)
                empty++;
            x++;
        }       
        
        
        if((double)empty/regions.length < percentCovered)
            findImperfectMatches(alignmentType, threshold, featureTypes);
       
        
        
        long end = System.currentTimeMillis();
        long timeElapsed = end - start;
        
        System.out.println("Elapsed time: " +(timeElapsed / 1000)+ " sec\n");

        
        return matches;        
    }
    
    public ArrayList<Annotation> findPerfectMatches(ArrayList<String> featureTypes)
    {
        matches = new ArrayList<Annotation>();
        
        FeatureManager manager = new FeatureManager();
        List<Feature> tempFeatures = manager.findAll();
        System.out.println(tempFeatures);
        
        ArrayList<Feature> features = new ArrayList<Feature>();
        
        
        for(Feature f: tempFeatures) {
            if(f.getType() == null)
                continue;
            
            //System.out.println(f + " " + f.getName() + " " + f.getType());
            if(featureTypes.contains(f.getType().getName()))
                features.add(f);
        }
        
        int ss = matches.size();
        for(Feature f:features) {
            findPerfectMatch(f);
            if(matches.size() != ss) {
                for(int p = 0; p < matches.size(); p++) {
                    System.out.println("INSIDE " + matches.get(p).getFeature().getDisplayName());
                }
                ss = matches.size();
            }
        }
        
        for(int p = 0; p < matches.size(); p++) {
            System.out.println("Annotator " + matches.get(p).getFeature().getDisplayName());
        }
        
        fixCollisions();
        
        
        
        
        return matches;
    }
    
    private Feature makeCopyOfFeature(Feature feature) {
        Feature f = new Feature();
        f.setDescription(feature.getDescription());
        f.setDisplayName(feature.getDisplayName());
        f.setName(feature.getName());
        f.setOrganism(feature.getOrganism());
        f.setPartType(feature.getPartType());
        f.setSequence(feature.getSequence());
        f.setType(feature.getType());
        return f;
    }
    
    private void findPerfectMatch(Feature f)
    {
        
       String[] fragments;
       int length = 0;
       
       String fSeq = f.getSequence();
       
       DNASequence seq = new DNASequence(sequence);
       
       String complement = seq.getReverseComplement().getSequenceAsString();
       
       int l;
       
       Feature tempFeature = new Feature();
       tempFeature.setName(f.getName());
       tempFeature.setSequence(f.getSequence());
       tempFeature.setType(f.getType());
       tempFeature.setOrganism(f.getOrganism());
       //tempFeature.setId(f.getId());
       
       int ss = matches.size();
       if(sequence.contains(fSeq))
       {
            fragments = sequence.split(fSeq);
  
            for(int i = 0; i < fragments.length - 1; i++)
            {
                length = length + fragments[i].length();
                   
                Annotation a = new Annotation();
                
                a.setStart(length + 1);
                   
                length = length + fSeq.length();
                   
                a.setEnd(length);
   
                Feature ff = this.makeCopyOfFeature(f);
                
                a.setFeature(ff);
                a.setPlasmid(plasmid);
                a.setStrand(Annotation.StrandType.ORIGINAL);       
                a.setScore(1.0);
                a.setColor(ColorConstants.restrictionEnzymeColor);           
                
                //a = this.makeCopyOfAnnotation(a);
                for(int p = 0; p < matches.size(); p++) {
                    System.out.println("CCEEEE " + matches.get(p).getFeature().getDisplayName());
                }
                 setDisplayNameUsingHashMap(a);

                //updateDisplayNamesHashMap(a);
                for(int p = 0; p < matches.size(); p++) {
                    System.out.println("BEEEE " + matches.get(p).getFeature().getDisplayName());
                }
                 matches.add(a); 

               System.out.println("anotator adding " + a.getFeature().getDisplayName());
                for(int p = 0; p < matches.size(); p++) {
                    System.out.println("GGGG " + matches.get(p).getFeature().getDisplayName());
                } 
               
               Log.addText(f.getName() + " found at " + a.getStart() + ".." + a.getEnd() + ", score: " + a.getScore() + ", direction: original", LogEventType.NEUTRAL);
                
                l = a.getStart()/100;
                
                while(l <= a.getEnd()/100)
                {
                    regions[l]++;
                    l++;
                } 

            }
        
       }
       if(matches.size() != ss) {
           for(int p = 0; p < matches.size(); p++) {
                    System.out.println("III " + matches.get(p).getFeature().getDisplayName());
                }
       }
       length = 0;
    
//       if(complement.contains(fSeq))
//       {
//            fragments = complement.split(fSeq);
//  
//            for(int i = 0; i < fragments.length - 1; i++)
//            {
//                length = length + fragments[i].length();
//                   
//                Annotation a = new Annotation();
//                
//                a.setStart(length + 1);
//                   
//                length = length + fSeq.length();
//                   
//                a.setEnd(length);
//
//                    
//                a.setFeature(tempFeature);
//                    
//                a.setPlasmid(plasmid);
//                a.setStrand(Annotation.StrandType.COMPLEMENT);       
//                a.setScore(1.0);
//                a.setColor(ColorConstants.restrictionEnzymeColor);           
//                
//                updateDisplayNamesHashMap(a);
//                matches.add(a); 
//                Log.addText(f.getName() + " found at " + a.getStart() + ".." + a.getEnd() + ", score: " + a.getScore() + ", direction: complement", LogEventType.NEUTRAL);
//                
//                l = a.getStart()/100;
//                
//                while(l <= a.getEnd()/100)
//                {
//                    complementRegions[l]++;
//                    l++;
//                }
//                                
//            }
//       }
       
        
    }
    
 
    
    public ArrayList<Annotation> findImperfectMatches(int alignmentType, double threshold, ArrayList<String> featureTypes) throws MatrixLoaderException
    {
        
        matches = new ArrayList<Annotation>();
        
        FeatureManager manager = new FeatureManager();
        List<Feature> tempFeatures = manager.findAll();
        
        ArrayList<Feature> features = new ArrayList<Feature>();
        
        
        for(Feature f: tempFeatures) {
            if(f.getType() == null) {
                continue;
            }
            if(featureTypes.contains(f.getType().getName()))
                features.add(f);
        }
        
        
        Map<Frame, Sequence<AminoAcidCompound>> targetFrames = getProteinSequence(sequence);
        Map<Frame, Sequence<AminoAcidCompound>> queryFrames;
        
        int frameFactor;
            
        Annotation.StrandType tempDir;
            
        for(Feature f: features)
        {               
            if(f.getType().getName().equals("Restriction Enzyme"))
            {
                findPerfectMatch(f);                    
            }
            else if(alignmentType == 1)
            {    
                tempFound = new ArrayList<Coordinates>();
                JAlignDNA(sequence, f, threshold, Annotation.StrandType.ORIGINAL, 0);
                    
                tempFound = new ArrayList<Coordinates>();
                JAlignDNA((new DNASequence(sequence)).getReverseComplement().getSequenceAsString(), f, threshold, Annotation.StrandType.COMPLEMENT, 0);
            }
            else
            {  
                queryFrames = getProteinSequence(f.getSequence());
                tempFound = new ArrayList<Coordinates>();
                for(Frame firstFrame : targetFrames.keySet())
                {
                    if(firstFrame.toString().contains("ONE"))
                        frameFactor = 0;
                    else if(firstFrame.toString().contains("TWO"))
                        frameFactor = 1;
                    else
                        frameFactor = 2; 
                            
                         
                        
                    if(firstFrame.toString().contains("_"))
                        tempDir = Annotation.StrandType.COMPLEMENT;
                    else
                        tempDir = Annotation.StrandType.ORIGINAL;
                        
                        
                        
                    for(Sequence<AminoAcidCompound> secondFrame : queryFrames.values())
                    {
                            
                        JAlignProtein(targetFrames.get(firstFrame).getSequenceAsString(), f, secondFrame.getSequenceAsString(), threshold, tempDir, frameFactor, firstFrame);
                    }
                    
                }                                
            }
        }
        
        
        fixCollisions();
        
        return matches;
    }
       
    private void JAlignDNA(String seq, Feature f, double threshold, Annotation.StrandType direction, int start) throws MatrixLoaderException
    {                     
        jaligner.Sequence s1 = new jaligner.Sequence(seq, "plasmid", "target", 1);
        jaligner.Sequence s2 = new jaligner.Sequence(f.getSequence(), "feature", "query", 1);
                                                
        Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 10f, 0.5f);     
   
        double score = (double)alignment.getIdentity()/f.getSequence().length();
        
        Coordinates tempCoordinates;
        
        String[] temp;
        String tempString;
        String[] tempSplit;
        
        String tempDir = "original";
        
        if(direction == Annotation.StrandType.COMPLEMENT)
            tempDir = "complement";
                
        
        temp = ( new Pair().format(alignment) ).split("\\s+");
	if(score >= threshold && temp.length > 1)
        {
            Annotation a = new Annotation();
                                  
            a.setStart(Integer.parseInt(temp[1]) + start);
            a.setEnd(a.getStart() + f.getSequence().length());
            
            tempCoordinates = new Coordinates(a.getStart(), a.getEnd());

            
            if(!tempFound.contains(tempCoordinates))
            {             
                if(direction == Annotation.StrandType.COMPLEMENT)
                {
                    Feature tempFeature = new Feature();
                    tempFeature.setName(f.getName());
                    tempFeature.setSequence(f.getSequence());
                    tempFeature.setType(f.getType());
                    tempFeature.setOrganism(f.getOrganism());
                    //tempFeature.setId(f.getId());
                    a.setFeature(tempFeature);
                }else
                {
                    a.setFeature(f);
                }
                
                a.setPlasmid(plasmid);
                a.setStrand(direction);        
                a.setScore(score);
                a.setReport(new Pair().format(alignment));
                
                if(a.getFeature().getType().getName().equals("Restriction Enzyme"))
                    a.setColor(ColorConstants.restrictionEnzymeColor);
                else if(a.getFeature().getType().getName().equals("Gene"))
                    a.setColor(ColorConstants.geneColor);
                else if(a.getFeature().getType().getName().equals("Promoter"))
                    a.setColor(ColorConstants.promoterColor);
                else if(a.getFeature().getType().getName().equals("Terminator"))
                    a.setColor(ColorConstants.terminatorColor);
                else
                    a.setColor(ColorConstants.defaultColor);
          
                tempFound.add(tempCoordinates);
                updateDisplayNamesHashMap(a);
                matches.add(a);
                Log.addText(f.getName() + " found at " + a.getStart() + ".." + a.getEnd() + ", score: " + a.getScore() + ", direction: " + tempDir, LogEventType.NEUTRAL);
                               System.out.println("anotator adding " + a.getFeature().getDisplayName());

                tempSplit = seq.split("(?<=\\G.{"+Integer.parseInt(temp[1])+"})");
                   
                tempString = StringUtils.stripBack(seq, tempSplit[1]);

                /*if((double)tempString.length() >= (f.getSequence().length() * threshold))
                    JAlignDNA(tempString, f, threshold, direction, start);
            
                tempSplit = seq.split("(?<=\\G.{"+Integer.parseInt(temp[3])+"})");
            
                tempString = StringUtils.stripFront(seq, tempSplit[0]);
                
                if((double)tempString.length() >= (f.getSequence().length() * threshold))
                    JAlignDNA(tempString, f, threshold, direction, a.getEnd()+1);       */      
            }     
        }
    }
    
    private void JAlignProtein(String seq, Feature f, String featureSeq, double threshold, Annotation.StrandType direction, int start, Frame firstFrame) throws MatrixLoaderException
    {                
        jaligner.Sequence s1 = new jaligner.Sequence(seq, "plasmid", "target", 2);
        jaligner.Sequence s2 = new jaligner.Sequence(featureSeq, "feature", "query", 2);
                
        Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 10f, 0.5f);
        
        double score = alignment.getSimilarity()/featureSeq.length();
        
        Coordinates tempCoordinates;
        
        String[] temp = ( new Pair().format(alignment) ).split("\\s+");
        
        int firstCoord;
        int secondCoord;
        
        String tempString;
        String[] tempSplit;
        
        String tempDir = "original";
        
        if(direction == Annotation.StrandType.COMPLEMENT)
            tempDir = "complement";
        
	if(score >= threshold && temp.length > 1)
        {   
            Annotation a = new Annotation();
         
            firstCoord = (Integer.parseInt(temp[1]) * 3) - 2;
            secondCoord = (Integer.parseInt(temp[3]) *3);
                               
            a.setStart(firstCoord + start);
            a.setEnd(secondCoord + start);
                        
            tempCoordinates = new Coordinates(a.getStart(), a.getEnd());
            
            if(!tempFound.contains(tempCoordinates))
            {             
                
                if(direction == Annotation.StrandType.COMPLEMENT)
                {
                    Feature tempFeature = new Feature();
                    tempFeature.setName(f.getName());
                    tempFeature.setSequence(f.getSequence());
                    tempFeature.setType(f.getType());
                    tempFeature.setOrganism(f.getOrganism());
                    //tempFeature.setId(f.getId());
                    a.setFeature(tempFeature);
                }else
                {
                    a.setFeature(f);
                }
                
                
                a.setPlasmid(plasmid);
                a.setStrand(direction);        
                a.setScore(score);
                a.setReport(new Pair().format(alignment));
                
                if(a.getFeature().getType().getName().equals("Gene"))
                    a.setColor(ColorConstants.geneColor);
                else if(a.getFeature().getType().getName().equals("Promoter"))
                    a.setColor(ColorConstants.promoterColor);
                else if(a.getFeature().getType().getName().equals("Terminator"))
                    a.setColor(ColorConstants.terminatorColor);
                else
                    a.setColor(ColorConstants.defaultColor);
                          
                tempFound.add(tempCoordinates);
                updateDisplayNamesHashMap(a);
                matches.add(a);
                               System.out.println("anotator adding " + a.getFeature().getDisplayName());

                Log.addText(f.getName() + " found at " + a.getStart() + ".." + a.getEnd() + ", score: " + a.getScore() + ", direction: " + tempDir, LogEventType.NEUTRAL);
                                
                tempSplit = seq.split("(?<=\\G.{"+Integer.parseInt(temp[1])+"})");
                   
                tempString = StringUtils.stripBack(seq, tempSplit[1]);

                if((double)tempString.length() >= (featureSeq.length() * threshold))
                    JAlignProtein(tempString, f, featureSeq, threshold, direction, start, firstFrame);
            
                tempSplit = seq.split("(?<=\\G.{"+Integer.parseInt(temp[3])+"})");
            
                tempString = StringUtils.stripFront(seq, tempSplit[0]);
                
                if((double)tempString.length() >= (featureSeq.length() * threshold))
                    JAlignProtein(tempString, f, featureSeq, threshold, direction, a.getEnd()+1, firstFrame);
                
            }
            
            for(Coordinates x: tempFound)
                System.out.println(x.getStart() + ".." + x.getEnd());
            
            System.out.println("\n\n");
            
        }
                
    }
       
    public static Map<Frame, Sequence<AminoAcidCompound>> getProteinSequence(String seq) 
    {
        
        DNASequence dna = new DNASequence(seq.toUpperCase());
        
        TranscriptionEngine te = TranscriptionEngine.getDefault();
        Frame[] frames = Frame.getAllFrames();
        Map<Frame, Sequence<AminoAcidCompound>> results = te.multipleFrameTranslation(dna, frames);
        
        return results;
    }  
    
    private PriorityQueue<Annotation> findCollisions(Annotation annot)
    {
        PriorityQueue<Annotation> list = new PriorityQueue<Annotation>();
        
        Coordinates c = new Coordinates(annot.getStart(), annot.getEnd());
        Coordinates tempC;
        
        for(Annotation a: matches)
        {
            tempC = new Coordinates(a.getStart(), a.getEnd());
            if(c.equals(tempC) && !a.getFeature().getName().equals(annot.getFeature().getName()) && a.getStrand() == annot.getStrand())
                list.add(a);
        }
        
        return list;
            
    }
    
    private void fixCollisions()
    {
        collisions = new HashMap<Annotation, PriorityQueue<Annotation>>();
        
        for(Annotation a: matches)
        {
            collisions.put(a, findCollisions(a));
        }        
    }
    
    private void updateDisplayNamesHashMap(Annotation a) {
        for(int p = 0; p < matches.size(); p++) {
                    System.out.println("ABOUT TO UPDATE DISPLAY NAMES HASH MAP " + matches.get(p).getFeature().getDisplayName());
                }
        if(plasmid.getFeatureDisplayNamesHashMap().containsKey(a.getFeature().getName())) {
                    int cur = plasmid.getFeatureDisplayNamesHashMap().get(a.getFeature().getName());
                    cur++;
                    a.getFeature().setDisplayName(a.getFeature().getName()+" ("+cur+")");
                    plasmid.getFeatureDisplayNamesHashMap().put(a.getFeature().getName(),cur);
                    System.out.println("AAAA setting display name " + a.getFeature().getName() + " " + cur);
                }
                else {
                    a.getFeature().setDisplayName(a.getFeature().getName()+" (1)");
                    plasmid.getFeatureDisplayNamesHashMap().put(a.getFeature().getName(), 1);
                    //Log.addText("setDisplayName: " + a.getFeature().getName() + "," + "1" + ": " + a.getFeature().getDisplayName());

                    System.out.println("AAAA setting display name " + a.getFeature().getName() + " 1");

                }
        
        for(int p = 0; p < matches.size(); p++) {
                    System.out.println("DONE UPDATING DISPLAY NAMES HASH MAP " + matches.get(p).getFeature().getDisplayName());
                }
    }

    private void setDisplayNameUsingHashMap(Annotation a) {
        HashMap<String, Integer> hashMap = plasmid.getFeatureDisplayNamesHashMap();
        String featureName = a.getFeature().getName();
        
        if(hashMap.containsKey(featureName)) {
            int cur = hashMap.get(a.getFeature().getName());
            cur++;
            a.getFeature().setDisplayName(a.getFeature().getName()+" ("+cur+")");
            hashMap.put(a.getFeature().getName(),cur);
            System.out.println("AAAA setting display name " + a.getFeature().getName() + " " + cur);
        }
        else {
                    a.getFeature().setDisplayName(a.getFeature().getName()+" (1)");
                    plasmid.getFeatureDisplayNamesHashMap().put(a.getFeature().getName(), 1);
                    //Log.addText("setDisplayName: " + a.getFeature().getName() + "," + "1" + ": " + a.getFeature().getDisplayName());

                    System.out.println("AAAA setting display name " + a.getFeature().getName() + " 1");

                }
    }

    
    
}

//fix collision checking stuff -- diff system for forward and reverse
//individual lists for each iteration of outer loop

