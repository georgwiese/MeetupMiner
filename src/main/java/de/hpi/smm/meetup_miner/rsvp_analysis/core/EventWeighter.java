package de.hpi.smm.meetup_miner.rsvp_analysis.core;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 * Weight events of a group according to its similarity to a base event.
 *
 */
public class EventWeighter {

	public static final long HALF_TIME_DAY = 24L * 60L * 60L * 1000L;
	public static final long HALF_TIME_MONTH = 30L * HALF_TIME_DAY;
	public static final long HALF_TIME_6MONTHS = 6L * HALF_TIME_MONTH;
	public static final long HALF_TIME_YEAR = 365L * HALF_TIME_DAY;
	
	private static final double SAME_TIME_CREATED_BOOST = 0.1;
	private static final double MIN_TITLE_SIMILARITY = 0.2;
	
	private static final int MAX_CACHE_SIZE = 1024 * 1024;
	private static ConcurrentHashMap<Pair<String, String>, Double>
			stringSimilarityCache = new ConcurrentHashMap<>();
	
	private Event baseEvent;
	private long halfTime;
	private AbstractStringMetric stringMetric;

	public EventWeighter(Event baseEvent, long halfTime) {
		this.baseEvent = baseEvent;
		this.halfTime = halfTime;
		stringMetric = new Levenshtein();
	}

	public double computeWeight(Event event) {
		double weight = getTitleSimilarity(event) * getTimeSimilarity(event) +
				getSameTimeCreatedBoost(event);
		
		return Math.min(weight, 1.0);
	}
	
	private double getTitleSimilarity(Event event) {
		String title1 = baseEvent.getTitle();
		String title2 = event.getTitle();
		Pair<String, String> cacheKey = Pair.of(title1, title2);
		
		if (stringSimilarityCache.size() > MAX_CACHE_SIZE) {
			stringSimilarityCache.clear();
		}
		Double similarity = stringSimilarityCache.get(cacheKey);
		if (similarity == null) {
			similarity = (double) stringMetric.getSimilarity(title1, title2);
			stringSimilarityCache.put(cacheKey, similarity);
		}
		return Math.max(MIN_TITLE_SIMILARITY, similarity);
	}
	
	private double getTimeSimilarity(Event event) {
		long timeDiff = Math.abs(event.getTime() - baseEvent.getTime());
		return Math.pow(2.0, - 1.0 / halfTime * timeDiff);
	}
	
	private double getSameTimeCreatedBoost(Event event) {
		if (event.getCreatedTime() == baseEvent.getCreatedTime()) {
			return SAME_TIME_CREATED_BOOST;
		} else {
			return 0.0;
		}
	}
}
