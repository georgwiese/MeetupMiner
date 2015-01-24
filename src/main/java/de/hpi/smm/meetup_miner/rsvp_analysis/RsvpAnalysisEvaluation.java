package de.hpi.smm.meetup_miner.rsvp_analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.EventLoader;
import de.hpi.smm.meetup_miner.db.GroupIdLoader;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedMemberLoyality;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.ExpectedSize;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.MemberLoyality;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlope;
import de.hpi.smm.meetup_miner.rsvp_analysis.features.TrendlineSlopeWeighted;

public class RsvpAnalysisEvaluation {
	
	private static final String CITY = "chicago";
	
	private static class RsvpAnalysisThread extends Thread {

		private List<Integer> groupIDs;
		private Connection connection;
		
		public RsvpAnalysisThread(List<Integer> groupIDs) {
			this.groupIDs = groupIDs;
			this.connection = null;
		}
		
		@Override
		public void run() {
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
					List<Event> events = filterPastEvents(eventLoader.load(Integer.toString(groupId)));
					if (events.size() < 2) {
						continue;
					}
					
		    		Event predictionEvent = getLatestEvent(events);
		    		events.remove(predictionEvent);
		    		Event secondLatestEvent = getLatestEvent(events);
		    		double secondLatestTimeDiff = (predictionEvent.getTime() - secondLatestEvent.getTime()) / 1000.0 / 3600.0 / 24.0;
		    		
					if (secondLatestTimeDiff < 0.0001) {
						continue;
					}
		    		
		    		System.out.println(this.getName() + "\tGroup " + groupId + " - events: " + events.size());
		    		
		    		double expectedSize = (new ExpectedSize()).forEvent(predictionEvent, events);
		    		double expectedMemberLoyality = (new ExpectedMemberLoyality()).forEvent(predictionEvent, events);
		    		double expectedTrend = (new TrendlineSlope()).forEvent(predictionEvent, events);
		    		double expectedTrendWeighted = (new TrendlineSlopeWeighted()).forEvent(predictionEvent, events);
		    		
		    		double actualSize = predictionEvent.getYesRsvpCount();
		    		double actualLoyality = (new MemberLoyality()).forEvent(predictionEvent, events);
		    		double actualTrend = (predictionEvent.getYesRsvpCount() - secondLatestEvent.getYesRsvpCount()) / secondLatestTimeDiff;
		    		
		    		List<Double> values = ImmutableList.of(
		    				expectedSize, actualSize,
		    				expectedMemberLoyality, actualLoyality,
		    				expectedTrend, expectedTrendWeighted, actualTrend);
		    		
		    		storeEvaluationResult(values, groupId);
				}
			} catch (ClassNotFoundException | SQLException e) {				
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						DatabaseConnector.closeConnection(connection);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			System.out.println(this.getName() + " exited.");
			super.run();
		}

		private void storeEvaluationResult(List<Double> values, int groupId) {
			PreparedStatement stmt = null;
			System.out.println(values);
			try {
				String query = "UPSERT Event_Predictions_Evaluation" +
						"(group_id, size_prediction, size_actual, loyality_prediction, " +
						"loyality_actual, trend_prediction, trend_prediction_weighted, trend_actual)" +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
						"WHERE group_id = ?";
				stmt = connection.prepareStatement(query);
				
				stmt.setInt(1, groupId);
				for (int i = 0; i < 7; i++) {
					stmt.setDouble(i + 2, values.get(i));
				}
				stmt.setInt(9, groupId);
				stmt.execute();
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
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
	
	public static List<Event> filterPastEvents(List<Event> allEvents) {
		List<Event> past = new ArrayList<>();
		for (Event event : allEvents) {
			if (event.getStatus().equals("past")) {
				past.add(event);
			}
		}
		return past;
	}
	
	public static Event getLatestEvent(List<Event> events) {
		Event latest = null;
		for (Event event : events) {
			if (latest == null || latest.getTime() < event.getTime()) {
				latest = event;
			}
		}
		return latest;
	}
	
	public static void printEvaluation(String feature, double expected, double actual) {
		System.out.println(feature + " - expected: " + expected + ", actual : " + actual);
	}
}
