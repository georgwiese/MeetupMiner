package de.hpi.smm.meetup_miner.rsvp_analysis.features;

import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.regression.GLSMultipleLinearRegression;

import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.EventNeighborhood;

public class TrendlineSlope implements AbstractFeature {

	// based on http://classroom.synonym.com/calculate-trendline-2709.html
	@Override
	public double forEvent(Event event, Collection<Event> pastEvents) {
		EventNeighborhood neighborhood = new EventNeighborhood(event, pastEvents);
		double[] y = new double[pastEvents.size()];
		double[][] x = new double[pastEvents.size()][1];
		double[][] omega = new double[pastEvents.size()][pastEvents.size()];
		
		int index = 0;
		for (Pair<Event, Double> eventWithWeight : neighborhood.getEventWeightPairs()) {
			Event currentEvent = eventWithWeight.getLeft();
			double currentWeight = eventWithWeight.getRight();
			
			y[index] = currentEvent.getYesRsvpCount();
			x[index][0] = normalizeTime(currentEvent);
			omega[index][index] = 1.0 / currentWeight;
		}
		
		GLSMultipleLinearRegression regression = new GLSMultipleLinearRegression();
		regression.newSampleData(y, x, omega); 
		double[] b = regression.estimateRegressionParameters();
		return b[1];
	}
	
	private long normalizeTime(Event event) {
		return (event.getTime() - event.getTime()) / 1000 / 3600 / 24;
	}

}
