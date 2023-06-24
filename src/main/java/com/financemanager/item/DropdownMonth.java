package com.financemanager.item;

import java.util.LinkedHashMap;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public class DropdownMonth extends Dropdown {

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
    }

    /**
     * Loads data into the month selector and returns the selected month
     *
     * @return The selected month in the month selector
     */
    public int load() {
        
        Map<String, String> month_select = new LinkedHashMap<>();

        // Fill dictionary of months
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};        
        for (String month : months) { month_select.put(month, "False"); }

        // Get selected month
        String month_selector = context.formParam(name + "_month_selector");
        if (month_selector != null) {
            selected_month = Helper.monthToInt(month_selector);
        }
        
        month_select.put(months[selected_month], "True");   
        model.put(name + "_months", month_select);

        return selected_month;
    }

}
