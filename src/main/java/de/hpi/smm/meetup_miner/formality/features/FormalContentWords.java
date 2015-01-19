package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.builder.FeatureBuilder;

public class FormalContentWords extends Feature{
	
	public FormalContentWords() throws IOException{
		setTargetWordList();
	}
	
	@Override
	protected void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("FormalContentWords");
	}
	
	public void getFeatureValue(){
	}

}
