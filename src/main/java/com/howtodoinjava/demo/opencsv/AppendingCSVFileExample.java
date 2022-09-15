package com.howtodoinjava.demo.opencsv;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.net.URL;

public class AppendingCSVFileExample {

  public static void main(String[] args) throws Exception {
    URL fileUrl = AppendingCSVFileExample.class.getClassLoader().getResource("data.csv");

    CSVWriter writer = new CSVWriter(new FileWriter(fileUrl.getFile(), true));

    //Create record
    String[] record = new String[]{"3","Rahul","Vaidya","India","35"};

    //Write the record to file
    writer.writeNext(record, false);

    //close the writer
    writer.close();
  }
}

