package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

public class FeatureMain {

	public static void main(String[] args) throws IOException {

		Feature feature = new FormalContentWords();
		System.out.println(feature.getTargetWordList());
	}
}
