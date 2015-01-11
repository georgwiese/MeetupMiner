package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractLoader {

	Connection connection;
	
	public AbstractLoader(Connection connection) {
		if (connection == null) {
			throw new IllegalArgumentException("The Eventloader needs a database connection!");
		}
		this.connection = connection;		
	}

	protected abstract void receiveTuple(ResultSet resultSet) throws SQLException;
	
	protected void load(PreparedStatement statement) {
		try {
			if (statement.execute()) {
				ResultSet rs = statement.getResultSet();
				while (rs.next()) {
					receiveTuple(rs);
				}
				rs.close();
			}					
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
