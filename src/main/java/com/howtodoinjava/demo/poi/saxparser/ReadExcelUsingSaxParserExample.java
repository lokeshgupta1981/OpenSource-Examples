package com.howtodoinjava.demo.poi.saxparser;

import com.howtodoinjava.demo.poi.ReadExcelDemo;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class ReadExcelUsingSaxParserExample {
  public static void main(String[] args) throws Exception {

    URL url = ReadExcelUsingSaxParserExample.class
        .getClassLoader()
        .getResource("howtodoinjava_demo.xlsx");

    new ExcelReaderHandler().readExcelFile(new File(url.getFile()));
  }
}
