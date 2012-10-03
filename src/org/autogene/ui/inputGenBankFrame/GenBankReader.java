/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputGenBankFrame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Annotation.StrandType;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.FeatureType;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.core.bio.managers.FeatureTypeManager;
import org.autogene.ui.cgview.ColorConstants;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;
import org.biojavax.Note;
 
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;

/**
 *
 * @author georgechen
 */
public class GenBankReader {
    private BufferedReader br;
    private SimpleNamespace ns;
    private FeatureTypeManager ftm;
    public boolean alreadyHasAnnotations = false;
   
    public GenBankReader() {
        ftm = new FeatureTypeManager();
        
    }
    /* Reads the genBankFile and returns a plasmid that corresponds to the file
     * @param File the genBank File
     * @return Plasmid the genBank plasmid
     */
    public Plasmid read(File file) {
        ArrayList<Annotation> annotation = new ArrayList<Annotation>();
        Plasmid plasmid = new Plasmid();
        plasmid.setAnnotations(annotation);
        
        try{
            System.out.println(file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file));
            ns = new SimpleNamespace("biojava");
 
            RichSequenceIterator rsi = RichSequence.IOTools.readGenbankDNA(br,ns);
                
            RichSequence rs = rsi.nextRichSequence();
            //check to see if it is a CONTIG file. Contig files don't contain the sequence.
            //TODO: manage these types of files
            if(rs.getDivision() != null && rs.getDivision().equals("CONTIG")) {
                Log.addText("Can not read CONTIG Files. Please provide a genbank file with the sequence", LogEventType.FAILURE);
            }

            //create plasmid parameters
            else {
                plasmid.setName(rs.getName());
                for(Object o : rs.getFeatureSet()) {
                    alreadyHasAnnotations = true;
                    Annotation annotate = new Annotation();
                    annotate.setPlasmid(plasmid);
                    //set a default score.
                    annotate.setScore(1.0);
                    
                    Feature feature = new Feature();
                    annotate.setFeature(feature);
                    RichFeature f = (RichFeature) o;
                    int max = f.getLocation().getMax();
                    int min = f.getLocation().getMin();
                    plasmid.setSequence(f.getSequence().seqString());
  //             System.out.println(plasmid.getSequence());     
                    annotate.getFeature().setSequence(f.getSequence().subStr(min, max));
  //             System.out.println(annotate.getFeature().getSequence());
                    String genbankfeature = f.getType().toString(); 
                    FeatureType tempFeatType = findSofaIDbyGenbankFeature(genbankfeature);
                    
                    
                    //if the featureType is "source, just skip this annotation
                    if(tempFeatType.getGenBankFeature().equals("source")) {
                        continue;
                    }
                
                    annotate.getFeature().setType(tempFeatType);
                    //set a color based on the feature type
                    annotate.setColor(ColorConstants.getColorForAnnotation(annotate));
   //            System.out.println(annotate.getColor().toString());
                    //set the strand
                    char wc =f.getStrand().getToken();
                    if(wc == '+')
                        annotate.setStrand(StrandType.ORIGINAL);
                    else if(wc == '-')
                        annotate.setStrand(StrandType.COMPLEMENT);
                
                    annotate.setStart(f.getLocation().getMin());   
                    annotate.setEnd(f.getLocation().getMax());
                    
                    //get the qualifier data. 
                    // /label, /gene, and /product are used to define the feature name
                    // /note is used to get the feature description
                    // /ApEinfo_fwdcolor is used to get the color. The color of the feature by default is blue
                    for(Object o3 : f.getNoteSet()) {                      
                        Note n = (Note) o3;
                        if(n.getTerm().getName().equals("label")||n.getTerm().getName().equals("gene")) {
                            
                                annotate.getFeature().setName(n.getValue()); 
              
                        }
                        else if(n.getTerm().getName().equals("product")) {
                            String newName = annotate.getFeature().getName() + ", " + n.getValue();
                            annotate.getFeature().setName(newName);
                           
                        }
                        else if(n.getTerm().getName().equals("note")) {
                            //first check if it has a SOFA id, if it does, then extract it
                            if(n.getValue().contains("SOFAType: ")) {
                                try {
                                   String sofa = n.getValue().substring(n.getValue().indexOf(" ") + 1);
                                   annotate.getFeature().setType(ftm.findBySofa_name(sofa));
                                   annotate.setColor(ColorConstants.getColorForAnnotation(annotate));
                                }
                                catch(Exception e) {
                                    System.out.println("SofaType could not be found");
                                }
                            }
                            else if(n.getValue().contains("AutogeneSCORE:")) {
                                try {
                                    Double score = Double.parseDouble(n.getValue().substring(n.getValue().indexOf(" ") + 1));
                                    annotate.setScore(score);
                                }
                                catch(Exception e) {
                                    System.out.println("Annotation score could not be found");
                                }
                            }
                            else {
                                annotate.getFeature().setDescription(n.getValue());
                            }
                        }
                        else if(n.getTerm().getName().equals("ApEinfo_fwdcolor")) {
                            String hexString = n.getValue();
                            if(hexString.charAt(0) == '#') {
                                try {
                                    hexString = hexString.substring(hexString.indexOf("#")+1);
                                    int rgb = Integer.parseInt(hexString,16);
                                    Color color = new Color(rgb);
                                    annotate.setColor(color);
                                }
                                catch (NumberFormatException e) {
                                    System.out.println("wasn't a hexString");           
                                }  
                            }
                            else {
                               try {
                                   Field field = Color.class.getField(hexString);
                                   Color color = (Color) field.get(null);
                                   annotate.setColor(color);
                               }
                               catch (NoSuchFieldException e) {
                                   System.out.println("wasn't a color name");
                               }
                            }
                        }
                    }
                    plasmid.getAnnotations().add(annotate);
                    updateDisplayNamesHashMap(annotate, plasmid); 
                }
            }
	}
	catch(Exception be){
            be.printStackTrace();
	}
        System.out.println(plasmid.toString());
        return plasmid;
    }//end read
    
    
    /* Helps to find the respective SofaID for each genBank feature
     * @param String the name of the genbank feature
     * @return the FeatureType that corresponds to the same Sofa ID
     */
    
    private FeatureType findSofaIDbyGenbankFeature(String genBankFeature) {
        FeatureType ft = new FeatureType();
        if(genBankFeature.equals("attenuator")) {
            ft = ftm.findBysofa_id("SO:0000140");
        }
        else if(genBankFeature.equals("C_region")) {
            ft = ftm.findBysofa_id("SO:0001834");
        }
        else if(genBankFeature.equals("CDS")) {
            ft = ftm.findBysofa_id("SO:0000316");
        }
        else if(genBankFeature.equals("CAAT_signal")) {
            ft = ftm.findBysofa_id("SO:0000172");
        }
        else if(genBankFeature.equals("centromere")) {
            ft = ftm.findBysofa_id("SO:0000577");
        }
        else if(genBankFeature.equals("D-loop")) {
            ft = ftm.findBysofa_id("SO:0000297");
        }
        else if(genBankFeature.equals("D_segment")) {
            ft = ftm.findBysofa_id("SO:0000458");
        }
        else if(genBankFeature.equals("enhancer")) {
            ft = ftm.findBysofa_id("SO:0000165");
        }
        else if(genBankFeature.equals("exon")) {
            ft = ftm.findBysofa_id("SO:0000147");
        }
        else if(genBankFeature.equals("gap")) {
            ft = ftm.findBysofa_id("SO:0000730");
        }
        else if(genBankFeature.equals("GC_signal")) {
            ft = ftm.findBysofa_id("SO:0000173");
        }
        else if(genBankFeature.equals("gene")) {
            ft = ftm.findBysofa_id("SO:0000704");
        }
        else if(genBankFeature.equals("iDNA")) {
            ft = ftm.findBysofa_id("SO:0000723");
        }
        else if(genBankFeature.equals("intron")) {
            ft = ftm.findBysofa_id("SO:0000188");
        }
        else if(genBankFeature.equals("J_segment")) {
            ft = ftm.findBysofa_id("SO:0000470");
        }
        else if(genBankFeature.equals("LTR")) {
            ft = ftm.findBysofa_id("SO:0000286");
        }
        else if(genBankFeature.equals("mat_peptide")) {
            ft = ftm.findBysofa_id("SO:0000419");
        }
        else if(genBankFeature.equals("misc_binding")) {
            ft = ftm.findBysofa_id("SO:0000409");
        }
        else if(genBankFeature.equals("misc_difference")) {
            ft = ftm.findBysofa_id("SO:0000413");
        }
        else if(genBankFeature.equals("misc_feature")) {
            ft = ftm.findBysofa_id("SO:0000001");
        }
        else if(genBankFeature.equals("misc_recomb")) {
            ft = ftm.findBysofa_id("SO:0000298");
        }
        else if(genBankFeature.equals("misc_RNA")) {
            ft = ftm.findBysofa_id("SO:0000673");
        }
        else if(genBankFeature.equals("misc_signal")) {
            ft = ftm.findBysofa_id("SO:0005836");
        }
        else if(genBankFeature.equals("misc_structure")) {
            ft = ftm.findBysofa_id("SO:0000002");
        }
        else if(genBankFeature.equals("mobile_element")) {
            ft = ftm.findBysofa_id("SO:0001037");
        }
        else if(genBankFeature.equals("modified_base")) {
            ft = ftm.findBysofa_id("SO:0000305");
        }
        else if(genBankFeature.equals("mRNA")) {
            ft = ftm.findBysofa_id("SO:0000234");
        }
        else if(genBankFeature.equals("ncRNA")) {
            ft = ftm.findBysofa_id("SO:0000655");
        }
        else if(genBankFeature.equals("N_region")) {
            ft = ftm.findBysofa_id("SO:0001835");
        }
        else if(genBankFeature.equals("old_sequence")) {
            ft.setGenBankFeature("old_sequence");
            ft.setName("Feature");
        }
        else if(genBankFeature.equals("operon")) {
            ft = ftm.findBysofa_id("SO:0000178");
        }
        else if(genBankFeature.equals("oriT")) {
            ft = ftm.findBysofa_id("SO:0000724");
        }
        else if(genBankFeature.equals("polyA_signal")) {
            ft = ftm.findBysofa_id("SO:0000551");
        }
        else if(genBankFeature.equals("polyA_site")) {
            ft = ftm.findBysofa_id("SO:0000553");
        }
        else if(genBankFeature.equals("precursor_RNA")) {
            ft = ftm.findBysofa_id("SO:0000185");
        }
        else if(genBankFeature.equals("prim_transcript")) {
            ft.setGenBankFeature("prim_transcript");
            ft.setSofa_id("SO:0000185");
            ft.setName("Feature");
            ft.setSofa_name("primary_transcript");
        }
        else if(genBankFeature.equals("primer_bind")) {
            ft = ftm.findBysofa_id("SO:0005850");
        }
        else if(genBankFeature.equals("promoter")) {
            ft = ftm.findBysofa_id("SO:0000167");
        }
        else if(genBankFeature.equals("protein_bind")) {
            ft = ftm.findBysofa_id("SO:0000410");
        }
        else if(genBankFeature.equals("RBS")) {
            ft = ftm.findBysofa_id("SO:0000139");
        }
        else if(genBankFeature.equals("repeat_region")) {
            ft = ftm.findBysofa_id("SO:0000657");
        }
        else if(genBankFeature.equals("rep_origin")) {
            ft = ftm.findBysofa_id("SO:0000296");
        }
        else if(genBankFeature.equals("repeat_region")) {
            ft = ftm.findBysofa_id("SO:0000657");
        }
        else if(genBankFeature.equals("rRNA")) {
            ft = ftm.findBysofa_id("SO:0000252");
        }
        else if(genBankFeature.equals("S_region")) {
            ft = ftm.findBysofa_id("SO:0001836");
        }
        else if(genBankFeature.equals("sig_peptide")) {
            ft = ftm.findBysofa_id("SO:0000418");
        }
        else if(genBankFeature.equals("repeat_region")) {
            ft = ftm.findBysofa_id("SO:0000657");
        }
        else if(genBankFeature.equals("source")) {
            ft.setSofa_id("SO:2000061");
            ft.setName("Feature");
            ft.setGenBankFeature("source");
            ft.setSofa_name("databank_entry");
        }
        else if(genBankFeature.equals("stem_loop")) {
            ft = ftm.findBysofa_id("SO:0000313");
        }
        else if(genBankFeature.equals("STS")) {
            ft = ftm.findBysofa_id("SO:0000331");
        }
        else if(genBankFeature.equals("TATA_signal")) {
            ft = ftm.findBysofa_id("SO:0000174");
        }
        else if(genBankFeature.equals("telomere")) {
            ft = ftm.findBysofa_id("SO:0000624");
        }
        else if(genBankFeature.equals("terminator")) {
            ft = ftm.findBysofa_id("SO:0000141");
        }
        else if(genBankFeature.equals("tmRNA")) {
            ft = ftm.findBysofa_id("SO:0000584");
        }
        else if(genBankFeature.equals("transit_peptide")) {
            ft = ftm.findBysofa_id("SO:0000725");
        }
        else if(genBankFeature.equals("tRNA")) {
            ft = ftm.findBysofa_id("SO:0000253");
        }
        else if(genBankFeature.equals("unsure")) {
            ft = ftm.findBysofa_id("SO:0000732");
        }
        else if(genBankFeature.equals("V_region")) {
            ft = ftm.findBysofa_id("SO:0001833");
        }
        else if(genBankFeature.equals("V_segment")) {
            ft = ftm.findBysofa_id("SO:0000466");
        }
        else if(genBankFeature.equals("variation")) {
            ft = ftm.findBysofa_id("SO:0000109");
        }
        else if(genBankFeature.equals("3'UTR")) {
            ft = ftm.findBysofa_id("SO:0000205");
        }
        else if(genBankFeature.equals("5'UTR")) {
            ft = ftm.findBysofa_id("SO:0000204");
        }
        else if(genBankFeature.equals("-10_signal")) {
            ft = ftm.findBysofa_id("SO:0000175");
        }
        else if(genBankFeature.equals("-35_signal")) {
            ft = ftm.findBysofa_id("SO:0000176");
        }
        
        return ft;
    }
    
    /* Helps to get a color by featureType general name
     * Promoter = Magenta
     * Feature = Green
     * Restriction Enzyme = Red
     * Regulatory = Orange
     * Origin = Pink
     * Terminator = Yellow
     * Primer = Cyan
     * Gene/Default = Blue
     * @param FeatureType the featureType
     * @return color the color that corresponds to the feature
     */
    
    private Color getColorbyFeatureType(FeatureType f) {
        Color c = Color.BLUE;
        if(f.getName().equals("Promoter")) {
            c = Color.MAGENTA;
        }
        else if(f.getName().equals("Feature")) {
            c = Color.GREEN;
        }
        else if(f.getName().equals("Restriction Enzyme")) {
            c = Color.RED;
        }
        else if(f.getName().equals("Regulatory")) {
            c = Color.ORANGE;
        }
        else if(f.getName().equals("Origin")) {
            c = Color.PINK;
        }
        else if(f.getName().equals("Terminator")) {
            c = Color.YELLOW;
        }
        else if(f.getName().equals("Primer")) {
            c = Color.CYAN;
        }
        return c;
    }
    /*
     * Helps to set the displayname of each annotation.
     */
        private void updateDisplayNamesHashMap(Annotation a, Plasmid plasmid) {
        if(plasmid.getFeatureDisplayNamesHashMap().containsKey(a.getFeature().getName())) {
                    int cur = plasmid.getFeatureDisplayNamesHashMap().get(a.getFeature().getName());
                    cur++;
                    a.getFeature().setDisplayName(a.getFeature().getName()+" ("+cur+")");
                    plasmid.getFeatureDisplayNamesHashMap().put(a.getFeature().getName(),cur);
                    //Log.addText("setDisplayName: " + a.getFeature().getName() + "," + cur + ": " + a.getFeature().getDisplayName());

                }
                else {
                    a.getFeature().setDisplayName(a.getFeature().getName()+" (1)");
                    plasmid.getFeatureDisplayNamesHashMap().put(a.getFeature().getName(), 1);
                    //Log.addText("setDisplayName: " + a.getFeature().getName() + "," + "1" + ": " + a.getFeature().getDisplayName());

              
                }
    }
	
}   

