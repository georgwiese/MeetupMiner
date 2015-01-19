package de.hpi.smm.meetup_miner.formality.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.mllib.regression.LabeledPoint;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.formality.LinearRegression;
import de.hpi.smm.meetup_miner.formality.builder.DataBuilder;
import de.hpi.smm.meetup_miner.formality.features.AbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.ContractionWords;
import de.hpi.smm.meetup_miner.formality.features.Feature;
import de.hpi.smm.meetup_miner.formality.features.FormalContentWords;
import de.hpi.smm.meetup_miner.formality.features.FormalWords;
import de.hpi.smm.meetup_miner.formality.features.InformalContentWords;
import de.hpi.smm.meetup_miner.formality.features.InformalWords;
import de.hpi.smm.meetup_miner.formality.features.NonAbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.NonContractionWords;

public class DataBase {

	private static void updateDataBase(HashMap<String, Double> formalityMap) throws ClassNotFoundException, SQLException{
		
		Connection con = DatabaseConnector.getNewConnection();
		
		for(String key : formalityMap.keySet()){
			
			String query = "UPDATE EVENT_PREDICTIONS SET DESCRIPTION_FORMALITY = " + formalityMap.get(key) + "WHERE ID = '" + key + "'";
			PreparedStatement pstmt;
			
			try {
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main (String[]  args) throws ClassNotFoundException, SQLException, IOException {

		updateDataBase(getFormalityPredictionMap());
		
	}
	
	private static HashMap<String, Double> getFormalityPredictionMap() throws ClassNotFoundException, SQLException, IOException{
		
		HashMap<String, Double> formalityMap = getEventIdMap();
		Connection con = DatabaseConnector.getNewConnection();
		List<Feature> featureList = getFeatureList();
		LinearRegression.train("data/Formality_Data.data", 2000);  
		
		for(String key : formalityMap.keySet()){

			String query = "SELECT DESCRIPTION FROM MEETUP.EVENTS WHERE ID = '" + key + "'";
			PreparedStatement stmt = con.prepareStatement(query);
			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();
				while(rs.next()){
					
					String description = rs.getString("DESCRIPTION");
					Double prediction;
					
					if(description == null) prediction = -1d;
					else{
						String featureData = DataBuilder.createFeatureDataFromDescription(description, featureList);
						LabeledPoint labeledPoint = DataBuilder.createLabeledPoint(featureData);
						prediction = LinearRegression.predict(labeledPoint);
					}

					System.out.println(prediction);
					if(prediction<0) prediction = 0d;
					if(prediction>10) prediction = 10d;
					formalityMap.put(key, prediction);
				}
			} else {
				System.out.println("Failed to execute the statement.");
			}
		}
		
		return formalityMap;
	}
	
	private static List<Feature> getFeatureList() throws IOException{
		
		List<Feature> features = new ArrayList<Feature>();
		features.add(new AbbreviationWords());
		features.add(new FormalContentWords());
		features.add(new InformalContentWords());
		features.add(new ContractionWords());
		features.add(new FormalWords());
		features.add(new InformalWords());
		features.add(new NonAbbreviationWords());
		features.add(new NonContractionWords());
		
		return features;
	}
	
	private static HashMap<String, Double> getEventIdMap() throws ClassNotFoundException, SQLException{
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		Connection con = DatabaseConnector.getNewConnection();
		String query = "SELECT * FROM MEETUP.EVENT_PREDICTIONS";
		PreparedStatement stmt = con.prepareStatement(query);
		if (stmt.execute()) {
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				map.put(rs.getString(1), null);				
			}
		} else {
			System.out.println("Failed to execute the statement.");
		}
		
		return map;
	}
	
	

}
