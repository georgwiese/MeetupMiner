package de.hpi.smm.meetup_miner.formality.features;

import java.util.Arrays;

public class FormalContentWords extends Feature{
	
	private String[] targetWords = {"socialising","workshop","registration","payment","fee","charge","business","donation","donate","admission","museum","coach","schedule","street action","discuss","ticket","dress code","entrepreneur","badge","session","course","insight","instruction","program","technique","seminar","scripture","dress","intelligent","attire","constructive feedback","business card","presentation","class","teacher","technically","approval","political","lecture","demonstration","leader","professional","development","services are led","bible","guideline","certified","program"};
	
	public FormalContentWords(){
		this.targetWordList = Arrays.asList(this.targetWords);
	}
}
