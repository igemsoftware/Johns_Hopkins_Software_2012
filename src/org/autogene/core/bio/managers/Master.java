/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import java.net.URISyntaxException;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Robert
 */
public class Master {
    
    public static EntityManagerFactory getEntityManager() {                                  
                                                                                
        Map<String, String> dbProps = new HashMap<String, String>();            
                                                                                
       // dbProps.put("eclipselink.logging.level",                                
        //        appConf.get("eclipselink.logging.level", "INFO").toString());   
                                                                                
        // On linux, the GSSAPI is not available. Use a default user/password   
        // pair to connect                                                      
       // if ("Linux".equals(System.getProperty("os.name"))) {                    
        
        
        /*
        dbProps.put("javax.persistence.jdbc.url",                           
                    String.format("jdbc:jtds:sqlserver://%s/%s", "jdbc:mysql://localhost:3306/", "autogenedb"));  
            
            dbProps.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");                        
            dbProps.put("javax.persistence.jdbc.password","");       
            
            dbProps.put("javax.persistence.jdbc.user","root");   
            
            
            
            
        //} else {                                                                
            /*dbProps.put("javax.persistence.jdbc.url",                           
                    String.format("jdbc:sqlserver://%s;databaseName=%s;integratedSecurity=true",
                    appConf.get("db.host", "my-default-host"),                          
                    appConf.get("db.database", "my-default-db")));                
            dbProps.put("javax.persistence.jdbc.driver",                        
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver");      
                    * 
                    */
        //}                                                                       
       // appConf.flush();
            
            
       dbProps.put("hibernate.connection.username",theUsername);
      dbProps.put("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
     dbProps.put("hibernate.connection.password",thePassword);
      dbProps.put("hibernate.connection.url",theHost);
      dbProps.put("hibernate.dialect","org.hibernate.dialect.MySQL5InnoDBDialect");
      dbProps.put("hibernate.cache.provider_class","org.hibernate.cache.NoCacheProvider");
           dbProps.put("hibernate.hbm2ddl.auto","update");

        EntityManagerFactory fact = Persistence.createEntityManagerFactory("autogenePU", dbProps);
        return fact  ;                                    
   }
    
 static String theHost, theUsername, thePassword;


    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("*** Driver loaded");
        }
        catch(Exception e)
        {
            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
        }

    }
    
    public void initialize() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(ClassLoader.getSystemResource("/org/autogene/ui/resources/images/autogenelogo2.png").toURI())));
            StringTokenizer st = new StringTokenizer(br.readLine(), "--");
            theHost = st.nextToken();
            theUsername = st.nextToken();
            thePassword = st.nextToken();
        } catch (Exception ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean propertiesFileExists() {
        try {
           // JOptionPane.showMessageDialog(null, "found properties file");
            BufferedReader br = new BufferedReader(new FileReader(new File(".").getAbsolutePath() + "/prop/Properties.prop")); 
            //File f = new File(ClassLoader.getSystemResource("/org/autogene/core/bio/managers/Properties.prop").toURI());
            //BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            if(line == null || line.equals(""))
                return false;
            else
                return true;
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(null, ex.getMessage());
            return false;
        }
    }
    


    public static void initializePropertiesValues(String host, String username, String password) {
        try {
            PrintWriter p = new PrintWriter(new File(new File(".").getAbsolutePath() + "/prop/Properties.prop"));
            p.println(host+"--"+username+"--"+password);
            
            theHost = host;
            theUsername = username;
            thePassword = password;
            p.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean testConnection(String host, String username, String password, boolean showSuccess) {
        try {
            System.out.println("testing connection with " + host + " " + username + " " + password);
            Connection c = DriverManager.getConnection(host, username,password);
           Statement stmt = c.createStatement();
      
      String sql = "CREATE DATABASE IF NOT EXISTS AutogeneDatabase";
      stmt.executeUpdate(sql);
      if(showSuccess) {
            JOptionPane.showMessageDialog(null,"Connected successfully!");
      }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"The connection failed: " + ex.getMessage());
            return false;
        }

        return true;
    }
    
    static int total = 1337098;
    static long timeElapsed;
    static long timeStarted;

    public static void setupDatabase(String host, String username, String password, JProgressBar progress, JLabel timeLab) throws SQLException
    {
        
        //make sure there is not a backslash at the end of host
        if(host.charAt(host.length()-1) == '/') 
            host = host.substring(0,host.length()-1);
        
        createTable(host,username,password);
        
        String s            = new String();
        StringBuffer sb = new StringBuffer();

        // Connection c = DriverManager.getConnection(JOptionPane.showInputDialog("Server (ex: jdbc:mysql://localhost:3306/AutogeneDatabase"), JOptionPane.showInputDialog("Username"), JOptionPane.showInputDialog("Password"));
         Connection c = DriverManager.getConnection(host+"/AutogeneDatabase", username,password);

            Statement st = c.createStatement();
            String total = "";
                        int count = 0;

        try
        {
            timeStarted = System.currentTimeMillis();
            System.out.println("ABOVE: " + host);
             InputStreamReader isReader= 
                      new InputStreamReader(
                          Master.class.getResourceAsStream("/org/autogene/database/Compiled.sql"));
      BufferedReader br = new BufferedReader(isReader); 
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character

            //JOptionPane.showMessageDialog(null,"running sql file");

          /*  String theRest = "";
            String line = br.readLine();
            int count = 0;
            while(true) {
                if(line == null)
                    break;
                
                line = line.trim();
                int ind = line.indexOf(";");
               
                //System.out.println("index is " + ind);
                if(ind == -1) {
                    total += line.trim();
                    line = br.readLine();
                }
                else {
                    
                    total += line.substring(0,ind).trim();
                   
                    //if(!total.substring(total.indexOf(";")-1,total.indexOf(";")).equals(")"))
                    //    System.out.println(total);
                    
                    st.executeUpdate(total);
                    count++;
                    total = "";
                    line = line.substring(ind+1);
                }
                progress.setValue((int)((count*100.0)/1337098));
            }
            
*/
            total = br.readLine();
            System.out.println(total);
            
            int yyy = 0;
            while(true) {
                if(total == null)
                    break;
                if(total.equals("") || total.trim().equals(""))
                    continue;
                
                //System.out.println("executing");
                st.executeUpdate(total);
                //System.out.println("done");
                total = br.readLine();
                count++;
                long timeNow = System.currentTimeMillis();
                long timeElapsed = timeNow - timeStarted;
                double percentDone = (count*1.0) / 1337098;

                yyy++;
                if(yyy == 1000) {
                    int timeExpected = (int) (((1 - percentDone)*(timeElapsed))/percentDone);
                    int sec = timeExpected / 1000;
                    int min = sec / 60;
                    sec = sec - (60*min);
                
                    String ss = min + " min. " + sec + " sec.";
                
                
                    timeLab.setText(ss);
                    progress.setValue((int)((count*100.0)/1337098));
                    yyy = 0;
                }

            }
            
            initializePropertiesValues(host+"/AutogeneDatabase",username,password);
            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            /*String[] inst = sb.toString().split(";");

           

            for(int i = 0; i<inst.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst[i].trim().equals(""))
                {
                     System.out.println(">>"+inst[i]);
                    st.executeUpdate(inst[i]);
                    System.out.println(">>"+inst[i]);
                }
            }
  */
        }
        catch(Exception e)
        {
                                System.out.println("crashed on count=" + count + ", line: " + total);

            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
        
                    JOptionPane.showMessageDialog(null,"The databases have finished initializing!");


    }
    
    // JDBC driver name and database URL
   //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   //static final String DB_URL = "jdbc:mysql://localhost/";

   
   public static void createTable(String host, String username, String password) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(host,username,password);

      //STEP 4: Execute a query
      System.out.println("Creating database...");
      stmt = conn.createStatement();
      
      String sql = "CREATE DATABASE IF NOT EXISTS AutogeneDatabase";
      stmt.executeUpdate(sql);
      System.out.println("Database created successfully...");
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
   }

}
