package com.pppetkov.printinghouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pppetkov.printinghouse.issue.Issue;
import com.pppetkov.printinghouse.printingpress.PaperType;
import com.pppetkov.printinghouse.printingpress.PrintingPress;
import com.pppetkov.printinghouse.worker.Worker;

public class PrintingHouse {
    private String name;
    private final BigDecimal requiredProfitsForBonus;
    private BigDecimal bonusPct;
    private BigDecimal issueMarkupPct;
    private long requiredIssuesForDiscount;
    private BigDecimal bulkDiscount;
    private BigDecimal workerSalaries; 

    private List<Worker> workers;
    private List<PrintingPress> printingPresses;
    private List<Issue> printedIssues;
    private BigDecimal profits;
    
    public PrintingHouse(String name, BigDecimal salaries, BigDecimal requiredProfits, BigDecimal bonus, BigDecimal markup, long requiredIssues, BigDecimal discount){
        this.name = name;
        this.requiredProfitsForBonus = BigDecimal.ZERO.max(requiredProfits);
        this.bonusPct = BigDecimal.ZERO.max(bonus);
        this.issueMarkupPct = BigDecimal.ZERO.max(markup);
        this.requiredIssuesForDiscount = Math.max(1, requiredIssues);
        this.bulkDiscount = BigDecimal.ZERO.max(discount);
        this.workerSalaries = BigDecimal.ONE.max(salaries);

        this.profits = BigDecimal.ZERO;
        this.workers = new ArrayList<Worker>();
        this.printingPresses = new ArrayList<PrintingPress>();
        this.printedIssues = new ArrayList<Issue>();
    }
    
    private BigDecimal getWorkerCosts(){
        BigDecimal total = BigDecimal.ZERO;
        for (Worker worker : this.workers) {
            total = total.add(worker.getSalary());
        }
        return total;
    }

    private BigDecimal getPaperCosts(){
        BigDecimal total = BigDecimal.ZERO;
        for (PrintingPress pp : this.printingPresses) {
            total = total.add(pp.getPaperCosts());
        }
        return total;
    }

    private void giveBonuses(){
        for(int i = 0; i < this.workers.size(); ++i){
            this.workers.get(i).receiveBonus(this.bonusPct);
        }
    }

    public void hireWorker(Worker worker){
        worker.setSalary(this.workerSalaries);
        this.workers.add(worker);
    }

    public void addPrintingPress(PrintingPress press){
        press.addPaper(press.getPaperCapacity());
        this.printingPresses.add(press);
    }

    public BigDecimal getCosts(){
        return this.getPaperCosts().add(this.getWorkerCosts());
    }

    public void print(Issue issue, Optional<PaperType> paperType){
        boolean isPaperTypePresent = paperType.isPresent();
        for (int i = 0; i < this.printingPresses.size(); i++) {
            PrintingPress pp = this.printingPresses.get(i);
            if(!pp.isCurrentlyPrinting()){
                if(pp.getPaperCapacity() >= issue.getPageCount() && pp.getPaperSize() == issue.getPageSize()){
                    if(isPaperTypePresent){
                        if(paperType.get() == pp.getPaperType()){
                            BigDecimal printCost = BigDecimal.valueOf(issue.getPageCount()*paperType.get().getValue()*issue.getPageSize().getValue());
                            BigDecimal profit = printCost.add(printCost.multiply(this.issueMarkupPct.divide(BigDecimal.valueOf(100))));
                            this.profits = this.profits.add(profit);
                            this.printedIssues.add(issue);
                            pp.loadIssue(issue);
                            pp.start();
                            return;
                        }
                    }
                    else{
                        BigDecimal printCost = BigDecimal.valueOf(issue.getPageCount()*pp.getPaperType().getValue()*issue.getPageSize().getValue());
                        BigDecimal profit = printCost.add(printCost.multiply(this.issueMarkupPct.divide(BigDecimal.valueOf(100))));
                        this.profits = this.profits.add(profit);
                        this.printedIssues.add(issue);
                        pp.loadIssue(issue);
                        pp.start();
                        return;
                    }
                }
            }
        }
    }

    public void exportToFile(){
        try{
            String filename = this.name;
            File f = new File(filename);
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for (PrintingPress pp : this.printingPresses) {
                if(pp.isCurrentlyPrinting()){
                    try {
                        pp.join();
                    } catch (Exception e) {}
                }
            }
            if(this.printedIssues.size() > this.requiredIssuesForDiscount){
                this.profits = this.profits.subtract(this.profits.multiply(this.bulkDiscount.divide(BigDecimal.valueOf(100))));
            }
            if(this.profits.compareTo(this.requiredProfitsForBonus) == 1){
                this.giveBonuses();
            }
            
            writer.write(this.name + " Printing house information\n");
            writer.write("Profits: " + this.profits.toString() + "\n");
            writer.write("Costs: " + this.getCosts().toString() + "\n");
            writer.write("Printed issues:" + "\n");
            for (Issue i : this.printedIssues) {
                writer.write(i.toString() + "\n");
            }
            writer.close();            
        }
        catch(IOException err){
            System.out.println(err.getMessage());
        }
    }

    static void readInfoFromFile(String printingHouseName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(printingHouseName));
            while(reader.ready()){
                System.out.println(reader.readLine());
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
