package de.hpi.smm.meetup_miner.text_analysis;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Compactness {
	
	private String id, description;
	private Connection connection;
	
	public Compactness(String id, String description, Connection connection) {
		this.id = id;
		this.description = description;
		this.connection = connection;
	}
	
	public double getCompactnessByWords(){
		
		NumberFormat numberFormat = new DecimalFormat("###.##");
		
		int numOfWords = Counter.countWords(description);
		int numOfKeyWords = new TfIdfAdapter(connection).getKeywordsFor(id).size();
		double compactness = (double) numOfKeyWords * 100 / numOfWords;
		
		return Double.parseDouble(numberFormat.format(compactness));
	}
	
	public double getCompactnessByChars(){
		
		NumberFormat numberFormat = new DecimalFormat("###.##");
		
		int numOfChars = Counter.countChars(description);
		int numOfKeyChars = Counter.countChars(new TfIdfAdapter(connection).getKeywordsFor(id));
		double compactness = (double) numOfKeyChars * 100 / numOfChars;
		
		return Double.parseDouble(numberFormat.format(compactness));
	}
	
}
