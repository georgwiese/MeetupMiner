package de.hpi.smm.meetup_miner.formality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.impl.set.mutable.UnifiedSet;

import de.hpi.smm.meetup_miner.formality.builder.DataBuilder;
import de.hpi.smm.meetup_miner.formality.features.*;

public class BestFeatureSetFinder {

	public static void main(String[] args) throws IOException {

		File file = new File("data/PowerSet.txt");
		
		if (!file.exists()) file.createNewFile();
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		MutableSet<Integer> powerSet = UnifiedSet.newSetWith(1);
		powerSet.powerSet();
		
		for (UnsortedSetIterable<Integer> set: powerSet.powerSet()){
			
			if(set.isEmpty()) continue;
			
			String buffStr = set.toString() + ":";
			List<Feature> features = new ArrayList<Feature>();
			
			Iterator<Integer> iterator = set.iterator();
			while(iterator.hasNext()) {
				Integer curInt = iterator.next();
				switch(curInt){
				case 1: 
					features.add(new FormalWords());
					break;
				case 2:
					features.add(new InformalWords());
					break;
				case 3:
					features.add(new ContractionWords());
					break;
				case 4:
					features.add(new NonContractionWords());
					break;
				case 5:
					features.add(new AbbreviationWords());
					break;
				case 6:
					features.add(new NonAbbreviationWords());
					break;
				case 7:
					features.add(new FormalContentWords());
					break;
				case 8:
					features.add(new InformalContentWords());
					break;
				default:
						break;
				}
			}//end while
			
			DataBuilder.writeDataFile(features);
			
			LinearRegression.train("data/Formality_Data.data", 2000);
			double MSE = LinearRegression.test();
			buffStr += MSE + "\n";
			
			bw.write(buffStr);
		}//end for
		
		bw.close();
	}
	
	
		
}
