package de.hpi.smm.meetup_miner.rsvp_analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.EventLoader;
import de.hpi.smm.meetup_miner.db.GroupIdLoader;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedMemberLoyality;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedSize;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlopeWeighted;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlope;

public class RsvpAnalysisMain {
	
	private static final String CITY = "chicago";
	
	private static class RsvpAnalysisThread extends Thread {

		private List<Integer> groupIDs;
		
		public RsvpAnalysisThread(List<Integer> groupIDs) {
			this.groupIDs = groupIDs;
		}
		
		@Override
		public void run() {
			Connection connection = null;
			try {
				connection = DatabaseConnector.getNewConnection();
				EventLoader eventLoader = new EventLoader(connection);
				while (true) {
					int groupId = -1;
					synchronized (groupIDs) {
						if (groupIDs.size() > 0) {
							groupId = groupIDs.remove(0);
							System.out.println(this.getName() + " - Started new group (" + groupIDs.size() + " remaining)");
						} else {
							break;
						}
					}
					List<Event> allEvents = eventLoader.load(Integer.toString(groupId));
		    		List<Event> pastEvents = new ArrayList<>();
		    		List<Event> upcomingEvents = new ArrayList<>();
		    		splitEvents(allEvents, pastEvents, upcomingEvents);
		    		System.out.println(this.getName() + "\tGroup " + groupId + " - upcoming: " + upcomingEvents.size() + "; past: " + pastEvents.size());
		    		
		    		for (int j = 0; j < upcomingEvents.size(); j++) {
		    			Event event = upcomingEvents.get(j);
		    			System.out.println("\t" + this.getName() + "\tEvent " + j + " of " + upcomingEvents.size());
		    			event.expectedSize = (float) (new ExpectedSize()).forEvent(event, pastEvents);
		    			event.expectedMemberLoyality = (float) (new ExpectedMemberLoyality()).forEvent(event, pastEvents);
		    			event.expectedTrend = (float) (new TrendlineSlope()).forEvent(event, pastEvents);
		    			event.expectedTrendWeighted = (float) (new TrendlineSlopeWeighted()).forEvent(event, pastEvents);
		    			
		    			event.saveToDatabase(connection);
		    		}
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
			}
			System.out.println(this.getName() + " exited.");
			super.run();
		}
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	Connection connection = DatabaseConnector.getNewConnection();
    	GroupIdLoader groupIdLoader = new GroupIdLoader(connection);    	    
     	
    	List<Integer> groupIDs = groupIdLoader.load(CITY);
    	ArrayList<Thread> threads = new ArrayList<Thread>();
    	int numberOfThreads = 8;
    	for (int i = 0; i < numberOfThreads; i++) {
    		RsvpAnalysisThread thread = new RsvpAnalysisThread(groupIDs);
    		threads.add(thread);
    		thread.start();
    	}
    	
    	for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void splitEvents(
			List<Event> allEvents, List<Event> past, List<Event> upcoming) {
		for (Event event : allEvents) {
			if (event.getStatus().equals("past")) {
				past.add(event);
			} else if (event.getStatus().equals("upcoming")) {
				upcoming.add(event);
			}
		}
	}
}
