package com.howtodoinjava.demo.webscrapper;

public class UrlRecord {

    private Integer rownum;
    private String rootUrl;
    private String visitedUrl;
    private String title;

    public Integer getRownum() {
        return rownum;
    }

    public void setRownum(Integer rownum) {
        this.rownum = rownum;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getVisitedUrl() {
        return visitedUrl;
    }

    public void setVisitedUrl(String visitedUrl) {
        this.visitedUrl = visitedUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UrlRecord{" +
            "rownum=" + rownum +
            ", rootUrl='" + rootUrl + '\'' +
            ", visitedUrl='" + visitedUrl + '\'' +
            ", title='" + title + '\'' +
            '}';
    }
}
