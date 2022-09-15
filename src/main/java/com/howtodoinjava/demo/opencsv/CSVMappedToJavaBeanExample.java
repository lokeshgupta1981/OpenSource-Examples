package com.howtodoinjava.demo.opencsv;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

public class CSVMappedToJavaBeanExample {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static void main(String[] args) throws Exception {

    URL fileUrl = CSVMappedToJavaBeanExample.class.getClassLoader().getResource("data.csv");
    CSVReader csvReader = new CSVReader(new FileReader(fileUrl.getFile()));

    CsvToBean csv = new CsvToBean();
    csv.setCsvReader(csvReader);
    csv.setMappingStrategy(setColumMapping());

    List list = csv.parse();

    for (Object object : list) {
      Employee employee = (Employee) object;
      System.out.println(employee);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static ColumnPositionMappingStrategy setColumMapping() {
    ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
    strategy.setType(Employee.class);
    String[] columns = new String[]{"id", "firstName", "lastName", "country", "age"};
    strategy.setColumnMapping(columns);
    return strategy;
  }
}
