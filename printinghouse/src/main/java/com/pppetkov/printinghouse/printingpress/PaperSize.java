package com.pppetkov.printinghouse.printingpress;

public enum PaperSize {
    A5, A4, A3, A2, A1;
    
    public int getValue(){
        switch(this){
            case A5: return 1;
            case A4: return 2;
            case A3: return 3;
            case A2: return 4;
            case A1: return 5;
            default: return 0;
        }
    }
}
