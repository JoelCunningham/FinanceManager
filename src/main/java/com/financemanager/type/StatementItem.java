package com.financemanager.type;

import com.financemanager.Helper;

/**
 * The StatementItem class represents a entry in a statement
 */
public class StatementItem implements Comparable<StatementItem>{

    public int id;
    public int category_id; 
    public float amount;
    public String details;
    public String date;

    /**
     * Constructs a new StatementItem object with given fields
     *
     * @param id The id of the statement item
     * @param category_id The id of the category of the statement item
     * @param amount The amount of the statement item
     * @param details The details of the statement item
     * @param date The date the statement item occurred
     */
    public StatementItem(int id, int category_id, float amount, String details, String date) {
        this.id = id;
        this.category_id = category_id;
        this.amount = amount;
        this.details = details;
        this.date = date;
    }

    /**
     * Constructs a new StatementItem object without an id
     *
     * @param category_id The id of the category of the statement item
     * @param amount The amount of the statement item
     * @param details The details of the statement item
     * @param date The date the statement item occurred
     */
    public StatementItem(int category_id, float amount, String details, String date) {
        this.id = -1;
        this.category_id = category_id;
        this.amount = amount;
        this.details = details;
        this.date = date;
    }

    public int compareTo(StatementItem other) {
        return Helper.stringToDate(this.date).compareTo(Helper.stringToDate(other.date));
    }
}