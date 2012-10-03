/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.exportGenBank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Annotation.StrandType;
import org.autogene.core.bio.entities.Plasmid;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.bio.symbol.Alphabet;

import org.biojavax.Note;
import org.biojavax.SimpleNote;
import org.biojavax.bio.seq.Position;
import org.biojavax.bio.seq.SimplePosition;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.SimpleRichLocation;
import org.biojavax.ontology.ComparableTerm;
import org.biojavax.ontology.SimpleComparableOntology;

/**
 *
 * @author georgechen
 */
public class ExportGenBank {
    
 //   private File genBankFile;
    private PrintWriter genBankWriter;
    private static SimpleDateFormat genBankFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
    
    public ExportGenBank() { 
    }
    
    /* The overload of exportFile
     * Creates a ApE format, circular synthetic DNA plasmid
     * @param File genBank the name of the genBank file
     * @param Plasmid plasmid the plasmid to be converted to a genBank file
     */
    public void exportFile(File genBank, Plasmid plasmid) throws IOException {
        exportFile(genBank, FileType.APE, DNAType.CIRCULAR, MoleculeType.DNA, GenBankDivision.SYN, plasmid);
    }
    
    /* exports a genBank file based on the given inputs
     * @param
     */
    public void exportFile(File genBank, FileType fileType, DNAType dnaType, MoleculeType moleculeType,GenBankDivision genBankDivision,Plasmid plasmid) throws IOException {
        FileOutputStream file = new FileOutputStream(genBank);
        try {
            
            
            //set up notes for additional information
            HashSet<Note> setNotes = new HashSet<Note>();
            
            //figure out the moleculeType
            Alphabet molType = Alphabet.EMPTY_ALPHABET;
            
            if(moleculeType ==MoleculeType.DNA || moleculeType == MoleculeType.MS_DNA || moleculeType == MoleculeType.SS_DNA)
                molType= DNATools.getDNA();
            else if(moleculeType == MoleculeType.RNA || moleculeType == MoleculeType.MS_RNA || moleculeType == MoleculeType.SS_RNA)
                molType = RNATools.getRNA();
            
            SimpleNote strandedness;
            if(moleculeType == MoleculeType.MS_DNA || moleculeType == MoleculeType.MS_RNA || moleculeType == MoleculeType.MS_NA) {
                strandedness = new SimpleNote(RichSequence.Terms.getStrandedTerm(),"mixed", setNotes.size()+1);
                setNotes.add(strandedness);
            }
            else if(moleculeType == MoleculeType.SS_DNA || moleculeType == MoleculeType.SS_RNA || moleculeType == MoleculeType.SS_NA) {
                strandedness = new SimpleNote(RichSequence.Terms.getStrandedTerm(),"single", setNotes.size()+1);
                setNotes.add(strandedness);
            }
            
            //set the plasmid sequence
            org.biojavax.bio.seq.RichSequence rs = org.biojavax.bio.seq.RichSequence.Tools.createRichSequence(plasmid.getName(), plasmid.getSequence(), molType);   
            rs.setDescription("");
           
            
            //set date
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            Note date = new SimpleNote(RichSequence.Terms.getDateUpdatedTerm(),format.format(now).toUpperCase(),1);  
            setNotes.add(date);
            
            rs.setNoteSet(setNotes);
            
            //define if DNA is circular or linear
            if(dnaType == DNAType.CIRCULAR) {
                rs.setCircular(true);
            }
            else if(dnaType == DNAType.LINEAR) {
                rs.setCircular(false);
            }
            //set the genbank division
            rs.setDivision(getGenDivision(genBankDivision));
            
            //initiate the feature set
            HashSet<Feature> features = new HashSet<Feature>();
            rs.setFeatureSet(features);
            
            for(Object o : plasmid.getAnnotations()) {
                Annotation a = (Annotation) o;
                //check if the annotation is a Restriction Enzyme and remove it if it is.
              //  if(a.getFeature().getType().getName().equals("Restriction Enzyme")) {
              //      continue;
              //  }
                //create the feature
                StrandedFeature.Template ft = new StrandedFeature.Template();
                //set position
                Position start = new SimplePosition(a.getStart());
                Position end = new SimplePosition(a.getEnd());
                
                //set Strand and position
                if(a.getStrand() == StrandType.ORIGINAL)  {
                    ft.location = new SimpleRichLocation(start,end, 0,RichLocation.Strand.POSITIVE_STRAND);
                    ft.strand = StrandedFeature.POSITIVE;
                }
                else if(a.getStrand() == StrandType.COMPLEMENT) {
                    ft.location = new SimpleRichLocation(start,end, 0,RichLocation.Strand.NEGATIVE_STRAND);
                    ft.strand = StrandedFeature.NEGATIVE;
                }
                else {
                    ft.location = new SimpleRichLocation(start,end, 0,RichLocation.Strand.UNKNOWN_STRAND);
                    ft.strand = StrandedFeature.UNKNOWN;
                }
                //set genBank type
                ft.type = a.getFeature().getType().getGenBankFeature();
                //set annotation
                ft.annotation = org.biojava.bio.Annotation.EMPTY_ANNOTATION;
                //set source
                ft.source = "autogene";
                RichFeature feature = (RichFeature) rs.createFeature(ft);                
                
                //add notes i.e. feature key qualifiers
                HashSet<Note> note = new HashSet<Note>();
                SimpleComparableOntology sco = new SimpleComparableOntology("genBank");

                    String value = null;
                    //initiate some classes
                    ComparableTerm ct = sco.getOrCreateTerm("note");
                    SimpleNote n = new SimpleNote(ct,"",0);
                    
                    //add the description of the feature in the a /note
                    if(isNullOrEmpty(a.getFeature().getDescription())) {
                        ct = sco.getOrCreateTerm("note");
                        value = a.getFeature().getDescription();
                        n = new SimpleNote(ct, value, 3);
                        note.add(n);
                    }
                    //set the sofaType using a note.
                    if(isNullOrEmpty(a.getFeature().getType().getSofa_name())) {
                        ct = sco.getOrCreateTerm("note");
                        value = "SOFAType: " + a.getFeature().getType().getSofa_name();
                        n = new SimpleNote(ct, value, 0);
                        note.add(n);
                    }
                    //set the score using a note    
                        ct = sco.getOrCreateTerm("note");
                        value = "AutogeneSCORE: " + a.getScore();
                        n = new SimpleNote(ct,value, 4);
                        note.add(n);
                    //set a color if the format wanted is ApE
                        if(fileType == FileType.APE) {
                            //set the forward color
                            value = Integer.toHexString(a.getColor().getRGB());
                            value = value.substring(2, value.length());
                            value = "#" + value;
                            ct = sco.getOrCreateTerm("ApEinfo_fwdcolor");
                            n = new SimpleNote(ct, value, 1);
                            note.add(n);
                            //set the reverse color
                        //    ct = sco.getOrCreateTerm("ApEinfo_revcolor");
                        //    n = new SimpleNote(ct, value, note.size() +1);
                        //    note.add(n);
                        } 
                    //set the name in a label
                        ct = sco.getOrCreateTerm("label");
                        value = a.getFeature().getName();                     
                        n = new SimpleNote(ct, value, 2);
                        note.add(n);
                                
                
                feature.setNoteSet(note);
                feature.setName(a.getFeature().getName());
                rs.getFeatureSet().add(feature);
                
               /* BufferedReader br = new BufferedReader(new FileReader(file));
                String text = "";
                while(true) {
                    String line = br.readLine();
                    if(line == null)
                        break;
                    
                    text += br.readLine() + "\n";
                }
                String text = file.toString();
                while(true) {
                    int index = text.indexOf("ApEinfo_fwdcolor");
                    if(index == -1)
                        break;
                    
                    text = text.substring(0,index+16) + text.substring(index+18,index+24) + text.substring(26);
                }
                
                System.out.println(text);
                * 
                */
            }
            RichSequence.IOTools.writeGenbank(file, rs, null);
            System.out.println("now exporting");
        }
        catch(BioException e) {
        }
        
        
    } 
      
    private String getGenDivision(GenBankDivision genBankDivision) {
    
        String genDivision;
            if(genBankDivision == GenBankDivision.NONE)
                genDivision = "";
            else if(genBankDivision == GenBankDivision.PRI) 
                genDivision = "PRI";
            else if(genBankDivision == GenBankDivision.ROD)
                genDivision = "ROD";
            else if(genBankDivision == GenBankDivision.MAM)
                genDivision = "MAM";
            else if(genBankDivision == GenBankDivision.VRT)
                genDivision = "VRT";
            else if(genBankDivision == GenBankDivision.INV)
                genDivision = "INV"; 
            else if(genBankDivision == GenBankDivision.PLN)
                genDivision = "PLN";
            else if(genBankDivision == GenBankDivision.BCT)
                genDivision = "BCT";
            else if(genBankDivision == GenBankDivision.VRL)
                genDivision = "VRL";
            else if(genBankDivision == GenBankDivision.PHG)
                genDivision = "PHG";
            else if(genBankDivision == GenBankDivision.SYN)
                genDivision = "SYN";
            else if(genBankDivision == GenBankDivision.UNA)
                genDivision = "UNA";
            else if(genBankDivision == GenBankDivision.EST)
                genDivision = "EST";
            else if(genBankDivision == GenBankDivision.VRT)
                genDivision = "VRT";
            else if(genBankDivision == GenBankDivision.PAT)
                genDivision = "PAT";
            else if(genBankDivision == GenBankDivision.STS)
                genDivision = "STS";
            else if(genBankDivision == GenBankDivision.GSS)
                genDivision = "GSS";
            else if(genBankDivision == GenBankDivision.HTG)
                genDivision = "HTG";
            else if(genBankDivision == GenBankDivision.HTC)
                genDivision = "HTC";
            else if(genBankDivision == GenBankDivision.ENV)
                genDivision = "ENV";
            else
                genDivision = "";
            
            return genDivision;
            
            
    }
    
    private String getMoleculeType(MoleculeType moleculeType) {
        
        String molecule;
        if(moleculeType == MoleculeType.NONE) 
                molecule="";
        else if(moleculeType == MoleculeType.NA) 
                molecule="NA";
        else if(moleculeType == MoleculeType.SS_NA) 
                molecule="ss-NA";
        //else if(moleculeType == MoleculeType.DS_NA) 
        //        molecule="ds-NA";
        else if(moleculeType == MoleculeType.MS_NA) 
                molecule="ms-NA";
               
        else if(moleculeType == MoleculeType.DNA) 
                molecule="DNA";
        
        else if(moleculeType == MoleculeType.SS_DNA) 
                molecule="ss-DNA";
        //else if(moleculeType == MoleculeType.DS_DNA) 
        //        molecule="ds-DNA";
        else if(moleculeType == MoleculeType.MS_DNA) 
                molecule="ms-DNA";
                
        else if(moleculeType == MoleculeType.RNA) 
                molecule="RNA";
        
        else if(moleculeType == MoleculeType.SS_RNA) 
                molecule="ss-RNA";
        //else if(moleculeType == MoleculeType.DS_RNA) 
        //        molecule="ds-RNA";
        else if(moleculeType == MoleculeType.MS_RNA) 
                molecule="ms-RNA";
        /*
        else if(moleculeType == MoleculeType.TRNA) 
                molecule="tRNA";
        else if(moleculeType == MoleculeType.SS_TRNA) 
                molecule="ss-tRNA";
        else if(moleculeType == MoleculeType.DS_TRNA) 
                molecule="ds-tRNA";
        else if(moleculeType == MoleculeType.MS_TRNA) 
                molecule="ms-tRNA";
        else if(moleculeType == MoleculeType.RRNA) 
                molecule="rRNA";
        else if(moleculeType == MoleculeType.SS_RRNA) 
                molecule="ss-rRNA";
        else if(moleculeType == MoleculeType.DS_RRNA) 
                molecule="ds-rRNA";
        else if(moleculeType == MoleculeType.MS_RRNA) 
                molecule="ms-rRNA";
        else if(moleculeType == MoleculeType.MRNA) 
                molecule="mRNA";
        else if(moleculeType == MoleculeType.SS_MRNA) 
                molecule="ss-mRNA";
        else if(moleculeType == MoleculeType.DS_MRNA) 
                molecule="ds-mRNA";
        else if(moleculeType == MoleculeType.MS_MRNA) 
                molecule="ms-mRNA";
        else if(moleculeType == MoleculeType.URNA) 
                molecule="uRNA";
        else if(moleculeType == MoleculeType.SS_URNA) 
                molecule="ss-uRNA";
        else if(moleculeType == MoleculeType.DS_URNA) 
                molecule="ds-uRNA";
        else if(moleculeType == MoleculeType.MS_URNA) 
                molecule="ms-uRNA";
        else if(moleculeType == MoleculeType.SNRNA) 
                molecule="snRNA";
        else if(moleculeType == MoleculeType.SS_SNRNA) 
                molecule="ss-snRNA";
        else if(moleculeType == MoleculeType.DS_SNRNA) 
                molecule="ds-snRNA";
        else if(moleculeType == MoleculeType.MS_SNRNA) 
                molecule="ms-snRNA";  
        else if(moleculeType == MoleculeType.SNORNA) 
                molecule="snoRNA";
        else if(moleculeType == MoleculeType.SS_SNORNA) 
                molecule="ss-snoRNA";
        else if(moleculeType == MoleculeType.DS_SNORNA) 
                molecule="ds-snoRNA";
        else if(moleculeType == MoleculeType.MS_SNORNA) 
                molecule="ms-snoRNA";  
                * 
                */
        else
                molecule="";
        return molecule;
        
    }
    
    private String getDNAType(DNAType dnaType) {
        String typeDNA;
        if(dnaType == DNAType.CIRCULAR)
                typeDNA = "circular";
            else if (dnaType == DNAType.LINEAR)
                typeDNA= "linear";
            else
                typeDNA = "";
        return typeDNA;
    }
    /* This is not used anymore. Used to check what genbank feature it was.
    private String getFeatureType(FeatureType featureType) {
        String featureTypeName;
        FeatureTypeManager featureTypeManager = new FeatureTypeManager();

        if(featureTypeManager.findByGenBankFeature("attenuator").contains(featureType)) {
            featureTypeName = "attenuator";
        }
        else if(featureTypeManager.findByGenBankFeature("C_region").contains(featureType)) {
            featureTypeName = "C_region";
        }
        else if(featureTypeManager.findByGenBankFeature("CAAT_signal").contains(featureType)) {
            featureTypeName = "CAAT_signal";
        }
        else if(featureTypeManager.findByGenBankFeature("CDS").contains(featureType)) {
            featureTypeName = "CDS";
        }
        else if(featureTypeManager.findByGenBankFeature("D-loop").contains(featureType)) {
            featureTypeName = "D-loop";
        }           
        else if(featureTypeManager.findByGenBankFeature("D_segment").contains(featureType)) {
            featureTypeName = "D_segment";
        }
        else if(featureTypeManager.findByGenBankFeature("enhancer").contains(featureType)) {
            featureTypeName = "enhancer";
        }
        else if(featureTypeManager.findByGenBankFeature("exon").contains(featureType)) {
            featureTypeName = "exon";
        }            
        else if(featureTypeManager.findByGenBankFeature("gap").contains(featureType)) {
            featureTypeName = "gap";
        }
        else if(featureTypeManager.findByGenBankFeature("GC_signal").contains(featureType)) {
            featureTypeName = "GC_signal";
        }
        else if(featureTypeManager.findByGenBankFeature("gene").contains(featureType)) {
            featureTypeName = "gene";
        }
        else if(featureTypeManager.findByGenBankFeature("iDNA").contains(featureType)) {
            featureTypeName = "iDNA";
        }
        else if(featureTypeManager.findByGenBankFeature("intron").contains(featureType)) {
            featureTypeName = "intron";
        }    
        else if(featureTypeManager.findByGenBankFeature("J_segment").contains(featureType)) {
            featureTypeName = "J_segment";
        }  
        else if(featureTypeManager.findByGenBankFeature("LTR").contains(featureType)) {
            featureTypeName = "LTR";
        }
        else if(featureTypeManager.findByGenBankFeature("mat_peptide").contains(featureType)) {
            featureTypeName = "mat_peptide";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_binding").contains(featureType)) {
            featureTypeName = "misc_binding";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_difference").contains(featureType)) {
            featureTypeName = "misc_difference";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_feature").contains(featureType)) {
            featureTypeName = "misc_feature";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_recomb").contains(featureType)) {
            featureTypeName = "misc_recomb";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_RNA").contains(featureType)) {
            featureTypeName = "misc_RNA";
        }
        else if(featureTypeManager.findByGenBankFeature("misc_signal").contains(featureType)) {
            featureTypeName = "misc_signal";
        }    
        else if(featureTypeManager.findByGenBankFeature("misc_structure").contains(featureType)) {
            featureTypeName = "misc_structure";
        }
        else if(featureTypeManager.findByGenBankFeature("mobile_element").contains(featureType)) {
            featureTypeName = "mobile_element";
        }
        else if(featureTypeManager.findByGenBankFeature("modified_base").contains(featureType)) {
            featureTypeName = "modified_base";
        }    
        else if(featureTypeManager.findByGenBankFeature("mRNA").contains(featureType)) {
            featureTypeName = "mRNA";
        }
        else if(featureTypeManager.findByGenBankFeature("ncRNA").contains(featureType)) {
            featureTypeName = "ncRNA";
        }
        else if(featureTypeManager.findByGenBankFeature("N_region").contains(featureType)) {
            featureTypeName = "N_region";
        }
        else if(featureTypeManager.findByGenBankFeature("operon").contains(featureType)) {
            featureTypeName = "operon";
        }
        else if(featureTypeManager.findByGenBankFeature("oriT").contains(featureType)) {
            featureTypeName = "oriT";
        } 
        else if(featureTypeManager.findByGenBankFeature("polyA_signal").contains(featureType)) {
            featureTypeName = "polyA_signal";
        }
        else if(featureTypeManager.findByGenBankFeature("polyA_site").contains(featureType)) {
            featureTypeName = "polyA_site";
        }
        else if(featureTypeManager.findByGenBankFeature("precursor_RNA").contains(featureType)) {
            featureTypeName = "precursor_RNA";
        }
        else if(featureTypeManager.findByGenBankFeature("prim_transcript").contains(featureType)) {
            featureTypeName = "prim_transcript";
        }
        else if(featureTypeManager.findByGenBankFeature("primer_bind").contains(featureType)) {
            featureTypeName = "primer_bind";
        }    
        else if(featureTypeManager.findByGenBankFeature("promoter").contains(featureType)) {
            featureTypeName = "promoter";
        }    
        else if(featureTypeManager.findByGenBankFeature("protein_bind").contains(featureType)) {
            featureTypeName = "protein_bind";
        }
        else if(featureTypeManager.findByGenBankFeature("RBS").contains(featureType)) {
            featureTypeName = "RBS";
        }
        else if(featureTypeManager.findByGenBankFeature("repeat_region").contains(featureType)) {
            featureTypeName = "repeat_region";
        }
        else if(featureTypeManager.findByGenBankFeature("rep_origin").contains(featureType)) {
            featureTypeName = "rep_origin";
        }
        else if(featureTypeManager.findByGenBankFeature("rRNA").contains(featureType)) {
            featureTypeName = "rRNA";
        }
        else if(featureTypeManager.findByGenBankFeature("S_region").contains(featureType)) {
            featureTypeName = "S_region";
        }
        else if(featureTypeManager.findByGenBankFeature("sig_peptide").contains(featureType)) {
            featureTypeName = "sig_peptide";
        } 
        else if(featureTypeManager.findByGenBankFeature("stem_loop").contains(featureType)) {
            featureTypeName = "stem_loop";
        }
        else if(featureTypeManager.findByGenBankFeature("STS").contains(featureType)) {
            featureTypeName = "STS";
        }
        else if(featureTypeManager.findByGenBankFeature("TATA_signal").contains(featureType)) {
            featureTypeName = "TATA_signal";
        }
        else if(featureTypeManager.findByGenBankFeature("terminator").contains(featureType)) {
            featureTypeName = "terminator";
        }
        else if(featureTypeManager.findByGenBankFeature("tmRNA").contains(featureType)) {
            featureTypeName = "tmRNA";
        }
        else if(featureTypeManager.findByGenBankFeature("transit_peptide").contains(featureType)) {
            featureTypeName = "transit_peptide";
        }
        else if(featureTypeManager.findByGenBankFeature("tRNA").contains(featureType)) {
            featureTypeName = "tRNA";
        }
        else if(featureTypeManager.findByGenBankFeature("unsure").contains(featureType)) {
            featureTypeName = "unsure";
        }
        else if(featureTypeManager.findByGenBankFeature("V_region").contains(featureType)) {
            featureTypeName = "V_region";
        }
        else if(featureTypeManager.findByGenBankFeature("V_segment").contains(featureType)) {
            featureTypeName = "V_segment";
        }
        else if(featureTypeManager.findByGenBankFeature("variation").contains(featureType)) {
            featureTypeName = "variation";
        }
        else if(featureTypeManager.findByGenBankFeature("3UTR").contains(featureType)) {
            featureTypeName = "3'UTR";
        }
        else if(featureTypeManager.findByGenBankFeature("5UTR").contains(featureType)) {
            featureTypeName = "5'UTR";
        }
        else if(featureTypeManager.findByGenBankFeature("-10_signal").contains(featureType)) {
            featureTypeName = "-10_signal";
        }
        else if(featureTypeManager.findByGenBankFeature("-35_signal").contains(featureType)) {
            featureTypeName = "-35_signal";
        }
        else {
            featureTypeName = "misc_feature";
        }
        
        return featureTypeName;
    }
    * 
    */
    
    public enum FileType {
        NCBI,
        APE;
    }
    public enum DNAType {
        CIRCULAR,
        LINEAR;
    }
    public enum MoleculeType {
       NONE,
       NA,
       SS_NA,
       //DS_NA,
       MS_NA,
       DNA,
       SS_DNA,
       //DS_DNA,
       MS_DNA,
       RNA,
       SS_RNA,
       //DS_RNA,
       MS_RNA;
       //TRNA,
       //SS_TRNA,
       //DS_TRNA,
       //MS_TRNA,
       //RRNA,
       //SS_RRNA,
       //DS_RRNA,
       //MS_RRNA,
       //MRNA,
       //SS_MRNA,
       //DS_MRNA,
       //MS_MRNA,
       //URNA,
       //SS_URNA,
       //DS_URNA,
       //MS_URNA,
       //SNRNA,
       //SS_SNRNA,
       //DS_SNRNA,
       //MS_SNRNA,
       //SNORNA,
       //SS_SNORNA,
       //DS_SNORNA,
       //MS_SNORNA;
    }
    public enum GenBankDivision {
        NONE,
        PRI,
        ROD,
        MAM,
        VRT,
        INV,
        PLN,
        BCT,
        VRL,
        PHG,
        SYN,
        UNA,
        EST,
        PAT,
        STS,
        GSS,
        HTG,
        HTC,
        ENV;

    } 
    private static boolean isNullOrEmpty(String myString)
    {
         return myString == null || "".equals(myString);
    }

}
