package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.RsvpMemberIdLoader;

/**
 * Represents an Event
 */
public class Event {
	
	private static Connection connection;
	
	// Values that are given by Meetup
	private String id;
	private String title;
	private int yesRSVPs;
	private long createdTime;
	private long time;
	private String status;
	private Set<Integer> yesMemberIds = null;
	
	// Values that are calculated
	public float expectedMemberLoyality = -Float.MAX_VALUE;
	public float expectedSize = -Float.MAX_VALUE;
	public float expectedTrend = -Float.MAX_VALUE;
	
	public Event(
			String id, String title, int yesRSVPcount, long createdTime, long time, String status) {
		this.id = id;
		this.title = title;
		this.yesRSVPs = yesRSVPcount;
		this.createdTime = createdTime;
		this.time = time;
		this.status = status;
	}
	
	public String getID() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * Epoch time of the event in ms
	 * @return time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Epoch time of the event creation in ms
	 * @return createdTime
	 */
	public long getCreatedTime() {
		return createdTime;
	}
	
	public int getYesRsvpCount() {
		return yesRSVPs;
	}
	
	public String getStatus() {
		return status;
	}
	
	/** 
	 * Member IDs of the members that have RSVPd with YES (lazy loading)
	 * @return
	 */
	public Set<Integer> getYesMemberIds() {
		if (yesMemberIds == null) {
			try {
				yesMemberIds = downloadYesMemberIds();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return yesMemberIds;
	}
	
	private Set<Integer> downloadYesMemberIds() throws ClassNotFoundException, SQLException {
		if (connection == null) {
			connection = DatabaseConnector.getNewConnection();
		}
		
		RsvpMemberIdLoader loader = new RsvpMemberIdLoader(connection);
		return ImmutableSet.copyOf(loader.load(id));
	}
	
	public void saveToDatabase() {
		String query = getSaveQuery();
		Connection con = null;
		try {
			con = DatabaseConnector.getNewConnection();
			if (con != null) {
				PreparedStatement stmt = con.prepareStatement(query);
				setValues(stmt);
				stmt.execute();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					DatabaseConnector.closeConnection(con);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	private String getSaveQuery() {
		StringBuilder queryBuilder = new StringBuilder("UPSERT Event_Predictions (id,");
		if (expectedMemberLoyality > -Float.MAX_VALUE)
			queryBuilder.append("expected_Member_Loyality,");
		if (expectedSize > -Float.MAX_VALUE)
			queryBuilder.append("expected_Size,");
		if (expectedTrend > -Float.MAX_VALUE)
			queryBuilder.append("expected_Trend,");
		queryBuilder.deleteCharAt(queryBuilder.length() - 1);
		queryBuilder.append(") VALUES (?,");
		if (expectedMemberLoyality > -Float.MAX_VALUE)
			queryBuilder.append("?,");
		if (expectedSize > -Float.MAX_VALUE)
			queryBuilder.append("?,");
		if (expectedTrend > -Float.MAX_VALUE)
			queryBuilder.append("?,");
		queryBuilder.deleteCharAt(queryBuilder.length() - 1);
		queryBuilder.append(") WHERE id = ?");
		return queryBuilder.toString();
	}
	
	private void setValues(PreparedStatement stmt) {
		try {
			stmt.setString(1, id);
			int i = 2;
			if (expectedMemberLoyality > -Float.MAX_VALUE) {
				stmt.setFloat(i, expectedMemberLoyality);
				i += 1;
			}
			if (expectedSize > -Float.MAX_VALUE) {
				stmt.setFloat(i, expectedSize);
				i += 1;
			}
			if (expectedTrend > -Float.MAX_VALUE) {
				stmt.setFloat(i, expectedTrend);
				i += 1;
			}
			stmt.setString(i, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
