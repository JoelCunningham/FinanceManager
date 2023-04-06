package com.financemanager.helper;

public class Index {
    int index;
    
    public Index() {
        this(0);
    }

    public Index(int index){
        this.index = index;
    }

    public int value() {
        return index;
    }

    public void increment() {
        index++;
    }

}
