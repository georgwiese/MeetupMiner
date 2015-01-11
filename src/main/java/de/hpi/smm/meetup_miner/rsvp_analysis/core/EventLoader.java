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
		String query = "SELECT ID, NAME, YES_RSVP_COUNT, CREATED, TIME FROM EVENTS WHERE group_id = ?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, groupID);
			if (statement.execute()) {
				ResultSet rs = statement.getResultSet();
				while (rs.next()) {
					String id = rs.getString("ID");
					String title = rs.getString("NAME");
					int yesRSVPs = rs.getInt("YES_RSVP_COUNT");
					long created = rs.getLong("CREATED");
					long time = rs.getLong("TIME");
					Event event = new Event(id, title, yesRSVPs, created, time);
					result.add(event);
				}
				rs.close();
			}					
			statement.close();
		} catch (SQLException e) {
			result = null;
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}	
}
