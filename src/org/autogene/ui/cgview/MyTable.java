/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import java.awt.dnd.*;
import javax.swing.JTable;
import org.autogene.ui.main.AutogeneFrame;

/**
 *
 * @author Robert
 */
public class MyTable extends JTable implements DropTargetListener {
    
    AutogeneFrame autogeneFrame; //the autogene frame application
    Viewer home; //where this jtable lives
    
    public MyTable(Viewer viewer) {
        super();
        home = viewer;
        
        
        //not sure why this is needed...but it is. DONT DELETE!
        new DropTarget(this,this);
       
    }


    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

        home.getHome().setLastSourceWasList(false);
        AutogeneFrame.setSourcePlasmidOfDrag(home.getPlasmid().getName());

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
                System.out.println("dropa");

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        System.out.println("drop");
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
