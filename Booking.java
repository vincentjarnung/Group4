

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.EmptyBorder;

//Copyright 2004 Juan Heyns. All rights reserved.
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
 




public class Booking {
	


	
	public void menu(Member m) {
		
		DBManager db = new DBManager();
		System.out.println(m.memID);
		 while (true){
			 String[] options = {"Logga Ut","Online Träning","Boka Pass","Våra Gym","Profil"};
	         int x = JOptionPane.showOptionDialog(null, "Välkommen Till FitnessAB " + m.first_name + "!","Meny",
	                                              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
	         //Om man stänger rutan stängs programmet
	         if (x == JOptionPane.CLOSED_OPTION){
	               System.exit(0);
	         }

	         switch(x){ 
	         	case 4:
	         	//Profil	
	         		Profil(m);
	         	break;	
	         	case 3:
	            //Våra Gym
	         		db.ourGym();
	            break;
	            case 2:
	            //Boka Pass
	            	BokaPass(m);
	            break;
	            case 1:
	            //Online Träning
	            	OnlineTräning(m);
	            break;
	            case 0:
	            //Logga Ut
	               db.menu();
	         break;
	         }
	      }
	}
	
	public void BokaPass(Member m) {
		
		ArrayList<String> values = new ArrayList<String>();
		String sqlMem = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,GroupTraining.groupTrainID,GroupTraining.date FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID INNER JOIN BookingGroupTraining ON BookingGroupTraining.groupTrainID = GroupTraining.groupTrainID WHERE memID = '" + m.memID + "'";
		values = getGroupTraining(sqlMem,false);
		JLabel l1= new JLabel("DINA PASS");
		l1.setFont(new Font("Arial", Font.BOLD, 20));
			Object[] message = {l1,"\n",generatePanel(m,values,false)};
			String[] choices = {"Hitta Pass Att Boka","Tillbaka"};
        
        
        	int option = JOptionPane.showOptionDialog(null,message,"Bokade Pass",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
	        
        	System.out.println(option);
	        if(option == -1) {
	        	menu(m);
	        }
	        else if(option == 1) {
	        	menu(m);
	        }else if(option == 0) {
	        	HittaPass(m,"","");
	        }
	        
	}
	
	public void HittaPass(Member m,String filters, String currentFilter) {
			Date d = new Date();
			LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			//Copyright 2004 Juan Heyns. All rights reserved.
        	UtilDateModel model = new UtilDateModel();
        	model.setDate(ld.getYear(), ld.getMonthValue()-1, ld.getDayOfMonth());
        	model.setSelected(true);
    		DateLabelFormatter dlf = new DateLabelFormatter();
    		Properties p = new Properties();
    		p.put("text.today", "Today");
    		p.put("text.month", "Month");
    		p.put("text.year", "Year");
    		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,dlf);
    		
    		String[] choices = {"Hitta Pass","Filtrera","Tillbaka"};
    		JLabel l1= new JLabel("HITTA PASS");
    		l1.setFont(new Font("Arial", Font.BOLD, 16));
    		
    		JLabel l2= new JLabel("Filter: " +  currentFilter);
    		l2.setFont(new Font("Arial", Font.BOLD, 14));
    		Object[] finalMes = {l1,l2,"\n",datePicker};
    		
    		System.out.println(dlf);
    		int n = JOptionPane.showOptionDialog(null,finalMes,"Boka Pass",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
    		if(n == 2)
    			BokaPass(m);
    		else if(n == -1)
    			menu(m);	
    		else if(n == 0) {
	    		while (true) {
	    			
		    		Date selectedDate = (Date) datePicker.getModel().getValue();
		    		System.out.println(selectedDate);
		    		if (selectedDate == null) {
		   				final JDialog dialog = new JDialog();
		   				dialog.setAlwaysOnTop(true);  
		   				JOptionPane.showMessageDialog(dialog, "Välj Ett Datum!");
		   				HittaPass(m,filters,currentFilter);
    				}
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    		String date = sdf.format(selectedDate);
		    		String sqlDate = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,GroupTraining.groupTrainID,GroupTraining.date FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID WHERE GroupTraining.date='" + date + "'" + filters;
		    		System.out.println(date);
		   			Object[] panels = generatePanel(m,getGroupTraining(sqlDate,false),true);
		   			Object[] dateMes = {l1,l2,"\n",datePicker, panels};
		   			System.out.println(sqlDate);
		   			int i = JOptionPane.showOptionDialog(null,dateMes,"Boka Pass",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
		   			System.out.println(i);
	    			if (i == -1) {
	    				menu(m);
	    				break;
	    			}else if(i == 1) {
	    				Filtrera(m);
	    			}
		    		else if (i == 2) {
		    			BokaPass(m);
		    			break;
		    		}
	    		}	
	    	}
    		else if(n == 1) {
    			Filtrera(m);
    		}
    		
	}
	
	public void Filtrera(Member m) {
		String filters = "";
		String currentFilters = "";
		String[] options = {"Sök Pass","Återställ","Tillbaka"};
		String[] gymChoices = {
				  "Alla gym",
	    		  "Björkvägen", 
	    		  "Aspvägen", 
	    		  "Ekdalen"	  
	    };
		String[] typChoices = {
	    		  "Alla pass",
				  "Styrka", 
	    		  "Kondtion", 
	    		  "Yoga",
	    		  "Stretch",
	    		  "Fit på 15"
		};
		String[] empChoices = {
				  "Alla instruktörer",
				  "Tinsley Mortimer",
	    		  "Lisa Vanderpump", 
	    		  "Dorit Kemsley", 
	    		  "NeNe Leakes",
	    		  "Ramona Singer",
	    		  "Erika Jayne",
	    		  "Kim Richards",
	    		  "Paul Nassif",
	    		  "Adrienne Maloof",
	    		  "Lisa Rinna"
			};
		
		JComboBox<String> gcb = new JComboBox<String>(gymChoices);
		JComboBox<String> tcb = new JComboBox<String>(typChoices);
		JComboBox<String> ecb = new JComboBox<String>(empChoices);
		
		Object[] mes = {gcb,tcb,ecb};
		
		int i = JOptionPane.showOptionDialog(null,mes,"Boka Pass",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
		if (i == 0) {
			if(gcb.getSelectedIndex() == 1) {
				filters += " AND Gym.gymID = 'gym01'";
				currentFilters += "Björkvägen ";
			}else if(gcb.getSelectedIndex() == 2){
				filters += " AND Gym.gymID = 'gym02'";
				currentFilters += "Aspvägen ";
			}else if(gcb.getSelectedIndex() == 3){
				filters += " AND Gym.gymID = 'gym03'";
				currentFilters += "Ekdalen ";
			}
			if(tcb.getSelectedIndex() == 1){
				filters += " AND trainType.trainTypeID = 'ttyp01'";
				currentFilters += "Styrka ";
			}else if(tcb.getSelectedIndex() == 2){
				filters += " AND trainType.trainTypeID = 'ttyp02'";
				currentFilters += "Kondition ";
			}else if(tcb.getSelectedIndex() == 3){
				filters += " AND trainType.trainTypeID = 'ttyp03'";
				currentFilters += "Yoga ";
			}else if(tcb.getSelectedIndex() == 4){
				filters += " AND trainType.trainTypeID = 'ttyp04'";
				currentFilters += "Stretch ";
			}else if(tcb.getSelectedIndex() == 5){
				filters += " AND trainType.trainTypeID = 'ttyp05'";
				currentFilters += "Fit på 15 ";
			}
			if(ecb.getSelectedIndex() == 1){
				filters += " AND Instructor.employeeID = 'emp001'";
				currentFilters += "Tinsley Mortimer ";
			}else if(ecb.getSelectedIndex() == 2){
				filters += " AND Instructor.employeeID = 'emp002'";
				currentFilters += "Lisa Vanderpump ";
			}else if(ecb.getSelectedIndex() == 3){
				filters += " AND Instructor.employeeID = 'emp003'";
				currentFilters += "Dorit Kemsley ";
			}else if(ecb.getSelectedIndex() == 4){
				filters += " AND Instructor.employeeID = 'emp004'";
				currentFilters += "NeNe Leakes ";
			}else if(ecb.getSelectedIndex() == 5){
				filters += " AND Instructor.employeeID = 'emp005'";
				currentFilters += "Ramona Singer ";
			}else if(ecb.getSelectedIndex() == 6){
				filters += " AND Instructor.employeeID = 'emp006'";
				currentFilters += "Erika Jayne ";
			}else if(ecb.getSelectedIndex() == 7){
				filters += " AND Instructor.employeeID = 'emp007'";
				currentFilters += "Kim Richards ";
			}else if(ecb.getSelectedIndex() == 8){
				filters += " AND Instructor.employeeID = 'emp008'";
				currentFilters += "Paul Nassif ";
			}else if(ecb.getSelectedIndex() == 9){
				filters += " AND Instructor.employeeID = 'emp009'";
				currentFilters += "Adrienne Maloof ";
			}else if(ecb.getSelectedIndex() == 10){
				filters += " AND Instructor.employeeID = 'emp010'";
				currentFilters += "Lisa Rinna ";
			}
			
			System.out.println(filters);
			HittaPass(m,filters,currentFilters);
		}else if(i == 1){
			Filtrera(m);
		}else if(i == 2){
			HittaPass(m,"","");
		}else if(i == -1){
			menu(m);
		}
	}
	
	public ArrayList<String> getGroupTraining(String sql,boolean rec) {
		int memBooked;
		ArrayList<String> values = new ArrayList<String>();
		
		Date d = new Date();
 	   	Calendar calendar = Calendar.getInstance();
 	   	calendar.setTime(d);
 	   	
 	   	Date today = calendar.getTime();
 	   	
 	   	int year;
		int month;
		int day;
		int endHour;
		int endMin;
		String date;
		String endTime;
 	   	
		 try {
	           Statement stmt  = DBManager.conn.createStatement();    
	           ResultSet rs    = stmt.executeQuery(sql);
	        	    
	           while(rs.next()) {
	        	   	date = rs.getString(11);
	        	   	year = Integer.parseInt(date.substring(0,4));
		        	month = Integer.parseInt(date.substring(5,7));
		        	day = Integer.parseInt(date.substring(8));
		        	calendar.set(Calendar.YEAR, year);
		        	calendar.set(Calendar.MONTH, month-1);
		        	calendar.set(Calendar.DAY_OF_MONTH, day);
		        	endTime = rs.getString(4);
		        	endHour = Integer.parseInt(endTime.substring(0,2));
		        	endMin = Integer.parseInt(endTime.substring(3));
		        	calendar.set(Calendar.HOUR_OF_DAY,endHour);
		        	calendar.set(Calendar.MINUTE,endMin);
		        	calendar.set(Calendar.SECOND,0);
		        	   
		        	Date trainDate = calendar.getTime();
		        	System.out.println(trainDate);
		        	
		        	if (!rec) {
			        	if (trainDate.after(today)) {
	
			           		values.add(rs.getString(1));//Namn
				       		values.add(rs.getString(2));//Träningstyp
				       		values.add(rs.getString(11));//Datum
				       		values.add(rs.getString(3));//Starttid
				       		values.add(rs.getString(4));//Sluttid
				       		values.add(rs.getString(5));//Instruktör förnamn
				       		values.add(rs.getString(6));//Instruktör efternamn
				       		values.add(rs.getString(7));//Gym namn
				       		values.add(rs.getString(8));//Rum namn
				       		
				       		
				       		String sqlCount = "SELECT count(*) FROM BookingGroupTraining WHERE groupTrainID = '" + rs.getString(10) + "'";
				       		try {
				       			Statement stmtCount = DBManager.conn.createStatement();
				       			ResultSet rsCount = stmtCount.executeQuery(sqlCount);
				       			memBooked = rsCount.getInt(1);
				       		}finally {
				       			System.out.print(false);
				       		}
				       		
				       		values.add(rs.getInt(9)-memBooked + "");//Platser kvar
				       		values.add(rs.getString(9));
				       		values.add(rs.getString(10));//GroupTrainID
				       		System.out.println(values);
			        	}
		        	}else {
		        		if (trainDate.before(today)) {
		        			
			           		values.add(rs.getString(1));//Namn
				       		values.add(rs.getString(2));//Träningstyp
				       		values.add(rs.getString(11));//Datum
				       		values.add(rs.getString(3));//Starttid
				       		values.add(rs.getString(4));//Sluttid
				       		values.add(rs.getString(5));//Instruktör förnamn
				       		values.add(rs.getString(6));//Instruktör efternamn
				       		values.add(rs.getString(7));//Gym namn
				       		values.add(rs.getString(8));//Rum namn
				       		
				       		
				       		String sqlCount = "SELECT count(*) FROM BookingGroupTraining WHERE groupTrainID = '" + rs.getString(10) + "'";
				       		try {
				       			Statement stmtCount = DBManager.conn.createStatement();
				       			ResultSet rsCount = stmtCount.executeQuery(sqlCount);
				       			memBooked = rsCount.getInt(1);
				       		}finally {
				       			System.out.print(false);
				       		}
				       		
				       		values.add(rs.getInt(9)-memBooked + "");//Platser kvar
				       		values.add(rs.getString(9));
				       		values.add(rs.getString(10));//GroupTrainID
		        		}
		        	}
	           }

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		return values;
	}
	
	public void getOnlineTraining(Member m, String sql) {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> urls= new ArrayList<String>();
		 try {
	           Statement stmt  = DBManager.conn.createStatement();
	           ResultSet rs    = stmt.executeQuery(sql);
	           while(rs.next()) {
	        	   name.add(rs.getString(1)); //Namn
	        	   urls.add(rs.getString(2)); //url
	        	   rs.getString(3); //Beskrivning
	        	   rs.getString(4); //ID
	           }
		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		 
		 
		 JButton[] buttons = new JButton[name.size()];
		 for(int u = 0; u<name.size();u++) {
			 buttons[u] = new JButton(name.get(u));
			 buttons[u].setName(urls.get(u));
			 buttons[u].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println(((JButton) e.getSource()).getText() + "");
						showTrainVideo(m,((JComponent) e.getSource()).getName(),((JButton) e.getSource()).getText() );
					}
			       });
		 }
		 
		 String[] opt = {"Tillbaka"};
		 
		 
		 int n = JOptionPane.showOptionDialog(null,buttons,"Online Träning",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, null);
		 if (n == 0) {
			 OnlineTräning(m);
		 }else if(n == -1) {
			 menu(m);
		 }
	}
	
	public void showTrainVideo(Member m,String url, String videoName) {
		int review = 0;
		String sql = "";
		try {
			File vid = new File("resources/" + url);
			Desktop.getDesktop().open(vid);
		} catch(Exception e) {
			final JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true); 
			JOptionPane.showMessageDialog(dialog, e);
		}
		if(isViewed(m,videoName)){
		
			String[] opt = {"Avbryt","1","2","3","4","5",};
			int n = JOptionPane.showOptionDialog(null,"Välj ett betyg!","Online Träning",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, null);
			
			if (n == 1) {
				review = 1;
			}else if (n == 2) {
				review = 2;
			}else if (n == 3) {
				review = 3;
			}else if (n == 4) {
				review = 4;
			}else if (n == 5) {
				review = 5;
			}
			
			if (review >0 && review <6) {
				sql = "INSERT INTO OnlineTrainingRecord VALUES ('" + m.memID + "',(SELECT onlineTrainID FROM onlineTraining where videoName = '" + videoName + "'),"+ review +")";
			}else {
				sql = "INSERT INTO OnlineTrainingRecord (memID,onlineTrainID,memView) VALUES ('" + m.memID + "',(SELECT onlineTrainID FROM onlineTraining where videoName = '" + videoName + "'))";
			}
			try {
				PreparedStatement pstmt = DBManager.conn.prepareStatement(sql);
				pstmt.executeUpdate();
	
			    }catch (SQLException e1) {
				   System.out.println(e1.getMessage() + " Gigaloser");
			    }
		}else {
			System.out.println("Inte första gången!");
		}
	}
	
	public Boolean isViewed(Member m, String videoName) {
		Boolean is = true;
		String sql = "SELECT count(*) FROM OnlineTrainingRecord WHERE memID = '" + m.memID + "' AND onlineTrainID = (SELECT onlineTrainID FROM onlineTraining where videoName = '" + videoName + "')";
		try {
			Statement stmt  = DBManager.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getInt(1)>0) 
				is = false;

		    }catch (SQLException e1) {
			   System.out.println(e1.getMessage() + " Gigaloser");
		    }
		return is;
	}
	
	public Object[] generatePanel(Member m,ArrayList<String> values,Boolean book) {
		
		int counter = values.size()/12;
		System.out.println(counter);
		Object[] message = new Object[counter];
		
		for (int i = 0,n = 0; i<counter; i++, n+= 11) {
			
			JLabel label1 = new JLabel(values.get(i+n) + " | " + values.get(i+1+n) + " | " + values.get(i+2+n) );
			JLabel label2 = new JLabel(values.get(i+3+n) + "-" + values.get(i+4+n) +  "|" + values.get(i+5+n) + " " + values.get(i+6+n));
			JLabel label3 = new JLabel(values.get(i+7+n) + " | " + values.get(i+8+n));
			JLabel label4 = new JLabel(values.get(i+9+n) + " lediga platser av " + values.get(i+10+n));
			String trainName = values.get(i+n+11);
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBackground(Color.white);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(1,1,1,1);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 0; 
	        panel.add(label1, constraints);
	        
	        constraints.gridy = 1;
	        panel.add(label2, constraints);
			
	        constraints.gridy = 2;     
	        panel.add(label3, constraints);
	        
	        constraints.gridy = 3;
	        panel.add(label4,constraints);
			
	        JButton b1;
			if (!book) {
				b1=new JButton("Avboka"); 
				b1.setPreferredSize(new Dimension(85,20));
				try {
					b1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							AvbokaPass(m,trainName);
							Window w = SwingUtilities.getWindowAncestor(b1);
							w.dispose();
		
						}
				       });
					BufferedImage img = ImageIO.read(getClass().getResource("resources/cancel.png"));
					b1.setIcon(new ImageIcon(img));
					
				}catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				b1=new JButton("Boka");
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						BokaEttPass(m,trainName);
					}
				});
					
			}
			constraints.gridx = 3;
	        constraints.gridy = 0; 
	        panel.add(b1,constraints);
        
	        message[i] = panel;
		}
		
		return message;
		
        
	}
	
	public void AvbokaPass(Member m, String groupTrainID) {

		String SQL = "DELETE FROM BookingGroupTraining WHERE memID = ? AND groupTrainID = ?";
		try {
			
			PreparedStatement pstmt = DBManager.conn.prepareStatement(SQL);
			System.out.println(m.memID + " och " + groupTrainID);
			pstmt.setString(1,m.memID);
			pstmt.setString(2,groupTrainID);
			pstmt.executeUpdate();
			
			final JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);    
			JOptionPane.showMessageDialog(dialog, "Passet är avbokat!");
			

		    }catch (SQLException e1) {
			   System.out.println(e1.getMessage() + " Gigaloser");
		    }
	}
	
	public void BokaEttPass(Member m, String groupTrainID) {
		
		String SQL = "INSERT INTO BookingGroupTraining VALUES(?,?)";
		try {
			PreparedStatement pstmt = DBManager.conn.prepareStatement(SQL);
			System.out.println(m.memID + " och " + groupTrainID);
			pstmt.setString(1,m.memID);
			pstmt.setString(2,groupTrainID);
			pstmt.executeUpdate();
			
			final JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);  
			JOptionPane.showMessageDialog(dialog, "Passet är bokat!");

		    }catch (SQLException e1) {
			   System.out.println(e1.getMessage() + " Gigaloser");
			   final JDialog dialog = new JDialog();
				dialog.setAlwaysOnTop(true);  
				JOptionPane.showMessageDialog(dialog, "Du har redan bokat det här passet!");
		    }
	}

	public void Profil(Member m) {

		String[] choices = {"Tillbaka","Avsluta Medlemskap","Betalningshistorik","Ändra Uppgifter","Träningshistorik","Mina Bokade Pass"};
		
		int n = JOptionPane.showOptionDialog(null, m.first_name + " " + m.last_name,"Profil",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
		
		switch(n){ 
      		case 5:
      			//Bokade Pass
      			BokaPass(m);
      		break;		
      		case 4:
      			//Träningshistorik
      			displayTrainRecord(m);
      		break;
      		case 3:
      			//Ändra uppgifter
      			Boolean[] li = new Boolean[9];
      			Arrays.fill(li,Boolean.TRUE);
      			String[] ans = {String.valueOf(m.PNR),m.first_name,m.last_name,m.email,String.valueOf(m.phoneNumber),m.adress,String.valueOf(m.postalCode),m.city,m.password};
      			changeValues(m,li,ans);
      		break;
      		case 2:
      			//Betalningshistorik
      			displayPayRecord(m);
      		break;
      		case 1:
      			//AvslutaMedlemskap
      			avslutaMedlemskap(m);
      		break;
      		case 0:
      			//Tillbaka
      			menu(m);
      		break;
      } 
	}

	public void OnlineTräning(Member m) {
		String sql = "SELECT videoName,urlVideo,description,onlineTrainID FROM OnlineTraining WHERE trainTypeID = ";
		String[] choices = {"Tillbaka","Fit på 15","Stretch","Yoga","Kondition","Styrka"};
		
		int n = JOptionPane.showOptionDialog(null,"Välj typ av träning","Online Träning",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
		
		switch(n){ 
  		case 5:
  			//Styrka
  			sql += "'ttyp01'";
  			getOnlineTraining(m,sql);
  		break;		
  		case 4:
  			//Trän
  			sql += "'ttyp02'";
  			getOnlineTraining(m,sql);
  		break;
  		case 3:
  			//Ändra uppgifter
  			sql += "'ttyp03'";
  			getOnlineTraining(m,sql);
  		break;
  		case 2:
  			//Betalningshistorik
  			sql += "'ttyp04'";
  			getOnlineTraining(m,sql);
  		break;
  		case 1:
  			//AvslutaMedlemskap
  			sql += "'ttyp05'";
  			getOnlineTraining(m,sql);
  		break;
  		case 0:
  			//Tillbaka
  			menu(m);
  		break;
  } 
	}
	
	public Object[] generatePanelTrainRecord(Member m,ArrayList<String> values) {
		
		int counter = values.size()/12;
		Object[] message = new Object[counter];
		
		for (int i = 0,n = 0; i<counter; i++, n+= 11) {
			JLabel label1 = new JLabel(values.get(i+n) + " | " + values.get(i+1+n) + " | " + values.get(i+2+n) );
			JLabel label2 = new JLabel(values.get(i+3+n) + "-" + values.get(i+4+n) +  "|" + values.get(i+5+n) + " " + values.get(i+6+n));
			JLabel label3 = new JLabel(values.get(i+7+n));
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBackground(Color.white);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(new EmptyBorder(5, 10, 5, 5));

	        panel.add(label1);
	        panel.add(label2);
	        panel.add(label3);
			
	        
	        message[i] = panel;
		}
		
		return message;
		
        
	}
	
	public void displayTrainRecord(Member m) {
		String sql ="SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,GroupTraining.groupTrainID,GroupTraining.date FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID INNER JOIN BookingGroupTraining ON BookingGroupTraining.groupTrainID = GroupTraining.groupTrainID WHERE memID = '" + m.memID + "'";
		Object[] panels = generatePanelTrainRecord(m,getGroupTraining(sql,true));
		String[] cho = {"Tillbaka"};
		
		int n = JOptionPane.showOptionDialog(null,panels,"Träningshistorik",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, cho, null);
		if (n==-1)
			menu(m);
		if (n == 0)
			Profil(m);
	}
	
	public Object[] generatePanelPay(Member m,ArrayList<String> values) {
		int counter = values.size()/3;
		Object[] message = new Object[counter];
		System.out.println(values.get(0));
		for (int i = 0,n = 0; i<counter; i++, n+= 3) {
			String trainName = values.get(i+n);
			JLabel label1 = new JLabel(values.get(i+n)+ "\n");
			JLabel label2 = new JLabel(values.get(i+1+n)+ "\n");
			JLabel label3 = new JLabel(values.get(i+2+n));
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBackground(Color.white);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(new EmptyBorder(5, 10, 5, 5));
			
	        panel.add(label1);
	        panel.add(label2);
	        panel.add(label3);
	        
	        message[i] = panel;
		}
		
		return message;	
		
	}
	
	public ArrayList<String> getPayRecord(Member m) {
		String sql = "SELECT date,price,paymentType FROM Payment where memID = '" + m.memID + "'";
		ArrayList<String> values = new ArrayList<String>();
		try {
	           Statement stmt  = DBManager.conn.createStatement();    
	           ResultSet rs    = stmt.executeQuery(sql);
	        	    
	           while(rs.next()) {
	           		values.add(rs.getString(1));//Datum
		       		values.add(rs.getString(2));//Pris
		       		values.add(rs.getString(3));//Betalningstyp

	           }    

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		System.out.println(values.get(0));
		return values;
	}
	
	public void displayPayRecord(Member m) {
		Object[] mes = {"Dina betalningar \n", generatePanelPay(m,getPayRecord(m))};
		
		String[] cho = {"Tillbaka"};
		
		int n = JOptionPane.showOptionDialog(null, mes, "Betalningar", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, cho, null);
		if (n==-1)
			menu(m);
		if (n == 0)
			Profil(m);
	}
	
	public void avslutaMedlemskap(Member m) {
		
		JTextField mejlF = new JTextField();
		JTextField passF = new JTextField();
		
		Object[] mes = {
			"Emejl: ",  mejlF,
			"Lösenord: ", passF	
		};
		String[] cho = {"Tillbaka","Avsluta Medlemskap"};
		
		int n = JOptionPane.showOptionDialog(null,mes,"Avsluta Medlemskap",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cho, null);
		
		if(n == -1) {
			menu(m);
		}else if(n == 0) {
			Profil(m);
		}else if(n == 1) {
			if(mejlF.getText().equals(m.email) && passF.getText().equals(m.password)) {
				String sql = "DELETE FROM Member WHERE memID = '" + m.memID + "'";
				String sql1 = "DELETE FROM Contract WHERE memID = '" + m.memID + "'";
				String sql2 = "DELETE FROM Payment WHERE memID = '" + m.memID + "'";
				String sql3 = "DELETE FROM OnlineTrainingRecord WHERE memID = '" + m.memID + "'";
            String sql4 = "DELETE FROM MemberGymRelation WHERE memID = '" + m.memID + "'";
            String sql5 = "DELETE FROM BookingGroupTraining WHERE memID = '" + m.memID + "'";
            try {
					
					PreparedStatement pstmt = DBManager.conn.prepareStatement(sql3);
					pstmt.executeUpdate();
					pstmt = DBManager.conn.prepareStatement(sql1);
					pstmt.executeUpdate();
					pstmt = DBManager.conn.prepareStatement(sql2);
					pstmt.executeUpdate();
               pstmt = DBManager.conn.prepareStatement(sql4);
               pstmt.executeUpdate();
               pstmt = DBManager.conn.prepareStatement(sql5);
					pstmt.executeUpdate();
               pstmt = DBManager.conn.prepareStatement(sql);
					pstmt.executeUpdate();
               
					final JDialog dialog = new JDialog();
					dialog.setAlwaysOnTop(true);    
					JOptionPane.showMessageDialog(dialog, m.first_name + " " + m.last_name + " är nu borttagen.");
					DBManager db = new DBManager(); 
               db.menu();

				    }catch (SQLException e1) {
					   System.out.println(e1.getMessage() + " Gigaloser");
				    }
				
				
			}
		}
		
	}
	
	public void changeValues(Member m,Boolean[] lis, String[] ans){

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

	      String[] cho = {"Ändra Uppgifte","Tillbaka"};
	      int option = JOptionPane.showOptionDialog(null, message,"Ändra uppgifer!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null,cho,cho[1]);
	      if (option == JOptionPane.CLOSED_OPTION)
	          menu(m);
	      else if (option == 1){
	         Profil(m);
	      }else if (option == 0){
		      Boolean[] nli = new Boolean[9];
		      if (option == JOptionPane.OK_OPTION){
		         if (fields[0].getText() != null && fields[0].getText().length() == 12 && fields[0].getText().matches("[0-9]+")) {
		            ans[0] = fields[0].getText();
		            m.PNR = Long.parseLong(fields[0].getText());
		            nli[0] = true;
	
		         }else{ans[0] = ""; nli[0] = false; checker = false;}
		         if (!fields[1].getText().equals("")) {
		            ans[1] = fields[1].getText();
		            m.first_name = fields[1].getText();
		            nli[1] = true;
		         }else{ans[1] = ""; nli[1] = false; checker = false;}
		         if (!fields[2].getText().equals("")) {
		            ans[2] = fields[2].getText();
		            m.last_name = fields[2].getText();
		            nli[2] = true;
		         }else{ans[2] = ""; nli[2] = false; checker = false;}
		         if (!fields[3].getText().equals("") && isValid(fields[3].getText())){
		            ans[3] = fields[3].getText();
		            m.email = fields[3].getText();
		            nli[3] = true;
		         }else{ans[3] = ""; nli[3] = false; checker = false;}
		         if (fields[4].getText() != null && fields[4].getText().matches("[0-9]+") && fields[4].getText().length() <= 10 ){
		            ans[4] = fields[4].getText();
		            m.phoneNumber = Integer.parseInt(fields[4].getText());
		            nli[4] = true;
		         }else{ans[4] = ""; nli[4] = false; checker = false;}
		         if (!fields[5].getText().equals("")){
		            ans[5] = fields[5].getText();
		            m.adress = fields[5].getText();
		            nli[5] = true;
		         }else{ans[5] = ""; nli[5] = false; checker = false;}
		         if (fields[6].getText() != null && fields[6].getText().matches("[0-9]+") && fields[6].getText().length() == 5){
		            ans[6] = fields[6].getText();
		            m.postalCode = Integer.parseInt(fields[6].getText());
		            nli[6] = true;
		         }else{ans[6] = ""; nli[6] = false; checker = false;}
		         if (!fields[7].getText().equals("")) {
		            ans[7] = fields[7].getText();
		            m.city = fields[7].getText();
		            nli[7] = true;
		         }else{ans[7] = ""; nli[7] = false; checker = false;}
		         if (!fields[8].getText().equals("")) {
		            ans[8] = fields[8].getText();
		            m.password = fields[8].getText();
		            nli[8] = true;
		         }else{ans[8] = ""; nli[8] = false; checker = false;}
	
		      }
	
		    if (checker == false) {
		    	changeValues(m,nli, ans);
		   	}else {
		   		if(m.Update()) {
		   			JOptionPane.showMessageDialog(null, "Uppgifter är nu ändrade!");
		   		}
		   }
	  }
	}

	static boolean isValid(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
	}

}
//Copyright 2004 Juan Heyns. All rights reserved.
//HÄMTAD KOD!!!
class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }

}


