package com.financemanager.pages;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.financemanager.Budget;
import com.financemanager.Header;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public class BudgetPage {

    private static final int BUDGET_COLS = 16;

    private static String selected_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    private static String previous_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    
    public static void loadBudgetPage(Context context, Map<String, Object> model, JDBC jdbc) {
       
        // Code for year selector
        Map<String, String> year_select = new HashMap<>();
        initializeYearSelect(context, year_select);      

        // Create a budget object
        Budget budget = new Budget();
        budget.load(Integer.parseInt(selected_year));

        String[][][] incomes_table = createBudgetTable(budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(budget, "Expenses");

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        System.out.println(budget_list);

        model.put("budget_table", new String[][][][]{incomes_table, expenses_table});
        model.put("years", year_select);
    }

    public static void initializeYearSelect(Context context, Map<String, String> year_select) {
        // String[] years = jdbc.getYears(); //TODO
        String[] years = {"2023", "2022", "2021"}; 
        // Fill dictionary of years
        for (String year : years) { year_select.put(year, "False"); }
        // Get selected year
        previous_year = selected_year;
        selected_year = context.formParam("year_selector");
        // If no selected year, reset to default
        if (selected_year == null) {selected_year = previous_year;}
        
        year_select.put(selected_year, "True");   
    }

    public static String[][][] createBudgetTable(Budget budget, String type) {

        //Header[] headers = jdbc.getHeaders(selected_year, type);
        Header[] headers = new Header[2];
        headers[0] = new Header("Head1", type, 3);
        headers[1] = new Header("Head2", type, 3);
        for (Header header : headers) {
            header.addCategory(header.name + "Cat1");
            header.addCategory(header.name + "Cat2");
            header.addCategory(header.name + "Cat3");
        }

        // Create table
        String[][][] table = new String[headers.length][][];

        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length][];

            for (int j = 0; j < table[i].length; j++) {

                table[i][j] = new String[BUDGET_COLS];
                table[i][j][0] = type;
                table[i][j][1] = headers[i].name;
                table[i][j][2] = headers[i].categories[j].name;
                float total = 0;

                for (int k = 3; k < table[i][j].length - 1; k++) {
                    float value = i+j+k;
                    table[i][j][k] = Float.toString(value);
                    total += value;
                }
                table[i][j][BUDGET_COLS - 1] = Float.toString(total);
            }
        }

        return table;
    }

    public static int getTableHeight(Header[] headers) {
        int header_count = headers.length;
        int category_count = 0;
        for (Header header : headers) {
            category_count += header.categories.length;
        }
        return 2 * header_count + category_count + 2 * 2;
    }

}
