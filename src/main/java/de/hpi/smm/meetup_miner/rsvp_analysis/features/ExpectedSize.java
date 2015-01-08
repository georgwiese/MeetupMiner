package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import com.google.common.base.Function;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.EventNeighborhood;

public class ExpectedSize {
	
	private static final Function<Event, Double> eventSize =
			new Function<Event, Double>() {
				public Double apply(Event event) {
					return (double) event.getYesRsvpCount();
				}
	};

	public static double forEvent(Event event, Iterable<Event> allEvents) {
		EventNeighborhood neighborhood = new EventNeighborhood(event, allEvents);
		return neighborhood.weightedAverage(eventSize);
	}

}
