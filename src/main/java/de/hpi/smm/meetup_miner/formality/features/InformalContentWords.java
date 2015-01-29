package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class InformalContentWords extends Feature{
	
	private String[] targetWords = {"karaoke","eating","fun","board game","game","mcdonalds","hangout","no location","player","casual","picnic","laugh","enjoy","movie","run","runner","jazz","free hug","single","dance","skate park","dancer","salsa","comedy","chat","skydiving","hanging out","beach","party","atmosphere","hey","flirt","nightlife","romance","friendship","friend","!!","let’s","tons of","groove","club","hikes","share in the moment","all you can eat","entertainer","a dime","thanx",":-)","lover","valentine’s day","love","dating","nuts","?!","dreamer","knitting","comfy","awesome"};
	
	public InformalContentWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
