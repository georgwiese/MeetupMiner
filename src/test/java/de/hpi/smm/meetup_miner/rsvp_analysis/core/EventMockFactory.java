package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventMockFactory {
	
	public static final long DEFAULT_EVENT_TIME = 1420742875000L;
	public static final long TIME_DELTA_WEEK = 7L * 24L * 60L * 60L * 1000L;
	public static final long TIME_DELTA_30DAYS = 30L * 24L * 60L * 60L * 1000L;
	public static final String DEFAULT_EVENT_TITLE = "DefaultEvent";
	public static final int DEFAULT_EVENT_YES_RSVP_COUNT = 20;
	
	public static Event get(long time, long createdTime, int yesRsvpCount) {
		return get(
				DEFAULT_EVENT_TITLE,
				time,
				createdTime,
				yesRsvpCount);
	}
	
	public static Event get(String title, long time, long createdTime) {
		return get(title, time, createdTime, DEFAULT_EVENT_YES_RSVP_COUNT);
	}
	
	public static Event get(
			String title, long time, long createdTime, int yesRsvpCount) {
		Event event = mock(Event.class);
		when(event.getTitle()).thenReturn(title);
		when(event.getTime()).thenReturn(time);
		when(event.getCreatedTime()).thenReturn(createdTime);
		when(event.getYesRsvpCount()).thenReturn(yesRsvpCount);
		return event;
	}
	
	public static Event getDefault() {
		return get(
				DEFAULT_EVENT_TITLE,
				DEFAULT_EVENT_TIME,
				DEFAULT_EVENT_TIME - TIME_DELTA_WEEK,
				DEFAULT_EVENT_YES_RSVP_COUNT);
	}
}
