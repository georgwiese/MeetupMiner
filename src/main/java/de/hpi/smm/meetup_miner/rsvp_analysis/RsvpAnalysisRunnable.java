package de.hpi.smm.meetup_miner.rsvp_analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedMemberLoyality;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedSize;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlope;

public class RsvpAnalysisRunnable implements Runnable {

	private List<Event> upcomingEvents;
	private List<Event> pastEvents;
	private CountDownLatch latch;
	private int groupID;
	
	public RsvpAnalysisRunnable(List<Event> upcomingEvents, List<Event> pastEvents, CountDownLatch latch, int groupID) {
		this.upcomingEvents = upcomingEvents;
		this.pastEvents = pastEvents;
		this.latch = latch;
		this.groupID = groupID;
	}

	@Override
	public void run() {		
		Calendar cal;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Connection connection = null;
		try {
			connection = DatabaseConnector.getNewConnection();
			float trend = (float) (new TrendlineSlope()).forEvent(null, pastEvents);
			Event currentEvent = null;
			while (true) {
				int remainingEvents = 0;
				synchronized (upcomingEvents) {
					remainingEvents = upcomingEvents.size();
					if (remainingEvents > 0)
						currentEvent = upcomingEvents.remove(0);
					else
						break;
				}
				cal = Calendar.getInstance();
				System.out.println(sdf.format(cal.getTime()) + " - " + Thread.currentThread().getName() + " - Group: " + groupID + " - " + remainingEvents + " Events remaining.");
    			currentEvent.expectedSize = (float) (new ExpectedSize()).forEvent(currentEvent, pastEvents);
    			currentEvent.expectedMemberLoyality = (float) (new ExpectedMemberLoyality()).forEvent(currentEvent, pastEvents);
    			currentEvent.expectedTrend = trend;
    			
    			currentEvent.saveToDatabase(connection);
    		}
		} catch (ClassNotFoundException | SQLException e) {				
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					DatabaseConnector.closeConnection(connection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			latch.countDown();
		}
	}
	
}
