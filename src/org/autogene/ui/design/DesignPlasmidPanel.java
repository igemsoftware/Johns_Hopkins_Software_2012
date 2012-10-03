/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import org.autogene.algorithms.Annotator;
import org.autogene.cgview.*;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Annotation.StrandType;
import org.autogene.core.bio.entities.Feature;
import org.autogene.ui.cgview.ColorConstants;
import org.autogene.ui.loggerframe.Log;
import org.autogene.ui.sessions.Environment;

/**
 *
 * @author Robert
 */
public class DesignPlasmidPanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener {

    /**
     * Creates new form DesignPlasmidPanel
     */
        
    
    private Cgview cgview;
    private org.autogene.core.bio.entities.Plasmid plasmid;
    private FeatureDragger home; //the view frame this plasmid panel lives in
    private JPanel homePanel; //the homePanel that should determine the size of this panel
    private int previousWidth, previousHeight;
    
    
    
    /**
     * PlasmidPanel constructor coming from a Viewer object, in which case the
     * panel that this PlasmidPanel should be in comes from the topPanel in the
     * Viewer class
     * @param p the plasmid to view
     * @param parent the Viewer object this PlasmidPanel lives in
     */
    public DesignPlasmidPanel(FeatureDragger parent) {
        this(parent.getRightBottomPanel(), parent);
        home = parent;
        previousWidth = -1;
        previousHeight = -1;
        setOpaque(false);
    }
    
    /**
     * Main PlamsidPanel constructor. A PlamsidPanel shows a Plasmid object and
     * lives inside of a JPanel. The size of the PlasmidPanel is determined from
     * the size of the panel in which it lives.
     * @param p the Plasmid object
     * @param pan the JPanel that this PlasmidPanel lives in
     */
    public DesignPlasmidPanel(JPanel pan, FeatureDragger parent) {     
        
        
        homePanel = pan;
        home = parent;


        setSize(homePanel.getSize());
        setPreferredSize(homePanel.getSize());
        

        System.out.println(parent.getListOfAnnotations());
        plasmid = generatePlasmid(parent.getListOfAnnotations());
        System.out.println("plasmid made with sequence !" + plasmid.getSequence());
        System.out.println("test ATCG " + Annotator.invertDNA("ATCG"));
        for(int i = 0; i < plasmid.getAnnotations().size(); i++) {
            System.out.println(plasmid.getAnnotations().get(i).getFeature().getName() + "---" + plasmid.getAnnotations().get(i).getFeature().getDisplayName());
        }
        
        //System.exit(1);
        initComponents();
        initCgview();
        addMouseListener(this);
        //addMouseMotionListener(this);
        
    }
    
    public void updatePlasmidView() {
        System.out.println("updating plasmid view");
        ArrayList<FeatureDraggerLabel> lab = home.getLabels();
        for(int i = 0; lab!= null && i < lab.size(); i++) {
            //System.out.println(lab.get(i).getText() + " " + lab.get(i).inverted);
        }
        System.out.println("ABCDE");
        for(int i = 0; i < home.getListOfAnnotations().size(); i++) {
            System.out.println(home.getListOfAnnotations().get(i));
            System.out.println(home.getListOfAnnotations().get(i).getFeature());
            System.out.println(home.getListOfAnnotations().get(i).getFeature().getDisplayName());
           // System.out.println(home.getListOfAnnotations().get(i).getStrand());
           // System.out.println("Annotation " + home.getListOfAnnotations().get(i).getFeature().getDisplayName() + " has strand " + home.getListOfAnnotations().get(i).getStrand().toString());
        }
        System.out.println("12: " + plasmid.getSequence());
         plasmid = generatePlasmid(home.getListOfAnnotations());
       System.out.println("13: " + plasmid.getSequence());

         initCgview(); //TODO: SOMETHING IS WRONG HERE WHEN YOU SNAP LABELS TO SPOTS AND SWITCH WITH ANOTHER LABEL
         repaint();
         System.out.println("updated plasmid!");
    }
    
    /* given an arraylist of features in the form "Feature (plasmid)", make a new plasmid*/
    private org.autogene.core.bio.entities.Plasmid generatePlasmid(ArrayList<Annotation> ann) {
        org.autogene.core.bio.entities.Plasmid p = new org.autogene.core.bio.entities.Plasmid();
        p.setName("Unnamed");
 
        ArrayList<Annotation> annotations = generateAnnotations(ann,p);
        //System.out.println("setting annotations to " + annotations);
        p.setAnnotations(annotations);
        return p;
    }
    
    private ArrayList<Annotation> generateAnnotations(ArrayList<Annotation> annotations,org.autogene.core.bio.entities.Plasmid p) {
//        ArrayList<Annotation> out = new ArrayList<Annotation>();
//        for(int i = 0; i < names.size(); i++) {
//            System.out.println("getting annotation for feature " + names.get(i) + " and inverted is " + home.getLabels().get(i).inverted);
//            home.printLabels();
//            Annotation a = getAnnotationForFeature(names.get(i));
//            out.add(a);
//        }
        
        
        ArrayList<Annotation> out = annotations;
        //System.out.println("generating annotations");
        
        System.out.println("CDEFGH");
        for(int i = 0; i < out.size(); i++) {
//            System.out.println("Annotation " + out.get(i).getFeature().getDisplayName() + " has strand " + out.get(i).getStrand().toString());
        }
        //now update start and end positions
        int start = 1;
        String totalSequence = "";
        for(int i = 0; i < out.size(); i++) {
            Annotation a = out.get(i);
            String s = a.getFeature().getSequence();
            
            /*boolean invertDNA = a.getStrand() == StrandType.COMPLEMENT;
                if(invertDNA) 
                    a.getFeature().setSequence(Annotator.invertDNA(s));
                else
                    a.getFeature().setSequence(s);
            */
          //  System.out.println("got sequence as " + s + " for feature " + a.getFeature().getDisplayName() + " because strand type is " + a.getStrand().toString());
            totalSequence += s;
            System.out.println("total sequence now " + s);
            a.setStart(start);
            a.setEnd(totalSequence.length());
            start = totalSequence.length();
        }
        
        //set the total sequence
        p.setSequence(totalSequence);
        
        return out;
    }
    
    public Annotation getAnnotationForFeature(String name) {
        
        //get feature name and plasmid name out of the 'name' object
        int index = name.lastIndexOf("(");
        String featureName = name.substring(0,index-1);
        String plasmidName = name.substring(index+1,name.lastIndexOf(")"));

        Annotation a = new Annotation();
        
        //System.out.println("about to call get copy of feature from plasmid and invertDNA is " + invertDNA);
        String strand = "";
        a.setFeature(getCopyOfFeatureFromPlasmid(featureName,plasmidName,a,strand));
        System.out.println("8765 " + strand);
        if(strand.equals("FORWARD")) {
         //   a.setStrand(StrandType.ORIGINAL);
        } else {
         //   a.setStrand(StrandType.COMPLEMENT);
        }
        System.out.println("33333 strand is " + a.getStrand());
        System.out.println("got the feature " + a.getFeature().getName() + " and sequence is " + a.getFeature().getSequence());
        a.setColor(ColorConstants.getColorForAnnotation(a));
        
        return a;
    }
    
    private org.autogene.core.bio.entities.Feature getCopyOfFeatureFromPlasmid(String feature, String plasmid, Annotation ahh, String strand) {
        org.autogene.core.bio.entities.Feature f = new org.autogene.core.bio.entities.Feature();
        org.autogene.core.bio.entities.Plasmid p = Environment.getWorkspacePlasmidByName(plasmid);
        System.out.println("getting workspace plasmid by name " + plasmid + " and its " + p);
        List<Annotation> annotations = p.getAnnotations();
        for(int i = 0; i < annotations.size(); i++) {
            System.out.println("getting copy of feature from plasmid " + i);
            Annotation a = annotations.get(i);
            org.autogene.core.bio.entities.Feature af = a.getFeature();
            System.out.println("the feature has a sequence of " + af.getSequence());
            if(af.getDisplayName().equals(feature)) {
                //System.out.println("display name is same as " + af.getDisplayName() + " and invertDNA is " + invertDNA);
                System.out.println("sequence " + af.getSequence());
                boolean invertDNA = a.getStrand() == StrandType.COMPLEMENT;
                if(invertDNA) 
                    f.setSequence(Annotator.invertDNA(af.getSequence()));
                else
                    f.setSequence(af.getSequence());
                
                System.out.println("done and sequence is " + f.getSequence());
                f.setOrganism(af.getOrganism());
                f.setPartType(af.getPartType());
                f.setType(af.getType());
                f.setName(feature);
                f.setDisplayName(feature + " ("+plasmid+")");
                
                System.out.println("98989 " + a.getStrand() + " " + a.getFeature().getDisplayName());
                if(a.getStrand() == StrandType.ORIGINAL) {
                    System.out.println("5656 setting strand to forward");
          
                    strand = "FORWARD";

                } 
                ahh.setStrand(a.getStrand());
                ahh.setScore(a.getScore());
                break;
            }
        }
        System.out.println(feature + "--"+plasmid+": f name is " + f.getName() + " " + f.getSequence());
        return f;
    }
    
    /**
     * Overridden paint component method. Update the size, then repaint the plasmid
     * @param g graphics object
     */
    
    public void paintComponent(Graphics g) {

        //super.paintComponent(g);
        System.out.println("paint component called");
        //if((previousWidth == -1 && previousHeight == -1) || (homePanel.getWidth() != previousWidth) || (homePanel.getHeight() != previousHeight)) {
            super.paintComponent(g);
            System.out.println("calling paint component because " + previousWidth + " " + previousHeight);
            setSize(homePanel.getWidth(), homePanel.getHeight());
             cgview.setWidth(getWidth());
            cgview.setHeight(getHeight());
            cgview.setBackboneRadius(Math.min(getWidth(),getHeight())*0.28);
	
            //updateCgviewSize();
            cgview.draw((Graphics2D)g);
            previousWidth = homePanel.getWidth();
            previousHeight = homePanel.getHeight();
        //}
    }
    
    /*public void repaint() {
        
        try {
        if((previousWidth == -1 && previousHeight == -1) || (homePanel.getWidth() != previousWidth) || (homePanel.getHeight() != previousHeight)) {
            super.repaint();
        } else {
            return;
        } } catch(Exception e) {
            super.repaint();
        }
    }*/
    
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

 /* Set up the CGView to display the plasmid */
    private void initCgview() {
        
        int length = plasmid.getSequence().length();
	cgview = new Cgview(length);
	
	//some optional settings
	cgview.setWidth(getWidth());
	cgview.setHeight(getHeight());
	cgview.setTitle(plasmid.getName());
	cgview.setLabelPlacementQuality(100);
	cgview.setShowWarning(true);
	cgview.setLabelLineLength(8.0d);
	cgview.setLabelLineThickness(0.5f);
        cgview.setLabelShuffle(false);
        
	//create a FeatureSlot to hold sequence features
	FeatureSlot featureSlot = new FeatureSlot(cgview, CgviewConstants.DIRECT_STRAND);

	//create random sequence features
        List<org.autogene.core.bio.entities.Annotation> annotations = plasmid.getAnnotations();
        HashMap<String,Integer> counts = new HashMap<String, Integer>();
        
        //create the feature annotations to view in the plasmid
	for (int i = 0; i < annotations.size(); i++) {

	    //update the label names such as Aapl (1) and Aapl (2) in the case that
            //there is more than one of the same annotation in two different locations
            Annotation a = annotations.get(i);
            String name = a.getFeature().getName();
            int cur = 1;
            if(counts.containsKey(name)) {
                cur = counts.get(name);
                cur++;
                counts.put(name,cur);
             //   Log.addText("plasmidpanel: " + name + "," + cur);
            }
            else {
                counts.put(name,cur);
             //   Log.addText("plasmidpanel: " + name + "," + cur);

            }
            
            //simply create the feature
            //org.autogene.cgview.Feature feature = new org.autogene.cgview.Feature(featureSlot, (name+" ("+cur+")"));
            String theNameToUse = a.getFeature().getDisplayName();
            int max_len = 15;
            if(theNameToUse.length() > max_len) {
                theNameToUse = theNameToUse.substring(0,max_len-3) + "...";
            }
            org.autogene.cgview.Feature feature = new org.autogene.cgview.Feature(featureSlot, theNameToUse);
            feature.setColor(ColorConstants.getColorForAnnotation(a));
            //feature.setColor(a.getScore() == 1.0 ? ColorConstants.NEUTRAL_COLOR_PERFECT_MATCH : ColorConstants.NEUTRAL_COLOR_IMPERFECT_MATCH);
            FeatureRange featureRange = new FeatureRange(feature, a.getStart(), a.getEnd());
	}
    }

    /* The size comes from the size of the JPanel that the PlasmidPanel lives in */
    int count = 0;
    void updateCgviewSize() {
        cgview.setWidth(getWidth());
        cgview.setHeight(getHeight());
        cgview.setBackboneRadius(Math.min(getWidth(),getHeight())*0.35);
	
        System.out.println("repainting plasmidpanel line 161 " + (count++));
        repaint();
    }
    
    /* Draws a feature called "SELECTED" when the user highlights DNA */
    void paintSelectedFeature(int start, int end) {
        //first see if a selected feature already exists
        //if it does, remove it
        removeSelectedFeature();
        
        
        //paint the new one
        org.autogene.cgview.Feature f = new org.autogene.cgview.Feature(cgview.getFeatureSlots().get(0), "SELECTED");
        f.setColor(Color.green);
        new FeatureRange(f,start,end);
        
        repaint();
    }
    
    /* Searches for the "SELECTED" feature and removes it if it exists */
    void removeSelectedFeature() {
        boolean found = true;
        ArrayList<FeatureSlot> slots = cgview.getFeatureSlots();
        for(int i = 0; i < slots.size(); i++) {
            FeatureSlot fs = slots.get(i);
            ArrayList<org.autogene.cgview.Feature> features = fs.getFeatures();
            for(int j = 0; j < features.size(); j++) {
                org.autogene.cgview.Feature f = features.get(j);
                if(f.getLabel().equals("SELECTED")) {
                    found = true;
                    features.remove(j);
                    break;
                }
            }
            
            if(found)
                break;
        }
        
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    ArrayList<String> selectedFeatures = new ArrayList<String>();
    @Override
    public void mousePressed(MouseEvent me) {
        
        /* Check to see if we clicked a label */
        
        int x = me.getX(), y = me.getY();
        
        org.autogene.cgview.Feature selectedFeature = null;
        ArrayList<OuterLabel> outerLabels = cgview.getOuterLabels();
        OuterLabel ol = null;
        for(int o = 0; o < outerLabels.size(); o++) {
            
            ol = outerLabels.get(o);
            Rectangle2D rect = ol.getBounds();

            double rectX = rect.getX(), rectY = rect.getY(), 
                   rectW = rect.getWidth(), rectH = rect.getHeight();
            
            if(x > rectX && x < (rectX+rectW) && y > rectY && y < (rectY+rectH)) {
                ArrayList<FeatureSlot> featureSlots = cgview.getFeatureSlots();

                for(int i = 0; i < featureSlots.size(); i++) {
                    FeatureSlot fs = featureSlots.get(i);
                    ArrayList<org.autogene.cgview.Feature> features = fs.getFeatures();
                    for(int j = 0; j < features.size(); j++) {
                        org.autogene.cgview.Feature f = features.get(j);
                        String featureLabel = f.getLabel();
                        
                        if(featureLabel.equals(ol.getLabelText())) {
                            selectedFeature = f;
                            break;
                        }
                        
                        if(selectedFeature != null)
                            break;

                    }
                    
                    if(selectedFeature != null)
                        break;
                }
               
            }
            
            if(selectedFeature != null)
                break;
     
        }
        
        /* If we have selected a feature with the mouse, either make it appear selected or 
         * deselected. */
        if(selectedFeature != null) {
        
            //paint the feature on the cgview 
            Annotation a = findAnnotationOfFeature(selectedFeature);
            
            //Color toUse = (a.getScore() == 1.0) ? ColorConstants.NEUTRAL_COLOR_PERFECT_MATCH : ColorConstants.NEUTRAL_COLOR_IMPERFECT_MATCH;
            Color toUse = a.getColor();
            Color fColor = selectedFeature.getColor();
            //Color blue = (a.getScore() == 1.0) ? ColorConstants.NEUTRAL_COLOR_PERFECT_MATCH : ColorConstants.NEUTRAL_COLOR_IMPERFECT_MATCH;
            Color blue = a.getColor();
            Color orange = ColorConstants.ROW_SELECTED_COLOR;
            boolean isBlue = fColor.getRed() == blue.getRed() && fColor.getGreen() == blue.getGreen() && fColor.getBlue() == blue.getBlue();
            boolean isOrange = fColor.getRed() == orange.getRed() && fColor.getGreen() == orange.getGreen() && fColor.getBlue() == orange.getBlue();
            Color turq = ColorConstants.CUSTOM_ANNOTATION_COLOR;
            boolean isTurquoise = fColor.getRed() == turq.getRed() && fColor.getGreen() == turq.getGreen() && fColor.getBlue() == turq.getBlue();
            Color ye = ColorConstants.VIEW_FEATURE_FROM_PRIVATE_REGISTRY_COLOR;
            boolean isYellow = fColor.getRed() == ye.getRed() && fColor.getGreen() == ye.getGreen() && fColor.getBlue() == ye.getBlue();
            if(isBlue || isOrange || isTurquoise || isYellow) {
                toUse = ColorConstants.SELECTED_COLOR;
                selectedFeatures.add(selectedFeature.getLabel());
            }
            else {
                toUse = a.getColor();
                //toUse = (a.getScore() == 1.0) ? ColorConstants.NEUTRAL_COLOR_PERFECT_MATCH : ColorConstants.NEUTRAL_COLOR_IMPERFECT_MATCH;
                selectedFeatures.remove(selectedFeature.getLabel());
            }
            
            
            selectedFeature.setColor(toUse);
            
            ArrayList<FeatureRange> f = selectedFeature.getRanges();
            for(int n = 0; n < f.size(); n++) {
                f.get(n).setColor(toUse);
            }
            
            //ol.setColor(Color.red);
            
            repaint();

            
  
            
        }
        else {
            System.out.println("selected feature is null");
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    public Cgview getCGView() {
        return cgview;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        
        /*int x = me.getX(), y = me.getY();
        
        Feature selectedFeature = null;
        ArrayList<OuterLabel> outerLabels = cgview.getOuterLabels();
        OuterLabel ol = null;
        for(int o = 0; o < outerLabels.size(); o++) {
            
            ol = outerLabels.get(o);
            Rectangle2D rect = ol.getBounds();

            double rectX = rect.getX(), rectY = rect.getY(), 
                   rectW = rect.getWidth(), rectH = rect.getHeight();
            
            if(x > rectX && x < (rectX+rectW) && y > rectY && y < (rectY+rectH)) {
                ArrayList<FeatureSlot> featureSlots = cgview.getFeatureSlots();

                for(int i = 0; i < featureSlots.size(); i++) {
                    FeatureSlot fs = featureSlots.get(i);
                    ArrayList<Feature> features = fs.getFeatures();
                    for(int j = 0; j < features.size(); j++) {
                        Feature f = features.get(j);
                        String featureLabel = f.getLabel();
                        
                        if(featureLabel.equals(ol.getLabelText())) {
                            //on a particular feature
                            
                            selectedFeature = f;
                            selectedFeature.setColor(Color.black);
                            
                            
                            
                        }
                        else
                        {
                           
                        }
                        
                     

                    }
                    
              
                }
                
               
            }
            else {
                                ArrayList<FeatureSlot> featureSlots = cgview.getFeatureSlots();

                for(int i = 0; i < featureSlots.size(); i++) {
                    FeatureSlot fs = featureSlots.get(i);
                    ArrayList<Feature> features = fs.getFeatures();
                    for(int j = 0; j < features.size(); j++) {
                        Feature f = features.get(j);
                boolean containsSelected = false;
                            for(int p = 0; p < selectedFeatures.size(); p++) {
                                if(selectedFeatures.get(p).equals(f.getLabel()))
                                    containsSelected = true;
                            }
                            
                            if(containsSelected)
                                f.setColor(Color.red);
                            else
                                f.setColor(Color.blue);
                    }}
            }
            
         
     
        }

* 
*/
    }

    private Annotation findAnnotationOfFeature(org.autogene.cgview.Feature f) {
        List<Annotation> annotations = plasmid.getAnnotations();
        for(int i = 0; i < annotations.size(); i++) {
            org.autogene.core.bio.entities.Feature o = annotations.get(i).getFeature();
            if(f.getLabel().equals(o.getDisplayName()))
                return annotations.get(i);
        }
        return null;
    }

    public org.autogene.core.bio.entities.Plasmid getPlasmid() {
        return plasmid;
    }

  


    

    
}

