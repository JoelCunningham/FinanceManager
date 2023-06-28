package com.financemanager.type;

public abstract class CashCollection<T> {

    public T[] items;

    public int month;
    public int year;

    /**
     * Constructs a new CashCollection object for a given month
     * 
     * @param month The month covered by the CashCollection
     * @param year The year of the month
     */
    public CashCollection(int month, int year) {
        this.month = month;
        this.year = year;
        load();
    }

    /**
     * Constructs a new CashCollection object for a given year
     * 
     * @param year The year covered by the CashCollection
     */
    public CashCollection(int year) {
        this.year = year;
        this.month = -1;
        load();
    }
    
    /**
     * Loads items into the object
     */
    public abstract void load();

    /**
     * Finds the value of an item over a span of time
     * 
     * @param category_id The id of the category of the item
     * @param span The span of the item
     * 
     */
    public abstract float findValue(int category_id, int span);

}
