package de.hpi.smm.meetup_miner.rsvp_analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.EventLoader;
import de.hpi.smm.meetup_miner.db.GroupIdLoader;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public class RsvpAnalysisMain {
	
	private static final String CITY = "chicago";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	Connection connection = DatabaseConnector.getNewConnection();
    	GroupIdLoader groupIdLoader = new GroupIdLoader(connection);    	    
     	
    	List<Integer> groupIDs = groupIdLoader.load(CITY);
    	int numberOfThreads = 8;
    	CountDownLatch latch = new CountDownLatch(groupIDs.size());
    	
    	connection = DatabaseConnector.getNewConnection();
		EventLoader eventLoader = new EventLoader(connection);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		while (true) {
			int groupId = -1;
			if (groupIDs.size() > 0) {
				groupId = groupIDs.remove(0);
			} else {
				break;
			}
			List<Event> allEvents = eventLoader.load(Integer.toString(groupId));
    		List<Event> pastEvents = new ArrayList<>();
    		List<Event> upcomingEvents = new ArrayList<>();
    		splitEvents(allEvents, pastEvents, upcomingEvents);
    		
    		for (int i = 0; i < Math.min(upcomingEvents.size(), numberOfThreads); i++) {
    			Runnable eventCalculator = new RsvpAnalysisRunnable(upcomingEvents, pastEvents, latch, groupId);
    			executor.submit(eventCalculator);
    		}
		}
		System.out.println("All groups enqueued.");
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
    	DatabaseConnector.closeConnection(connection);
    	System.out.println("Finished RSVP Analyis at: " + System.currentTimeMillis());
	}
	
	private static void splitEvents(
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
