import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import org.sqlite.SQLiteConfig;

public class DBManager {
   
   public static int memID = 00000;
   
   public static Connection conn = null;
   // Sökväg till SQLite-databas. OBS! Ändra sökväg så att den pekar ut din databas
   public static final String DB_URL = "jdbc:sqlite:/Users/vincent/Desktop/Group4Java/FitnessAB.sqlite";  
   // Namnet på den driver som används av java för att prata med SQLite
   public static final String DRIVER = "org.sqlite.JDBC";  
   
   public static void main(String[] args){
         // Kod för att skapa uppkoppling mot SQLite-dabatasen
         String KEY_ON = "PRAGMA foreign_keys = on";
      try {
         Class.forName(DRIVER);
         SQLiteConfig config = new SQLiteConfig();  
         config.enforceForeignKeys(true);// Denna kodrad ser till att sätta databasen i ett läge där den ger felmeddelande ifall man bryter mot någon främmande-nyckel-regel
         conn = DriverManager.getConnection(DB_URL,config.toProperties());
         }catch (Exception e) {
            // Om java-progammet inte lyckas koppla upp sig mot databasen (t ex om fel sökväg eller om driver inte hittas) så kommer ett felmeddelande skrivas ut
         System.out.println( e.toString() );
         System.exit(0);
  
      }
      menu();
      
   }
   public static void menu(){
      while (true){
         String[] options = {"Avsluta","Logga In","Våra Gym","Boka Pass","Bli Medlem"};
         int x = JOptionPane.showOptionDialog(null, "Välkommen Till FitnessAB","Meny",
                                              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
         //Om man stänger rutan stängs programmet
         if (x == JOptionPane.CLOSED_OPTION){
               System.exit(0);
         }
      
         switch(x){
            case 4:
            //Bli Medlem
            JOptionPane.showMessageDialog(null,(BliMedlem()));
            break;
            case 3:
            //Boka pass
         
            break;  
            case 2:
            //Våra Gym
            
            break;   
            case 1:
            //Logga In
            
            break;
            case 0:
            //Avsluta
               System.exit(0);
         break;    
         }
      }
   }
   
   public static String BliMedlem(){
      String sql = "INSERT INTO Members VALUES(?,?,?,?,?,?,?,?,?,?,?)";
      String result = "";
      
      int PNR = 0;
      String first_name = "";
      String last_name = "";
      String email = "";
      int phoneNumber = 0;
      String adress = "";
      int postalCode = 0;
      String city = "";
      String password = "";
      String memship = "";
      
      JTextField field1 = new JTextField();
      JTextField field2 = new JTextField();
      JTextField field3 = new JTextField();
      JTextField field4 = new JTextField();
      JTextField field5 = new JTextField();
      JTextField field6 = new JTextField();
      JTextField field7 = new JTextField();
      JTextField field8 = new JTextField();
      JTextField field9 = new JTextField();
      
      Object[] message = {
         "Personnummer ÅÅMMDD:", field1,
         "Förnamn:", field2,
         "Efternamn:", field3,
         "Mejl:", field4,
         "Telefonnummer:", field5,
         "Adress:", field6,
         "Postnummer:", field7,
         "Stad:", field8,
         "Lösenord:", field9,
      };
     
      int option = JOptionPane.showConfirmDialog(null, message, "Skriv in dina uppgifer!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (option == JOptionPane.OK_OPTION){  
         try {
            PNR = Integer.parseInt(field1.getText());
         }
         catch(Exception e){
            String t = "<html><font color=#ffffdd>Hello</font> world!";
         }
         first_name = field2.getText();
         last_name = field3.getText();
         email = field4.getText();
         phoneNumber = Integer.parseInt(field5.getText());
         adress = field6.getText();
         postalCode = Integer.parseInt(field7.getText());
         city = field8.getText();
         password = field9.getText();
      }
      String[] options = {"Gruppträning","Gymträning"};
      int x = JOptionPane.showOptionDialog(null, "Välj Medlemskap \n\nGymträning: \nDu får tillgång till gymmen och kan träna där fritt! \n\nGruppträning: \nDu får tillgång till gymmen och gruppträningar samt onlineträning! ","Välj Medlemskap",
                                              JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, JOptionPane.PLAIN_MESSAGE);
      if (x == JOptionPane.CLOSED_OPTION)
         System.exit(0);   
      if (x==0)
         memship = "Gruppträning";
      if (x==1)
         memship = "Gymträning";
      
      memID = 5;
      System.out.println(memID);
      try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("Funkar kung");
            
            
            //Funkar allt ändrar jag frågetäcknena i sql-stringen(sql) till de som någon har matat in
            pstmt.setInt(1, memID);
            pstmt.setInt(2, PNR);
            pstmt.setString(3, first_name);
            pstmt.setString(4, last_name);
            pstmt.setString(5, email);
            pstmt.setInt(6, phoneNumber);
            pstmt.setString(7, adress);
            pstmt.setInt(8, postalCode);
            pstmt.setString(9, city);
            pstmt.setString(10, password);
            pstmt.setString(11, memship);
            
            pstmt.executeUpdate();
            //Det lyckades och då sätts stringen(result) som ska returnas till ett bekräftande
            result = first_name + " " + last_name + " är tillagd.";           
      }
      catch (SQLException e){
            //Det misslyckades och då sätts stringen(result) till felmeddelandet som kommer från sql
            System.out.println(e.toString()); 
            result = e.toString();
      } 
        
      return result;
   }       
}