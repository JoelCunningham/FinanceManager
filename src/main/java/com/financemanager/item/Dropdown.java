package com.financemanager.item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public abstract class Dropdown<T> {

    protected Context context;
    protected Map<String, Object> model;
    protected JDBC jdbc;
    protected String name;

    protected List<T> items;

    /**
     * Constructor for the Dropdown class
     *
     * @param context The context in which the Dropdown is being used
     * @param model   The model to which the Dropdown will add data
     * @param jdbc    The JDBC object used to retrieve data from the database
     * @param name    The name of the Dropdown
     */
    public Dropdown(Context context, Map<String, Object> model, JDBC jdbc, String name) {
        this.context = context;
        this.model = model;
        this.jdbc = jdbc;
        this.name = name;
    }

    /**
     * Loads data into the dropdown and returns the selected value
     * Used by year and month dropdowns 
     *
     * @param type The type of dropdown (year or month)
     * @param items The items to be loaded into the dropdown
     * @param selected The selected value in the dropdown
     * @return The selected value in the dropdown
     */
    public int load(String type, List<String> items, int selected) {
        
        // Fill dictionary of items
        Map<String, String> select = new LinkedHashMap<>();
        for (String item : items) { select.put(item, "False"); }

        // Get selected value
        String selector = context.formParam(name + "_" + type + "_selector");
        if (selector == null) {
            selector = context.formParam(name + "_" + type + "_selector_alt");
        }
        if (selector == null) {
            selector = context.formParam(name + "_" + type + "_selector_alt_alt");
        }
        if (selector != null) {
            selected = type.equals("year") ? Integer.parseInt(selector) : Helper.monthToInt(selector);
        } 
        if (selected != -1) {
            select.put(type.equals("year") ? Integer.toString(selected) : items.get(selected), "True"); 
        }
        
        model.put(name + "_" + type + "s", select);

        return selected;
    }

     /**
     * Removes an item from the Dropdown
     * Changes take effect after using load()
     * 
     * @param item The item to remove
     */
    public void remove(T item) {
        this.items.remove(item);
    }

    /**
     * Add an item to the Dropdown
     * Changes take effect after using load()
     * 
     * @param item The item to add
     */
    public void add(T item) {
        this.items.add(item);
    }
}