package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.FeatureBuilder;

public class FormalWords extends Feature{
	
	public FormalWords() throws IOException{
		setTargetWordList();
	}

	@Override
	protected void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("FormalWords");
	}
	
}
