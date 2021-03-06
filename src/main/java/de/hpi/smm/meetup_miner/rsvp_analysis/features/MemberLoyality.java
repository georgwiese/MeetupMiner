package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.EventNeighborhood;

/**
 * Compute member loyality: The weighted average of the percentage of common members
 * with events past to the event.
 */
public class MemberLoyality implements AbstractFeature {
	
	Set<Integer> baseEventMembers;
	
	private Function<Event, Double> percentageMemberInCommon =
			new Function<Event, Double>() {
				@Override
				public Double apply(Event event) {
					return (double) Sets.intersection(
							event.getYesMemberIds(), baseEventMembers).size() /
							baseEventMembers.size();
				}
			};

	@Override
	public double forEvent(Event event, Collection<Event> pastEvents) {
		baseEventMembers = event.getYesMemberIds();
		if (baseEventMembers.size() == 0) {
			return 0.5;
		}
		
		List<Event> relativePastEvents = getRelativePastEvents(event, pastEvents);
		EventNeighborhood neighborhood = new EventNeighborhood(event, relativePastEvents);
		return neighborhood.weightedAverage(percentageMemberInCommon);
	}
	
	/**
	 * From pastEvents, collect all events that are past relative to event
	 */
	private List<Event> getRelativePastEvents(Event event, Iterable<Event> pastEvents) {
		List<Event> result = new ArrayList<>();
		for (Event pastEvent : pastEvents) {
			if (pastEvent.getTime() < event.getTime()) {
				result.add(pastEvent);
			}
		}
		return result;
	}

}
