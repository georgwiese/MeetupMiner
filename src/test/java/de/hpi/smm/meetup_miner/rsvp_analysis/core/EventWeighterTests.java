package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.DEFAULT_EVENT_TIME;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.DEFAULT_EVENT_TITLE;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.TIME_DELTA_30DAYS;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.TIME_DELTA_WEEK;

@RunWith(JUnit4.class)
public class EventWeighterTests {

	private static final double EPSILON = 1E-5;
	
	private Event baseEvent;
	private EventWeighter eventWeighter;
	
	@Before
	public void setUp() {
		baseEvent = EventMockFactory.getDefault();
		eventWeighter = new EventWeighter(baseEvent, EventWeighter.HALF_TIME_MONTH);
	}

	@Test
	public void computeWeight_sameEvent() {
		assertEquals(eventWeighter.computeWeight(baseEvent), 1.0, EPSILON);
	}
	
	@Test
	public void computeWeight_halfTime() {
		Event event = EventMockFactory.get(
				DEFAULT_EVENT_TITLE,
				DEFAULT_EVENT_TIME - TIME_DELTA_30DAYS,
				DEFAULT_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.5, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_quaterTime() {
		Event event = EventMockFactory.get(
				DEFAULT_EVENT_TITLE,
				DEFAULT_EVENT_TIME - 2 * TIME_DELTA_30DAYS,
				DEFAULT_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.25, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_differentTitle() {
		Event event = EventMockFactory.get(
				DEFAULT_EVENT_TITLE + DEFAULT_EVENT_TITLE,
				DEFAULT_EVENT_TIME,
				DEFAULT_EVENT_TIME - TIME_DELTA_WEEK - 1);
		
		assertEquals(0.5, eventWeighter.computeWeight(event), EPSILON);
	}
	
	@Test
	public void computeWeight_allWithBoost() {
		Event event = EventMockFactory.get(
				DEFAULT_EVENT_TITLE + DEFAULT_EVENT_TITLE,
				DEFAULT_EVENT_TIME - TIME_DELTA_30DAYS,
				DEFAULT_EVENT_TIME - TIME_DELTA_WEEK);
		
		assertEquals(0.35, eventWeighter.computeWeight(event), EPSILON);
	}
}
