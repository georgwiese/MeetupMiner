package de.hpi.smm.meetup_miner.formality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHandler {
	
	private static ArrayList<String> getContentWords() throws IOException{
		
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
	
	public static void writeDataFile() throws IOException{
		
		File writeFile = new File("data/Formality_Data.data");
		if (!writeFile.exists()) {
			writeFile.createNewFile();
		}
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
			
			String testStr = createDataFormatForCurrentRow(row);
			bw.write(testStr);
			
		}
		
		myWorkBook.close();
		bw.close();
	}
	
	private static String createDataFormatForCurrentRow(Row row){
		
		for(int cellNum = 1 ; cellNum < 5 ; cellNum++){
			Cell cell = row.getCell(cellNum);
			
			switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                //System.out.print(cell.getStringCellValue() + "\t");
                //todo
                break;
            case Cell.CELL_TYPE_NUMERIC:
                //System.out.print(cell.getNumericCellValue() + "\t");
                //todo
                break;
            default :
         
            }
		}//end for
		
		return "";
	}
	
	public static void main(String[] args) throws IOException {
		
		writeDataFile();
	}


	
}
