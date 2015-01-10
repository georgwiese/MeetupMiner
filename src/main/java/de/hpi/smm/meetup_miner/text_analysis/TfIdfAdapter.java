package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TfIdfAdapter {
	
	public static final double THRESHOLD = 0.5;
	public static final String QUERY = "SELECT ta_token, ta_tfidf FROM meetup.test WHERE id = %d and ta_tfidf >= %s ORDER BY ta_tfidf";
	
	private Connection connection;

	public TfIdfAdapter(Connection connection) {
		super();
		this.connection = connection;
	}

	public HashMap<Integer, List<String>> getKeywords(int fromId, int toId) {
		HashMap<Integer, List<String>> result = new HashMap<>();
		for (int id = fromId; id <= toId; id++) {
			result.put(id, getKeywordsFor(id));
		}
		return result;
	}
	
	public ArrayList<String> getKeywordsFor(int id) {
		String query = String.format(QUERY, id, getFormatedThreshold());
		ArrayList<String> results = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				results.add(resultSet.getString(1));
			}
		} catch (SQLException e) {}
		return results;
	}
	
	private static String getFormatedThreshold() {
		return Double.toString(THRESHOLD);
	}
	
}
