/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputframe;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.autogene.ui.frames.BaseInternalFrame;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;

/**
 * A class to allow the user to upload a plasmid data file containing a sequence
 * @author Robert Eisinger
 */
public class LoadInputFileFrame extends BaseInternalFrame {
    private final ImportPlasmidFrame importPlasmidFrame;
    private File inputFile;

    public File getSelectedFile() {
        return inputFile;
    }

    private static class InputDataFileFilter extends FileFilter {

        /* Allowed file extensions for opening files */
        private ArrayList<String> allowedFileExtensions;
        
        public InputDataFileFilter() {
            allowedFileExtensions = new ArrayList<String>();
            allowedFileExtensions.add("fasta");
        }
        
        /**
         * Accept specific files only
         */
        @Override
        public boolean accept(File file) {
            if(file.isDirectory())
                return true;
            
            String extension = getExtension(file);
            if(extension == null)
                return false;
            
            if(allowedFileExtensions.contains(extension)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
            return "Fasta files only.";
        }

        
        /**
         * Gets the extension of a file (ex. txt). Does NOT include the
         * dot in the extension (ex. does NOT return .txt)
         * @param file the file to find the extension of
         * @return the extension of the file
         */
        private String getExtension(File file) {
            String name = file.getAbsolutePath();
            int ind = name.lastIndexOf(".");
            if(ind == -1)
                return null;
            
            return name.substring(ind+1, name.length());
            
        }
    }
    
    /**
     * Creates a frame that allows the user to select a file to input
     * @param ipf the input data frame that this load input file frame is working for
     */
    public LoadInputFileFrame(ImportPlasmidFrame ipf) {
        importPlasmidFrame = ipf;
        initComponents();
    }
    
    private void initComponents() {
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new InputDataFileFilter());
        int result = chooser.showOpenDialog(this);
        
        if(result == JFileChooser.APPROVE_OPTION) {
            
            //get the selected file
            inputFile = chooser.getSelectedFile();
            
            //update the gui to display the selected file
            importPlasmidFrame.setSelectedFile(inputFile);
            
        }
    }
    
}
