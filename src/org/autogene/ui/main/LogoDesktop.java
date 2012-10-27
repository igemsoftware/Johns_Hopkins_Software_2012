/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

/**
 *
 * @author Robert
 */
public class LogoDesktop extends JDesktopPane {
            BufferedImage img = null;
            BufferedImage back = null;
    public LogoDesktop() {
        super();
        try {
         //  File f = getClass().getResource("/org/autogene/ui/resources/images/qc.png");
          // new javax.swing.ImageIcon(getClass().getResource("/org/autogene/ui/resources/images/qc.png")).getImage();
            //Resource r = getClass().getResource("/org/autogene/ui/resources/images/green-bullet.png");
            img = ImageIO.read(getClass().getResource("/org/autogene/ui/resources/images/autogenelogo2.png"));

             // img = ImageIO.read(new File("/Users/Robert/Desktop/iGEMDesignIcons/autogenelogo_plasmid_bw.png"));

           // back = ImageIO.read(new File("/Users/Robert/Desktop/iGEMDesignIcons/autogene_blueprint.png"));
            back = ImageIO.read(getClass().getResource("/org/autogene/ui/resources/images/autogene_blueprint.png"));
            //new ImageIcon(getClass().getResource("/org/autogene/ui/resources/images/red-bullet.png"));
            //new ImageIcon(getClass().getResource("/org/autogene/ui/resources/images/autogenelogo.png"));

            //new ImageIcon(this.getClass().getClassLoader().getResource("/org/autogene/resources/images/autogenelogo.png")).getImage();
            //new ImageIcon(this.getClass().getResource("/org/autogene/logo/autogenelogo.png"));
            //img = imageToBufferedImage( new ImageIcon(this.getClass().getResource("/org/autogene/logo/autogenelogo.png")).getImage());
        } catch (IOException e) {
            System.out.println(new File(".").getAbsolutePath());
            e.printStackTrace();
        }

    }
    
    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
  }
    
    public void paintComponent(Graphics g) {
              g.drawImage(back,0,0,this);

        
        int w = getWidth(), h = getHeight();
        int iw = img.getWidth(), ih = img.getHeight();
        int x = (w/2)-(iw/2);
        int y = (h/2)-(ih/2);
        //g.drawImage(img, x, y, this)
        g.drawImage(img,-25,getHeight()-265,300,300,this);
        
       
    }
}
