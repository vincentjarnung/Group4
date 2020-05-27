import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Member {
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

	
	public Member(String memID) {
		String sql = "select * from Member where memID = '" + memID + "'";

		   try {
	           Statement stmt  = DBManager.conn.createStatement();
	           ResultSet rs    = stmt.executeQuery(sql);
	           this.memID = rs.getString(1);
	           PNR = rs.getLong(2);
	           first_name = rs.getString(3);
	           last_name = rs.getString(4);
	           email = rs.getString(5);
	           phoneNumber = rs.getInt(6);
	           adress = rs.getString(7);
	           postalCode= rs.getInt(8);
	           city = rs.getString(9);
	           password = rs.getString(10);
	           memshipID = rs.getString(11);

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
	}
	
	public boolean Update() {
		String sql = "UPDATE Member SET pnr = ?, first_name = ?, last_name = ?, mail = ?, phoneNr = ?, address = ?, postalCode = ?, city = ?, password = ?, memshipID = ? WHERE memID = '" + memID + "'";
		
		
		boolean checker = false;
		try {
			PreparedStatement pstmt = DBManager.conn.prepareStatement(sql);
			pstmt.setString(1,memID);
			pstmt.setLong(1,PNR);
			pstmt.setString(2,first_name);
			pstmt.setString(3,last_name);
			pstmt.setString(4,email);
			pstmt.setInt(5,phoneNumber);
			pstmt.setString(6,adress);
			pstmt.setInt(7,postalCode);
			pstmt.setString(8,city);
			pstmt.setString(9,password);
			pstmt.setString(10,memshipID);
			
			pstmt.executeUpdate();
			checker = true;
			
		}catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
		return checker;
	}
	
	
}
