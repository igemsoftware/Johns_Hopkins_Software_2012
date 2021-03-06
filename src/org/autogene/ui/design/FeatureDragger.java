/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.design;

import java.awt.*;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import org.autogene.algorithms.Annotator;
import org.autogene.cgview.HighlightedObject;
import org.autogene.core.bio.entities.Annotation;
import org.autogene.core.bio.entities.Annotation.StrandType;
import org.autogene.core.bio.entities.Feature;
import org.autogene.ui.cgview.BorderHighlighter;
import org.autogene.ui.cgview.ColorConstants;
import org.autogene.ui.cgview.PlasmidPanel;
import org.autogene.ui.frames.BaseInternalFrame;
import org.autogene.ui.main.AutogeneFrame;
import org.autogene.ui.main.PlasmidPopUpOption;
import org.autogene.ui.sessions.Environment;

/**
 *
 * @author Robert
 */
public class FeatureDragger extends BaseInternalFrame implements DropTargetListener {
    
    private ArrayList<FeatureDraggerLabel> labels;
    private ArrayList<FeatureSpotLabel> spots;
    
    public static int SPOT_WIDTH = 80, SPOT_HEIGHT = 95;
    int PANEL_WIDTH = 450, PANEL_HEIGHT = 2000;

    
    //String[] st = {"PcII2_promoter_oky", "BYE THERE SALLY", "GO", "CAT", "DOG", "MOUSE", "BLAH", "BVA", "DAFAD", "ABCD", "EFG", "HIJKLM", "NOP", "QRSTU", "VWXY", "ZZZ", "AAA", "BBBBB", "CCCC"};
//    String[] st = {"HI","BYE"};
    ArrayList<Annotation> annotations;
    AutogeneFrame autogeneFrame;
    DesignPlasmidPanel plasmidPanel;
            
    /**
     * Creates new form FeatureDragger
     */
    public FeatureDragger(AutogeneFrame af) {
        initComponents();
        

       
        setTitle("Design");
        panel.setTransferHandler(new FeatureDraggerTransferHandler(af,this));
        //panel.setDropMode(DropMode.ON_OR_INSERT);
        autogeneFrame = af;
        
        
        
        //calling setSize will call the resized event which will
        //initialize all the labels and spots
        //panel.setLayout(null);
        int w = mainSplitPane.getDividerLocation();
        PANEL_WIDTH = w;
        panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        //panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        
        rightPanel.setSize(400,panel.getHeight());
        bottomRightPanel.setSize(400,panel.getHeight()/2);
        plasmidPanel = new DesignPlasmidPanel(bottomRightPanel,this);
        bottomRightPanel.add(plasmidPanel);
        System.out.println(plasmidPanel.getSize());
        System.out.println(rightPanel.getSize());
        
        JList list = autogeneFrame.getPrivateRegistryList();
        int size = 0;
        annotations = new ArrayList<Annotation>();

        
        validate();

        System.out.println(panel.getSize());
        /*initSpots();
        initLabels();
        * 
        */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mainSplitPane = new javax.swing.JSplitPane();
        rightPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        bottomRightPanel = new javax.swing.JPanel();
        rightTopPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dnaPane = new javax.swing.JTextPane();

        mainSplitPane.setDividerLocation(453);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mainSplitPanePropertyChange(evt);
            }
        });

        org.jdesktop.layout.GroupLayout rightPanelLayout = new org.jdesktop.layout.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        mainSplitPane.setRightComponent(rightPanel);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setBackground(new java.awt.Color(204, 213, 222));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rearrange the features", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 15))); // NOI18N
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelMouseReleased(evt);
            }
        });
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panelComponentResized(evt);
            }
        });
        panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelMouseDragged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 420, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 484, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        jButton2.setText("Add Plasmid to Workspace");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Name:");

        jTextField1.setText("Unnamed");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField1PropertyChange(evt);
            }
        });

        org.jdesktop.layout.GroupLayout leftPanelLayout = new org.jdesktop.layout.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(leftPanelLayout.createSequentialGroup()
                .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(leftPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, leftPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jButton2)))
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(leftPanelLayout.createSequentialGroup()
                .add(leftPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2))
        );

        mainSplitPane.setLeftComponent(leftPanel);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        org.jdesktop.layout.GroupLayout bottomRightPanelLayout = new org.jdesktop.layout.GroupLayout(bottomRightPanel);
        bottomRightPanel.setLayout(bottomRightPanelLayout);
        bottomRightPanelLayout.setHorizontalGroup(
            bottomRightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 247, Short.MAX_VALUE)
        );
        bottomRightPanelLayout.setVerticalGroup(
            bottomRightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 328, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(bottomRightPanel);

        jScrollPane2.setViewportView(dnaPane);

        org.jdesktop.layout.GroupLayout rightTopPanelLayout = new org.jdesktop.layout.GroupLayout(rightTopPanel);
        rightTopPanel.setLayout(rightTopPanelLayout);
        rightTopPanelLayout.setHorizontalGroup(
            rightTopPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );
        rightTopPanelLayout.setVerticalGroup(
            rightTopPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(rightTopPanel);

        mainSplitPane.setRightComponent(jSplitPane1);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainSplitPane)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainSplitPane)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    int currentIndex = -1; //the current index we are dragging
    boolean snapped = false;
    Point offset;
    private void panelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMouseDragged
        int x = evt.getX(), y = evt.getY();
        
        int previousIndex = currentIndex;
        if(currentIndex == -1) {
            int lab = getLabelIndexAtPosition(x,y);
            if(lab == -1)
                return;
            
            //we weren't dragging anything, but now we are
            currentIndex = lab;
            //System.out.println("set current index to " + lab);
            FeatureDraggerLabel fdl = labels.get(currentIndex);
            
            //find the offset of the mouse to the top left corner of the label
            offset = new Point(x - fdl.getLocation().x, y - fdl.getLocation().y);
        }

        if(currentIndex != -1) {
            labels.get(currentIndex).setLocation(x-offset.x,y-offset.y);
            panel.setComponentZOrder(labels.get(currentIndex), 1);
            labels.get(currentIndex).addTransparency();
        }

        

        
    }//GEN-LAST:event_panelMouseDragged

    private void panelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMouseReleased
        int x = evt.getX(), y = evt.getY();
        //check if current is still null after searching the labels
        if(currentIndex != -1) {
            
            //either move current to the mouse position, or snap it into a spot
            FeatureDraggerLabel fdl = labels.get(currentIndex);
            int closestSpotIndex = getClosestSpotIndex(x,y);
            FeatureSpotLabel closestSpot = spots.get(closestSpotIndex);
            //find distance between closest spot top left (x,y) and the current dragged label (x,y)
            double dist = distance(fdl.getLocation().x,fdl.getLocation().y,closestSpot.getX(),closestSpot.getY());
            System.out.println(dist);
            if(dist < 25) {
                //is false if we are already snapped there
                boolean b = snapLabelToSpot(currentIndex, closestSpotIndex);
                //if(b) {
                 //   snapped = true;
                 //   System.out.println("snapped");
                //}
            }
            else {
                fdl.setSpot(null);
                spots.get(currentIndex).hasFeature(false);
            }
        }
//        if(currentIndex != -1) {
//            if(!snapped)
//                labels.get(currentIndex).setSpot(null);
//        }
        currentIndex = -1;
        //snapped = false;
        
        printLabelsAndSpots();
        
        //remake st
        //st = new ArrayList<String>();
        //for(int i = 0; i < labels.size(); i++) {
        //    st.add(labels.get(i).getText());
        //}
        System.out.println("calling update plasmid view from panel mouse released");
                System.out.println("TESTME1: " + plasmidPanel.getPlasmid().getSequence().toLowerCase());

        plasmidPanel.updatePlasmidView();
        System.out.println("TESTME2: " + plasmidPanel.getPlasmid().getSequence().toLowerCase());
        dnaPane.setText(plasmidPanel.getPlasmid().getSequence().toLowerCase());
        this.updateDNAHighlighting();

    }//GEN-LAST:event_panelMouseReleased

    private void panelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelComponentResized
        

    }//GEN-LAST:event_panelComponentResized

   /* public void paintComponent(Graphics g) {
          panel.setOpaque(false);
        JLabel pic = new JLabel(new ImageIcon("/Users/Robert/Desktop/iGEMDesignIcons/graphpaper_new.png"));
        pic.setSize(getWidth(),getHeight());
        pic.setLocation(0,0);
        //panel.setComponentZOrder(pic, -1);
        panel.add(pic);
        
        //super.paintComponent(g);
        
       
        
    }*/
    
    public void updatePanel() {
        panel.removeAll();
        
        
        
        PANEL_WIDTH = mainSplitPane.getDividerLocation();
        PANEL_HEIGHT = panel.getHeight();
        

        
        //System.out.println("calling init spots " + PANEL_WIDTH + " " + PANEL_HEIGHT);
        initSpots();
        initLabels();  
        
        for(int i = 0; i < labels.size(); i++) {
            panel.setComponentZOrder(labels.get(i), 0);
        }

        plasmidPanel.updatePlasmidView();

        dnaPane.setText(plasmidPanel.getPlasmid().getSequence().toLowerCase());
        this.updateDNAHighlighting();
        panel.repaint();
        //panel.validate();
        revalidate();
        repaint();
        //setVisible(true);
    }
    
    public JPanel getRightBottomPanel() {
        return bottomRightPanel;
    }
    
    private void mainSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mainSplitPanePropertyChange
        
        updatePanel();
    }//GEN-LAST:event_mainSplitPanePropertyChange

    private void panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMousePressed
        if(evt.isPopupTrigger()) {
            int lab = getLabelIndexAtPosition(evt.getX(),evt.getY());
            if(lab == -1)
                return;
            
            DesignOperatorPopUpOption p = new DesignOperatorPopUpOption(this,lab);
            p.show(evt.getComponent(),evt.getX(),evt.getY());
        }
    }//GEN-LAST:event_panelMousePressed

    private boolean importPlasmid(org.autogene.core.bio.entities.Plasmid p) {
        org.autogene.core.bio.entities.Plasmid pl = Environment.getWorkspacePlasmidByName(jTextField1.getText());
        if(pl != null) {
            
            JOptionPane.showMessageDialog(null, "Please rename this plasmid. Plasmid " + jTextField1.getText() + " already exists.");
            return false;
        }
        p.setName(jTextField1.getText());
        p.setHasBegunAnnotating();
        p.setIsAnnotating(false);
        environment.addPlasmidToWorkspace(plasmidPanel.getPlasmid());
        setVisible(false);
        return true;
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        importPlasmid(plasmidPanel.getPlasmid());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField1PropertyChange
        //plasmidPanel.getPlasmid().setName(jTextField1.getText());
    }//GEN-LAST:event_jTextField1PropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomRightPanel;
    private javax.swing.JTextPane dnaPane;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel rightTopPanel;
    // End of variables declaration//GEN-END:variables

    private void initLabels() {
        ArrayList<FeatureDraggerLabel> old = labels;
        System.out.println("old labels is");
        for(int i = 0; old!= null && i < old.size(); i++) {
            //System.out.println(old.get(i).getText() + " " + old.get(i).inverted);
        }
        labels = new ArrayList<FeatureDraggerLabel>();
        
        
        for(int i = 0; i < annotations.size(); i++) {
           FeatureDraggerLabel a = new FeatureDraggerLabel(annotations.get(i));
           
           
           //a.setStrandImageShouldUse(annotations.get(i).getStrand());
           
           
           
           a.setSize(SPOT_WIDTH,SPOT_HEIGHT);
            a.setSpot(spots.get(i));
            if(i < old.size())
                a.getAnnotation().setStrand(old.get(i).getAnnotation().getStrand());
            
            //a.setBackground(Color.lightGray);
            //a.setOpaque(true);
            //a.setForeground(Color.blue);
            //a.setBorder(BorderFactory.createLineBorder(Color.black));
            labels.add(a);
            panel.add(a);
            
           // panel.setComponentZOrder(a, );

        }
        
        System.out.println("just finished initing labels");
        for(int i = 0; i < labels.size(); i++) {
            FeatureDraggerLabel f = labels.get(i);
            //System.out.println(f.getText() + " " + f.inverted);
        }
     
    }

    private int getLabelIndexAtPosition(int x, int y) {
        for(int i = 0; i < labels.size(); i++) {
            FeatureDraggerLabel l = labels.get(i);
            Point q = l.getLocation();
            int xx = q.x, yy = q.y;
            if(x >= xx && x <= (xx+l.getWidth()) && y >= yy && y <= (yy+l.getHeight())) 
                return i;
        }
        return -1;
    }

    private void initSpots() {
        spots = new ArrayList<FeatureSpotLabel>();
        int numSpots = annotations.size();
        
        int offsetY = 20;
        int offsetX = 20;
        int initialOffsetY = 20;
        
        int w = PANEL_WIDTH, h = PANEL_HEIGHT;
        int numCol = (w-offsetX) / (offsetX + SPOT_WIDTH);
        int numRow = (h-offsetY) / (offsetY + SPOT_HEIGHT);
        
       // System.out.println(w + " " + h + " " + numCol + " " + numRow);
        int count = 0;
        for(int i = 0; i < numRow; i++) {
            for(int j = 0; j < numCol; j++) {
                
                int x = j * SPOT_WIDTH + (offsetX*(j+1));
                int y = i * SPOT_HEIGHT + (offsetY*(i+1)) + initialOffsetY;
                
                FeatureSpotLabel l = new FeatureSpotLabel(count+1);
                l.hasFeature(true);
                
               // System.out.println("drawing " + x + " " + y);
                l.setLocation(x,y);
               
                panel.add(l);
                
                spots.add(l);
                
                count++;
                if(count == numSpots)
                    return;
            }
        }
    }

    private int getClosestSpotIndex(int x, int y) {
        double bestDistance = Double.MAX_VALUE;
        int bestSpotIndex = -1;
        
        for(int i = 0; i < spots.size(); i++) {
            FeatureSpotLabel l = spots.get(i);
            double dist = distance(x,y,l.getX(),l.getY());
            if(dist < bestDistance) {
                bestDistance = dist;
                bestSpotIndex = i;
            }
        }
        
        return bestSpotIndex;
    }

    private double distance(int x, int y, int x0, int y0) {
        return Math.sqrt(Math.pow(x-x0,2)+Math.pow(y-y0,2));
    }

    private boolean snapLabelToSpot(int index, int closestSpotIndex) {
       
        System.out.println("snap label to spot");
        //label to snap in
        FeatureSpotLabel closestSpot = spots.get(closestSpotIndex);
        FeatureDraggerLabel current = labels.get(index);
        
        //if current has a spot and its the same spot we just started at
        //if(current.getSpot() != null && current.getSpot().getLocation().equals(closestSpot.getLocation())) 
        //    return false;
        
        JLabel previousSpot = current.getSpot();
        
        
        //see if there is a label in the new spot
        FeatureDraggerLabel labelInNewSpot = null;
        for(int i = 0; i < labels.size(); i++) {
            FeatureDraggerLabel l = labels.get(i);
            
            if(labels.get(i).getSpot() == null)
                continue;
            
            if(labels.get(i).getSpot().getLocation().equals(closestSpot.getLocation())) {
                labelInNewSpot = labels.get(i);
                //System.out.println("found label in new spot -" + labelInNewSpot.getText() + "-");
                break;
            }
        }
        
        //closest spot - snapping in to this
        //previous spot - snapping from
        //swapping previous spot with closest spot
        
        int indexPreviousSpot = index;
        int currentSpot = closestSpotIndex;
        Annotation temp = makeCopyOfAnnotation(annotations.get(indexPreviousSpot));//plasmidPanel.getAnnotationForFeature(annotations.get(indexPreviousSpot).getFeature().getDisplayName());
        FeatureDraggerLabel tempLabel = labels.get(indexPreviousSpot);
        
        //String temp = st.get(index);
        current.setSpot(closestSpot);
        labels.set(indexPreviousSpot, labels.get(currentSpot));
        Annotation aaa = makeCopyOfAnnotation(annotations.get(currentSpot));//plasmidPanel.getAnnotationForFeature(annotations.get(currentSpot).getFeature().getDisplayName());
        annotations.set(indexPreviousSpot,aaa);
        //spots.get(indexPreviousSpot).hasFeature(true);
        current.removeTransparency();
//        closestSpot.showSnapEffect();
        
        if(previousSpot != null && labelInNewSpot != null) {
            //if this label was in a spot before the drag and its target had a label in them already
            //switch the two spots
            labelInNewSpot.setSpot(previousSpot);
            annotations.set(currentSpot, temp);
            labels.set(currentSpot,tempLabel);
        } else if(previousSpot == null && labelInNewSpot != null) {
            //if this label was not in a spot before but there is a label in the new spot
            
        }
        
        
        return true;
        
    }

    private void printLabelsAndSpots() {
        String s = "";
        for(int i = 0; i < labels.size(); i++) {
            s += labels.get(i).getText();
            if(labels.get(i).getSpot() == null) {
                s += ": null, ";
            } else {
                s += ": " + labels.get(i).getSpot().getLocation() + ", ";
            }
        }
        

        System.out.println(s);
        //                System.out.println();

    }

    ArrayList<Annotation> getListOfAnnotations() {
        if(annotations == null) {
            return new ArrayList<Annotation>();
        }
        return annotations;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void addDraggableFeatures(Object[] selectedValues) {
        for(int i = 0; i < selectedValues.length; i++) {
            //plasmidPanel.getAnnotationForFeature((String)selectedValues[i])
            annotations.add(plasmidPanel.getAnnotationForFeature((String)selectedValues[i]));
        }
  
        updatePanel();
        panel.repaint();
        panel.validate();
        panel.revalidate();
        //panel.repaint();
       // panel.setSize(panel.getWidth(), panel.getHeight()); 
       //panel.get
        //panel.setVisible(false);
        //panel.setVisible(true);
        plasmidPanel.repaint();
        plasmidPanel.validate();
        //validate();
    }

    void invertSelectedFeature(int p) {
         System.out.println("before inverting  " + p + " sequence is now " + labels.get(p).getAnnotation().getFeature().getSequence());
        //labels.get(p).invertImage();
         System.out.println("inverted " + p + " so sequence is now " + labels.get(p).getAnnotation().getFeature().getSequence());

        invertAnnotation(annotations.get(p));
        //labels.get(p).setStrandImageShouldUse(labels.get(p).getAnnotation().getStrand());
        labels.get(p).initTheImageIcon();
        
        //System.out.println("just finished inverting a  label");
        //for(int i = 0; i < labels.size(); i++) {
        //    FeatureDraggerLabel f = labels.get(i);
            //System.out.println(f.getText() + " " + f.inverted);
       // }
        //this.updatePanel();
        plasmidPanel.updatePlasmidView();
        dnaPane.setText(plasmidPanel.getPlasmid().getSequence().toLowerCase());
        updateDNAHighlighting();
        
    }

    ArrayList<FeatureDraggerLabel> getLabels() {
        return labels;
    }

    void printLabels() {
        for(int i = 0; i < labels.size(); i++) {
            FeatureDraggerLabel f = labels.get(i);
            //System.out.println("FeatureDragger printLabels: " + f.getText() + " " + f.inverted);
        }
    }
    
    void updateDNAHighlighting() {
       // return;
        
        if(plasmidPanel.getPlasmid() == null)
            return;
        
        System.out.println("highlighting!!!!");
        
        List<Annotation> an = plasmidPanel.getPlasmid().getAnnotations();
        for(int i = 0; i < an.size(); i++) {
            Annotation a = an.get(i);
            System.out.println("getting color for annotation " + a);
            Color c = ColorConstants.getColorForAnnotation(a);
            
            try {
                System.out.println("about to higlight " + a.getFeature().getDisplayName());
                System.out.println("start: " + a.getStart());
                System.out.println("end: " + a.getEnd());
                
                dnaPane.getHighlighter().addHighlight(a.getStart()-1, a.getEnd(), getHighlightPainter(c));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
                //Logger.getLogger(FeatureDragger.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
       System.out.println("highlightingggggg");
       
       
    }
    
    private Highlighter.HighlightPainter getHighlightPainter(Color c) {
        return new DefaultHighlighter.DefaultHighlightPainter(c);

    }

    void duplicateSelectedFeature(int index) {
        FeatureDraggerLabel f = labels.get(index);
        this.addDraggableFeatures(new Object[]{f.getText()});
    }

    void deleteSelectedFeature(int index) {
        FeatureDraggerLabel f = labels.get(index);
        annotations.remove(index);
        updatePanel();
        plasmidPanel.repaint();
    }

    public Annotation makeCopyOfAnnotation(Annotation a) {
        Annotation out = new Annotation();
        out.setFeature(makeCopyOfFeature(a.getFeature()));
        out.setPlasmid(a.getPlasmid());
        out.setEnd(a.getEnd());
        out.setStart(a.getStart());
        out.setScore(a.getScore());
        out.setStrand(a.getStrand());
       return out;
    }

    private Feature makeCopyOfFeature(Feature feature) {
        Feature f = new Feature();
        f.setDescription(feature.getDescription());
        f.setDisplayName(feature.getDisplayName());
        f.setName(feature.getName());
        f.setOrganism(feature.getOrganism());
        f.setPartType(feature.getPartType());
        f.setSequence(feature.getSequence());
        f.setType(feature.getType());
        return f;
    }

    private void invertAnnotation(Annotation g) {
        System.out.println("annotation with feature " + g.getFeature().getDisplayName() + " strand is " + g.getStrand());

        if(g.getStrand() == StrandType.COMPLEMENT) 
            g.setStrand(StrandType.ORIGINAL);
        else
            g.setStrand(StrandType.COMPLEMENT);
        
        g.getFeature().setSequence(Annotator.invertDNA(g.getFeature().getSequence()));
        System.out.println("annotation with feature " + g.getFeature().getDisplayName() + " strand is " + g.getStrand());
    }
}
