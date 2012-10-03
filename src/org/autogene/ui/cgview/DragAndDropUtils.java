/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

/**
 *
 * @author Robert
 */
public class DragAndDropUtils {



    public static ArrayList<String> getNameAssumingFromFeatureTable(Transferable t, String plasmidName) {
        //assume the transferable object is from the table of features
        try {
            
            String data = (String)t.getTransferData(DataFlavor.stringFlavor);
            
            //if we are dragginw within the list, then data would be a single feature
            if(data.indexOf("\t") == -1) {
                ArrayList<String> out = new ArrayList<String>();
                out.add(data);
                return out;
            }
            
            //otherwise, split it up
            String[] s = data.split("\n");
            ArrayList<String> out = new ArrayList<String>();
            for(int i = 0; i < s.length; i++) {
                out.add(s[i].split("\t")[1] + " (" + plasmidName + ")");
            }
            return out;
        } 
        catch (Exception e) { 
            return null;
        }
    }
    
}
