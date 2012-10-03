/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.Oligo;
import org.autogene.core.bio.managers.OligoManager;
import org.biojava3.core.sequence.DNASequence;


/**
 *
 * @author Emily
 */
public class OligoMatcher {
    
    
    private String sequence;
    public ArrayList<OligoMatch> matches;
    public OligoMatcher(String plasmidSequence)
    {
        //Logger.getRootLogger().removeAllAppenders();

        sequence = plasmidSequence;
        matches = new ArrayList<OligoMatch>();
    }
    public ArrayList<OligoMatch> findOligos()
    {
        
        OligoManager manager = new OligoManager();
        List<Oligo> oligos = manager.findAll();
        
        for(Oligo oligo:oligos)
            match(oligo.getSequence(), oligo.getDescription(), oligo.getId());
        
        
        
        return matches;
    }  
    public void match(String oligo, String name, String id)
    {
        
        
        DNASequence dna = new DNASequence(sequence.toUpperCase());
        
        String complement = dna.getReverseComplement().getSequenceAsString();
        
        String twenty;
        char[] rest;
        
        if(oligo.length() >= 20)
        {
            twenty = oligo.substring(oligo.length()-20);
        
            
            if(oligo.length() == 20)
                rest = new char[0];
            else
                rest = oligo.substring(0, oligo.length()-21).toCharArray();
        }
        else
        {
            twenty = oligo;
            rest = new char[0];
        }
        
        
        
        //System.out.println(oligo);
       // System.out.println(twenty);
        
        //System.out.println(sequence);
        
        String[] split;
        split = sequence.split(twenty, -1);
        
        
        //System.out.println("Split size: "+split.length);
        //ArrayList<Coordinates> tempMatches = new ArrayList<Coordinates>();
        //ArrayList<Coordinates> complementMatches = new ArrayList<Coordinates>();
        
        int firstCoord;
        int secondCoord;
        int length = 1;

        int i = rest.length-1;
        
        int x;
        
        String fragment;
    
        //System.out.println(split.length);
        
        int tempIndex = split.length-1;
        if(split.length==0)
            tempIndex++;
        //else if(sequence.substring(sequence.length()-21).contains(twenty))
          //  tempIndex++;
        
        //for(String s:split)
         //   System.out.println(s);
        
        for(int p = 0; p < tempIndex; p++)
        {
            
            
            fragment = split[p];
            x = fragment.length()-1;
            length += fragment.length();
            firstCoord = length;
            
            length += twenty.length();
            
            secondCoord = length-1;
            
            if(rest.length != 0)
            {  
            
                while(rest[i] == fragment.charAt(x))
                {
                    i--;
                    x--;
                    firstCoord--;
                
                    if(i < 0)
                        break;
                }
            }
            
            
            matches.add(new OligoMatch(id, name, firstCoord, secondCoord, "original"));
            //System.out.println(name+": original-" + firstCoord + ".." + secondCoord);
            
        }
        
        split = complement.split(twenty, -1);
        length = 1;
        
        
        tempIndex = split.length-1;
        if(split.length==0)
            tempIndex++;
        //else if(complement.substring(sequence.length()-21).contains(twenty))
          //  tempIndex++;
        
        
        
        for(int p = 0; p < tempIndex; p++)
        {
            
            fragment = split[p];
            x = fragment.length()-1;
            
            length += fragment.length();
            
            firstCoord = length;
            
            length += twenty.length();
            
            secondCoord = length-1;
            
            if(rest.length != 0)
            {  
                while(rest[i] == fragment.charAt(x))
                {
                    i--;
                    x--;
                    firstCoord--;
                
                    if(i == 0)
                        break;
                }
            }
            matches.add(new OligoMatch(id, name, firstCoord, secondCoord, "complement"));
            
            System.out.println(name+": complement-" + firstCoord + ".." + secondCoord);
            
        }
        
        
        //coords: sequence length minus coord in reverse or complement
        
        //start, stop, threshold, size (length?)
        
        //threshold??
        //match can only be back part of oligo, what about matches in front, middle?
        
        
    }
       
    public static void main(String args[])
    {
        
        OligoMatcher matcher = new OligoMatcher("ACCCGTCAGCCTGAGCTTGCCACCCGTCAGCCTGAGCTTGCCACCCGTCAGCCTGAGCTTGCC");
        
       /* OligoManager manager = new OligoManager();
        List<Oligo> oligos = manager.findAll();
        
        for(Oligo oligo:oligos)
            System.out.println("\n\n\n\n\n" + "oligo: "+ oligo.getSequence()+"\n\n\n\n");
        
        
        */
        
        matcher.match("CCCGTCAGCCTGAGCTTGCC", "", "");
        
        System.out.println("Match size:" + matcher.matches.size());
        
        
    }
}
//CCCGTCAGCCTGAGCTTGCCCCCGTCAGCCTGAGCTTGCCCCCGTCAGCCTGAGCTTGCC