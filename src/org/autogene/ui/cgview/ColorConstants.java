/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import java.awt.Color;
import org.autogene.core.bio.entities.Annotation;

/**
 *
 * @author Robert
 */
public class ColorConstants {
    public static final Color SELECTED_COLOR = Color.red,
                              NEUTRAL_COLOR_PERFECT_MATCH = Color.blue,
                              NEUTRAL_COLOR_IMPERFECT_MATCH = new Color(0, 191, 255),
                              ROW_SELECTED_COLOR = Color.orange,
                              CUSTOM_ANNOTATION_COLOR = new Color(0, 206, 209);
    public static Color VIEW_FEATURE_FROM_PRIVATE_REGISTRY_COLOR = new Color(139,139,0);

    public static Color restrictionEnzymeColor = Color.RED;
    public static Color geneColor = Color.GREEN;
    public static Color promoterColor = Color.PINK;
    public static Color terminatorColor = Color.BLUE;
    public static Color defaultColor = Color.ORANGE;
    public static Color primerColor = new Color(159,128,0);
    public static Color originColor = new Color(139,69,19);
    static Color getNeutralColorForAnnotation(Annotation a) {
        if(a.getScore() == 1.0) {
            return NEUTRAL_COLOR_PERFECT_MATCH;
        } else {
            return NEUTRAL_COLOR_IMPERFECT_MATCH;
        }
    }
    
    public static Color getColorForAnnotation(Annotation a) {
        if(a.getFeature().getType().getName().equals("Gene"))
            return (ColorConstants.geneColor);
        else if(a.getFeature().getType().getName().equals("Promoter"))
            return (ColorConstants.promoterColor);
        else if(a.getFeature().getType().getName().equals("Terminator"))
            return (ColorConstants.terminatorColor);
        else if(a.getFeature().getType().getName().equals("Primer"))
            return (ColorConstants.primerColor);
        else if(a.getFeature().getType().getName().equals("Origin"))
            return (ColorConstants.originColor);
        else if(a.getFeature().getType().getName().equals("Restriction Enzyme")) 
            return (ColorConstants.restrictionEnzymeColor);
        else
            return (ColorConstants.defaultColor);
    
    }
    
}
