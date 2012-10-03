/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Robert
 */
public class DesignOperatorPopUpOption extends JPopupMenu {
    private JMenuItem invert, duplicate, delete;
    private int index;
    private FeatureDragger home;
    
    //index is the feature we clicked on
    public DesignOperatorPopUpOption(FeatureDragger af, final int index) {
        home = af;
        this.index = index;
        invert = new JMenuItem("Invert");
        duplicate = new JMenuItem("Duplicate");
        delete = new JMenuItem("Delete");
        
        add(invert);
        add(duplicate);
        add(delete);
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == invert) {
                    home.invertSelectedFeature(index);
                } else if(e.getSource() == duplicate) {
                    home.duplicateSelectedFeature(index);
                } else if(e.getSource() == delete) {
                    home.deleteSelectedFeature(index);
                }
            }
        };
        
        invert.addActionListener(actionListener);
        duplicate.addActionListener(actionListener);
        delete.addActionListener(actionListener);
        
    }
    
}
