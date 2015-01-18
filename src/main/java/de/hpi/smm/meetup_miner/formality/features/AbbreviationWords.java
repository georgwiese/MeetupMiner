package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.FeatureBuilder;

public class AbbreviationWords extends Feature{
	
	public AbbreviationWords() throws IOException{
		setTargetWordList();
	}

	@Override
	protected void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("AbbreviationWords");
	}

}
