/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.exportGenBank;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.frames.BaseInternalFrame;
import org.autogene.ui.loggerframe.Log;

/**
 *
 * @author georgechen
 */
public class NewExportGenBankFrame {

    /**
     * Creates new form ExportGenBankFrame
     */
    File exportFile;
    JFileChooser jFileChooser1;
    ExportGenBank exportGenBank;
    public NewExportGenBankFrame(ExportGenBank egb, Plasmid p) throws PropertyVetoException {
        jFileChooser1 = new JFileChooser();
        this.exportGenBank = egb;

        try {
            Plasmid plasmid = p;
            exportFile = new File(plasmid.getName() + ".gb");
            jFileChooser1.setSelectedFile(exportFile);
        }
        catch(NullPointerException e) {
            System.out.println("Could not find plasmid");
        }
        int result = jFileChooser1.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            exportFile = jFileChooser1.getSelectedFile();
            Log.addText("Exporting: " + exportFile.getName(), LogEventType.NEUTRAL);
            exportFile = addExtension(exportFile);
            System.out.println(exportFile.getAbsolutePath());
            try {
                Plasmid plasmid = p;
                exportGenBank.exportFile(exportFile, plasmid);
                jFileChooser1.setVisible(false);
                //this.setVisible(false);
                //this.dispose();
                Log.addText("Export Successful! Exported to: " + exportFile.getAbsolutePath() , LogEventType.SUCCESS);
            }
            catch(IOException e) {
                Log.addText("Failed to Export", LogEventType.FAILURE);
            }
 //           catch(NullPointerException e) {
 //               Log.addText("Could not find plasmid", LogEventType.FAILURE);
 //           }
        }   
        
                //setVisible(false);

        
        
    }
    public static class InputFileType extends FileFilter {
         /* Allowed file extensions for opening files */
        private ArrayList<String> allowedFileExtensions;
        
        public InputFileType() {
            allowedFileExtensions = new ArrayList<String>();
            allowedFileExtensions.add("gb");
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
            
            return "ApE and GenBank files";
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

    
    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {                                              
        
    }                                             

    /**
     * @param args the command line arguments
     */
  /*  public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
  /*      try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExportGenBankFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportGenBankFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportGenBankFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportGenBankFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
/*        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ExportGenBankFrame().setVisible(true);
            }
        });
    }*/
    // Variables declaration - do not modify                     
    // End of variables declaration                   

private File addExtension(File exportFile) {
    String filePath = exportFile.getAbsolutePath();
    File newExportFile;
    int ind = filePath.indexOf(".");
    if(ind == -1) {
        filePath = filePath + ".gb";
        newExportFile = new File(filePath);
    }
    else {
        newExportFile = exportFile;
    }
    return newExportFile;
}
    
}
