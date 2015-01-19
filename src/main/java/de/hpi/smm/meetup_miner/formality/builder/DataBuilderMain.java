package de.hpi.smm.meetup_miner.formality.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hpi.smm.meetup_miner.formality.features.AbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.ContractionWords;
import de.hpi.smm.meetup_miner.formality.features.Feature;
import de.hpi.smm.meetup_miner.formality.features.FormalContentWords;
import de.hpi.smm.meetup_miner.formality.features.FormalWords;
import de.hpi.smm.meetup_miner.formality.features.InformalContentWords;
import de.hpi.smm.meetup_miner.formality.features.InformalWords;
import de.hpi.smm.meetup_miner.formality.features.NonAbbreviationWords;
import de.hpi.smm.meetup_miner.formality.features.NonContractionWords;

public class DataBuilderMain {

	public static void main(String[] args) throws IOException {

		String description = "Please bring a mat if you have one. Dress comfortably for movement. Drawing from multiple faith traditions, yoga has evolved across the ages as a means of tuning the body for prayer and meditation. Join us as we explore the multiple spiritual and physical benefits of yoga practice while explicitly integrating prayers and spiritual themes of our Catholic faith. Typical sessions include an opening prayer, inspired movement & strengthening, and contemplative prayer to close. The program focuses on various themes to coincide with the liturgical calendar (weekly Sunday Scripture Readings in the Catholic church) and progression of our faith life across the seasons.  Prior yoga experience is not necessary. Participants RSVP via email and MeetUp, so others may be attending who are not indicated through this group. Instructors Dina Wolf and Ali Niederkorn are both certified yoga instructors and have been teaching within the community in Chicago for the past four years.";
		
		List<Feature> features = new ArrayList<Feature>();
		features.add(new AbbreviationWords());
		features.add(new FormalContentWords());
		features.add(new InformalContentWords());
		features.add(new ContractionWords());
		features.add(new FormalWords());
		features.add(new InformalWords());
		features.add(new NonAbbreviationWords());
		features.add(new NonContractionWords());
		
		System.out.println(DataBuilder.createFeatureDataFromDescription(description, features));
	}

}
