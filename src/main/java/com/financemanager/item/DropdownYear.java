package com.financemanager.item;

import java.util.LinkedHashMap;
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
    }

    /**
     * Loads data into the year selector and returns the selected year
     *
     * @return The selected year in the year selector
     */
    public int load() {
        
        // Fill dictionary of years
        Map<String, String> year_select = new LinkedHashMap<>();
        for (String year : items) { year_select.put(year, "False"); }

        // Get selected year
        String year_selector = context.formParam(name + "_year_selector");
        if (year_selector == null) {
            year_selector = context.formParam(name + "_year_selector_alt");
        }
        if (year_selector == null) {
            year_selector = context.formParam(name + "_year_selector_alt_alt");
        }
        if (year_selector != null) {
            selected_year = Integer.parseInt(year_selector);
        } 
        if (selected_year != -1) {
            year_select.put(Integer.toString(selected_year), "True"); 
        }
          
        model.put(name + "_years", year_select);

        return selected_year;
    }
    
}
