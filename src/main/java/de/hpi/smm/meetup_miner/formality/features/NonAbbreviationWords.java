package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.builder.FeatureBuilder;

public class NonAbbreviationWords extends Feature{
	
	public NonAbbreviationWords() throws IOException{
		setTargetWordList();
	}

	@Override
	public void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("NonAbbreviationWords");
	}

}
