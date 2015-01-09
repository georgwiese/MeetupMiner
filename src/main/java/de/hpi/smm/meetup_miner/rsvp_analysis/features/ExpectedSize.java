package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import com.google.common.base.Function;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.EventNeighborhood;

public class ExpectedSize implements AbstractFeature {
	
	private static final Function<Event, Double> eventSize =
			new Function<Event, Double>() {
				public Double apply(Event event) {
					return (double) event.getYesRsvpCount();
				}
	};

	@Override
	public double forEvent(Event event, Iterable<Event> pastEvents) {
		EventNeighborhood neighborhood = new EventNeighborhood(event, pastEvents);
		return neighborhood.weightedAverage(eventSize);
	}

}
