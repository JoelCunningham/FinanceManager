package com.financemanager.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public class Generic {
        
    public static int loadYearSelector(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_year) {
        
        Map<Integer, String> year_select = new HashMap<>();

        // Fill dictionary of years
        List<Integer> years = jdbc.getYears();
        for (int year : years) { year_select.put(year, "False"); }

        // Get selected year
        int previous_year = selected_year;
        String year_selector = context.formParam(name + "_year_selector");

        if (year_selector == null) {
            selected_year = previous_year;
        }
        else {
            selected_year = Integer.parseInt(year_selector);
        }
        
        year_select.put(selected_year, "True");   
        model.put(name + "_years", year_select);

        return selected_year;
    }

}