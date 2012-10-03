/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.main;

import java.awt.BorderLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;

/**
 *
 * test
 * @author Robert
 */
public class Temp {
    private int temppp;
    
    Temp() {
        boolean resizable = true;
boolean closeable = true;
boolean maximizable  = true;
boolean iconifiable = true;
String title = "Frame Title";
JInternalFrame iframe = new JInternalFrame(title, resizable, closeable, maximizable, iconifiable);

// Set an initial size
int width = 200;
int height = 50;
iframe.setSize(width, height);

// By default, internal frames are not visible; make it visible
iframe.setVisible(true);

// Add components to internal frame...
iframe.getContentPane().add(new JTextArea());

// Add internal frame to desktop
JDesktopPane desktop = new JDesktopPane();
desktop.add(iframe);

// Display the desktop in a top-level frame
JFrame frame = new JFrame();
frame.getContentPane().add(desktop, BorderLayout.CENTER);
frame.setSize(300, 300);
frame.setVisible(true);
    }
    public static void main (String[] args) {
        new Temp();
    }
}
