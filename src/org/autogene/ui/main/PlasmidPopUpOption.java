/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Robert
 */
public class PlasmidPopUpOption extends JPopupMenu {
    private JMenuItem annotate, view, delete, rename;
    
    private AutogeneFrame home;
    
    public PlasmidPopUpOption(AutogeneFrame af) {
        home = af;
        
        annotate = new JMenuItem("Annotate");
        view = new JMenuItem("View");
        delete = new JMenuItem("Delete");
        rename = new JMenuItem("Rename");
        
        add(view);
        add(annotate);
        add(delete);
        add(rename);
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == annotate) {
                    home.annotateSelectedPlasmid();
                } else if(e.getSource() == view) {
                    home.viewSelectedPlasmid();
                } else if(e.getSource() == delete) {
                    home.deleteSelectedPlasmidOrFeature();
                } else if(e.getSource() == rename) {
                    
                }
            }
        };
        
        annotate.addActionListener(actionListener);
        view.addActionListener(actionListener);
        rename.addActionListener(actionListener);
        delete.addActionListener(actionListener);
        
    }
    
}
