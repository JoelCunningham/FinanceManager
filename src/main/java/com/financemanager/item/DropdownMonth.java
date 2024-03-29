package com.financemanager.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public class DropdownMonth extends Dropdown<String> {

    private int selected_month;

    /**
     * Constructor for the DropdownMonth class
     *
     * @param context        The context in which the DropdownMonth is being used
     * @param model          The model to which the DropdownMonth will add data
     * @param jdbc           The JDBC object used to retrieve data from the database
     * @param name           The name of the DropdownMonth
     * @param selected_month The initially selected month in the DropdownMonth
     */
    public DropdownMonth(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_month) {
        super(context, model, jdbc, name);
        this.selected_month = selected_month;
        this.items = new ArrayList<>(List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
        this.items.add("All");
    }

    /**
     * Loads data into the month selector and returns the selected month
     *
     * @return The selected month in the month selector
     */
    public int load() {  
        return load("month", items, selected_month);
    }

}
