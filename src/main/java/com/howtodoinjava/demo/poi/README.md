Source code for the article https://howtodoinjava.com/java/library/readingwriting-excel-files-in-java-poi-tutorial/ and the related Apache POI tutorials below.

# Related Tutorials

- [Read and Write Excel File in Java](https://howtodoinjava.com/java/library/readingwriting-excel-files-in-java-poi-tutorial/)
- [Read Excel File with SAX Parser](https://howtodoinjava.com/java/library/poi-read-excel-with-sax-parser/)
- [Appending Rows to Excel](https://howtodoinjava.com/java/library/poi-append-rows-to-excel/)

# Versions

- Apache POI 5.5.1 (`poi-ooxml`, which brings `poi`, `commons-io` 2.21.0 and `log4j-api` 2.24.3)
- Java 25 (the code also compiles with the repo's Java 21 setting)

# Programs

| Class | What it shows |
|---|---|
| `PoiQuickStart` | Write a one-row .xlsx file and read it back |
| `WriteExcelDemo` | Write the "Employee Data" sheet to `howtodoinjava_demo.xlsx` |
| `ReadExcelDemo` | Read `howtodoinjava_demo.xlsx` by cell type and with `DataFormatter` (run `WriteExcelDemo` first) |
| `StyledReportDemo` | Styles, number and date formats, formulas, auto-sized columns, freeze panes, auto filter |
| `ExcelFormulaDemo` | Add a formula cell and evaluate it with `FormulaEvaluator` |
| `ExcelStylingDemo` | Conditional formatting rules |
| `LargeExcelWriteDemo` | Write 1,000,000 rows with `SXSSFWorkbook` (or `XSSFWorkbook` for comparison) |
| `LargeExcelReadDemo` | Read the large file with the XSSF event API (or `WorkbookFactory` for comparison) |
| `ExcelErrorsDemo` | Reproduce common POI exceptions and their fixes |

All programs write their files to the working directory.

# Run

```bash
mvn -q compile
mvn -q exec:java -Dexec.mainClass=com.howtodoinjava.demo.poi.StyledReportDemo
```

To compare memory use for large files, run with a small heap:

```bash
mvn -q dependency:build-classpath -Dmdep.outputFile=cp.txt
java -Xmx64m -cp target/classes:$(cat cp.txt) com.howtodoinjava.demo.poi.LargeExcelWriteDemo sxssf 1000000
java -Xmx64m -cp target/classes:$(cat cp.txt) com.howtodoinjava.demo.poi.LargeExcelReadDemo event large-sxssf.xlsx
```
