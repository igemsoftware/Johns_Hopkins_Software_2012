/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.cgview;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.loggerframe.Log;

/**
 *
 * @author Robert
 */
public class HighlightedObject {

    
    private int start, stop;
    private String label;
    private Color color;
    public boolean isSelectedOnThePlasmid = false;
    private Color originalColor;
    
    private static ArrayList<Color> colors;

    
    private static int colorIndex = 0;
    
    public HighlightedObject(HighlightedObject o) {
        this.start = o.getStart();
        this.stop = o.getStop();
        this.label = o.getLabel();
        this.color = o.getColor();
        this.originalColor = color;
    }
    
    public HighlightedObject(int start, int stop , String label, Color c) {
        this(start,stop,label);
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
        originalColor = color;
        
    }
    
    public HighlightedObject(int start, int stop, String label) {
        this.start = start;
        this.stop = stop;
        this.label = label;
        
        
        if(colors == null) {
            initColorsList();
        }
        
        
        color = colors.get(colorIndex);
        originalColor = color;
       //Log.addText("color index: " + colorIndex + " color=" + color, LogEventType.SUCCESS);

        
        colorIndex++;

        if(colorIndex == colors.size()) {
            colorIndex = 0;
        }
    }
    
    public void initColorsList() {
        colors = new ArrayList<Color>();
        //http://web.njit.edu/~kevin/rgb.txt.html
        
        int a = 100;
        
        colors.add(new Color(185,211,238,a)); //SlateGray2
        colors.add(new Color(118,238,198,a)); //aquamarine2
        colors.add(new Color(238,180,180,a)); //RosyBrown3
        colors.add(new Color(235,199,158,a)); //New Tan
        colors.add(new Color(193,255,193,a)); //DarkSeaGreen1
        colors.add(new Color(240,130,140,a)); //khaki1
        colors.add(new Color(143,188,143,a)); //Pale Green
        colors.add(new Color(255,165,0,a));   //orange
        colors.add(new Color(255,182,193,a)); //LightPink
        colors.add(new Color(173,234,234,a)); //Turquoise
        colors.add(new Color(255,255,0,a));   //yellow
        colors.add(new Color(153,204,50,a));   //yellow green
        colors.add(new Color(255,64,64,a));    //brown1
        colors.add(new Color(92,178,205,a));    //steel blue
 
        
        //Collections.shuffle(colors);

    }

    public HighlightedObject(FeatureRange fr) {
        this(fr.getStart(),fr.getStop(),fr.getLabel());
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }
    
    public Color getColor() {
        return color;
    }

    public boolean equalsFeatureRange(FeatureRange fr) {
        return fr.getStart() == start && fr.getStop() == stop && fr.getLabel().equals(label);
    }
    
    public static void decrementColorIndex() {
        colorIndex--;
        if(colorIndex==-1)
            colorIndex=0;
    }

    private String getLabel() {
        return label;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setInvisible() {
        setColor(new Color(0,0,0,0));
    }
    
    public Color getOriginalColor() {
        return originalColor;
    }

    public String toString() {
        return isSelectedOnThePlasmid+"";
    }
}
