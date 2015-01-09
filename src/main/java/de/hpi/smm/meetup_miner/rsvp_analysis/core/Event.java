package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Event
 */
public class Event {


	public String getTitle() {
		// TODO: Implement
		return "";
	}
	
	/**
	 * Epoch time of the event in ms
	 * @return time
	 */
	public long getTime() {
		// TODO: Implement
		return 0;
	}

	/**
	 * Epoch time of the event creation in ms
	 * @return createdTime
	 */
	public long getCreatedTime() {
		// TODO: Implement
		return 0;
	}
	
	public int getYesRsvpCount() {
		// TODO: Implement
		return 0;
	}
	
	public Set<Integer> getYesMemberIds() {
		// TODO: Implement
		return new HashSet<>();
	}
}
