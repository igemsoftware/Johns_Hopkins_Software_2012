/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.sessions;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JDesktopPane;
import org.autogene.core.bio.entities.Part;
import org.autogene.core.bio.entities.Plasmid;
import org.autogene.ui.main.AutogeneFrame;

/**
 *
 * @author giovanni
 */
public class Environment implements Serializable{

    public static JDesktopPane getDesktopPane() {
        System.out.println("getting desktop pane: " + desktopPane);
        return desktopPane;
    }

    public static ArrayList<Plasmid> getWorkspacePlasmids() {
        return workspacePlasmids;
    }

    public static Plasmid getWorkspacePlasmidByName(String name) {
        for(int i = 0; i < workspacePlasmids.size(); i++) {
            Plasmid p = workspacePlasmids.get(i);
            if(p.getName().equals(name))
                return p;
        }
        return null;
    }

    

    private LinkedList<Part> parts;
    public static final String PROP_PARTS = "parts";
    private static ArrayList<Plasmid> workspacePlasmids;
    private Plasmid activePlasmid;
    //private static Plasmid activePlasmid;
    public final int MENU_BAR_HEIGHT = 70; //the menu bar + the toolbar
    public final int WINDOW_TOOLBAR_HEIGHT = 20; //the toolbar with the maximize button, etc
    private static JDesktopPane desktopPane;
    public AutogeneFrame autogeneFrame;


    public Environment(AutogeneFrame autogeneFrame) {
        parts = new LinkedList<Part>();
        workspacePlasmids = new ArrayList<Plasmid>();
        this.autogeneFrame = autogeneFrame;
    }
    
    

    /**
     * Get the value of parts
     *
     * @return the value of parts
     */
    public LinkedList<Part> getParts() {
        return parts;
    }

    /**
     * Set the value of parts
     *
     * @param parts new value of parts
     */
    public void setParts(LinkedList<Part> parts) {
        LinkedList<Part> oldParts = this.parts;
        this.parts = parts;
        propertyChangeSupport.firePropertyChange(PROP_PARTS, oldParts, parts);
    }
    
    
    /**
     * Add a Part p to the Environment
     * @param p 
     */
    public void addPart(Part p){
        LinkedList<Part> oldParts = this.parts;
        this.parts = new LinkedList<Part>(oldParts);
        this.parts.add(p);
        propertyChangeSupport.firePropertyChange(PROP_PARTS,oldParts, this.parts);
    }
    
    /**
     * Remove a Part p from the Environment
     * @param p 
     */
    public void removePart(Part p){
        LinkedList<Part> oldParts = this.parts;
        this.parts = new LinkedList<Part>(oldParts);
        this.parts.remove(p);
        propertyChangeSupport.firePropertyChange(PROP_PARTS,oldParts, this.parts);
    }
    
    
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void addPlasmidToWorkspace(Plasmid p) {
        workspacePlasmids.add(p);
        autogeneFrame.updatePlasmidList();
    }
    
    public void removePlasmidFromWorkspace(String p) {
        System.out.println("removing plasmid with name " + p);
         for(int i = 0; i < workspacePlasmids.size(); i++) {
                System.out.println("w LIST MODEL: " + workspacePlasmids.get(i).getName());
        }
        //workspacePlasmids.remove(pl);
        System.out.println("--");
        
        for(int i = 0; i < workspacePlasmids.size(); i++) {
            if(workspacePlasmids.get(i).getName().equals(p)) {
                workspacePlasmids.remove(i);
                break;
            }
        }
        //System.out.println("found plasmid with name " + pl.getName());
        for(int i = 0; i < workspacePlasmids.size(); i++) {
                System.out.println("w LIST MODEL: " + workspacePlasmids.get(i).getName());
        }
        //workspacePlasmids.remove(pl);
        
        autogeneFrame.updatePlasmidList();
        
    }
    
    public void setActivePlasmid(Plasmid plasmid) {
        activePlasmid = plasmid;
    }
    public Plasmid getActivePlasmid() {
        System.out.println("returning active plasmid");
        return activePlasmid;
    }

    /*public void setActivePlasmid(org.autogene.core.bio.entities.Plasmid p) {
        activePlasmid = p;
    }

    public Plasmid getActivePlasmid() {
        return activePlasmid;
    }
    * 
    */

    public static void setDesktopPane(JDesktopPane desktopPane) {
        Environment.desktopPane = desktopPane;
    }

 
}
