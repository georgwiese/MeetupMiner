package de.hpi.smm.meetup_miner.formality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;

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
	
	public static void train(String path, int iteration){ // "data/Formality_Data.data"
		
		SparkConf conf = new SparkConf().setAppName("Linear Regression for Formality").setMaster("local");
		sc = new JavaSparkContext(conf);
		
		// Load and parse the data
	    JavaRDD<String> data = sc.textFile(path);
	    parsedData = data.map(
	      new Function<String, LabeledPoint>() {

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
	    parsedData.cache();
	    buildModel(iteration);
	}

	private static void buildModel(int iteration){
	    int numIterations = iteration;
	    model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations);	    
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
	
	public static double predict(LabeledPoint labeledPoint){
		
		if(model == null){
			System.out.println("train and build a model first. ");
			return -1;
		}
		
		return  model.predict(labeledPoint.features());
	}
	
	public static double test(){
		
		if(model == null){
			System.out.println("train and build a model first. ");
			return -1;
		}
		
	    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
	  	      new Function<LabeledPoint, Tuple2<Double, Double>>() {

				private static final long serialVersionUID = -2174768193533525100L;

				public Tuple2<Double, Double> call(LabeledPoint point) {
	  	          double prediction = model.predict(point.features());
	  	          System.out.printf("%f\n", prediction);
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
	    sc.close();
	    return MSE;
	}

	public static void run(String path) {
		
		SparkConf conf = new SparkConf().setAppName("Linear Regression for Formality").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
	    // Load and parse the data
	    JavaRDD<String> data = sc.textFile(path);
	    JavaRDD<LabeledPoint> parsedData = data.map(
	      new Function<String, LabeledPoint>() {

			private static final long serialVersionUID = 4283402431233585959L;

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
	    parsedData.cache();
	    
	 // Building the model
	    int numIterations = 100;
	    final LinearRegressionModel model = 
	      LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations);
	    
	 // analyze
	    //ArrayList<ArrayList<Double>> analyze = new ArrayList<ArrayList<Double>>();
//	    ArrayList<Double> test = new ArrayList<Double>();
//	    double[] v = new double[4];
//	    LabeledPoint labelPoint = new LabeledPoint(1.0, Vectors.dense(v));
//	    double prediction = model.predict(labelPoint.features());
	    
	 // Evaluate model on training examples and compute training error
	    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
	      new Function<LabeledPoint, Tuple2<Double, Double>>() {

			private static final long serialVersionUID = -3346048035752468229L;

			public Tuple2<Double, Double> call(LabeledPoint point) {
	          double prediction = model.predict(point.features());
	          //System.out.println(prediction + ", " + point.label());
	          return new Tuple2<Double, Double>(prediction, point.label());
	        }
	      }
	    );
	    
	    double MSE = new JavaDoubleRDD(valuesAndPreds.map(
	    	      new Function<Tuple2<Double, Double>, Object>() {

					private static final long serialVersionUID = 4980643517111584188L;

					public Object call(Tuple2<Double, Double> pair) {
	    	          return Math.pow(pair._1() - pair._2(), 2.0);
	    	        }
	    	      }
	    	    ).rdd()).mean();
	    System.out.println("training Mean Squared Error = " + MSE);
	    
	    sc.close();
	}
	
}
