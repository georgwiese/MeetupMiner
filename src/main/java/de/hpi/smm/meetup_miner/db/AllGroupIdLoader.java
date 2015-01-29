package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AllGroupIdLoader extends GroupIdLoader {

	public AllGroupIdLoader(Connection connection) {
		super(connection);
	}
	
	public List<Integer> load() {
		return super.load(null);
	}

	@Override
	protected PreparedStatement getStatement(String selectionAttributeValue) {
		String query = "SELECT distinct(groups.id) FROM Groups JOIN Events On groups.id = events.group_id WHERE events.status = 'upcoming'";
		
		try {
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
