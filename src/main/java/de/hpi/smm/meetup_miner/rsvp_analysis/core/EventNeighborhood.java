package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

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
		this(baseEvent, events, EventWeighter.HALF_TIME_TWO_MONTHS);
	}
	
	public EventNeighborhood(Event baseEvent, Iterable<Event> events, long halfTime) {
		this.baseEvent = baseEvent;
		eventWeighter = new EventWeighter(baseEvent, halfTime);
		
		eventInfos = new ArrayList<EventInfo>();
		for (Event event : events) {
			if (event != baseEvent) {
				eventInfos.add(new EventInfo(event, eventWeighter.computeWeight(event)));
			}
		}
	}
	
	public Event getHighestWeightedNeighbor() {
		EventInfo highest = null;
		for (EventInfo eventInfo : eventInfos) {
			if (highest == null ||  highest.weight < eventInfo.weight) {
				highest = eventInfo;
			}
		}
		return highest.event;
	}
	
	public List<Pair<Event, Double>> getEventWeightPairs() {
		List<Pair<Event, Double>> list = new ArrayList<>();
		for (EventInfo eventInfo : eventInfos) {
			list.add(Pair.of(eventInfo.event, eventInfo.weight));
		}
		return list;
	}
	
	public List<Double> getWeights() {
		List<Double> weights = new ArrayList<Double>();
		for (EventInfo info : eventInfos) {
			weights.add(info.weight);
		}
		return weights;
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
