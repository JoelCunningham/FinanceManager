package com.financemanager.type;

import java.util.Arrays;

/**
 * The Header class represents a header for categories.
 */
public class Header implements Comparable<Header> {
    public String name;
    public String type;
    public Category[] categories;

    /**
     * Constructs a new Header object with the given name, type and size.
     *
     * @param name The name of the header.
     * @param type The type of the header.
     * @param size The size of the categories array.
     */
    public Header(String name, String type, int size) {
        this.name = name;
        this.type = type;
        this.categories = new Category[size];
    }

    /**
     * Constructs a new Header object with the given name, type and categories array.
     *
     * @param name The name of the header.
     * @param type The type of the header.
     * @param categories The categories array of the header.
     */
    public Header(String name, String type, Category[] categories) {
        this.name = name;
        this.type = type;
        this.categories = categories;
    }

    /**
     * Adds a new category to the categories array of the header. 
     *
     * @param category_id The id of the category to add to the header.
     * @param category_name The name of the category to add to the header.
     */
    public void addCategory(int category_id, String category_name) {
        
        Category new_category = new Category(category_id, category_name, this.name, this.type);

        for (int i = 0; i < categories.length; i++) {
            if (categories[i] == null) {
                categories[i] = new_category;
                return;
            }
        }
        categories = Arrays.copyOf(categories, categories.length + 1);
        categories[categories.length - 1] = new_category;
    } 

    /**
     * Overloads the compareTo method to enable sorting
     * 
     * @param other The item to compare to
     * @return The result of the comparison
     */
    public int compareTo(Header other) {
        if (this.name.equals("Other")) {
            return 1;
        } else if (other.name.equals("Other")) {
            return -1;
        } else {
            return this.name.compareTo(other.name);
        }
    }
}