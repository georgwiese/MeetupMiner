package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import java.util.Collection;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public interface AbstractFeature {

	double forEvent(Event event, Collection<Event> pastEvents);

}
