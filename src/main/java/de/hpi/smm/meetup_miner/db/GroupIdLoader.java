package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupIdLoader extends AbstractLoader {

	Connection connection;
	List<Integer> groupIds;
	
	public GroupIdLoader(Connection connection) {
		super(connection);	
	}
	
	/**
	 * Get all group ids for a crawledCity
	 * @param crawledCity
	 * @return List of group ids
	 */
	public List<Integer> loadGroupIds(String crawledCity) {
		groupIds = new ArrayList<>();
		load(getStatement(crawledCity));
		return groupIds;
	}

	@Override
	protected void receiveTuple(ResultSet resultSet) throws SQLException {
		groupIds.add(resultSet.getInt("ID"));
	}

	protected PreparedStatement getStatement(String crawledCity) {
		String query = "SELECT id FROM Groups WHERE crawled_city = ?";
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, crawledCity);
			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
