package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;

public class Compactness {
	
	private int rownumber;
	private String description;
	private Connection connection;
	
	public Compactness(int rownumber, String description, Connection connection) {
		this.rownumber = rownumber;
		this.description = description;
		this.connection = connection;
	}
	
	public double getCompactnessByWords(){		
		int numOfWords = Counter.countWords(description);
		int numOfKeyWords = new TfIdfAdapter(connection).getKeywordsFor(rownumber).size();
		
		double compactness = 0;
		if (numOfWords != 0) {
			compactness = (double) numOfKeyWords * 100 / numOfWords;
		}
		
		return compactness;
	}
	
	public double getCompactnessByChars(){		
		int numOfChars = Counter.countChars(description);
		int numOfKeyChars = Counter.countChars(new TfIdfAdapter(connection).getKeywordsFor(rownumber));
		
		double compactness = 0;
		if (numOfChars != 0) {
			compactness = (double) numOfKeyChars * 100 / numOfChars;
		}
		
		return compactness;
	}
	
}
