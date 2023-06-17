package com.financemanager.type;

import com.financemanager.Helper;

/**
 * The StatementItem class represents a entry in a statement
 */
public class StatementItem implements Comparable<StatementItem>{

    public int category_id; 
    public float amount;
    public String details;
    public String date;

    /**
     * Constructs a new StatementItem object with the given category, year, month and amount
     *
     * @param category_id The id of the category of the statement item
     * @param amount The amount of the statement item
     * @param details The details of the statement item
     * @param date The date the statement item occurred
     */
    public StatementItem(int category_id, float amount, String details, String date) {
        this.category_id = category_id;
        this.amount = amount;
        this.details = details;
        this.date = date;
    }

    public int compareTo(StatementItem other) {
        return Helper.stringToDate(this.date).compareTo(Helper.stringToDate(other.date));
    }
}