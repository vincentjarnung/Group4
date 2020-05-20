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

		values = getGroupTraining(m,"",true);
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
	        	HittaPass(m);
	        }
	        
	}
	
	public void HittaPass(Member m) {
			//Copyright 2004 Juan Heyns. All rights reserved.
        	UtilDateModel model = new UtilDateModel();
    		DateLabelFormatter dlf = new DateLabelFormatter();
    		Properties p = new Properties();
    		p.put("text.today", "Today");
    		p.put("text.month", "Month");
    		p.put("text.year", "Year");
    		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,dlf);
    		
    		String[] choices = {"Hitta Pass","Tillbaka"};
    		JLabel l1= new JLabel("HITTA PASS");
    		l1.setFont(new Font("Arial", Font.BOLD, 18));
    		Object[] finalMes = {l1,"\n",datePicker};
    		
    		System.out.println(dlf);
    		int n = JOptionPane.showOptionDialog(null,finalMes,"Bokade Pass",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
    		if(n == 1)
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
		   				HittaPass(m);
    				}
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    		String date = sdf.format(selectedDate);
		    		System.out.println(date);
		   			Object[] panels = generatePanel(m,getGroupTraining(m,date, false),true);
		   			Object[] dateMes = {l1,"\n",datePicker, panels};
		   			int i = JOptionPane.showOptionDialog(null,dateMes,"Bokade Pass",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, null);
		   			System.out.println(i);
	    			if (i == -1) {
	    				menu(m);
	    				break;}
		    		else if (i == 1) {
		    			BokaPass(m);
		    			break;
		    			}
	    		}	
	    	}
	}

	
	public ArrayList<String> getGroupTraining(Member m, String date, Boolean dif) {
		String sql = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity,bookedMems FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID INNER JOIN BookingGroupTraining ON BookingGroupTraining.groupTrainID = GroupTraining.groupTrainID WHERE memID = '" + m.memID + "'";
		String sql1 = "SELECT GroupTraining.name,trainType.name,beginTime,endTime,Instructor.first_name,Instructor.last_name,Gym.name,Room.name,capacity FROM GroupTraining INNER JOIN Room ON Room.roomID = GroupTraining.roomID INNER JOIN Gym ON Gym.gymID = Room.gymID INNER JOIN Instructor ON Instructor.employeeID = GroupTraining.employeeID INNER JOIN trainType ON trainType.trainTypeID = GroupTraining.trainTypeID WHERE GroupTraining.date = '" + date + "'";;
		ArrayList<String> values = new ArrayList<String>();
		 try {
	           Statement stmt  = DBManager.conn.createStatement();
	           
	           if(!dif) {
	        	   sql = sql1;
	           }
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
		       		values.add(rs.getString(9));//Platser kvar
		       		
	           }    

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		return values;
	}
	
	public void getOnlineTraing(Member m) {
		String sql = "SELECT * FROM OnlineTraining WHERE memID = '" + m.memID + "'";
		
		 try {
	           Statement stmt  = DBManager.conn.createStatement();
	           ResultSet rs    = stmt.executeQuery(sql);
	           

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		
	}
	
	public Object[] generatePanel(Member m,ArrayList<String> values,Boolean book) {
		
		int counter = values.size()/9;
		Object[] message = new Object[counter];
		
		for (int i = 0,n = 0; i<counter; i++, n+= 8) {
			String trainName = values.get(i+n);
			JLabel label1 = new JLabel(values.get(i+n) + " | " + values.get(i+1+n));
			JLabel label2 = new JLabel(values.get(i+2+n) + "-" + values.get(i+3+n) +  "|" + values.get(i+4+n) + " " + values.get(i+5+n));
			JLabel label3 = new JLabel(values.get(i+6+n) + " | " + values.get(i+7+n));
			JLabel label4 = new JLabel(values.get(i+8+n) + " Lediga platser");
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