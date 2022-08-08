package com.howtodoinjava.demo.poi.saxparser;

public class ExcelReaderHandler extends SheetHandler {

  @Override
  protected boolean processSheet(String sheetName) {
    //Decide which sheets to read; Return true for all sheets
    //return "Sheet 1".equals(sheetName);
    System.out.println("Processing start for sheet : " + sheetName);
    return true;
  }

  @Override
  protected void startSheet() {
    //Any custom logic when a new sheet starts
    System.out.println("Sheet starts");
  }

  @Override
  protected void endSheet() {
    //Any custom logic when sheet ends
    System.out.println("Sheet ends");
  }

  @Override
  protected void processRow() {
    if(rowNumber == 1 && !header.isEmpty()) {
      System.out.println("The header values are at line no. " + rowNumber + " " +
          "are :" + header);
    }
    else if (rowNumber > 1 && !rowValues.isEmpty()) {

      //Get specific values here
      /*String a = rowValues.get("A");
      String b = rowValues.get("B");*/

      //Print whole row
      System.out.println("The row values are at line no. " + rowNumber + " are :" + rowValues);
    }
  }
}
