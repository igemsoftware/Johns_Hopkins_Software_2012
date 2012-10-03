/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Robert
 */
public class FeatureSpotLabel extends JLabel {
//    public static int MAX_BORDER_SIZE = 4;
    
    private int num;
    
    public FeatureSpotLabel(int num) {
        this.num = num;
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(FeatureDragger.SPOT_WIDTH, FeatureDragger.SPOT_HEIGHT);
        setBackground(new Color(255,255,255,0));
        setForeground(Color.gray);
        Font f = new Font("Monospaced",Font.BOLD,40);
        setFont(f);
        setText(num+"");
        setHorizontalAlignment(SwingConstants.CENTER);
        
        
    }
    
    public void hasFeature(boolean b) {
        //if(b)
        //    setText("");
        //else
            setText(num+"");
    }
    
    public void makeBorderGray() {
        setBorder(BorderFactory.createLineBorder(Color.gray));
    }
    
   
//    public void showSnapEffect() {
//        new ShowSnapEffect(this);
////        setBorder(BorderFactory.createLineBorder(Color.black,1));
//
//    }


}

//class ShowSnapEffect implements Runnable {
//    FeatureSpotLabel fsl;
//    public ShowSnapEffect(FeatureSpotLabel f) {
//        fsl = f;
//        new Thread(this).start();
//    }
//
//    @Override
//    public void run() {
//        for(int i = 2; i <= 4; i++) {
//            try {
//                Thread.sleep(50);
//            } catch(Exception e) {
//                
//            }
//        }
//        for(int i = 3; i >= 1; i--) {
//            System.out.println("blh " + i);
//            fsl.setBorder(BorderFactory.createLineBorder(Color.black,i));
//            fsl.repaint();
//            try {
//                Thread.sleep(50);
//            } catch(Exception e) {
//                
//            }
//        }
//        
//    }
//}
