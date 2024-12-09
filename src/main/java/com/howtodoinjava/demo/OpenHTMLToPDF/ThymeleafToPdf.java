package com.howtodoinjava.demo.OpenHTMLToPDF;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafToPdf {

  public static void main(String[] args) {
    try {

      String htmlContent = getHtmlContent();

      // Step 4: Generate PDF from HTML
      Path pdfOutput = Path.of("C:\\temp\\htmltopdf\\invoice-thymeleaf.pdf");
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

  private static String getHtmlContent() {

    // Step 1: Resolve the template
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);

    // Step 2: Create Thymeleaf Context and add values
    Context context = new Context();
    context.setVariable("name", "John Doe");
    context.setVariable("amount", "$1,200");
    context.setVariable("date", "2024-12-05");

    // Step 3: Generate HTML content
    String htmlContent = templateEngine.process("invoice", context);
    return htmlContent;
  }
}
