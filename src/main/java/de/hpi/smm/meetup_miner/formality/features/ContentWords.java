package de.hpi.smm.meetup_miner.formality.features;

import java.io.IOException;

import de.hpi.smm.meetup_miner.formality.FeatureBuilder;

public class ContentWords extends Feature{
	
	public ContentWords() throws IOException{
		setTargetWordList();
	}
	
	@Override
	protected void setTargetWordList() throws IOException {
		this.targetWordList = FeatureBuilder.getFeature("ContentWords");
	}
	
	public void getFeatureValue(){
		System.out.println("COntentWords");
	}

}
