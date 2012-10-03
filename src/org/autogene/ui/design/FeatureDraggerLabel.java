/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.autogene.algorithms.Annotator;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Annotation.StrandType;
import org.autogene.core.bio.entities.Feature;
import org.autogene.ui.cgview.ColorConstants;

/**st
 *
 * @author Robert
 */
public class FeatureDraggerLabel extends JLabel {

    public void initTheImageIcon() {
        System.out.println("initing the image icon and strand type is " + annotation.getStrand());
        Feature f = annotation.getFeature();
 imageStrandType = annotation.getStrand();
        String img = "";
        if(imageStrandType == StrandType.ORIGINAL) {
            if(f.getType().getName().equals("Gene"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/CDS_forward.png";
            else if(f.getType().getName().equals("Promoter"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/promoter_forward.png";
            else if(f.getType().getName().equals("Terminator"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Terminator_forward.png";
            else if(f.getType().getName().equals("Primer"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Primer_forward.png";
            else if(f.getType().getName().equals("Origin"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Origin_forward.png";
            else
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Generic_forward.png";
        }
        else {
            if(f.getType().getName().equals("Gene"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/CDS_reverse.png";
            else if(f.getType().getName().equals("Promoter"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/promoter_reverse.png";
            else if(f.getType().getName().equals("Terminator"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Terminator_reverse.png";
            else if(f.getType().getName().equals("Primer"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Primer_reverse.png";
            else if(f.getType().getName().equals("Origin"))
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Origin_reverse.png";
            else
                img = "/Users/Robert/Desktop/iGEMDesignIcons/Generic_reverse.png";
        }
             
            
        System.out.println(img);
        imageIcon = new ImageIcon(img);
        this.setIcon(imageIcon);
    }
    
    private StrandType imageStrandType;
    private ImageIcon imageIcon;
    private JLabel spot; //a spot is a jlabel with a border
    private Annotation annotation; //the annotation associated with this label
    
    FeatureDraggerLabel(Annotation a) {
        //super("<html><body style='width: 50px'>"+tesT_ME, new ImageIcon("/Users/Robert/Desktop/iGEMDesignIcons/scissors.jpg"), JLabel.CENTER);
       // super(a.getFeature().getDisplayName(), img, JLabel.CENTER);
                super(a.getFeature().getDisplayName(), JLabel.CENTER);
                        annotation = a;
    initTheImageIcon();
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setIconTextGap(-2);
        setFont(new Font("SansSerif",Font.PLAIN,12));
        setBorder(BorderFactory.createLineBorder(Color.black));

        
       // super(new ImageIcon("/images/scissors.jpg"));
       // System.out.println(new File(".").getAbsolutePath());
    }
    
 

  public static int getMaxFittingFontSize(Graphics g, Font font, String string, int width, int height){
    int minSize = 0;
    int maxSize = 288;
    int curSize = font.getSize();

    while (maxSize - minSize > 2){
      FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
      int fontWidth = fm.stringWidth(string);
      int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

      if ((fontWidth > width) || (fontHeight > height)){
        maxSize = curSize;
        curSize = (maxSize + minSize) / 2;
      }
      else{
        minSize = curSize;
        curSize = (minSize + maxSize) / 2;
      }
    }

    return curSize;
  }
//    
//    public void setLocation(int x, int y) {
//        int deduction = FeatureSpotLabel.MAX_BORDER_SIZE;
//        super.setLocation(x+deduction,y+deduction);
//    }
    
    
    //a FeatureDraggerLabel has a spot which it resides
    //if the spot is null, it is not in a spot
    
    public JLabel getSpot() {
        return spot;
    }
    
    public void setSpot(JLabel s) {
        spot = s;
        if(spot != null) {
            setLocation(spot.getX(), spot.getY());
            //System.out.println("setting label -"+getText()+"- to " + spot.getLocation());
        }
       
    }

    void addTransparency() {
        //System.out.println("adding transparency");
        setOpaque(false);
    }

    void removeTransparency() {
        //setOpaque(true);
    }

    void invertImage() {
        StrandType st = annotation.getStrand();
        System.out.println("attempting to invert. now, it's " + st.toString() + " and " + annotation.getFeature().getSequence());
        
        
       /* if(st == StrandType.ORIGINAL) {
            annotation.setStrand(StrandType.COMPLEMENT);
        }
        else {
            annotation.setStrand(StrandType.ORIGINAL);
        }
        
        annotation.getFeature().setSequence(Annotator.invertDNA(annotation.getFeature().getSequence()));
        */
        initTheImageIcon();
        
        System.out.println("and after inverted is now " + annotation.getStrand().toString() + " so sequence is " + annotation.getFeature().getSequence());
    }

    void setInverted(boolean b) {
        if(b)
            annotation.setStrand(StrandType.COMPLEMENT);
        else
            annotation.setStrand(StrandType.ORIGINAL);
        
        initTheImageIcon();
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    void setStrandImageShouldUse(StrandType strand) {
        imageStrandType = strand;
        initTheImageIcon();
    }
    
}
