/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.autogene.core.bio.entities.Plasmid;

/**
 *
 * @author Robert
 */
public class SequenceViewer extends JPanel {
    private final int SPACE_PER_LETTER = 10;
    private final String sequence;
    private BufferedImage offscreen;
    
    public SequenceViewer(Plasmid p) {
        this(p.getSequence());
    }
    
    public SequenceViewer(String s) {
        sequence = s;
        setLayout(null);
        
        setSize(sequence.length() * SPACE_PER_LETTER, 100);
                setPreferredSize(new Dimension(sequence.length() * SPACE_PER_LETTER, 100));

        
    }
    
   
    @Override
    public void paintComponent(Graphics go) {
        offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        
        Graphics g = offscreen.getGraphics();
        //System.err.println("sv panel paintComponent");
        g.setColor(Color.LIGHT_GRAY);
               // g.setColor(new Color(100, (int)(Math.random()*250), 120));

        g.fillRect(0, 0, getWidth(), getHeight());
        //System.err.println("repainting");
       
        
        //draw original
        for(int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if(c == 'A')
                g.setColor(Color.blue);
            else if(c == 'T')
                g.setColor(Color.green);
            else if(c == 'G')
                g.setColor(Color.yellow);
            else
                g.setColor(Color.red);
            
            g.drawString(c+"", (i*SPACE_PER_LETTER), 10);
        }
        
        //draw complement
        for(int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if(c == 'A')
                g.setColor(Color.blue);
            else if(c == 'T')
                g.setColor(Color.green);
            else if(c == 'G')
                g.setColor(Color.yellow);
            else
                g.setColor(Color.red);
            
            g.drawString(c+"", (i*SPACE_PER_LETTER), 30);
        }
        
        //draw labels
        g.setColor(Color.black);
        int N = sequence.length();
        for(int i = 1; i <= N; i++) {
            if(i == 1 || i % 10 == 0) {
                int spot = ((i-1)*SPACE_PER_LETTER);
                g.drawString(i+"",spot,50);
            }
        }
        
        go.drawImage(offscreen, 0, 0, null);
    }
    
    public static void main (String[] args) {
        JFrame f = new JFrame();
        f.setPreferredSize(new Dimension(500,200));
        f.add(new SequenceViewer("ATATATATCGAGATACATAGACATAGACATAGACATGAGAGACCCCCAATT"));
        //f.pack();
        f.setVisible(true);
    }
}
