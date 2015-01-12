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
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlope;

public class RsvpAnalysisMain {
	
	private static final String CITY = "chicago";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	Connection connection = DatabaseConnector.getNewConnection();
    	Connection saveConnection = DatabaseConnector.getNewConnection();
    	GroupIdLoader groupIdLoader = new GroupIdLoader(connection);
    	EventLoader eventLoader = new EventLoader(connection);    	
    	
    	List<Integer> groupIDs = groupIdLoader.load(CITY);
    	for (int i = 0; i < groupIDs.size(); i++) {
    		int groupId = groupIDs.get(i);    		
        	List<Event> allEvents = eventLoader.load(Integer.toString(groupId));
    		List<Event> pastEvents = new ArrayList<>();
    		List<Event> upcomingEvents = new ArrayList<>();
    		splitEvents(allEvents, pastEvents, upcomingEvents);
    		System.out.println("Group " + i + " of " + groupIDs.size() + " (upcoming: " + upcomingEvents.size() + "; past: " + pastEvents.size() + ")");
    		
    		for (int j = 0; j < upcomingEvents.size(); j++) {
    			Event event = upcomingEvents.get(j);
    			if (j % 10 == 0)
    				System.out.println("\tEvent " + j + " of " + upcomingEvents.size());
    			event.expectedSize = (float) (new ExpectedSize()).forEvent(event, pastEvents);
    			event.expectedMemberLoyality = (float) (new ExpectedMemberLoyality()).forEvent(event, pastEvents);
    			event.expectedTrend = (float) (new TrendlineSlope()).forEvent(event, pastEvents);
    			
    			event.saveToDatabase(saveConnection);
    		}
    	}
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
