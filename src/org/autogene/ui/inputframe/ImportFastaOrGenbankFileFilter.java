/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.inputframe;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Robert
 */
class ImportFastaOrGenbankFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if(file.isDirectory())
            return true;
        
        String filename = file.getName();
        return filename.endsWith(".gb") || filename.endsWith(".fasta") || filename.endsWith(".xml");
    }

    @Override
    public String getDescription() {
        return "*.gb, *.fasta, or *.xml";
    }
    
}
