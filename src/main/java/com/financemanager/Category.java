package com.financemanager;

/**
 * The Category class represents a category of incomes or expenses.
 */
public class Category {
    public String name;
    public String header_name;
    public String type;

    public Category() {
        this("", "", "");
    }

    /**
     * Constructs a new Category object with the given name and header.
     *
     * @param name The name of the category.
     * @param header_name The name of the header of the category.
     * @param type The type of the category
     */
    public Category(String name, String header_name, String type) {
        this.name = name;
        this.header_name = header_name;
        this.type = type;
    }
}