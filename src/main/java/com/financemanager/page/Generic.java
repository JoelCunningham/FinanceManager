package com.financemanager.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public class Generic {
        
    public static int loadYearSelector(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_year) {
        
        Map<Integer, String> year_select = new HashMap<>();

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

    public static int loadMonthSelector(Context context, Map<String, Object> model, JDBC jdbc, String name, int selected_month) {
        
        Map<String, String> month_select = new HashMap<>();

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