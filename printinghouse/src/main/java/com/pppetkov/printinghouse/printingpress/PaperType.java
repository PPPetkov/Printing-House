package com.pppetkov.printinghouse.printingpress;

public enum PaperType {
    NEWSPRINT, REGULAR, COATED;

    public int getValue(){
        switch(this){
            case NEWSPRINT: return 1;
            case REGULAR  : return 2;
            case COATED   : return 3;
            default       : return 0;
        }
    }
    
}
