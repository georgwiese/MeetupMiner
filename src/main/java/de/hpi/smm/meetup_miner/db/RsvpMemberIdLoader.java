package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RsvpMemberIdLoader extends AbstractLoader<Integer> {

	String crawledCity;
	
	public RsvpMemberIdLoader(Connection connection) {
		super(connection);	
	}

	@Override
	protected Integer extractEntity(ResultSet resultSet) throws SQLException {
		return resultSet.getInt(1);
	}

	@Override
	protected PreparedStatement getStatement(String eventId) {
		String query = "SELECT MEMBER_MEMBER_ID FROM RSVPS WHERE EVENT_ID = ?";
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, eventId);
			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
