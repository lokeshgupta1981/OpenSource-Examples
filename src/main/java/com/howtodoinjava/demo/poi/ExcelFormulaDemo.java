package com.howtodoinjava.demo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import com.howtodoinjava.demo.jackson.Jackson2Demo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFormulaDemo
{
	static URL url = ExcelFormulaDemo.class.getClassLoader().getResource("formulaDemo.xlsx");
	public static void main(String[] args) 
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("Calculate Simple Interest");
	 
	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("Pricipal");
	    header.createCell(1).setCellValue("RoI");
	    header.createCell(2).setCellValue("Time");
	    header.createCell(3).setCellValue("Interest (P r t)");
	     
	    Row dataRow = sheet.createRow(1);
	    dataRow.createCell(0).setCellValue(14500d);
	    dataRow.createCell(1).setCellValue(9.25);
	    dataRow.createCell(2).setCellValue(3d);
	    dataRow.createCell(3).setCellFormula("A2*B2*C2");
	     
	    try {
				URL url = Jackson2Demo.class.getClassLoader().getResource("formulaDemo.xlsx");
	        FileOutputStream out =  new FileOutputStream(url.getFile());
	        workbook.write(out);
	        out.close();
	        System.out.println("Excel written successfully..");
	        
	        readSheetWithFormula();
	         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void readSheetWithFormula()
	{
		try
		{
			FileInputStream file = new FileInputStream(url.getFile());

			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			
			//Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			//Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) 
			{
				Row row = rowIterator.next();
				//For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while (cellIterator.hasNext()) 
				{
					Cell cell = cellIterator.next();
					//If it is formula cell, it will be evaluated otherwise no change will happen
					switch (evaluator.evaluateInCell(cell).getCellType()) 
					{

						case NUMERIC:
							System.out.print(cell.getNumericCellValue() + "\t\t");
							break;
						case STRING:
							System.out.print(cell.getStringCellValue() + "\t\t");
							break;
						case FORMULA:
							//Not again
							break;
					}
				}
				System.out.println("");
			}
			file.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
