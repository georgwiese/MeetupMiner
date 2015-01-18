package de.hpi.smm.meetup_miner.formality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.atteo.evo.inflector.English;

import de.hpi.smm.meetup_miner.formality.features.AbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.ContentWords;
import de.hpi.smm.meetup_miner.formality.features.ContractionWords;
import de.hpi.smm.meetup_miner.formality.features.Feature;
import de.hpi.smm.meetup_miner.formality.features.FormalWords;
import de.hpi.smm.meetup_miner.formality.features.InformalWords;
import de.hpi.smm.meetup_miner.formality.features.NonAbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.NonContractionWords;

public class FormalityMain {

	public static void main(String[] args) throws IOException {
		
		
//		List<Feature> features = new ArrayList<Feature>();
//		features.add(new AbbreviationWords());
//		features.add(new ContentWords());
//		features.add(new ContractionWords());
//		features.add(new FormalWords());
//		features.add(new InformalWords());
//		features.add(new NonAbbreviationWords());
//		features.add(new NonContractionWords());
//		
//		DataBuilder.writeDataFile(features);
//		
//		LinearRegression.run("data/Formality_Data.data");
		
		LinearRegression.train("data/Formality_Data.data");
		LinearRegression.test();

	}

}
