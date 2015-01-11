package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;

/**
 * Represents an Event
 */
public class Event {
	
	// Values that are given by Meetup
	private String id;
	private String title;
	private int yesRSVPs;
	private long createdTime;
	private long time;	
	
	// Values that are calculated
	public float expectedMemberLoyality;
	public float expectedSize;
	
	public Event(String id, String title, int yesRSVPcount, long createdTime, long time) {
		this.id = id;
		this.title = title;
		this.yesRSVPs = yesRSVPcount;
		this.createdTime = createdTime;
		this.time = time;
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
	
	/** 
	 * Member IDs of the members that have RSVPd with YES (lazy loading)
	 * @return
	 */
	public Set<Integer> getYesMemberIds() {
		HashSet<Integer> result = new HashSet<Integer>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = DatabaseConnector.getNewConnection();
			if (connection != null) {
				String query = "SELECT MEMBER_MEMBER_ID FROM RSVPS WHERE EVENT_ID = ?";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, id);
				if (stmt.execute()) {
					rs = stmt.getResultSet();
					while (rs.next()) {
						result.add(rs.getInt(1));
					}
				}				
			}
		} catch (ClassNotFoundException | SQLException e) {
			result = null;
			e.printStackTrace();
		} finally {
			try {
				DatabaseConnector.closeConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		return result;
	}
}
