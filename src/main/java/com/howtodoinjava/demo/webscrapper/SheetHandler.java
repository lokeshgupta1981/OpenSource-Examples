package com.howtodoinjava.demo.webscrapper;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SheetHandler extends DefaultHandler {
    protected String sheetName;
    protected int sheetNumber = 0;
    protected Map<String, String> header = new HashMap();
    protected Map<String, String> rowValues = new HashMap();
    protected StylesTable stylesTable;
    protected Map<String, CTXf> rowStyles = new HashMap();
    protected long rowNumber = 0;
    protected String cellId;
    private SharedStringsTable sharedStringsTable;
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
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // Clear contents cache
        contents = "";
        // element row represents Row
        if (name.equals("row")) {
            String rowNumStr = attributes.getValue("r");
            //System.out.println("Row# " + rowNumStr);
            rowNumber = Long.parseLong(rowNumStr);
        }
        // element c represents Cell
        else if (name.equals("c")) {
            cellId = getColumnId(attributes.getValue("r"));
            // attribute r represents the cell reference
            //System.out.print(attributes.getValue("r") + " - ");
            // attribute t represents the cell type
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                // cell type s means value will be extracted from SharedStringsTable
                fromSST = true;
            }
            String cellStyleStr = attributes.getValue("s");
            if (cellStyleStr != null) {
                int cellStyleInt = Integer.parseInt(cellStyleStr);
                CTXf cellStyle = stylesTable.getCellXfAt(cellStyleInt);
                rowStyles.put(cellId, cellStyle);
            }
            // element v represents value of Cell
        } else if (name.equals("v")) {
            isCellValue = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isCellValue) {
            contents += new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (isCellValue && fromSST) {
            int index = Integer.parseInt(contents);
            contents = new XSSFRichTextString(sharedStringsTable.getItemAt(index).getString()).toString();
            //System.out.format("%d %s %s\n", rowNumber, cellId, contents);
            rowValues.put(cellId, contents);
            cellId = null;
            isCellValue = false;
            fromSST = false;
        } else if (isCellValue) {
            rowValues.put(cellId, contents);
            isCellValue = false;
        } else if (name.equals("row")) {
            if (rowNumber == 1) {
                for (Map.Entry<String, String> row : rowValues.entrySet()) {
                    header.put(row.getValue(), row.getKey());
                }
            }
            try {
                processRow();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rowValues.clear();
        }
    }

    protected boolean processSheet() {
        return true;
    }

    protected void startSheet() {
    }

    protected void endSheet() {
    }

    protected void processRow() throws ExecutionException, InterruptedException {
    }

    public void readExcelFile(String filename) throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        try (OPCPackage opcPackage = OPCPackage.open(filename)) {
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();
            stylesTable = xssfReader.getStylesTable();
            ContentHandler handler = this;
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            if (sheets instanceof XSSFReader.SheetIterator) {
                XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) sheets;

                while (sheetIterator.hasNext()) {

                    try (InputStream sheet = sheetIterator.next()) {
                        sheetName = sheetIterator.getSheetName();
                        sheetNumber++;
                        startSheet();
                        saxParser.parse(sheet, (DefaultHandler) handler);
                        endSheet();
                    }
                }
            }
        }
    }
}