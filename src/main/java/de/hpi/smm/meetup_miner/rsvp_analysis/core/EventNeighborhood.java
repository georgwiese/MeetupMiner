package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Represent events, weighted by their similarity to a base event.
 */
public class EventNeighborhood {
	
	private Event baseEvent;
	private EventWeighter eventWeighter;
	private Collection<EventInfo> eventInfos;
	
	public EventNeighborhood(Event baseEvent, Iterable<Event> events) {
		this.baseEvent = baseEvent;
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
	
	/**
	 * Return the weighted average of whatever mapFunction maps the neighborhood to.
	 * If the neighborhood is empty, return mapFunction.apply(baseEvent).
	 */
	public double weightedAverage(final Function<Event, Double> mapFunction) {
		if (eventInfos.isEmpty()) {
			return mapFunction.apply(baseEvent);
		}
		
		Collection<Double> weightedValues = mapWeighted(mapFunction);
		
		double sum = 0;
		for (double value : weightedValues) {
			sum += value;
		}
		return sum / getWeightSum();
	}
	
	public double getWeightSum() {
		double sum = 0;
		for (EventInfo eventInfo : eventInfos) {
			sum += eventInfo.weight;
		}
		return sum;
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
