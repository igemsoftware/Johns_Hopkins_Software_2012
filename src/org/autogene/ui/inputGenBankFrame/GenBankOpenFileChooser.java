/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputGenBankFrame;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.autogene.ui.inputcsvframe.ImportCsvFrame;
import org.autogene.ui.inputcsvframe.OpenFileChooser;

/**
 *
 * @author georgechen
 */
public class GenBankOpenFileChooser {
    private final GenBankReaderForm gbrf;
    private File inputFile;
    
    public File getSelectedFile() {
        return inputFile;
    }
    
    public static class InputFileType extends FileFilter {
         /* Allowed file extensions for opening files */
        private ArrayList<String> allowedFileExtensions;
        
        public InputFileType() {
            allowedFileExtensions = new ArrayList<String>();
            allowedFileExtensions.add("gb");
            allowedFileExtensions.add("ape");
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
            return "genbank formatted files only.";
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
    
    public GenBankOpenFileChooser(GenBankReaderForm gbrf) {
        this.gbrf = gbrf;
        initComponents();
    }
        
    private void initComponents() {
       
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new GenBankOpenFileChooser.InputFileType());
        int result = chooser.showOpenDialog(null);
        
        if(result == JFileChooser.APPROVE_OPTION) {
            
            //get the selected file
            inputFile = chooser.getSelectedFile();
            
            //update the gui to display the selected file
            gbrf.setSelectedFile(inputFile);
            System.out.println("Got the file!");
        }
        
       
    }
}
  
