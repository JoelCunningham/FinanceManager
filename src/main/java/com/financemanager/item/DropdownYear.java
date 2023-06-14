package com.financemanager.item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public class DropdownYear extends Dropdown {
   
    private int selected_year;

    /**
     * Constructor for the DropdownYear class.
     *
     * @param context       The context in which the DropdownYear is being used.
     * @param model         The model to which the DropdownYear will add data.
     * @param jdbc          The JDBC object used to retrieve data from the database.
     * @param name          The name of the DropdownYear.
     * @param selected_year The initially selected year in the DropdownYear.
     */
    public DropdownYear(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_year) {
        super(context, model, jdbc, name);
        this.selected_year = selected_year;
    }

    /**
     * Loads data into the year selector and returns the selected year.
     *
     * @return The selected year in the year selector.
     */
    public int load() {
        
        Map<Integer, String> year_select = new LinkedHashMap<>();

        // Fill dictionary of years
        List<Integer> years = jdbc.getYears();
        for (int year : years) { year_select.put(year, "False"); }

        // Get selected year
        String year_selector = context.formParam(name + "_year_selector");
        if (year_selector != null) {
            selected_year = Integer.parseInt(year_selector);
        }
        
        year_select.put(selected_year, "True");   
        model.put(name + "_years", year_select);

        return selected_year;
    }
    
}
