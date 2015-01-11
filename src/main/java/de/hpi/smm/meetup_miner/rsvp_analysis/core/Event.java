package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Event
 */
public class Event {
	
	private String title;
	private int yesRSVPs;
	private long createdTime;
	private long time;
	
	public Event(String title, int yesRSVPcount, long createdTime, long time) {
		this.title = title;
		this.yesRSVPs = yesRSVPcount;
		this.createdTime = createdTime;
		this.time = time;
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
	
	public Set<Integer> getYesMemberIds() {
		// TODO: Implement
		return new HashSet<>();
	}
}
