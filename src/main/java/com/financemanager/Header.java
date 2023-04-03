package com.financemanager;

import java.util.Arrays;

/**
 * The Header class represents a header for categories.
 */
public class Header {
    private String name;
    private String type;
    private Category[] categories;

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
     * Returns the name of the header.
     *
     * @return The name of the header.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the header.
     *
     * @return The type of the header.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the categories array of the header.
     *
     * @return The categories array of the header.
     */
    public Category[] getCategories() {
        return categories;
    }

    /**
     * Sets the categories array of the header to a new array.
     *
     * @param categories The new categories array to set for the header.
     */
    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    /**
     * Adds a new category to the categories array of the header. If there is an empty slot in
     * the array, it will be filled with the new category. Otherwise, a new array will be created
     * with one more slot than the current array and all elements will be copied over along with
     * the new category at the end.
     *
     * @param new_category The new category to add to the categories array of the header.
     */
    public void addCategory(Category new_category) {
        
        for (int i = 0; i < categories.length; i++) {
            if (categories[i] == null) {
                categories[i] = new_category;
                return;
            }
        }
        categories = Arrays.copyOf(categories, categories.length + 1);
        categories[categories.length - 1] = new_category;
    } 
}