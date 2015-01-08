package de.hpi.smm.meetup_miner.rsvp_analysis;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Represent events, weighted by their similarity to a base event.
 */
public class EventNeighborhood {
	
	private EventWeighter eventWeighter;
	private Collection<EventInfo> eventInfos;
	
	public EventNeighborhood(Event baseEvent, Iterable<Event> events) {
		eventWeighter = new EventWeighter(baseEvent);
		
		eventInfos = new ArrayList<EventInfo>();
		for (Event event : events) {
			if (event != baseEvent) {
				eventInfos.add(new EventInfo(event, eventWeighter.computeWeight(event)));
			}
		}
	}
	
	/**
	 * Map events according a mapFunction and multiply each result with the
	 * event's weight.
	 */
	public Collection<Double> mapWeighted(final Function<Event, Double> mapFunction) {
		return Collections2.transform(eventInfos, new Function<EventInfo, Double>() {
			public Double apply(EventInfo eventInfo) {
				return mapFunction.apply(eventInfo.event) * eventInfo.weight;
			}
		});
	}
	
	private class EventInfo {
		public Event event;
		public double weight;
		
		public EventInfo(Event event, double weight) {
			this.event = event;
			this.weight = weight;
		}
	}
}
