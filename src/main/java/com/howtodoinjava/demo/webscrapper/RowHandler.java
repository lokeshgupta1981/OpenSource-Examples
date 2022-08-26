package com.howtodoinjava.demo.webscrapper;

import com.howtodoinjava.demo.webscrapper.executor.JobSubmitter;
import org.apache.commons.collections4.map.HashedMap;


import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RowHandler extends SheetHandler {

    protected Map<String, String> headerRowMapping = new HashedMap<>();

    @Override
    protected boolean processSheet() {
        return "Sheet 1".equals(sheetName);
    }

    @Override
    protected void startSheet() {
    }

    @Override
    protected void endSheet() {
    }

    @Override
    protected void processRow() throws ExecutionException, InterruptedException {
        if (rowNumber > 1 && !rowValues.isEmpty()) {
            String url = rowValues.get("B");
            if(url != null && !url.trim().equals("")) {
                UrlRecord entity = new UrlRecord();
                entity.setRownum((int) rowNumber);
                entity.setRootUrl(url.trim());
                JobSubmitter.submitTask(entity);
            }
        }
    }
}
