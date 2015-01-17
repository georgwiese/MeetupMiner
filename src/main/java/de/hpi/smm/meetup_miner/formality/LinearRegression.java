package de.hpi.smm.meetup_miner.formality;

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
	
	JavaSparkContext sc;
	JavaRDD<LabeledPoint> parsedData;
	LinearRegressionModel model;
	
	public LinearRegression(){
		this.sc = null;
		this.parsedData = null;
		this.model = null;
	}
	
	public void train(String path){ // "data/Formality_Data.data"
		
		SparkConf conf = new SparkConf().setAppName("Linear Regression for Formality").setMaster("local");
		sc = new JavaSparkContext(conf);
		
		// Load and parse the data
	    JavaRDD<String> data = sc.textFile(path);
	    parsedData = data.map(
	      new Function<String, LabeledPoint>() {

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
	    buildModel();
	}

	private void buildModel(){
	    int numIterations = 100;
	    model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations);
	}
	
	public void test(){
	    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
	  	      new Function<LabeledPoint, Tuple2<Double, Double>>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 2444496605724582428L;

				public Tuple2<Double, Double> call(LabeledPoint point) {
	  	          double prediction = model.predict(point.features());
	  	          return new Tuple2<Double, Double>(prediction, point.label());
	  	        }
	  	      }
	  	    );
	  	    
	    double MSE = new JavaDoubleRDD(valuesAndPreds.map(
	  	    	      new Function<Tuple2<Double, Double>, Object>() {
	  	    	    	  
						/**
						 * 
						 */
						private static final long serialVersionUID = 1575720147092581378L;

						public Object call(Tuple2<Double, Double> pair) {
	  	    	          return Math.pow(pair._1() - pair._2(), 2.0);
	  	    	        }
	  	    	      }
	  	    	    ).rdd()).mean();
	    System.out.println("training Mean Squared Error = " + MSE);
	    sc.close();
	}

	public static void run() {
		
		SparkConf conf = new SparkConf().setAppName("Linear Regression for Formality").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
	    // Load and parse the data
	    String path = "data/lpsa.data";
	    JavaRDD<String> data = sc.textFile(path);
	    JavaRDD<LabeledPoint> parsedData = data.map(
	      new Function<String, LabeledPoint>() {

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
	    
	 // Evaluate model on training examples and compute training error
	    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
	      new Function<LabeledPoint, Tuple2<Double, Double>>() {

			public Tuple2<Double, Double> call(LabeledPoint point) {
	          double prediction = model.predict(point.features());
	          return new Tuple2<Double, Double>(prediction, point.label());
	        }
	      }
	    );
	    
	    double MSE = new JavaDoubleRDD(valuesAndPreds.map(
	    	      new Function<Tuple2<Double, Double>, Object>() {

					public Object call(Tuple2<Double, Double> pair) {
	    	          return Math.pow(pair._1() - pair._2(), 2.0);
	    	        }
	    	      }
	    	    ).rdd()).mean();
	    System.out.println("training Mean Squared Error = " + MSE);
	    
	    sc.close();
	}
	
}
