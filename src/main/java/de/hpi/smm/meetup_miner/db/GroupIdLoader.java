package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupIdLoader extends AbstractLoader<Integer> {

	String crawledCity;
	
	public GroupIdLoader(Connection connection) {
		super(connection);	
	}

	@Override
	protected Integer extractEntity(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("ID");
	}

	@Override
	protected PreparedStatement getStatement(String selectionAttributeValue) {
		String query = "SELECT distinct(groups.id) FROM Groups JOIN Events On groups.id = events.group_id WHERE crawled_city = ? AND events.status = 'upcoming'";
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, selectionAttributeValue);
			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
