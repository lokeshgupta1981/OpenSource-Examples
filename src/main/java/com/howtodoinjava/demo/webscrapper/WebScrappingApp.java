package com.howtodoinjava.demo.webscrapper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class WebScrappingApp {

    public static BlockingQueue<UrlRecord> processedRecords = new LinkedBlockingQueue<UrlRecord>();
    private static Random random = new Random();
    public static int rowCounter = 1;

    public static void main(String[] args) throws Exception {
        //output file
        File outFile = new File("C:\\temp\\webscrapping-output-"+ random.nextLong() +".xlsx");
        Map<Integer, Object[]> columns = new HashMap<Integer, Object[]>();
        columns.put(rowCounter++, new Object[] {"URL", "Title"});
        ReportWriter.createReportFile(columns, outFile);

        //append rows to report
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        es.scheduleAtFixedRate(runAppendRecords(outFile), 10000, 30000, TimeUnit.MILLISECONDS);

        //input file
        RowHandler handler = new RowHandler();
        handler.readExcelFile("C:\\temp\\webscrapping-root-urls.xlsx");
    }

    private static Runnable runAppendRecords(File file) {
        return new Runnable() {
            @Override
            public void run() {
                if(processedRecords.size() > 0) {
                    List<UrlRecord> recordsToWrite = new ArrayList<>();
                    processedRecords.drainTo(recordsToWrite);

                    Map<Integer, Object[]> data = new HashMap<>();
                    for(UrlRecord entity : recordsToWrite) {
                        data.put(rowCounter++, new Object[] {entity.getVisitedUrl(), entity.getTitle()});
                    }
                    System.out.println("###########Writing "+data.size()+" records to excel file############################");
                    try {
                        ReportWriter.appendRows(data, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("===========Nothing to write. Waiting.============================");
                }
            }
        };
    }
}
