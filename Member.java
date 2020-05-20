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
	           memshipID = rs.getString(10);

		   }catch (SQLException e) {
			   System.out.println("Funkar ej");
			   System.out.println(e.getMessage());
		   }
	}
	
	
}
