package com.financemanager.item;

import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public class DropdownYear extends Dropdown<String> {
   
    public int selected_year;

    /**
     * Constructor for the DropdownYear class
     *
     * @param context       The context in which the DropdownYear is being used
     * @param model         The model to which the DropdownYear will add data
     * @param jdbc          The JDBC object used to retrieve data from the database
     * @param name          The name of the DropdownYear
     * @param selected_year The initially selected year in the DropdownYear
     */
    public DropdownYear(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_year) {
        super(context, model, jdbc, name);
        this.selected_year = selected_year;
        this.items = Helper.intToStringList(jdbc.getYears());
        this.items.add("All");
    }

    /**
     * Loads data into the year selector and returns the selected year
     *
     * @return The selected year in the year selector
     */
    public int load() {
        return load("year", items, selected_year);
    }
    
}
