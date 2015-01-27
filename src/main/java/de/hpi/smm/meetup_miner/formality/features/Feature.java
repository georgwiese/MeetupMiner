package de.hpi.smm.meetup_miner.formality.features;

import java.util.ArrayList;
import java.util.List;

public abstract class Feature {

	protected List<String> targetWordList;
	
	public Feature(){
		this.targetWordList = new ArrayList<String>();
	}

	public List<String> getTargetWordList() {
		return targetWordList;
	}
	
	public double getFeatureValue(String description, boolean onlyFreq){
		
		if(description == null) description = "";
		
		double totalCount = 0d;
		ArrayList<String> singleWordList = new ArrayList<String>();
		ArrayList<String> multipleWordList = new ArrayList<String>();
		
		//targetWordList = singleWordList(e.g., "hello") + multipleWordList(e.g., "throw out")
		for(String curWord : targetWordList){
			if(curWord.split(" ").length == 1) singleWordList.add(curWord);
			else multipleWordList.add(curWord);
		}
		
		//singleWordList
		 String strArray[] = description.split(" ");
		 int descriptionLength = strArray.length;
		 for(int i=0; i < descriptionLength; i++){
			 if(singleWordList.contains(strArray[i])) totalCount++;
		 }
		
		//multipleWordList
		for(String curStr : multipleWordList){
			totalCount += (double) getNumOfOccurence(description, curStr);
		}
		
		double featureVal = onlyFreq ? totalCount : totalCount/(double)descriptionLength ;
		return featureVal;
	}
	
	protected int getNumOfOccurence(String description, String findStr){
		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1){

		       lastIndex = description.indexOf(findStr,lastIndex);

		       if( lastIndex != -1){
		             count ++;
		             lastIndex+=findStr.length();
		      }
		}
		
		return count;
	}

}
