package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoader<T> {

	Connection connection;
	
	public AbstractLoader(Connection connection) {
		if (connection == null) {
			throw new IllegalArgumentException("The Eventloader needs a database connection!");
		}
		this.connection = connection;		
	}

	protected abstract T extractEntity(ResultSet resultSet) throws SQLException;
	protected abstract PreparedStatement getStatement(String selectionAttributeValue);
	
	public List<T> load(String selectionAttributeValue) {
		List<T> result = new ArrayList<>();
		PreparedStatement statement = getStatement(selectionAttributeValue);
		try {
			if (statement.execute()) {
				ResultSet rs = statement.getResultSet();
				while (rs.next()) {
					result.add(extractEntity(rs));
				}
				rs.close();
			}					
			statement.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}	

}
