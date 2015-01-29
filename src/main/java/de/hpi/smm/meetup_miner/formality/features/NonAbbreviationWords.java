package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class NonAbbreviationWords extends Feature{
	
	private String[] targetWords = {"for example","that is","and so on","okay","january","february","march","april","may","june","july","august","september","october","november","december","saturday","sunday","monday","tuesday","wednesday","thursday","friday","airplane","laboratory","as soon as possible","united states of america","tonnes","undergraduate","graduate","human resources","professor","artificial intelligence","your","and","about","might","should","which","would","yours","second","third","account","advertisement","afternoon","ante meridiem","anti-catholic","archaeological","association","british museum","breakfast","commander of the order","century","chapter","chapters","department","edition","english","house of lords","history","hours","inches","lord","lady","loving","maximum","memoires","minutes","physical","probably","received","secretary","temperature","to equal","transactions","university","volume","christmas","christian","yesterday","years","academic","administration","bible"};
	
	public NonAbbreviationWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
