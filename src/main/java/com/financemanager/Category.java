package com.financemanager;

/**
 * The Category class represents a category of incomes or expenses.
 */
public class Category {
    private int id;
    private String name;

    /**
     * Constructs a new Category object with the given id and name.
     *
     * @param name The name of the category.
     * @param id The id of the category.
     */
    public Category(int id, String name) {
        this.name = name;
        this.id = id;
    }

    /**
     * Copy constructor for the Category class.
     *
     * @param category The Category object to be copied.
     */
    public Category(Category category) {
        this.name = category.name;
        this.id = category.id;
    }

    /**
     * Returns the name of the category.
     *
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id of the category.
     *
     * @return The id of the category.
     */
    public int getId() {
        return id;
    }
}