package com.financemanager.type;

public abstract class CashCollection<T> {

    public T[] items;
    
    /**
     * Loads budget items from the specified year into the object
     */
    public abstract void load();

    /**
     * Finds the value of a budget item
     * 
     * @param category_id The id of the category of the budget item
     * @param span The span of the budget item
     * 
     */
    public abstract float findValue(int category_id, int span);

}
