package com.financemanager;

/**
 * The Budget class represents a budget.
 */
public class Budget {
    public int year;
    public BudgetItem[] items;

     /**
     * Constructs a new Budget object with default values.
     */
    public Budget() {
        this(0, new BudgetItem[]{new BudgetItem()});
    }

    /**
     * Constructs a new Budget object with the given year and items array.
     *
     * @param year The year of the budget.
     * @param items The items array of the budget.
     */
    public Budget(int year, BudgetItem[] items) {
        this.year = year;
        this.items = items;
    }

     /**
     * Loads budget form specified year into Budget object. May be replaced
     *
     * @param year The year of the budget to load. 
     */
    public void load(int year) {
        //this.items = jdbc.getBudget('year') TODO
        this.year = year;

        items = new BudgetItem[3];
        items[0] = new BudgetItem("Head1", "Head1Cat1", 1, 20);
        items[1] = new BudgetItem("Head1", "Head1Cat2", 7, 33);
        items[2] = new BudgetItem("Head2", "Head2Cat3", 4, 21);
    }
}