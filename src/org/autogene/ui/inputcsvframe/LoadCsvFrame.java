/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputcsvframe;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.CSVParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.persistence.NoResultException;
import org.autogene.core.bio.entities.Feature;
import org.autogene.core.bio.entities.Feature.sourceType;
import org.autogene.core.bio.managers.FeatureManager;
import org.autogene.core.bio.managers.FeatureTypeManager;
import org.autogene.core.bio.managers.OrganismManager;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.frames.BaseInternalFrame;
import org.autogene.ui.loggerframe.Log;

/**
 *
 * @author georgechen
 */

public class LoadCsvFrame extends BaseInternalFrame {
    
    private ImportCsvFrame importCsvFrame;
    private File csvFile;
    private OrganismManager organismManager;
    private FeatureTypeManager featureTypeManager;
    private FeatureManager featureManager;
    private ArrayList<String> features;
    private org.autogene.ui.main.AutogeneFrame af;
    
    public LoadCsvFrame(ImportCsvFrame importCsvFrame, org.autogene.ui.main.AutogeneFrame ag) {
        af = ag;
        this.importCsvFrame = importCsvFrame;
        features = new ArrayList<String>();
        csvFile = importCsvFrame.getFile();
        if (csvFile == null) {
           //TODO: Manage cases where user did not import file 
        }
        organismManager = new OrganismManager();
        featureTypeManager = new FeatureTypeManager();
        featureManager = new FeatureManager();
    }
    
    public void getTheData() throws IOException {
        CSVParse getData = new CSVParser(new FileInputStream(csvFile));
        //Scanner getData = new Scanner(new FileReader(csvFile));
        
        if(importCsvFrame.getRemoveFirstCol()) {
            getData.getLine();   
        }
        //will count the amount of errors
        int errors = 0;
        //will count the amount of information not provided
        int notprovided = 0;;
        while(true) {
            try {
                String[] lines = getData.getLine();
        //        System.out.println(lines[0] + " " + lines[1]);
                String name =null;
                String soID = null;
                String taxID =null;
                String sequence = null;
                String description =null;
                if(lines.length == 5) {
                    name = lines[0];
                    soID = lines[1];
                    taxID = lines[2];
                    sequence = lines[3];
                    description = lines[4];
                }
                else if(lines.length == 4) {
                    name = lines[0];
                    soID = lines[1];
                    taxID = lines[2];
                    sequence = lines[3];
                }
            
                Feature f = new Feature();
                System.out.println("instantiated feature");
                try{
                    Feature temp = featureManager.findBySequence(sequence);
                    System.out.println("feature:" + name + " is already in database");
                    //Log.addText("feature:" + name + " is already in database (" + temp.getName() + ")", LogEventType.FAILURE);
                }
                catch(NoResultException exp) {     
              //  Feature temp = featureManager.findAll().get(featureManager.findAll().indexOf(sequence));
              //  System.out.println("feature found with same sequence is:" + temp.getName()); 
                    System.out.println("inside first if statement");
                    //first find sofaId data.
                    if(soID != null) {
                        System.out.println("now finding SOID");
                        if(isInt(soID)) {
                            int sofa_id = Integer.parseInt(soID);
                            try {
                                f.setType(featureTypeManager.findBysofa_id(sofa_id));
                                Log.addText( name + ": sofa id was set as: " + f.getType().getName(), LogEventType.NEUTRAL);
                   
                            }
                            catch(NoResultException ex) {
                                System.out.println("FeatureType was not found");
                                //Log.addText( name + ": sofa id could not be found: " + soID, LogEventType.FAILURE);
                                errors++;
                            }
                        }
                        else {
                            try {
                                f.setType(featureTypeManager.findBySofa_name(soID));
                                Log.addText( name + ": sofa id was set as: " + f.getType().getName(), LogEventType.NEUTRAL);
                            }
                            catch(NoResultException ex) {
                                try {
                                f.setType(featureTypeManager.findBysofa_id(soID));
                                Log.addText( name + ": sofa id was set as: " + f.getType().getName(), LogEventType.NEUTRAL);
                                }
                                catch(NoResultException e) {
                            //TODO: include cases where the user provides genbank feature key
                                    System.out.println("FeatureType was not found");
                                    //Log.addText( name + ": sofa id could not be found: " + soID, LogEventType.FAILURE);
                                    errors++;
                                }
                            }                          
                        }    
                    }
                    else {
                        Log.addText(name + ": sofa id was not found", LogEventType.NEUTRAL);
                        f.setType(featureTypeManager.findBySofa_name("sequence_feature"));
                        notprovided++;
                    }
                    //now get the name
                    if (name != null) {
                        f.setName(name);
                        features.add(name);
                        System.out.println("set name");
                    }
                    else {
                        
                        notprovided++;
                    }
                    //now find the organism
                    if (taxID != null) {
                        if(isInt(taxID)) {
                            try {
                                f.setOrganism(organismManager.findByTaxId(Long.parseLong(taxID)));
                            }
                            catch(NoResultException ex) {
                                System.out.println("Organism was not found");
                                errors++;
                            }
                        }
                        else {
                            try {
                                f.setOrganism(organismManager.findByName(taxID));
                            }
                            catch (NoResultException ex) {
                                System.out.println("Organism was not found");
                                errors++;
                            }
                        }
                    }
                    else {
                        notprovided++;
                    }
                    //add the sequence
                    if (sequence != null) {
                        f.setSequence(sequence);
                    }
                    else {
                        //Log.addText("Could not find sequence", LogEventType.FAILURE);
                        notprovided++;
                    }
                    //add description
                    if(description != null) {
                        f.setDescription(description);
                    }
                    f.setPartType(sourceType.MANUAL);
                    featureManager.persist(f); 
                    System.out.println("Made feature");
                    Log.addText("Imported feature: " + name,LogEventType.NEUTRAL);
             
                }
            }
            catch(NullPointerException e) {
                System.out.println("Importing finished");
                Log.addText("Importing finished! " + errors + "errors occurred, " + notprovided + " information not provided." , LogEventType.SUCCESS);
                importCsvFrame.setVisible(false);
                break;
            }
        }
        
        af.addFeaturesToPrivateRegistry(features);
        
    }//end get the data
    
    private ArrayList<String> getFeatures() {
        return features;
    }
        
    //checks if string is a number
    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
}
