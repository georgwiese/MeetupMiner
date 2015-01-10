package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;

public class TextAnalyzer {
	
	public TextAnalyzer() {
		DatabaseConnector.setup();
	}
	
	private String getDescriptionFor(String id) {
		String result = "";
		Connection connection;
		try {
			connection = DatabaseConnector.getNewConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT description FROM meetup.groups WHERE id = ?");
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getString(1);
			}
			DatabaseConnector.closeConnection(connection);
		} catch (ClassNotFoundException | SQLException e) {}
		return result;
	}
	
	public double calculateCompactness(String id) throws ClassNotFoundException, SQLException {
		double result = 0;
		Connection connection = DatabaseConnector.getNewConnection();
		
		result = new Compactness(id, getDescriptionFor(id), connection).getCompactnessByWords();
		
		DatabaseConnector.closeConnection(connection);
		return result;
	}
	
}
