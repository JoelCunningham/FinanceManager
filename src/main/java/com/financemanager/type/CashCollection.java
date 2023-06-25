package com.financemanager.type;

public abstract class CashCollection<T> {

    public T[] items;
    
    /**
     * Loads items into the object
     */
    public abstract void load();

    /**
     * Finds the value of an item over a span of time
     * 
     * @param category_id The id of the category of the item
     * @param span The span of the item
     * @param size The size to split into
     * 
     */
    public abstract float findValue(int category_id, int span, int size);

}
