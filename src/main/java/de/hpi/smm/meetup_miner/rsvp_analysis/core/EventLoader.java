package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventLoader {

	Connection connection;
	
	public EventLoader(Connection connection) {
		if (connection == null) {
			throw new IllegalArgumentException("The Eventloader needs a database connection!");
		}
		this.connection = connection;		
	}
	
	/**
	 * Loads all Events for a specified group identified by its id
	 * @param groupID
	 * @return a list containing Event Objects that are organized by this group
	 */
	public ArrayList<Event> loadEventsForGroup(String groupID) {
		ArrayList<Event> result = new ArrayList<Event>();
		String query = "SELECT NAME, YES_RSVP_COUNT, CREATED, TIME FROM EVENTS WHERE group_id = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, groupID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String title = rs.getString("NAME");
				int yesRSVPs = rs.getInt("YES_RSVP_COUNT");
				long created = rs.getLong("CREATED");
				long time = rs.getLong("TIME");
				Event event = new Event(title, yesRSVPs, created, time);
				result.add(event);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}	
}
