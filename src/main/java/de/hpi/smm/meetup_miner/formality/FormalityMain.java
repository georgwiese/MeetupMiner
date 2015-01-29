package de.hpi.smm.meetup_miner.formality;

import java.io.IOException;

public class FormalityMain {

	public static void main(String[] args) throws IOException {
		
		/** usage example **/
		
		/** train and save the model **/
		LinearRegression.train("data/Formality_Data.data", 2000);
		LinearRegression.saveModel("data/model");
		
		/** load the model and predict **/
		String description = "This is a group meet up - hosted by JustGiving - but for anyone in the tech, charity or agency scene with the emphasis on best practice sharing. Each meet up we'll come together around a different theme, and we'll try to arrange for guest speakers to \"share\" their insights, their experiences, their painful learnings! That way we can all be better organisations as a result. This is not a charity exclusive meet up - far from it. JustGiving will provide the space, the food, the beer, others can provide the intellect!";
		LinearRegression.loadModel("data/model");
		double predictedFormality = LinearRegression.predict(description);
		System.out.println(predictedFormality);
	}

}
