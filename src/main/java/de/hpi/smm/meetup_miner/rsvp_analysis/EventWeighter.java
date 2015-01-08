package de.hpi.smm.meetup_miner.rsvp_analysis;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 * Weight events of a group according to its similarity to a base event.
 *
 */
public class EventWeighter {
	
	/** Half time of time similarity in ms */
	private static final long TIME_SIMILARITY_HALF_TIME =
			30 * 24 * 60 * 60 * 1000; // 1 month
	private static final double SAME_TIME_CREATED_BOOST = 0.1;
	
	private Event baseEvent;
	private AbstractStringMetric stringMetric;

	public EventWeighter(Event baseEvent) {
		this.baseEvent = baseEvent;
		stringMetric = new Levenshtein();
	}

	public double computeWeight(Event event) {
		double weight = getTitleSimilarity(event) * getTimeSimilarity(event) +
				getSameTimeCreatedBoost(event);
		
		return Math.min(weight, 1.0);
	}
	
	private double getTitleSimilarity(Event event) {
		return stringMetric.getSimilarity(
				baseEvent.getTitle(), event.getTitle());
	}
	
	private double getTimeSimilarity(Event event) {
		long timeDiff = Math.abs(event.getTime() - baseEvent.getTime());
		return Math.pow(2.0, - 1.0 / TIME_SIMILARITY_HALF_TIME * timeDiff);
	}
	
	private double getSameTimeCreatedBoost(Event event) {
		if (event.getCreatedTime() == baseEvent.getCreatedTime()) {
			return SAME_TIME_CREATED_BOOST;
		} else {
			return 0.0;
		}
	}
}
