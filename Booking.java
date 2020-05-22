import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
	         		String[] option = {"Ekdalen","Aspvägen","Björkvägen"};
	            	int choice = JOptionPane.showOptionDialog(null, "Välj Gym","Våra Gym",
	                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, null);
	            	if (choice == 2)
	            		db.gymInfo("gym01");
	            	else if (choice == 1)
	            		db.gymInfo("gym02");
	            	else if (choice == 2)
	            		db.gymInfo("gym03");
	            	else if (choice == JOptionPane.CLOSED_OPTION)
	            		menu(m);
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
		String sqlMem = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,GroupTraining.groupTrainID FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID INNER JOIN BookingGroupTraining ON BookingGroupTraining.groupTrainID = GroupTraining.groupTrainID WHERE memID = '" + m.memID + "'";
		values = getGroupTraining(sqlMem);
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
		    		String sqlDate = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,GroupTraining.groupTrainID FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID WHERE GroupTraining.date='" + date + "'" + filters;
		    		System.out.println(date);
		   			Object[] panels = generatePanel(m,getGroupTraining(sqlDate),true);
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
	    		  "Lisa Vanderpump", 
	    		  "Lisa Rinna", 
	    		  "NeNe Leakes",
	    		  "Ramona Singer",
	    		  "Erika Jayne"
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
				filters += " AND trainType.trainTypeID = 'ttype01'";
				currentFilters += "Styrka ";
			}else if(tcb.getSelectedIndex() == 2){
				filters += " AND trainType.trainTypeID = 'ttype02'";
				currentFilters += "Kondition ";
			}else if(tcb.getSelectedIndex() == 3){
				filters += " AND trainType.trainTypeID = 'ttype03'";
				currentFilters += "Yoga ";
			}else if(tcb.getSelectedIndex() == 4){
				filters += " AND trainType.trainTypeID = 'ttype04'";
				currentFilters += "Stretch ";
			}else if(tcb.getSelectedIndex() == 5){
				filters += " AND trainType.trainTypeID = 'ttype05'";
				currentFilters += "Fit på 15 ";
			}
			if(ecb.getSelectedIndex() == 1){
				filters += " AND Instructor.employeeID = 'emp01'";
				currentFilters += "Lisa Vanderpump ";
			}else if(ecb.getSelectedIndex() == 2){
				filters += " AND Instructor.employeeID = 'emp02'";
				currentFilters += "Lisa Rinna ";
			}else if(ecb.getSelectedIndex() == 3){
				filters += " AND Instructor.employeeID = 'emp03'";
				currentFilters += "NeNe Leakes ";
			}else if(ecb.getSelectedIndex() == 4){
				filters += " AND Instructor.employeeID = 'emp04'";
				currentFilters += "Ramona Singer ";
			}else if(ecb.getSelectedIndex() == 5){
				filters += " AND Instructor.employeeID = 'empo5'";
				currentFilters += "Erika Jayne ";
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
	
	public ArrayList<String> getGroupTraining(String sql) {
		int memBooked;
		ArrayList<String> values = new ArrayList<String>();
		 try {
	           Statement stmt  = DBManager.conn.createStatement();    
	           ResultSet rs    = stmt.executeQuery(sql);
	        	    
	           while(rs.next()) {
	           		values.add(rs.getString(1));//Namn
		       		values.add(rs.getString(2));//Träningstyp
		       		values.add(rs.getString(3));//Starttid
		       		values.add(rs.getString(4));//Sluttid
		       		values.add(rs.getString(5));//Instruktör förnamn
		       		values.add(rs.getString(6));//Instruktör efternamn
		       		values.add(rs.getString(7));;//Gym namn
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
	           }    

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		return values;
	}
	
	public void getOnlineTraining(Member m, String sql) {
		ArrayList<String> name = new ArrayList<String>();
		
		 try {
	           Statement stmt  = DBManager.conn.createStatement();
	           ResultSet rs    = stmt.executeQuery(sql);
	           while(rs.next()) {
	        	   name.add(rs.getString(1)); //Namn
	        	   rs.getString(2); //url
	        	   rs.getString(3); //Beskrivning
	        	   rs.getString(4); //ID
	           }
		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		 String[] names = new String[name.size()];
		
		 for(int i = 0; i<name.size();i++)
			 names[i] = name.get(i);
		 
		 JButton b1;
		 for(int n = 0; n<names.length;n++)
			 b1 = new JButton(names[n]);
		 
		 
		 int n = JOptionPane.showOptionDialog(null,"Välj typ av träning","Online Träning",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, names, null);
		 if (n == names.length-1) {
			 OnlineTräning(m);
		 }else if(n == names.length-2) {
			 
		 }else if(n == names.length-2) {
			 
		 }else if(n == names.length-2) {
			 
		 }else if(n == names.length-2) {
			 
		 }
	}
	
	
	public Object[] generatePanel(Member m,ArrayList<String> values,Boolean book) {
		
		int counter = values.size()/9;
		Object[] message = new Object[counter];
		
		for (int i = 0,n = 0; i<counter; i++, n+= 9) {
			String trainName = values.get(i+n);
			JLabel label1 = new JLabel(values.get(i+n) + " | " + values.get(i+1+n));
			JLabel label2 = new JLabel(values.get(i+2+n) + "-" + values.get(i+3+n) +  "|" + values.get(i+4+n) + " " + values.get(i+5+n));
			JLabel label3 = new JLabel(values.get(i+6+n) + " | " + values.get(i+7+n));
			JLabel label4 = new JLabel(values.get(i+8+n) + " lediga platser av " + values.get(i+9+n));
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBackground(Color.white);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(1,1,1,1);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 0; 
	        panel.add(label1, constraints);
	        System.out.println(label1.getY());
	        
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
	
	
	public void AvbokaPass(Member m, String trainName) {

		String SQL = "DELETE FROM BookingGroupTraining WHERE memID = ? AND groupTrainID = (SELECT groupTrainID FROM GroupTraining where name = ?)";
		try {
			
			PreparedStatement pstmt = DBManager.conn.prepareStatement(SQL);
			System.out.println(m.memID + " och " + trainName);
			pstmt.setString(1,m.memID);
			pstmt.setString(2,trainName);
			pstmt.executeUpdate();
			
			final JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);    
			JOptionPane.showMessageDialog(dialog, "Passet är avbokat!");
			

		    }catch (SQLException e1) {
			   System.out.println(e1.getMessage() + " Gigaloser");
		    }
	}
	
	public void BokaEttPass(Member m, String trainName) {
		
		String SQL = "INSERT INTO BookingGroupTraining VALUES(?,(SELECT groupTrainID FROM GroupTraining where name = ?),5)";
		try {
			PreparedStatement pstmt = DBManager.conn.prepareStatement(SQL);
			System.out.println(m.memID + " och " + trainName);
			pstmt.setString(1,m.memID);
			pstmt.setString(2,trainName);
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
      			
      		break;		
      		case 4:
      			//Träningshistorik
      		
      		break;
      		case 3:
      			//Ändra uppgifter
        	 
      		break;
      		case 2:
      			//Betalningshistorik
         
      		break;
      		case 1:
      			//AvslutaMedlemskap
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
  			sql += "'ttype01'";
  			getOnlineTraining(m,sql);
  		break;		
  		case 4:
  			//Trän
  			sql += "'ttype02'";
  			getOnlineTraining(m,sql);
  		break;
  		case 3:
  			//Ändra uppgifter
  			sql += "'ttype03'";
  			getOnlineTraining(m,sql);
  		break;
  		case 2:
  			//Betalningshistorik
  			sql += "'ttype04'";
  			getOnlineTraining(m,sql);
  		break;
  		case 1:
  			//AvslutaMedlemskap
  			sql += "'ttype05'";
  			getOnlineTraining(m,sql);
  		break;
  		case 0:
  			//Tillbaka
  			menu(m);
  		break;
  } 
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