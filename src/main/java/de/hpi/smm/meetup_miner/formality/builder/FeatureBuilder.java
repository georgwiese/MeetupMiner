package de.hpi.smm.meetup_miner.formality.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atteo.evo.inflector.English;

public class FeatureBuilder {
	
	public static ArrayList<String> getFeature(String featureName) throws IOException{
		
		ArrayList<String> targetWords = new ArrayList<String>();
		
		File myFile = new File("data/WordList/Formal_Informal_List.xlsx");
		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = null;
		
		for(int sheetNum = 0 ; sheetNum < myWorkBook.getNumberOfSheets() ; sheetNum++){
			XSSFSheet currentSheet = myWorkBook.getSheetAt(sheetNum);
			if(currentSheet.getSheetName().equals(featureName)) {
				mySheet = currentSheet;
				break;
			}
		}
		
		Iterator<Row> rowIterator = mySheet.iterator();
		while (rowIterator.hasNext()) {
			
			Row row = rowIterator.next();
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				
				Cell cell = cellIterator.next();
				String cellVal = cell.getStringCellValue().toLowerCase().trim();
				if(!targetWords.contains(cellVal)) targetWords.add(cellVal);
				
				String cellValPlural = English.plural(cellVal);
				if(!targetWords.contains(cellValPlural)) targetWords.add(cellValPlural);
			}
		}
		
		myWorkBook.close();
		return targetWords;
	}

	public static ArrayList<String> getContentWords() throws IOException{
		
		ArrayList<String> contentWords = new ArrayList<String>();
		
		File myFile = new File("data/Annoate_Formality.xlsx");
		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Iterator<Row> rowIterator = mySheet.iterator();
		
		int rowNum = 0;
		int colNum;
		
		while (rowIterator.hasNext()) {
			
			colNum = 0;
			rowNum++;
			Row row = rowIterator.next();
			if(rowNum == 1) continue;
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				
				Cell cell = cellIterator.next();
				colNum++;
				if(colNum!=5) continue;
				
				String value = cell.getStringCellValue();
				if(value.isEmpty()) continue;
				
				String[] words = value.split(",");
				for(int index = 0 ; index < words.length ; index++){
					String currentWord = words[index].toLowerCase().trim();
					if(contentWords.contains(currentWord)) continue;
					contentWords.add(currentWord);
				}
			}
		}
		
		myWorkBook.close();
		return contentWords;
	}
	
}
