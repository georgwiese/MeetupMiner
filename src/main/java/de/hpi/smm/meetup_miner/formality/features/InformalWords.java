package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.FeatureBuilder;

public class InformalWords extends Feature{
	
	public InformalWords() throws IOException{
		setTargetWordList();
	}

	@Override
	public void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("InformalWords");
	}

}
