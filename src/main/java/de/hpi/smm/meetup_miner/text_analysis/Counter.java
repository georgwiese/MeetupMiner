package de.hpi.smm.meetup_miner.text_analysis;

public class Counter {

	public static int countWords(String string){
		if (string == null)
			return 0;
		String[] countingWords = string.split(" ");
		return countingWords.length;
	}
	
	public static int countChars(String string){
		int counter = 0;
	    for (int i = 0; i < string.length(); i++) {
	      if (Character.isLetter(string.charAt(i)))
	        counter++;
	    }
	    return counter;
	}
	
	public static int countWords(Iterable<String> strings) {
		int result = 0;
		for (String item : strings) {
			result += countWords(item);
		}
		return result;
	}
	
	public static int countChars(Iterable<String> strings) {
		int result = 0;
		for (String item : strings) {
			result += countChars(item);
		}
		return result;
	}
	
}
