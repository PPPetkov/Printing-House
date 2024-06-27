package com.pppetkov.printinghouse.printingpress;
import java.math.BigDecimal;
import java.util.Optional;

import com.pppetkov.printinghouse.issue.Issue;

public class PrintingPress extends Thread{
    private final long serialNum;
    private int pagesPerMinute;
    private int currentPaper;
    private int paperCapacity;
    private PaperType paperType;
    private PaperSize paperSize;
    private BigDecimal paperCosts;
    private boolean isInColor;
    private Optional<Issue> currentIssue;

    private static long next_num = 0;

    public PrintingPress(int paperCapacity, PaperType paperType, PaperSize paperSize, boolean isInColor, int ppm){
        this.paperCapacity = 0;
        if(paperCapacity > 0){ this.paperCapacity = paperCapacity; }
        this.paperType = paperType;
        this.paperSize = paperSize;
        this.isInColor = isInColor;
        this.pagesPerMinute = Math.max(60, ppm);

        this.paperCosts = BigDecimal.ZERO;
        this.currentPaper = 0;
        this.currentIssue = Optional.empty();
        this.serialNum = next_num;
        ++next_num;
    }

    public long getSerialNumber() { return this.serialNum; }
    public int getPaperCapacity() { return this.paperCapacity; }
    public int getCurrentPaper() { return this.currentPaper; }
    public PaperType getPaperType() { return this.paperType; }
    public PaperSize getPaperSize() { return this.paperSize; }
    public boolean isInColor() { return this.isInColor; }
    public BigDecimal getPaperCosts(){ return this.paperCosts; }
    public boolean isCurrentlyPrinting(){ return !this.currentIssue.isEmpty(); }

    public void loadIssue(Issue issue){ this.currentIssue = Optional.of(issue); }

    public void addPaper(int amount){
        if(amount > 0){
            if(this.currentPaper + amount > this.paperCapacity){
                amount = this.paperCapacity - this.currentPaper;
            }
            this.currentPaper += amount;
            this.paperCosts = this.paperCosts.add(BigDecimal.valueOf(amount*this.paperSize.getValue()*this.paperType.getValue()));
        }
    }

    public void run(){
        if(this.currentIssue.isPresent()){
            Issue issue = this.currentIssue.get();
            int pps = Math.round(this.pagesPerMinute/60);
            for(int i = 0; i < issue.getPageCount(); i += pps){
                System.out.println("Printing press " + this.serialNum + " is printing " + issue.getTitle() + ". Currently on page " + i + " out of " + issue.getPageCount());
                try {
                    sleep(30);
                } catch (InterruptedException e) {}
            }
            System.out.println("Printing press " + this.serialNum + " has finished printing " + issue.getTitle());
            this.currentPaper -= issue.getPageCount();
            this.addPaper(issue.getPageCount());
            this.currentIssue = Optional.empty();
        }
    }
}

