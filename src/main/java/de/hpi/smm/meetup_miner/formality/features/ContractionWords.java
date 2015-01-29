package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class ContractionWords extends Feature{
	
	private String[] targetWords = {"ain't","aren't","can't","couldn't","didn't","doesn't","don't","hadn't","hasn't","haven't","he'd","he'll","he's","i'd","i'll","i'm","i've","isn't","let's","mightn't","mustn't","shan't","she'd","she'll","she's","shouldn't","that's","there's","they'd","they'll","they're","they've","we'd","we're","we've","weren't","what'u","what'll","what're","what's","what've","where's","who's","who'll","who're","who've","won't","wouldn't","would've","you'd","you'll","you're","you've","that'll","it's","we'll","it'd"};
	
	public ContractionWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
