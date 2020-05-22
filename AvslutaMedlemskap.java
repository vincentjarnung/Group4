import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.util.Scanner;
import org.sqlite.SQLiteConfig;

public class Avslutamedlemskap {

   public static Connection conn = null;
   // Sökväg till SQLite-databas. OBS! Ändra sökväg så att den pekar ut din databas

   public static final String DB_URL = "jdbc:sqlite:/Users/esbetshire/Desktop/GitHub/Group4/FitnessAB.sqlite";  

     // Namnet på den driver som används av java för att prata med SQLite
   public static final String DRIVER = "org.sqlite.JDBC";

   public static void main(String[] args){
         // Kod för att skapa uppkoppling mot SQLite-dabatasen
         //String KEY_ON = "PRAGMA foreign_keys = on";
      try {
         Class.forName(DRIVER);
         SQLiteConfig config = new SQLiteConfig();
         config.enforceForeignKeys(true);// Denna kodrad ser till att sätta databasen i ett läge där den ger felmeddelande ifall man bryter mot någon främmande-nyckel-regel
         conn = DriverManager.getConnection(DB_URL,config.toProperties());
      }catch (Exception e) {
            // Om java-progammet inte lyckas koppla upp sig mot databasen (t ex om fel sökväg eller om driver inte hittas) så kommer ett felmeddelande skrivas ut
         System.out.println( e.toString() );
         System.exit(0);

 
     int pnr = 0;
     String förnamn;
     String efternamn;
     String sql;
     PreparedStatement statement;
      
   
     
   while (true){
      
      System.out.println("Välkommen Till FitnessAB");
      System.out.println("A - AvslutaMedlemskap\n" 
                          + "B - Ändra medlemskap\n");
                          

 
        String meny = "skriv ditt val";
        System.out.println(meny);
        meny = sc.nextLine();
       
        switch (meny.toUpperCase()) {
        
        case "A":
  
         System.out.println("Vänligen ange ditt personnummer: ");
         personnummer = sc.nextInt();

            
         System.out.println(" Skriv Förnamn: ");
         förnamn = sc.nextLine();
         sc.nextLine();

         System.out.println("Skriv Efternamn: ");
         efternamn = sc.nextLine();
         sql = "DELETE FROM Member AND Membership WHERE pnr ='Personnummer';(?,?,?)";
         
        try{
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, personnummer);
            statement.setString(2, förnamn);
            statement.setString(3, efternamn);
            statement.executeUpdate();

         }
            catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
         }

         break;
           
           
        case "B":
         
         System.out.println("Personnummer");
         personnummer = sc.nextInt();
         System.out.println("Välj medlemskap och längd: " );
         timmar = sc.nextInt(); 
       
         String[] options = {"Nästa","Tillbaka"};
         String[] choices = {
    		  "Gymträning 3 månader 249 kr/månaden", 
    		  "Gymträning 6 månader 229 kr/månaden", 
    		  "Gymträning 12 månader 199 kr/månaden",
    		  "Gruppträning 3 månader 299 kr/månaden",
    		  "Gruppträning 6 månader 279 kr/månaden",
    		  "Gruppträning 12 månader 249 kr/månaden";}
        
        
                  
         sql = "INSERT INTO Payment  VALUES(?,?,?)";
         
         try{
            
            statement = conn.prepareStatement(sql);
            statement.setInt(1, personnummer);
            statement.setInt(2, datum);
            statement.setInt(3, timmar);
            statement.executeUpdate();

         }
            catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
         }

         break;
         
  while (true) {
			 String[] options = {"För dyrt","Flyttar till annan stad","Tränar på annat sätt","Annan Fritext"};
	         int x = JOptionPane.showOptionDialog(null, "Välkommen Till FitnessAB " + m.first_name + "!","Meny",
	                                              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
	         //Om man stänger rutan stängs programmet
	         if (x == JOptionPane.CLOSED_OPTION){
	               System.exit(0);
     
            }
         }


           
           
           
           
           
   
                }
   

          }
   
   
   
   
   
       }
