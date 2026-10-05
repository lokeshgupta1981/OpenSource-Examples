package com.howtodoinjava.demo.poi;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.InputStream;

/**
 * Reads the sheet written by LargeExcelWriteDemo either with the XSSF event API
 * (row by row, small memory) or with WorkbookFactory (whole file in memory).
 *
 * Usage: LargeExcelReadDemo [event|usermodel] [file]
 */
public class LargeExcelReadDemo {

  public static void main(String[] args) throws Exception {
    String mode = args.length > 0 ? args[0] : "event";
    File file = new File(args.length > 1 ? args[1] : "large-sxssf.xlsx");

    long start = System.nanoTime();
    long[] result = mode.equals("usermodel") ? readWithUserModel(file) : readWithEventApi(file);
    long millis = (System.nanoTime() - start) / 1_000_000;
    System.out.printf("%s: %,d data rows, total quantity %,d, in %,d ms%n",
        mode, result[0], result[1], millis);
  }

  static long[] readWithEventApi(File file) throws Exception {
    long[] result = new long[2];   // [rows, quantity sum]

    SheetContentsHandler handler = new SheetContentsHandler() {
      @Override
      public void startRow(int rowNum) {
      }

      @Override
      public void endRow(int rowNum) {
        if (rowNum > 0) {
          result[0]++;
        }
      }

      @Override
      public void cell(String cellReference, String formattedValue,
                       XSSFComment comment) {
        // cellReference is "C2", "C3", ...; skip the header row
        if (cellReference.startsWith("C") && !cellReference.equals("C1")) {
          result[1] += Long.parseLong(formattedValue);
        }
      }
    };

    try (OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ)) {
      XSSFReader reader = new XSSFReader(pkg);
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
      StylesTable styles = reader.getStylesTable();

      XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
      while (sheets.hasNext()) {
        try (InputStream sheet = sheets.next()) {
          XMLReader parser = XMLHelper.newXMLReader();
          parser.setContentHandler(new XSSFSheetXMLHandler(
              styles, strings, handler, new DataFormatter(), false));
          parser.parse(new InputSource(sheet));
        }
      }
    }
    return result;
  }

  static long[] readWithUserModel(File file) throws Exception {
    long rows = 0;
    long quantity = 0;
    try (Workbook workbook = WorkbookFactory.create(file, null, true)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          continue;
        }
        rows++;
        quantity += (long) row.getCell(2).getNumericCellValue();
      }
    }
    return new long[]{rows, quantity};
  }
}
