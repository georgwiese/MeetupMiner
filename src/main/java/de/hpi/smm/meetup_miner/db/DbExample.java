package de.hpi.smm.meetup_miner.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DbExample {

	public static void main (String[]  args) throws ClassNotFoundException, SQLException {
		DatabaseConnector.setup(4);
		Connection con = DatabaseConnector.getNewConnection();
		String query = "SELECT COUNT(*) FROM GROUPS WHERE crawled_city = 'chicago'";
		PreparedStatement stmt = con.prepareStatement(query);
		if (stmt.execute()) {
			ResultSet rs = stmt.getResultSet();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			System.out.println("Query: " + query);
			while (rs.next()) {
				int columnCount = rsMetaData.getColumnCount();
				System.out.println("New Result");
				for (int i = 0; i < columnCount; i++)					
					System.out.println("\t" + rsMetaData.getColumnLabel(i + 1) 
										+ " (" + (i + 1) + "/" + columnCount + "): " 
										+ rs.getString(i + 1));
			}
		} else {
			System.out.println("Failed to execute the statement.");
		}
	}
	
}
