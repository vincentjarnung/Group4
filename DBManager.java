


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;
import java.util.Date;

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
   
   String startDate = "";
   String endDate = "";
   int discount = 0;
   
   String paymentID = "";
   int price = 0;
   String payType = "Autogiro";
   String exDate = "";
   
   static Booking book;

   public static Connection conn = null;
   // Sökväg till SQLite-databas. OBS! Ändra sökväg så att den pekar ut din databas
   public static final String DB_URL = "jdbc:sqlite:/Users/Hanna/Desktop/Projekt/Group4/FitnessAB.sqlite";
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
      
      book = new Booking();
      
      DBManager men = new DBManager();
      men.updateDB();
      men.menu();



   }

   public void menu(){
	   
	   while (true){
		 String[] options = {"Avsluta","Logga In","Våra Gym","Bli Medlem"};
         int x = JOptionPane.showOptionDialog(null, "Välkommen Till FitnessAB","Meny",
                                              JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
         //Om man stänger rutan stängs programmet
         if (x == JOptionPane.CLOSED_OPTION){
               System.exit(0);
         }

         switch(x){
            case 3:
            //Bli Medlem
            	JOptionPane.showMessageDialog(null,(BliMedlem()));
            	menu();
            break;
            case 2:
            //Våra Gym
            	ourGym();
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
	  Date date = new Date();
	  Calendar calendar = Calendar.getInstance();
	  calendar.setTime(date);
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	      
	  startDate = sdf.format(calendar.getTime());
	   
	  String result = "";
      

      String[] options = {"Nästa","Tillbaka"};
      String[] choices = {
    		  "Gymträning 3 månader 249 kr/månaden", 
    		  "Gymträning 6 månader 229 kr/månaden", 
    		  "Gymträning 12 månader 199 kr/månaden",
    		  "Gruppträning 3 månader 299 kr/månaden",
    		  "Gruppträning 6 månader 279 kr/månaden",
    		  "Gruppträning 12 månader 249 kr/månaden"
    		  
      };
      JComboBox<String> cb = new JComboBox<String>(choices);
      String mem1 = "\nGymträning: \nDu får tillgång till gymmen och kan träna där fritt!";
      String mem2 = "\nGruppträning: \nDu får tillgång till gymmen och gruppträningar samt onlineträning!";
      
      Object[] message = {
    		  "Välj medlemsskap och längd",
    		  mem1, mem2,
    		  cb
      };
      int option = JOptionPane.showOptionDialog(null,message,"Välj medlemsskap",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
      if (option == JOptionPane.CLOSED_OPTION)
    	  menu();
      else if (option == 1)
    	  menu();
      else if (option == JOptionPane.OK_OPTION) {
    	  int n = cb.getSelectedIndex();
    	  if (n == 0) {
    		  memshipID = "gym3";
    		  price = 249; 
    	  }
    	  if (n == 1) {
    		  memshipID = "gym6";
    		  price = 229;
    	  }
    	  if (n == 2) {
    		  memshipID = "gym12";
    		  price = 199;
    	  }
    	  if (n == 3) {
    		  memshipID = "group3";
    		  price = 299;
    	  }
    	  if (n == 4) {
    		  memshipID = "group6";
    		  price = 279;  
    	  }
    	  if (n == 5) {
    		  memshipID = "group12";
    		  price = 249;
    	  }
      }
      
      System.out.println(Integer.parseInt(memshipID.substring(memshipID.length()-1)));
      
      if (Integer.parseInt(memshipID.substring(memshipID.length()-1)) == 3)
    	  calendar.add(Calendar.MONTH, 3);
      else if(Integer.parseInt(memshipID.substring(memshipID.length()-1)) == 6)
    	  calendar.add(Calendar.MONTH, 6);
      else if(Integer.parseInt(memshipID.substring(memshipID.length()-1)) == 2)
    	  calendar.add(Calendar.MONTH, 12);
      
      endDate = sdf.format(calendar.getTime());
      System.out.println(startDate + " : " + endDate);
      
      Boolean[] li = new Boolean[9];
      Arrays.fill(li,Boolean.TRUE);
      String[] ans = new String[9];
      generateMemDialog(li, ans);
      
      memID = getID("Member");
      paymentID = getID("Payment");
      System.out.println(paymentID);
      
      calendar.add(Calendar.DAY_OF_MONTH, 14);
	   
	  exDate = sdf.format(calendar.getTime());
	   
	  System.out.println(exDate);
      

      System.out.println(memID);
      Object[] memVal = {memID , PNR, first_name, last_name, email, phoneNumber, adress, postalCode, city, password, memshipID};
      Object[] conVal = {memID, memshipID, startDate, discount, endDate};
      Object[] payVal = {paymentID, payType, startDate, exDate, memID, price};
     
      
      
      
      if (insertToDB("Member",memVal))
    	  if(insertToDB("Contract",conVal))
    		  if (insertToDB("Payment",payVal))
    			  result = first_name + " " + last_name + " är tillagd.";
      else
    	  result = "Där uppstod ett problem med databasen försök igen eller kontakta kundservice.";
      
      return result;
   }

   public String LoggaIn() {
	   String sql = "SELECT mail,password,memID FROM Member";	   
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
	           if (checker) {
	        	   Member mem = new Member(rs.getString("memID"));
	        	   book.menu(mem);
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
      new JPasswordField()};

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
         if (!fields[1].getText().equals("")) {
            ans[1] = fields[1].getText();
            first_name = fields[1].getText();
            nli[1] = true;
         }else{ans[1] = ""; nli[1] = false; checker = false;}
         if (!fields[2].getText().equals("")) {
            ans[2] = fields[2].getText();
            last_name = fields[2].getText();
            nli[2] = true;
         }else{ans[2] = ""; nli[2] = false; checker = false;}
         if (!fields[3].getText().equals("") && isValid(fields[3].getText())){
            ans[3] = fields[3].getText();
            email = fields[3].getText();
            nli[3] = true;
         }else{ans[3] = ""; nli[3] = false; checker = false;}
         if (fields[4].getText() != null && fields[4].getText().matches("[0-9]+") && fields[4].getText().length() <= 10 ){
            ans[4] = fields[4].getText();
            phoneNumber = Integer.parseInt(fields[4].getText());
            nli[4] = true;
         }else{ans[4] = ""; nli[4] = false; checker = false;}
         if (!fields[5].getText().equals("")){
            ans[5] = fields[5].getText();
            adress = fields[5].getText();
            nli[5] = true;
         }else{ans[5] = ""; nli[5] = false; checker = false;}
         if (fields[6].getText() != null && fields[6].getText().matches("[0-9]+") && fields[6].getText().length() == 5){
            ans[6] = fields[6].getText();
            postalCode = Integer.parseInt(fields[6].getText());
            nli[6] = true;
         }else{ans[6] = ""; nli[6] = false; checker = false;}
         if (!fields[7].getText().equals("")) {
            ans[7] = fields[7].getText();
            city = fields[7].getText();
            nli[7] = true;
         }else{ans[7] = ""; nli[7] = false; checker = false;}
         if (!fields[8].getText().equals("")) {
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

   public Boolean insertToDB(String table, Object[] values) {
	   Boolean suc = false;
	   String nuOfVal = "";
	   
	   for(int i = 1; i<values.length;i++) {
		   nuOfVal += ",?";
	   }
	   
	   String sql = "INSERT INTO " + table + " VALUES (?" + nuOfVal + ")";
	   System.out.println(sql);
	   
	   try {
           PreparedStatement pstmt = conn.prepareStatement(sql);
           System.out.println("Funkar kung");

           //Funkar allt ändrar jag frågetäcknena i sql-stringen(sql) till de som någon har matat in
	       for(int i = 0; i<values.length;i++) {
	    	   pstmt.setObject(i+1, values[i]);
	       }
           pstmt.executeUpdate();
           //Det lyckades och då sätts stringen(result) som ska returnas till ett bekräftande
           suc = true;
     }
     catch (SQLException e){
           //Det misslyckades och då sätts stringen(result) till felmeddelandet som kommer från sql
           System.out.println(e.toString());     
     }
	 return suc;
   }
   
   public void updateDB() {
	   String getsql = "SELECT endDate, memID, date FROM Contract NATURAL JOIN Member NATURAL JOIN Payment";
	   ArrayList<String> isOutdated = new ArrayList<String>(); 
	   ArrayList<String> newPayment = new ArrayList<String>();
	   Date date = new Date();
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(date);
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   String today = sdf.format(calendar.getTime());
	   int year = calendar.get(Calendar.YEAR);
	   int month = calendar.get(Calendar.MONTH);
	   int day = calendar.get(Calendar.DAY_OF_MONTH);
	   
	   int endYear = 0;
	   int endMonth = 0;
	   int endDay = 0;

	   try {
           Statement stmt  = conn.createStatement();
           ResultSet rs    = stmt.executeQuery(getsql);
           while(rs.next()) {
        	   
        	   String endDate = rs.getString("endDate");
        	   String mem = rs.getString("memID");
        	   String payDate = rs.getString("date");
        	   
        	   System.out.println(endDate + " : " + mem);

        	   endYear = Integer.parseInt(endDate.substring(0,4));
        	   endMonth = Integer.parseInt(endDate.substring(5,7));
        	   endDay = Integer.parseInt(endDate.substring(8));
        	   
        	   if(year == endYear ) {
        		   if(month>endMonth)
        			   isOutdated.add(mem);
        		   else if(month == endMonth && day>endDay) 
        			   isOutdated.add(mem); 
        		   else if(month < endMonth && day == endDay && !payDate.equals(today))
        			   newPayment.add(mem);
        		   else if(month == endMonth && day == endDay && !payDate.equals(today))
        			   newPayment.add(mem);
        	   }else if(year > endYear)
        		   isOutdated.add(mem);
        	   else if(day == endDay && !payDate.equals(today))
        		   newPayment.add(mem);
        	   
           }

	   }catch (SQLException e) {
		   System.out.println("Funkar ej");
		   System.out.println(e.getMessage());
	   }
	
	   
	   for (int i = 0;i<isOutdated.size(); i++) {
		   String insertsql = "DELETE FROM Member WHERE memID = '" + isOutdated.get(i) + "'";
		   
		   try {
	           Statement stmt  = conn.createStatement();
	           stmt.executeQuery(insertsql);
	           
		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
	   }
	   for (int i = 0;i<newPayment.size(); i++) {
		   String getPaymentsql = "SELECT price FROM Membership NATURAL JOIN Contract NATURAL JOIN Member where memID = '" + newPayment.get(i) + "'";
		   
		   try {
	           Statement stmt  = conn.createStatement();
	           ResultSet rs = stmt.executeQuery(getPaymentsql);
	           price = rs.getInt("price");
		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		   
		   
		   paymentID = getID("Payment");
		   calendar.add(Calendar.DAY_OF_MONTH, 14);
		   exDate = sdf.format(calendar.getTime());
		   
		   Object[] payVal = {paymentID, payType, today, exDate, newPayment.get(i), price};
		   
		   insertToDB("Payment",payVal);
		   
	   }
	   
   }
   
   public String getID(String id) {
	   int counter = getNumOfRows(id);
	   String ID = "";
	   String startID = id.substring(0,3).toLowerCase();
	   
	   if (counter <10)
	          ID = startID+"0000" + counter;
	       else if (counter <100)
	          ID = startID+"000" + counter;
	       else if (counter <1000)
	          ID = startID+"00" + counter;
	       else if (counter <10000)
	          ID = startID+"pay0" + counter;
	       else
	    	  ID = startID+ counter;

	   return ID;
   }
   
   public void ourGym(){
	   String[] option = {"Tillbaka","Ekdalen","Aspvägen","Björkvägen"};
   	int choice = JOptionPane.showOptionDialog(null, "Välj Gym","Våra Gym",
               JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, null);
   	if (choice == 3)
   		gymInfo("gym01");
   	else if (choice == 2)
   		gymInfo("gym02");
   	else if (choice == 1)
   		gymInfo("gym03");
   }
   
   public void gymInfo(String gymID) {
	   String roomSql = "SELECT Room.name, capacity FROM Gym INNER JOIN Room ON Gym.gymID = Room.gymID where Gym.gymID = '" + gymID + "'";
	   String machineSql = "SELECT Machine.name, trainArea  FROM Gym INNER JOIN Machine ON Gym.gymID = Machine.gymID where Gym.gymID = '" + gymID + "'";
	   String weightSql = "SELECT Weight.name, weight FROM Gym INNER JOIN Weight ON Gym.gymID = Weight.gymID where Gym.gymID = '" + gymID + "'";
	   String openingHoursSql = "SELECT openingHours FROM Gym where Gym.gymID = '" + gymID + "'";
	   ArrayList<String> roomInfo = new ArrayList<String>();
	   ArrayList<String> machineInfo = new ArrayList<String>();
	   ArrayList<String> weightInfo = new ArrayList<String>();
	   roomInfo.add("<html> Rum: <br> ");
	   machineInfo.add("<html> Maskiner: <br> ");
	   weightInfo.add("<html> Vikter: <br> ");
	   String openingHours = "";
	   try {
           Statement stmt  = conn.createStatement();
           ResultSet rs    = stmt.executeQuery(roomSql);
           while(rs.next()) {
        	   roomInfo.add(rs.getString(1) + " med " + rs.getString(2) + " platser <br> " );
           }
           rs    = stmt.executeQuery(machineSql);
           while(rs.next()) {
        	   machineInfo.add(rs.getString(1) + "där man tränar " + rs.getString(2) + " <br> ");
           }
           rs    = stmt.executeQuery(weightSql);
           while(rs.next()) {
        	   weightInfo.add(rs.getString(1) + " " + rs.getString(2) + "kg " + rs.getString(2) + " stycken <br> ");
           }
           rs    = stmt.executeQuery(openingHoursSql);
           openingHours = rs.getString(1);
	   }catch (SQLException e) {
		   System.out.println("Funkar ej");
		   System.out.println(e.getMessage());
	   }
	   
	   roomInfo.add(" </html>");
	   machineInfo.add(" </html>");
	   weightInfo.add(" </html>");
	   StringBuilder builder1 = new StringBuilder();
	   for (String value : roomInfo) {
	       builder1.append(value);
	   }
	   String roomtext = builder1.toString();
	   
	   StringBuilder builder2 = new StringBuilder();
	   for (String value : machineInfo) {
	       builder2.append(value);
	   }
	   String machinetext = builder2.toString();
	   
	   StringBuilder builder3 = new StringBuilder();
	   for (String value : weightInfo) {
	       builder3.append(value);
	   }
	   String weighttext = builder3.toString();
	   
	   
	   
	   JLabel roomL = new JLabel(roomtext);
	   JLabel machineL = new JLabel(machinetext);
	   JLabel weightL = new JLabel(weighttext);
	   JLabel openHoursL = new JLabel(openingHours);
	   
	   GridBagConstraints constraints = new GridBagConstraints();
	   constraints.anchor = GridBagConstraints.NORTHWEST;
	   JPanel panel = new JPanel();

	   panel.setLayout(new GridBagLayout());
	   constraints.gridx = 0;
       constraints.gridy = 0; 
	   panel.add(roomL,constraints);
	   constraints.gridx = 1;
	   panel.add(Box.createHorizontalStrut(20),constraints);
	   constraints.gridx = 2;
	   panel.add(machineL,constraints);
	   constraints.gridx = 3;
	   panel.add(Box.createHorizontalStrut(20),constraints);
	   constraints.gridx = 4;
	   panel.add(weightL,constraints);
	   constraints.gridx = 5;
	   panel.add(Box.createHorizontalStrut(20),constraints);
	   constraints.gridx = 6;
	   panel.add(openHoursL,constraints);
	   
	   Object[] message = {"Rum: \n", roomtext, "\nMaskiners: \n", machinetext, "\nVikter: \n", weighttext, "\nÖppetider: \n", openingHours};
	   String[] options = {"Tillbaka"};
	   
	   int n = JOptionPane.showOptionDialog(null, panel,"Skriv in dina uppgifer!", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,null);
	   
	   if (n == 0) {
		   ourGym();
	   }
   }
      
   
   //HÄMTAD KOD!!! 
   //https://www.tutorialspoint.com/validate-email-address-in-java
   static boolean isValid(String email) {
	      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	      return email.matches(regex);
	   }
}
