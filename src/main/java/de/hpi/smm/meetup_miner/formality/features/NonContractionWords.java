package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class NonContractionWords extends Feature{
	
	private String[] targetWords = {"am not","are not","cannot","could not","did not","does not","do not","had not","has not","have not","he had","he would","he shall","he will","he is","he has","i would","i had","i shall","i will","i am","i have","is not","let us","might not","must not","shall not","she had","she would","she will","she shall","she is","she has","should not","that is","that has","there is","there has","they would","they had","they will","they shall","they are","they have","we would","we had","we are","we have","were not","what shall","what will","what are","what is","what has","what have","where has","where is","who had","who would","who will","who shall","who are","who has","who is","who have","will not","would not","would have","you had","you would","you will","you shall","you are","you have","that will","it is","we will","we shall","it would","it had"};
	
	public NonContractionWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
