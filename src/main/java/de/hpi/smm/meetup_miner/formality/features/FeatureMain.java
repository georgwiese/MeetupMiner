package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

public class FeatureMain {

	public static void main(String[] args) throws IOException {

		Feature feature = new ContentWords();
		feature.setTargetWordList();
		System.out.println(feature.getTargetWordList());
		
	}

}
