/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.util;

import java.text.SimpleDateFormat;
import java.util.Date;

    import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
 
/**
 *
 * @author Robert
 */
public class DateUtil {
    public static Date getCurrentDate() {
        return new Date();
    }
    
    public static String formatDate(Date d) {
        return new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(d);
    }
}
