package com.pppetkov.printinghouse.worker;
import java.math.BigDecimal;

public class Worker {
    private String name;
    private final long id;
    private BigDecimal salary;
    private boolean isManager;
    private boolean hasReceivedBonus;

    private static long nextId = 0;

    public Worker(String name, BigDecimal salary, boolean isManager){
        this.name = name;
        this.salary = BigDecimal.ZERO;
        if(salary.compareTo(BigDecimal.ZERO) == 1){
            this.salary = salary;
        }
        
        this.id = nextId;
        ++nextId;
        this.isManager = isManager;
        this.hasReceivedBonus = false;
    }

    public String getName(){ return this.name; }
    public long getId() { return this.id; }
    public BigDecimal getSalary() { return this.salary; }

    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public void receiveBonus(BigDecimal bonus){
        if(!this.hasReceivedBonus && this.isManager){
            this.salary.add(this.salary.multiply(bonus.divide(BigDecimal.valueOf(100))));
            this.hasReceivedBonus = true;
        }
    }

    
}
