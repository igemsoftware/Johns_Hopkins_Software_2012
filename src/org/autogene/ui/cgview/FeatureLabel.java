/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import org.autogene.cgview.Feature;

/**
 *
 * @author Robert
 */
public class FeatureLabel {
    private String label;
    private String nickname;
    
    private Feature feature;
    
    public FeatureLabel(Feature feature) {
        this.feature = feature;
        this.label = feature.getLabel();
        
        if(label.lastIndexOf("_") != -1) {
            this.nickname = label.substring(0,label.lastIndexOf("_"));
        }
        else {
            this.nickname = label;
        }
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public String toString() {
        return label;
    }
}
