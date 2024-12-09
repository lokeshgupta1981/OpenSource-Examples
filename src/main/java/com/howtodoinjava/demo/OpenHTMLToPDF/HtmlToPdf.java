package com.howtodoinjava.demo.OpenHTMLToPDF;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HtmlToPdf {

  public static void main(String[] args) throws Exception {

    File template = new File(
      HtmlToPdf.class.getClassLoader().getResource("templates/report_in.html").toURI());

    try (OutputStream os = new FileOutputStream("C:\\temp\\htmltopdf\\report_out.pdf")) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withFile(template);
      builder.toStream(os);
      builder.run();
    }
  }
}
