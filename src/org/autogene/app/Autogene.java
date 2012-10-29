/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.app;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.autogene.core.bio.managers.FeatureManager;
import org.autogene.core.bio.managers.InitialSetUpWindow;
import org.autogene.core.bio.managers.Master;
import org.autogene.ui.main.AutogeneFrame;


/**
 *
 * @author giovanni
 */
public class Autogene {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	
    	//JOptionPane.showMessageDialog(null, new File(".").getAbsolutePath());
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AutogeneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AutogeneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AutogeneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AutogeneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
            	
                try {
                /*   InputStreamReader isReader= 
                      new InputStreamReader(
                          Master.class.getResourceAsStream("/org/autogene/core/bio/managers/Properties.prop"));
                    BufferedReader br = new BufferedReader(isReader); 
                    InputStream resourceAsStream = Master.class.getResourceAsStream("/org/autogene/core/bio/managers/Properties.prop");
                
                */
               
                System.out.println("wrote!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                
                Master mas = new Master();
                if(mas.propertiesFileExists()) {
                    new AutogeneFrame().setVisible(true);
                } else {
                    InitialSetUpWindow ini = new InitialSetUpWindow();
                    ini.setVisible(true);
                }
                    
                    //Master.createTable();
                
                //new FeatureManager();
                System.out.println("done calling feature manager");
                /*try {
                    System.out.println("caling set up");
                    Master.setupDatabase();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.toString());
                }
                
                try {
                   // getClass().getResource("/org/autogene/ui/resources/images/Regulatory_forward.png");
                } catch(Exception e) { 
                    //e.printStackTrace();
                    //System.exit(1);
                }*/
                }
                
              
            
        });
    }
    
}
