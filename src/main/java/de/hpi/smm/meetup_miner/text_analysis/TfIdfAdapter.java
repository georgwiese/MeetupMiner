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
	public static final String QUERY = "SELECT ta_token, ta_tfidf FROM meetup.test WHERE id = ? and ta_tfidf >= ? ORDER BY ta_tfidf";
	
	private Connection connection;

	public TfIdfAdapter(Connection connection) {
		super();
		this.connection = connection;
	}

	public HashMap<String, List<String>> getKeywords(Iterable<String> ids) {
		HashMap<String, List<String>> result = new HashMap<>();
		for (String id : ids) {
			result.put(id, getKeywordsFor(id));
		}
		return result;
	}
	
	public ArrayList<String> getKeywordsFor(String id) {
		ArrayList<String> results = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(QUERY);
			statement.setString(1, id);
			statement.setDouble(2, THRESHOLD);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				results.add(resultSet.getString(1));
			}
		} catch (SQLException e) {}
		return results;
	}
	
}