package com.pppetkov.printinghouse.issue;
import com.pppetkov.printinghouse.printingpress.PaperSize;

public class Issue{
    private String title;
    private int pageCount;
    private PaperSize pageSize;

    public Issue(String title, int pageCount, PaperSize pageSize){
        this.title = title;
        this.pageSize = pageSize;
        this.pageCount = 1;
        if(pageCount > 0){
            this.pageCount = pageCount;
        }
    }

    public String getTitle(){ return this.title; }
    public int getPageCount(){ return this.pageCount; }
    public PaperSize getPageSize(){ return this.pageSize; }
    
    public String toString(){
        return title + " " + String.valueOf(this.pageCount) + " " + this.pageSize.toString();
    }
}