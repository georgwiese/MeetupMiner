package de.hpi.smm.meetup_miner.formality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;

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
import scala.Tuple2;

public class LinearRegression {
	
	static JavaSparkContext sc = null;
	static JavaRDD<LabeledPoint> parsedData = null;
	static LinearRegressionModel model = null;
	
	public static void reset(){
		sc = null;
		parsedData = null;
		model = null;
	}
	
	private static List<Feature> getFeatures(){
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
	
	public static double predict(String description){
		
		String featureData = getParsedData(description);
		LabeledPoint labeledPoint = DataBuilder.createLabeledPoint(featureData);
		
		return predict(labeledPoint);
	}
	
	public static double predict(LabeledPoint labeledPoint){
		
		if(model == null){
			System.out.println("train and build a model first or load a model. ");
			return -1;
		}
		
		return  model.predict(labeledPoint.features());
	}
	
	private static String getParsedData(String description){
		
		List<Feature> features = getFeatures();
		String descriptionData = DataBuilder.createFeatureDataFromDescription(description, features);
	
		return descriptionData;
	}
	
	private static JavaRDD<LabeledPoint> getParsedData(File file){
		
		String path = file.toString();
		
		// Load and parse the data
	    JavaRDD<String> data = sc.textFile(path);
	    JavaRDD<LabeledPoint> parsedData = data.map(
	      new Function<String, LabeledPoint>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4899047225011705574L;

			public LabeledPoint call(String line) {
	          String[] parts = line.split(",");
	          String[] features = parts[1].split(" ");
	          double[] v = new double[features.length];
	          for (int i = 0; i < features.length - 1; i++)
	            v[i] = Double.parseDouble(features[i]);
	          return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
	        }
	      }
	    );
	    return parsedData;
	}
	
	public static void train(String path){
		train(path, 2000); // default iteration: 2000
	}
	
	public static void train(String path, int iteration){ // "data/Formality_Data.data"
		
		SparkConf conf = new SparkConf().setAppName("Linear Regression for Formality").setMaster("local");
		sc = new JavaSparkContext(conf);
		
		File file = new File(path);
	    parsedData = getParsedData(file);
	    parsedData.cache();
	    buildModel(iteration);
	}

	private static void buildModel(int iteration){
	    model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), iteration);	    
	}
	
	public static void writeResult() throws IOException{
		
		if(model == null){
			System.out.println("train and build a model first. ");
			return;
		}
		
		File writeFile = new File("data/Result.txt");
		
		if (writeFile.exists()) {
			if(writeFile.delete()) System.out.println(writeFile.getName() + " is deleted");
			else System.out.println("Delete operation is failed.");
		}
		writeFile.createNewFile();

		FileWriter fw = new FileWriter(writeFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		String content = "";
		Iterator<LabeledPoint> iterator = parsedData.toLocalIterator();
		while(iterator.hasNext()) {
			LabeledPoint curPoint = iterator.next();
			double prediction = model.predict(curPoint.features());
			double MSE = Math.pow(prediction - curPoint.label(), 2.0);
			
			content = curPoint.label() + " " + MSE + "\n";
			bw.write(content);
		}
		
		bw.close();
	}
	
	public static void saveModel(String path){
		
		if(model == null){
			System.out.println("train and build a model first. ");
			return;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(model);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadModel(String path){
		
		File file = new File(path);
		if(!file.exists()){
			System.out.println("The model file does not exist!");
			return;
		}
		
		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			model = (LinearRegressionModel) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static double test(){  //test parsedData
		
		if(model == null){
			System.out.println("train and build a model first. ");
			return -1;
		}
		
		if(parsedData == null){
			System.out.println("parsedData is null. ");
			return -1;
		}
		
	    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
	  	      new Function<LabeledPoint, Tuple2<Double, Double>>() {

				private static final long serialVersionUID = -2174768193533525100L;

				public Tuple2<Double, Double> call(LabeledPoint point) {
	  	          double prediction = model.predict(point.features());
	  	          return new Tuple2<Double, Double>(prediction, point.label());
	  	        }
	  	      }
	  	    );
	  	    
	    double MSE = new JavaDoubleRDD(valuesAndPreds.map(
	  	    	      new Function<Tuple2<Double, Double>, Object>() {

						private static final long serialVersionUID = 1003612371207826393L;

						public Object call(Tuple2<Double, Double> pair) {
							
							double mathPow = Math.pow(pair._1() - pair._2(), 2.0);
							
	  	    	          return mathPow;
	  	    	        }
	  	    	      }
	  	    	    ).rdd()).mean();
	    System.out.println("training Mean Squared Error = " + MSE);
	    return MSE;
	}	
}
