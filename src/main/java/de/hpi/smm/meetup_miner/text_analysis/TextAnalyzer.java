package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.EventLoader;
import de.hpi.smm.meetup_miner.db.GroupIdLoader;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public class TextAnalyzer {
	
	private static final String CITY = "chicago";
	
	private Connection connection;
	private HashMap<String, Integer> id2rownumber = new HashMap<>();
	
	public TextAnalyzer(Connection connection) {
		this.connection = connection;
	}
	
	public static void main(String... args) {
		Connection connection = null;
		try {
			connection = DatabaseConnector.getNewConnection();
			GroupIdLoader groupLoader = new GroupIdLoader(connection);
			EventLoader eventLoader = new EventLoader(connection);
			TextAnalyzer analyzer = new TextAnalyzer(connection);
			
			analyzer.setupRowNumbers();
			
			for (Integer groupId : groupLoader.load(CITY)) {
				List<Event> allEvents = eventLoader.load(Integer.toString(groupId));
				for (Event event : allEvents) {
					System.out.println(String.format("ID %s: row number: %d", event.getID(), analyzer.id2rownumber.get(event.getID())));
					double compactness = analyzer.calculateCompactness(event.getID(), analyzer.id2rownumber.get(event.getID()));
					System.out.println(compactness);
				}
			}
			
		} catch (SQLException | ClassNotFoundException e) {}
	}
	
	private void setupRowNumbers() {
		System.out.println("Fetching row numbers...");
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT id, row_number() over () FROM meetup.events");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				id2rownumber.put(resultSet.getString(1), resultSet.getInt(2));
			}
		} catch (SQLException e) {}
		System.out.println("Done.");
	}
	
	private String getDescriptionFor(String id) {
		String result = "";
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT description FROM meetup.events WHERE id = ?");
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getString(1);
			}
		} catch (SQLException e) {}
		return result;
	}
	
	public double calculateCompactness(String id, int rownumber) throws ClassNotFoundException, SQLException {
		return new Compactness(rownumber, getDescriptionFor(id), connection).getCompactnessByWords();
	}
	
}
