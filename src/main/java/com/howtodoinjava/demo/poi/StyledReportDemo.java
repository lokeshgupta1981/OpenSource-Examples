package com.howtodoinjava.demo.poi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * Writes a styled grocery report with typed cells, number and date formats,
 * formulas, auto-sized columns, a frozen header row and an auto filter.
 * Then reads it back with DataFormatter and FormulaEvaluator.
 */
public class StyledReportDemo {

  record Purchase(String item, int quantity, double unitPrice, LocalDate purchasedOn) {
  }

  public static void main(String[] args) throws IOException {
    Path file = Path.of("groceries.xlsx");

    List<Purchase> purchases = List.of(
        new Purchase("apple", 5, 1.20, LocalDate.of(2026, 10, 1)),
        new Purchase("banana", 3, 0.50, LocalDate.of(2026, 10, 2)),
        new Purchase("cherry", 12, 0.25, LocalDate.of(2026, 10, 3)));

    write(file, purchases);
    read(file);
  }

  static void write(Path file, List<Purchase> purchases) throws IOException {
    try (Workbook workbook = new XSSFWorkbook();
         OutputStream out = Files.newOutputStream(file)) {

      Sheet sheet = workbook.createSheet("Groceries");
      CreationHelper helper = workbook.getCreationHelper();

      // 1. Create styles once per workbook, not once per cell
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.WHITE.getIndex());

      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setBorderBottom(BorderStyle.THIN);

      CellStyle moneyStyle = workbook.createCellStyle();
      moneyStyle.setDataFormat(helper.createDataFormat().getFormat("#,##0.00"));

      CellStyle dateStyle = workbook.createCellStyle();
      dateStyle.setDataFormat(helper.createDataFormat().getFormat("yyyy-mm-dd"));

      // 2. Header row
      String[] headers = {"Item", "Quantity", "Unit Price", "Purchased On", "Total"};
      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // 3. Data rows: string, numeric, date and formula cells
      int rowNum = 1;
      for (Purchase purchase : purchases) {
        Row row = sheet.createRow(rowNum);
        int excelRow = rowNum + 1;    // formulas use 1-based row numbers

        row.createCell(0).setCellValue(purchase.item());
        row.createCell(1).setCellValue(purchase.quantity());

        Cell price = row.createCell(2);
        price.setCellValue(purchase.unitPrice());
        price.setCellStyle(moneyStyle);

        Cell date = row.createCell(3);
        date.setCellValue(purchase.purchasedOn());
        date.setCellStyle(dateStyle);

        Cell total = row.createCell(4);
        total.setCellFormula("B" + excelRow + "*C" + excelRow);
        total.setCellStyle(moneyStyle);
        rowNum++;
      }

      // 4. Grand total row
      Row sumRow = sheet.createRow(rowNum);
      sumRow.createCell(3).setCellValue("Grand Total");
      Cell grandTotal = sumRow.createCell(4);
      grandTotal.setCellFormula("SUM(E2:E" + rowNum + ")");
      grandTotal.setCellStyle(moneyStyle);

      // 5. Layout: freeze the header row, add a filter, fit the columns
      sheet.createFreezePane(0, 1);
      sheet.setAutoFilter(new CellRangeAddress(0, rowNum - 1, 0, headers.length - 1));
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }

      // 6. Store formula results in the file for readers that do not calculate
      workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

      workbook.write(out);
    }
    System.out.println(file + " written");
  }

  static void read(Path file) throws IOException {
    try (Workbook workbook = WorkbookFactory.create(file.toFile(), null, true)) {
      FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
      DataFormatter formatter = new DataFormatter();
      Sheet sheet = workbook.getSheetAt(0);

      for (Row row : sheet) {
        StringBuilder line = new StringBuilder();
        // getCell() returns null for a missing cell, and formatCellValue(null) returns ""
        for (int col = 0; col < row.getLastCellNum(); col++) {
          Cell cell = row.getCell(col);
          line.append(String.format("%-14s", formatter.formatCellValue(cell, evaluator)));
        }
        System.out.println(line.toString().stripTrailing());
      }

      Row apple = sheet.getRow(1);
      LocalDate purchasedOn = apple.getCell(3).getLocalDateTimeCellValue().toLocalDate();
      String formula = apple.getCell(4).getCellFormula();
      double total = evaluator.evaluate(apple.getCell(4)).getNumberValue();
      System.out.println("purchasedOn = " + purchasedOn + ", formula = " + formula + ", total = " + total);
    }
  }
}
