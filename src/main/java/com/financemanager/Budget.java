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

        Header[] headers = new Header[2];
        headers[0] = new Header("Head1", "Income", 3);
        headers[1] = new Header("Head2", "Expense", 3);
        for (Header header : headers) {
            header.addCategory(header.name + "Cat1");
            header.addCategory(header.name + "Cat2");
            header.addCategory(header.name + "Cat3");
        }
        items = new BudgetItem[3];
        items[0] = new BudgetItem(headers[0].categories[0], 1, 20);
        items[1] = new BudgetItem(headers[0].categories[1], 7, 33);
        items[2] = new BudgetItem(headers[1].categories[2], 4, 21);
    }

    public float findValue(String type, String header_name, String category_name, int month) {

        float value = 0;

        for (BudgetItem item : items) {
            
            if (item.month == month &&
                item.category.type.equals(type) &&
                item.category.name.equals(category_name) &&
                item.category.header_name.equals(header_name)) {

                value = item.amount;
                break;
            }
        }
        return value;
    }
}