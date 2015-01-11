package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public class EventLoader extends AbstractLoader<Event>{
	
	public EventLoader(Connection connection) {
		super(connection);
	}

	@Override
	protected Event extractEntity(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("ID");
		String title = resultSet.getString("NAME");
		int yesRSVPs = resultSet.getInt("YES_RSVP_COUNT");
		long created = resultSet.getLong("CREATED");
		long time = resultSet.getLong("TIME");
		Event event = new Event(id, title, yesRSVPs, created, time);
		return event;
	}

	@Override
	protected PreparedStatement getStatement(String selectionAttributeValue) {
		String query = "SELECT ID, NAME, YES_RSVP_COUNT, CREATED, TIME FROM EVENTS WHERE group_id = ?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, selectionAttributeValue);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return statement;
	}	
}
