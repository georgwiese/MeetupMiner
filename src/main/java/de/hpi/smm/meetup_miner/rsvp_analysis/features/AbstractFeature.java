package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public interface AbstractFeature {

	double forEvent(Event event, Iterable<Event> pastEvents);

}
