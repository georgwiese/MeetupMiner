package de.hpi.smm.meetup_miner.formality.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import de.hpi.smm.meetup_miner.formality.features.Feature;

public class DataBuilder {
	
	public static void writeDataFile(List<Feature> features) throws IOException{
		
		File writeFile = new File("data/Formality_Data.data");
		
		if (writeFile.exists()) {
			if(writeFile.delete()) System.out.println(writeFile.getName() + " is deleted");
			else System.out.println("Delete operation is failed.");
		}
		writeFile.createNewFile();

		FileWriter fw = new FileWriter(writeFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		File myFile = new File("data/Annoate_Formality.xlsx");
		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Iterator<Row> rowIterator = mySheet.iterator();
		
		int rowNum = 0;
		while (rowIterator.hasNext()) {

			rowNum++;
			Row row = rowIterator.next();
			if(rowNum == 1) continue;
			
			String testStr = createDataFormatForCurrentRow(row, features);
			bw.write(testStr);
		}
		
		myWorkBook.close();
		bw.close();
	}
	
	private static String createDataFormatForCurrentRow(Row row, List<Feature> features){
		
		String rowData = "";
		String description = "";
		double annoatedFormality = 0d;
		
		for(int cellNum = 3 ; cellNum >0 ; cellNum--){
			Cell cell = row.getCell(cellNum);
			
			switch (cell.getCellType()) {
	            case Cell.CELL_TYPE_STRING:
	            	description += " " + cell.getStringCellValue().toLowerCase().trim();
	                break;
	            case Cell.CELL_TYPE_NUMERIC:
	            	annoatedFormality = cell.getNumericCellValue();
	                break;
	            default :
	            }
		}//end for
		
		rowData = annoatedFormality + ",";					
		int featureIndex = 0;
		
		for(Feature feature : features){
			rowData += (featureIndex == 0) ? "" + feature.getFeatureValue(description, false) : " " + feature.getFeatureValue(description, false);   
			featureIndex++;
		}
		
		return rowData + "\n";
	}
	
	public static String createFeatureDataFromDescription(String description, List<Feature> features) {
		
		String featureData = "";
		int featureIndex = 0;
		
		for(Feature feature : features){
			featureData += (featureIndex == 0) ? "" + feature.getFeatureValue(description, false) : " " + feature.getFeatureValue(description, false);   
			featureIndex++;
		}
		
		return featureData;
	}
	
	public static LabeledPoint createLabeledPoint(String featureData){
		
		LabeledPoint labeledPoint = null;
		
        String[] features = featureData.split(" ");
        double[] v = new double[features.length];
        for (int i = 0; i < features.length - 1; i++) v[i] = Double.parseDouble(features[i]);
        labeledPoint =  new LabeledPoint(-1, Vectors.dense(v));
		
		return labeledPoint;
	}

}
