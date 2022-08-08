package com.howtodoinjava.demo.poi.saxparser;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SheetHandler extends DefaultHandler
{
  protected Map<String, String> header = new HashMap<>();
  protected Map<String, String> rowValues = new HashMap<>();
  private SharedStringsTable sharedStringsTable;

  protected long rowNumber = 0;
  protected String cellId;
  private String contents;
  private boolean isCellValue;
  private boolean fromSST;

  protected static String getColumnId(String attribute) throws SAXException {
    for (int i = 0; i < attribute.length(); i++) {
      if (!Character.isAlphabetic(attribute.charAt(i))) {
        return attribute.substring(0, i);
      }
    }
    throw new SAXException("Invalid format " + attribute);
  }

  @Override
  public void startElement(String uri, String localName, String name,
                           Attributes attributes) throws SAXException {
    // Clear contents cache
    contents = "";
    // element row represents Row
    switch (name) {
      case "row" -> {
        String rowNumStr = attributes.getValue("r");
        rowNumber = Long.parseLong(rowNumStr);
      }
      // element c represents Cell
      case "c" -> {
        cellId = getColumnId(attributes.getValue("r"));
        // attribute t represents the cell type
        String cellType = attributes.getValue("t");
        if (cellType != null && cellType.equals("s")) {
          // cell type s means value will be extracted from SharedStringsTable
          fromSST = true;
        }
      }
      // element v represents value of Cell
      case "v" -> isCellValue = true;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (isCellValue) {
      contents += new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String uri, String localName, String name) {
    if (isCellValue && fromSST) {
      int index = Integer.parseInt(contents);
      contents = new XSSFRichTextString(sharedStringsTable.getItemAt(index).getString()).toString();
      rowValues.put(cellId, contents);
      cellId = null;
      isCellValue = false;
      fromSST = false;
    } else if (isCellValue) {
      rowValues.put(cellId, contents);
      isCellValue = false;
    } else if (name.equals("row")) {
      header.clear();
      if (rowNumber == 1) {
        header.putAll(rowValues);
      }
      try {
        processRow();
      } catch (ExecutionException | InterruptedException e) {
        e.printStackTrace();
      }
      rowValues.clear();
    }
  }

  protected boolean processSheet(String sheetName) {
    return true;
  }

  protected void startSheet() {
  }

  protected void endSheet() {
  }

  protected void processRow() throws ExecutionException, InterruptedException {
  }

  public void readExcelFile(File file) throws Exception {

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    try (OPCPackage opcPackage = OPCPackage.open(file)) {
      XSSFReader xssfReader = new XSSFReader(opcPackage);
      sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();

      System.out.println(sharedStringsTable.getUniqueCount());

      Iterator<InputStream> sheets = xssfReader.getSheetsData();

      if (sheets instanceof XSSFReader.SheetIterator sheetIterator) {
        while (sheetIterator.hasNext()) {
          try (InputStream sheet = sheetIterator.next()) {
            String sheetName = sheetIterator.getSheetName();
            if(!processSheet(sheetName)) {
              continue;
            }
            startSheet();
            saxParser.parse(sheet, this);
            endSheet();
          }
        }
      }
    }
  }

}
