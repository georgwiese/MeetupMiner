package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

public class TrendlineSlope implements AbstractFeature {
	
	private class TimeComparator implements Comparator<Event> {

		@Override
		public int compare(Event o1, Event o2) {
			return Float.compare(o1.getTime(), o2.getTime());			
		}
		
	}

	// based on http://classroom.synonym.com/calculate-trendline-2709.html
	@Override
	public double forEvent(Event event, Iterable<Event> pastEvents) {
		ArrayList<Event> events = createOrderedList(pastEvents);		
		float a = 0, b = 0, b1 = 0, b2 = 0, c = 0, d = 0;
		for (int i = 0; i < events.size(); i++) {
			Event currentEvent = events.get(i);
			a += (i + 1) * currentEvent.getYesRsvpCount();
			b1 += i;
			b2 += currentEvent.getYesRsvpCount();
			c += i * i;
			d += i;
		}
		a *= events.size();
		b = b1 * b2;
		c *= events.size();
		d *= d;
		return (a - b) / (c - d);
	}
	
	private ArrayList<Event> createOrderedList(Iterable<Event> events) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event event : events) {
			result.add(event);
		}
		Collections.sort(result, new TimeComparator());
		return result;
	}

}
