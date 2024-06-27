package com.pppetkov.printinghouse;

import java.math.BigDecimal;
import java.util.Optional;

import com.pppetkov.printinghouse.issue.Issue;
import com.pppetkov.printinghouse.printingpress.PrintingPress;
import com.pppetkov.printinghouse.printingpress.PaperSize;
import com.pppetkov.printinghouse.printingpress.PaperType;
import com.pppetkov.printinghouse.worker.Worker;


public class Main {
    public static void main(String[] args) {
        PrintingHouse p = new PrintingHouse("Hermes",BigDecimal.valueOf(50), BigDecimal.valueOf(2000), BigDecimal.valueOf(5), BigDecimal.valueOf(100), 2, BigDecimal.valueOf(10));

        Issue i1 = new Issue("Don Quixote", 25, PaperSize.A5);
        Issue i2 = new Issue("It", 15, PaperSize.A4);
        Issue i3 = new Issue("24 Chasa", 5, PaperSize.A3);

        Worker w1 = new Worker("Jim", BigDecimal.ZERO, false);
        Worker w2 = new Worker("Michael", BigDecimal.ZERO, true);
        Worker w3 = new Worker("Tim", BigDecimal.ZERO, false);

        p.hireWorker(w1);
        p.hireWorker(w2);
        p.hireWorker(w3);

        PrintingPress pp1 = new PrintingPress(30, PaperType.COATED, PaperSize.A5, true, 60);
        PrintingPress pp2 = new PrintingPress(25, PaperType.REGULAR, PaperSize.A4, false, 180);
        PrintingPress pp3 = new PrintingPress(25, PaperType.REGULAR, PaperSize.A3, false, 120);

        p.addPrintingPress(pp1);
        p.addPrintingPress(pp2);
        p.addPrintingPress(pp3);

        p.print(i1, Optional.of(PaperType.COATED));
        p.print(i2, Optional.empty());
        p.print(i3, Optional.empty());
        p.exportToFile();
        
        PrintingHouse.readInfoFromFile("Hermes");
    }
}
