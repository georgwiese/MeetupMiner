package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TfIdfAdapter {
	
	public static final double THRESHOLD = 0.2;
	public static final String QUERY = "SELECT * FROM MINING_GETTFIDF('MEETUP:EVENTS','DESCRIPTION', ?) WHERE tfidf >= ?";
	
	private Connection connection;

	public TfIdfAdapter(Connection connection) {
		super();
		this.connection = connection;
	}

	public HashMap<Integer, List<String>> getKeywords(Iterable<Integer> ids) {
		HashMap<Integer, List<String>> result = new HashMap<>();
		for (int id : ids) {
			result.put(id, getKeywordsFor(id));
		}
		return result;
	}
	
	public ArrayList<String> getKeywordsFor(int rownumber) {
		ArrayList<String> results = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(QUERY);
			statement.setInt(1, rownumber);
			statement.setDouble(2, THRESHOLD);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				results.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
}
