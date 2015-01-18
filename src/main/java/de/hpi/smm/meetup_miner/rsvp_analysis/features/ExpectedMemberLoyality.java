package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import java.util.Collection;

import com.google.common.base.Function;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.EventNeighborhood;

public class ExpectedMemberLoyality implements AbstractFeature {
	
	private MemberLoyality memberLoyality = new MemberLoyality();
	private Collection<Event> currentPastEvents;
	
	private Function<Event, Double> getMemberLoyality =
			new Function<Event, Double>() {
				@Override
				public Double apply(Event event) {
					return memberLoyality.forEvent(event, currentPastEvents);
				}
			};

	@Override
	public double forEvent(Event event, Collection<Event> pastEvents) {
		currentPastEvents = pastEvents;
		EventNeighborhood neighborhood = new EventNeighborhood(event, pastEvents);
		return neighborhood.weightedAverage(getMemberLoyality);
	}

}
