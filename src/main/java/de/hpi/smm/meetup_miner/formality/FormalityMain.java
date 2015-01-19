package de.hpi.smm.meetup_miner.formality;

import java.io.IOException;

public class FormalityMain {

	public static void main(String[] args) throws IOException {
		
		//select features
//		List<Feature> features = new ArrayList<Feature>();
//		features.add(new AbbreviationWords());
//		features.add(new FormalContentWords());
//		features.add(new InformalContentWords());
//		features.add(new ContractionWords());
//		features.add(new FormalWords());
//		features.add(new InformalWords());
//		features.add(new NonAbbreviationWords());
//		features.add(new NonContractionWords());
//		
//		DataBuilder.writeDataFile(features);
		LinearRegression.train("data/Formality_Data.data", 2000);
		LinearRegression.test();
		

	}

}
