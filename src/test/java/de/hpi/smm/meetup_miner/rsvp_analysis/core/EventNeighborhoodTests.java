package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.DEFAULT_EVENT_TIME;
import static de.hpi.smm.meetup_miner.rsvp_analysis.core.EventMockFactory.TIME_DELTA_30DAYS;

@RunWith(JUnit4.class)
public class EventNeighborhoodTests {

	private static final double EPSILON = 1E-5;
	
	private static final long CREATED_TIME1 = DEFAULT_EVENT_TIME - TIME_DELTA_30DAYS;
	private static final long CREATED_TIME2 = DEFAULT_EVENT_TIME - TIME_DELTA_30DAYS - 1;
	private static final List<Event> EVENTS = ImmutableList.of(
			EventMockFactory.get(
					DEFAULT_EVENT_TIME, CREATED_TIME1, 10),
			EventMockFactory.get(
					DEFAULT_EVENT_TIME - TIME_DELTA_30DAYS, CREATED_TIME2, 20),
			EventMockFactory.get(
					DEFAULT_EVENT_TIME - 2 * TIME_DELTA_30DAYS, CREATED_TIME2, 80)
	);
	private static final List<Double> NEIBORHOOD_WEIGHTS = ImmutableList.of(0.5, 0.25);
	
	private static final Function<Event, Double> getEventSize =
			new Function<Event, Double> (){
				public Double apply(Event event) {
					return (double) event.getYesRsvpCount();
				}
	};
	
	private EventNeighborhood neighborhood;
	
	@Before
	public void setUp() {
		neighborhood = new EventNeighborhood(EVENTS.get(0), EVENTS);
	}
	
	@Test
	public void getWeights() {
		assertEquals(NEIBORHOOD_WEIGHTS, neighborhood.getWeights());
	}
	
	@Test
	public void mapWeighted() {
		List<Double> expectedSizes = ImmutableList.of(10.0, 20.0);
		Collection<Double> actualSizes = neighborhood.mapWeighted(getEventSize);
		
		assertEquals(expectedSizes.size(), actualSizes.size());
		int i = 0;
		for (double actualSize : actualSizes) {
			assertEquals(expectedSizes.get(i), actualSize, EPSILON);
			i++;
		}
	}
	
	@Test
	public void weightedAverage() {
		assertEquals(40.0, neighborhood.weightedAverage(getEventSize), EPSILON);
	}
}
