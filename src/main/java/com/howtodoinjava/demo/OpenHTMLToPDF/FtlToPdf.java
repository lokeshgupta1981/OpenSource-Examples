package com.howtodoinjava.demo.OpenHTMLToPDF;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FtlToPdf {

  public static void main(String[] args) {
    try {
      // Step 1: Configure FreeMarker
      Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
      cfg.setClassForTemplateLoading(FtlToPdf.class, "/templates");
      cfg.setDefaultEncoding("UTF-8");

      // Step 2: Load the template
      Template template = cfg.getTemplate("invoice.ftl");

      // Step 3: Prepare data model
      Map<String, Object> dataModel = new HashMap<>();
      dataModel.put("name", "John Doe");
      dataModel.put("amount", "$1,200");
      dataModel.put("date", "2024-12-05");

      // Step 4: Merge template with data model
      StringWriter stringWriter = new StringWriter();
      template.process(dataModel, stringWriter);
      String htmlContent = stringWriter.toString();

      // Step 5: Generate PDF
      Path pdfOutput = Path.of("C:\\temp\\htmltopdf\\invoice.pdf");
      try (OutputStream os = Files.newOutputStream(pdfOutput)) {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(htmlContent, null);
        builder.toStream(os);
        builder.run();
      }

      System.out.println("PDF generated successfully at: " + pdfOutput.toAbsolutePath());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
