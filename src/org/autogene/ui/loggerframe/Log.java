/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.loggerframe;

import java.awt.Color;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import org.autogene.ui.cgview.LogEventType;
import org.autogene.ui.main.AutogeneFrame;
import org.autogene.ui.sessions.Environment;
import org.autogene.util.DateUtil;

/**
 *
 * @author Robert
 */
public class Log {
    private static Log projectLog;
    
    

    public static void initText() {
        try {
            String s = ("Autogene launched: " + DateUtil.formatDate(DateUtil.getCurrentDate()));
            projectLog.doc.insertString(0, s, regular);
        } catch (BadLocationException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addText(String string) {
        addText(string, LogEventType.NEUTRAL);
    }

    public static void clearLog() {
        projectLog.textPane.setText("");
        addText("Cleared log.",false,LogEventType.SUCCESS);
    }
    private final Export export;
    private String logText;

    /**
     * Creates new form Log
     */

    
    public static Log getLogObject(AutogeneFrame af, int width) {
        return new Log(new Export(), af, width);
    }

    public static Log getProjectLog() {
        return projectLog;
    }

    public static void setProjectLog(Log log) {
        projectLog = log;
    }
    private Environment environment;
    private int logHeight = 150;
    private AutogeneFrame home;
    private JTextPane textPane;
    
    public Log(Export export, AutogeneFrame af, int width) {
        home = af;
        textPane = home.getLogTextPane();
        
        //initComponents();
        initMyComponents();
        //setSize(width, logHeight);
        this.export = export;
    }

    
    private void exportOptionActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        //export to txt
        logText = textPane.getText();
        export.addLogText(logText);
        export.setVisible(true);
        
    }  
    
    public static void addText(String text, LogEventType type) {
        addText(text, true, type);
    }
    
    public static void addText(String text, boolean addNewLineCharacter,LogEventType type) {
        if(text.indexOf("\n") != 0 && addNewLineCharacter) 
            text = "\n" + text;
        
        Style style = regular;
        if(type == LogEventType.SUCCESS)
            style = green;
        else if(type == LogEventType.FAILURE)
            style = red;
       
        
        try {
            projectLog.doc.insertString(projectLog.doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        projectLog.textPane.setCaretPosition(projectLog.textPane.getDocument().getLength());

    }

   

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private static Style regular, italic, bold, red, green;
    public StyledDocument doc;
    private void initMyComponents() {
        // Define document styles
        doc = textPane.getStyledDocument();

        // Load the default style and add it as the "regular" text
        Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
        regular = doc.addStyle("regular", def );

        // Create an italic style
        italic = doc.addStyle( "italic", regular );
        StyleConstants.setItalic( italic, true );

        // Create a bold style
        bold = doc.addStyle("bold", regular );
        StyleConstants.setBold( bold, true );

        // Create a red style
        red = doc.addStyle("red", regular);
        StyleConstants.setForeground(red, Color.red);
        
        // Create a green style
        green = doc.addStyle("green", regular);
        StyleConstants.setForeground(green, new Color(0, 100, 0));
    }

}
