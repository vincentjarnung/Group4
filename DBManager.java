import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import org.sqlite.SQLiteConfig;

public class DBManager {
   
   String memID = "";
   long PNR = 0;
   String first_name = "";
   String last_name = "";
   String email = "";
   int phoneNumber = 0;
   String adress = "";
   int postalCode = 0;
   String city = "";
   String password = "";
   String memshipID = "";
   
   public static Connection conn = null;
   // Sökväg till SQLite-databas. OBS! Ändra sökväg så att den pekar ut din databas
   public static final String DB_URL = "jdbc:sqlite:/Users/vincent/Desktop/Group4Java/FitnessAB.sqlite";  
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
  
      }
      DBManager men = new DBManager();
      men.menu();
      
      
      
   }
   
   public void menu(){
	   System.out.println(getNumOfRows("Member"));
	   
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
            	menu();
            break;
            case 3:
            //Boka pass
         
            break;  
            case 2:
            //Våra Gym
            
            break;   
            case 1:
            //Logga In
            	JOptionPane.showMessageDialog(null,(LoggaIn()));
            	menu();
            break;
            case 0:
            //Avsluta
               System.exit(0);
         break;    
         }
      }
   }
   
   public String BliMedlem(){
      String sql = "INSERT INTO Member VALUES(?,?,?,?,?,?,?,?,?,?,?)";
      String result = ""; 
      Boolean[] li = new Boolean[9];
      Arrays.fill(li,Boolean.TRUE);
      String[] ans = new String[9];
      
      System.out.println(li[0]);
      generateMemDialog(li, ans);  
      
      String[] options = {"Gruppträning","Gymträning"};
      int x = JOptionPane.showOptionDialog(null, "Välj Medlemskap \n\nGymträning: \nDu får tillgång till gymmen och kan träna där fritt! \n\nGruppträning: \nDu får tillgång till gymmen och gruppträningar samt onlineträning! ","Välj Medlemskap",
                                              JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, JOptionPane.PLAIN_MESSAGE);
      if (x == JOptionPane.CLOSED_OPTION)
         menu();   
      if (x==0)
         memshipID = "memship02";
      if (x==1)
         memshipID = "memship01";
      
      int counter = getNumOfRows("Member");
      
      if (counter <10)
          memID = "mem0000" + counter;
       else if (counter <100)
          memID = "mem000" + counter;
       else if (counter <1000)
          memID = "mem00" + counter;
       else if (counter <10000)
          memID = "mem0" + counter;
       else
    	  memID = "mem" + counter;
      
      System.out.println(memID);
   
      try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("Funkar kung");
            
            //Funkar allt ändrar jag frågetäcknena i sql-stringen(sql) till de som någon har matat in
            pstmt.setObject(1, memID);
            pstmt.setObject(2, new BigDecimal(PNR));
            pstmt.setObject(3, first_name);
            pstmt.setObject(4, last_name);
            pstmt.setObject(5, email);
            pstmt.setObject(6, phoneNumber);
            pstmt.setObject(7, adress);
            pstmt.setObject(8, postalCode);
            pstmt.setObject(9, city);
            pstmt.setObject(10, password);
            pstmt.setObject(11, memshipID);
            
            
            
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

   public String LoggaIn() {
	   String sql = "SELECT mail,password FROM Member";
	   String result = ""; 
	   Boolean wrong = false;
	   Boolean checker = false;
	   String[] results = new String[2];
	   
	   
	   
	   while(true) {
		   results = generateLogInDialog(wrong,results);
		   
		   try {
	           Statement stmt  = conn.createStatement();
	           ResultSet rs    = stmt.executeQuery(sql);
	           
	           while(rs.next()) {
	        	   System.out.println(rs.getString("mail") + " : " + results[0]+ "\n" + rs.getString("password") + " : " + results[1]);
	        	   if ( results[0].equals(rs.getString("mail")) && results[1].equals(rs.getString("password")) ) {
	        	   		checker = true;
	        	   		break;
	        	   }
	           }
	           System.out.println(checker);
	           
	           if (checker) {
	        	   result = "Välkommen";
	        	   break;
	           }else {
	        	   wrong = true;
	           }
	       
		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
	   }
	   return result;
   }
   
   public void generateMemDialog(Boolean[] lis, String[] ans){

      
      JTextField[] fields = new JTextField[]{
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField(),
      new JTextField()};

      Boolean checker = true;
      
      Object[] message = {
         "Personnummer - ÅÅÅÅMMDDNNNN:", fields[0],
         "Förnamn:", fields[1],
         "Efternamn:", fields[2],
         "Mejl:", fields[3],
         "Telefonnummer:", fields[4],
         "Adress:", fields[5],
         "Postnummer:", fields[6],
         "Stad:", fields[7],
         "Lösenord:", fields[8],
      };
      
      
      for(int n = 0, i = 0; i < lis.length; i ++, n += 2){
         if (lis[i] == false) 
            message[n] = "<html><font color=#FF0000>" + message[n] + "</font>";
         else{
            fields[i].setText(ans[i]); 
         }
      }
      
 
      int option = JOptionPane.showConfirmDialog(null, message,"Skriv in dina uppgifer!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (option == JOptionPane.CLOSED_OPTION)
          menu();  
      if (option == JOptionPane.CANCEL_OPTION){
         menu();
      }
      Boolean[] nli = new Boolean[9];
      if (option == JOptionPane.OK_OPTION){  
         if (fields[0].getText() != null && fields[0].getText().length() == 12 && fields[0].getText().matches("[0-9]+")) {
            ans[0] = fields[0].getText();
            PNR = Long.parseLong(fields[0].getText());
            nli[0] = true;
            
         }else{ans[0] = ""; nli[0] = false; checker = false;}
         if (fields[1].getText() != null) {
            ans[1] = fields[1].getText();
            first_name = fields[1].getText();
            nli[1] = true;
         }else{ans[1] = ""; nli[1] = false; checker = false;}
         if (fields[2].getText() != null) {
            ans[2] = fields[2].getText();
            last_name = fields[2].getText();
            nli[2] = true;
         }else{ans[2] = ""; nli[2] = false; checker = false;}
         if (fields[3].getText() != null){
            ans[3] = fields[3].getText();
            email = fields[3].getText();
            nli[3] = true;
         }else{ans[3] = ""; nli[3] = false; checker = false;}
         if (fields[4].getText() != null && fields[4].getText().matches("[0-9]+")){
            ans[4] = fields[4].getText();
            phoneNumber = Integer.parseInt(fields[4].getText());
            nli[4] = true;
         }else{ans[4] = ""; nli[4] = false; checker = false;}
         if (fields[5].getText() != null){
            ans[5] = fields[5].getText();
            adress = fields[5].getText();
            nli[5] = true;
         }else{ans[5] = ""; nli[5] = false; checker = false;}
         if (fields[6].getText() != null && fields[6].getText().matches("[0-9]+")){
            ans[6] = fields[6].getText();
            postalCode = Integer.parseInt(fields[6].getText());
            nli[6] = true;
         }else{ans[6] = ""; nli[6] = false; checker = false;}
         if (fields[7].getText() != null) {
            ans[7] = fields[7].getText();
            city = fields[7].getText();
            nli[7] = true;
         }else{ans[7] = ""; nli[7] = false; checker = false;}
         if (fields[8].getText() != null) {
            ans[8] = fields[8].getText();
            password = fields[8].getText();
            nli[8] = true;
         }else{ans[8] = ""; nli[8] = false; checker = false;}

      }
      
     
      if (checker == false)
         generateMemDialog(nli, ans);
   }
   
   public String[] generateLogInDialog(Boolean wrong, String[] result) {
	   
	   
	   JTextField usernameField = new JTextField(result[0]);
	   JPasswordField passwordField = new JPasswordField();
	   
	   
	   Object[] options = {"Log In", "Tillbaka", "Bli Medlem"};
	   
	   Object[] message = {
			   "Email: ", usernameField,
			   "Lösenord: ", passwordField,
	   };
	   Object[] messageWrong = {
			   "<html><font color=#FF0000>Fel användarnamn och/eller lösenord</font>" ,
			   "Email: ", usernameField,
			   "Lösenord: ", passwordField,
	   };
	   
	   if(!wrong) {
		   int option = JOptionPane.showOptionDialog(null, message,"Skriv in dina uppgifer!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,options[0]);
		     System.out.println("Option = " + option);
		   	  if (option == JOptionPane.CLOSED_OPTION) 
		    	  menu();
		      else if (option == 1) 
		    	  menu();
		      else if (option == 0) {
		    	  result[0] = usernameField.getText();
	    		  result[1] = String.valueOf(passwordField.getPassword());
		      }else if (option == 2){
		    	  BliMedlem();
		      }
		   	  
	   }else {
		   int option = JOptionPane.showOptionDialog(null, messageWrong,"Skriv in dina uppgifer!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,options[0]);
		      if (option == JOptionPane.CLOSED_OPTION) {
		    	  menu(); System.out.print("close");}
		      else if (option == 1) {
		          menu(); System.out.print("close");}
		      else if (option == 0) {
		    	  result[0] = usernameField.getText();
	    		  result[1] = String.valueOf(passwordField.getPassword());	    
		      }else if (option == 2) {
		    	  BliMedlem();
		      }
		      
	   }
	return result;  
   }	   
   
   public int getNumOfRows(String table) {
	   String sql = "select count(*) from "+ table;
	   int result = 0;
	   
	   try {
           Statement stmt  = conn.createStatement();
           ResultSet rs    = stmt.executeQuery(sql);
           result = rs.getInt(1);
       
	   }catch (SQLException e) {
		   System.out.println("Funkar ej");
		   System.out.println(e.getMessage());
	   }

	   return result;
   }
     
}




	

