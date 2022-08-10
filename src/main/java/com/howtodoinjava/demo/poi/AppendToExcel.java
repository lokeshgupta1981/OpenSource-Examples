package com.howtodoinjava.demo.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class AppendToExcel {
  public static void main(String[] args) {

    File file = new File("C:\\temp\\data.xlsx");

    List<BusinessEntity> recordsToWrite = List.of(
        new BusinessEntity("Charles", "Babej", 60),
        new BusinessEntity("John", "Doe", 70),
        new BusinessEntity("Loreum", "Ipsum", 80)
    );

    try {
      appendRows(recordsToWrite, file);
    } catch (IOException | InvalidFormatException e) {
      e.printStackTrace();
    }
  }

  private static Map<Integer, Object[]> prepareData(int rowNum,
                                                    List<BusinessEntity> recordsToWrite) {
    Map<Integer, Object[]> data = new HashMap<>();
    for (BusinessEntity entity : recordsToWrite) {
      rowNum++;
      data.put(rowNum, new Object[]{rowNum, entity.getFirstName(),
          entity.getLastName(), entity.getAge()});
    }
    return data;
  }

  public static void appendRows(List<BusinessEntity> recordsToWrite, File file)
      throws IOException, InvalidFormatException {

    XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
    Sheet sheet = workbook.getSheetAt(0);
    int rowNum = sheet.getLastRowNum() + 1;

    Map<Integer, Object[]> data = prepareData(rowNum, recordsToWrite);

    Set<Integer> keySet = data.keySet();
    for (Integer key : keySet) {
      Row row = sheet.createRow(rowNum++);
      Object[] objArr = data.get(key);
      int cellNum = 0;
      for (Object obj : objArr) {
        Cell cell = row.createCell(cellNum++);
        if (obj instanceof String)
          cell.setCellValue((String) obj);
        else if (obj instanceof Integer)
          cell.setCellValue((Integer) obj);
      }
    }
    try {
      FileOutputStream out = new FileOutputStream(file);
      workbook.write(out);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
