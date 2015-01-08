package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class EventWeighterTests {

	private static final double EPSILON = 1E-5;
	private static final long BASE_EVENT_TIME = 1420742875000L;
	private static final long TIME_DELTA_WEEK = 7L * 24L * 60L * 60L * 1000L;
	private static final long TIME_DELTA_30DAYS = 30L * 24L * 60L * 60L * 1000L;
	private static final String BASE_EVENT_TITLE = "BaseEvent";
	
	private Event baseEvent;
	private EventWeighter eventWeighter;
	
	@Before
	public void setUp() {
		baseEvent = getMockedEvent(
				BASE_EVENT_TITLE, BASE_EVENT_TIME, BASE_EVENT_TIME - TIME_DELTA_WEEK);
		eventWeighter = new EventWeighter(baseEvent);
	}
	
	private Event getMockedEvent(String title, long time, long createdTime) {
		Event event = mock(Event.class);
		when(event.getTitle()).thenReturn(title);
		when(event.getTime()).thenReturn(time);
		when(event.getCreatedTime()).thenReturn(createdTime);
		return event;
	}

	@Test
	public void computeWeight_sameEvent() {
		assertEquals(eventWeighter.computeWeight(baseEvent), 1.0, EPSILON);
	}
	
	@Test
	public void computeWeight_halfTime() {
		Event event = getMockedEvent(
				BASE_EVENT_TITLE,
				BASE_EVENT_TIME - TIME_DELTA_30DAYS,
				BASE_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.5, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_quaterTime() {
		Event event = getMockedEvent(
				BASE_EVENT_TITLE,
				BASE_EVENT_TIME - 2 * TIME_DELTA_30DAYS,
				BASE_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.25, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_differentTitle() {
		Event event = getMockedEvent(
				BASE_EVENT_TITLE + BASE_EVENT_TITLE,
				BASE_EVENT_TIME,
				BASE_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.5, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_allWithBoost() {
		Event event = getMockedEvent(
				BASE_EVENT_TITLE + BASE_EVENT_TITLE,
				BASE_EVENT_TIME - TIME_DELTA_30DAYS,
				BASE_EVENT_TIME - TIME_DELTA_WEEK);
		
		assertEquals(0.35, eventWeighter.computeWeight(event), EPSILON);
	}
}
