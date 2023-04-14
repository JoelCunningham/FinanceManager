package com.financemanager;

/**
 * The Category class represents a category of incomes or expenses.
 */
public class Category {
    public int id;
    public String name;
    public String header_name;
    public String type;

    public Category() {
        this(0, "", "", "");
    }

    /**
     * Constructs a new Category object with the given name and header.
     *
     * @param id The id of the category.
     * @param name The name of the category.
     * @param header_name The name of the header of the category.
     * @param type The type of the category
     */
    public Category(int id, String name, String header_name, String type) {
        this.id = id;
        this.name = name;
        this.header_name = header_name;
        this.type = type;
    }
}