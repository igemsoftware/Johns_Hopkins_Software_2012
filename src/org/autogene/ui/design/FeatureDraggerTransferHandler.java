/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;

import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.autogene.ui.main.AutogeneFrame;

/**
 *
 * @author Robert
 */
class FeatureDraggerTransferHandler extends TransferHandler {
    AutogeneFrame home;
    FeatureDragger featureDragger;
    public FeatureDraggerTransferHandler(AutogeneFrame af, FeatureDragger fd) {
        home = af;
        featureDragger = fd;
    }
    
    public boolean canImport(TransferHandler.TransferSupport info) {
         return true;
    }
     
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }
            
    @Override
    protected Transferable createTransferable(JComponent c) {
            return null;
    }
    
    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        featureDragger.addDraggableFeatures(home.getPrivateRegistryList().getSelectedValues());
        return true;
    }
}
