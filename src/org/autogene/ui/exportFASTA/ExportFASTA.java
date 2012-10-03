/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.exportFASTA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojava.bio.symbol.Alphabet;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojavax.Namespace;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichSequence;

/**
 *
 * @author georgechen
 */
public class ExportFASTA {
    public ExportFASTA() {
    }
    /* exports a plasmid sequence to a given file in FASTA format
     * @param File file the file to export out to
     * @param Plasmid plasmid the plasmid that contains the sequence
     * @return nothing
     */
    public void export(File file, Plasmid plasmid) {
        try {
            RichSequence dna;
            FileOutputStream fileout = new FileOutputStream(file);
            String sequence = plasmid.getSequence();
            String name = plasmid.getName();
            Namespace ag = new SimpleNamespace(name);
            Alphabet molType = DNATools.getDNA();
            dna = RichSequence.Tools.createRichSequence(name, sequence, molType);
            dna.setDescription(plasmid.getComment());
            RichSequence.IOTools.writeFasta(fileout, dna, ag);
            
            }
        catch (IllegalSymbolException e) {
            e.printStackTrace();
            Log.addText("Failed to Export FASTA", LogEventType.FAILURE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(BioException e) {
            e.printStackTrace();
        }
    }
    /* exports a feature sequence to a given file in FASTA format
     * @param File file the file to export out to
     * @param Feature feature the feature that contains the sequence
     * @return nothing
     */
    public void export(File file, List<Feature> feature) {
        
        try{
            SequenceDB db = new HashSequenceDB();
            FileOutputStream fileout = new FileOutputStream(file);
            
            for(Feature f: feature) {
                RichSequence dna;
                String sequence = f.getSequence();
                String name = f.getName(); 
                Alphabet molType = DNATools.getDNA();
                dna = RichSequence.Tools.createRichSequence(name, sequence, molType);
                dna.setDescription(f.getDescription());
                db.addSequence(dna);
            }
            Namespace ag = new SimpleNamespace("");           
            RichSequence.IOTools.writeFasta(fileout,db.sequenceIterator(), ag);
            
        }
        catch (IllegalSymbolException e) {
            e.printStackTrace();
            Log.addText("Failed to Export FASTA", LogEventType.FAILURE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(BioException e) {
            e.printStackTrace();
        }
    }
    
    
    public void export(File file, Feature feature) {
        try {
            RichSequence dna;
            FileOutputStream fileout = new FileOutputStream(file);
            String sequence = feature.getSequence();
            String name = feature.getName();
            Namespace ag = new SimpleNamespace(name);
            Alphabet molType = DNATools.getDNA();
            dna = RichSequence.Tools.createRichSequence(name, sequence, molType);
            dna.setDescription(feature.getDescription());
            RichSequence.IOTools.writeFasta(fileout, dna, ag);
            }
        catch (IllegalSymbolException e) {
            e.printStackTrace();
            Log.addText("Failed to Export FASTA", LogEventType.FAILURE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(BioException e) {
            e.printStackTrace();
        }
    }

}
