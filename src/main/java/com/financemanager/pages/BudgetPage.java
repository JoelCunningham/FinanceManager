package com.financemanager.pages;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.financemanager.Budget;
import com.financemanager.Category;
import com.financemanager.Header;
import com.financemanager.JDBC;
import com.financemanager.helper.Helper;
import com.financemanager.helper.Index;

import io.javalin.http.Context;

public class BudgetPage {

    private static final int BUDGET_COLS = 14;
    private static       int BUDGET_ROWS =  0;

    private static String selected_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    private static String previous_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    
    public static void loadBudgetPage(Context context, Map<String, Object> model, JDBC jdbc) {
       
        // Code for year selector
        Map<String, String> year_select = new HashMap<>();
        initializeYearSelect(context, year_select);      

        // Create a budget object
        Budget budget = new Budget();
        budget.load(Integer.parseInt(selected_year));

        // Create a table resprestation of the budget object
        List<List<Map.Entry<String, String>>> budget_table = createBudgetTable(budget);

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");

        System.out.println(budget_list);

        model.put("budget_table", budget_table);
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

    public static List<List<Map.Entry<String, String>>> createBudgetTable(Budget budget) {

        //Header[] headers = jdbc.getHeaders(selected_year);
        Header[] headers = new Header[2];
        headers[0] = new Header("Head1", "Income", 3);
        headers[1] = new Header("Head2", "Expense", 3);
        for (Header header : headers) {
            header.addCategory(header.name + "Cat1");
            header.addCategory(header.name + "Cat2");
            header.addCategory(header.name + "Cat3");
        }

        // Determine table dimension
        BUDGET_ROWS = Helper.getTableHeight(headers);

        // Create table
        List<List<Map.Entry<String, String>>> budget_table = new ArrayList<>();
        for (int i = 0; i < BUDGET_ROWS; i++) {
            List<Map.Entry<String, String>> temp_row = new ArrayList<>();
            for (int j = 0; j < BUDGET_COLS; j++) {
                temp_row.add(new AbstractMap.SimpleEntry<String, String>(null, null));
            }
            budget_table.add(temp_row);
        }

        fillBudgetTableCategories(budget_table, headers);
        fillBudgetTableValues(budget_table, budget);
        //fillBudgetTableValues();


        return budget_table;
    }

    public static void fillBudgetTableCategories(List<List<Map.Entry<String, String>>> budget_table, Header[] headers) {

        Index current_row = new Index();

        fillHeaderType(budget_table, headers, current_row, "Income");
        fillHeaderType(budget_table, headers, current_row, "Expense");
    }

    public static void fillHeaderType(List<List<Map.Entry<String, String>>> table, Header[] headers, Index row, String type) {
        fillCellByRow(table, row, new AbstractMap.SimpleEntry<String, String>("type", type));
        for (Header header : headers) {
            if (header.type == type) {  
                fillCellByRow(table, row, new AbstractMap.SimpleEntry<String, String>("header", header.name));
                for (Category category : header.categories) {
                    fillCellByRow(table, row, new AbstractMap.SimpleEntry<String, String>("category", category.name));
                }
                fillCellByRow(table, row, new AbstractMap.SimpleEntry<String, String>("header total", header.name + " Total"));
            }     
        }
        fillCellByRow(table, row, new AbstractMap.SimpleEntry<String, String>( "type total", type + " Total"));
    }

    public static void fillBudgetTableValues(List<List<Map.Entry<String, String>>> table, Budget budget){

        String type = "";
        String header = "";
        String category = "";

        for (int i = 0; i < BUDGET_ROWS; i++) {
            
            Map.Entry<String,String> head = table.get(i).get(0);

            // Set variables to keep track
            if (head.getKey() == "type") { type = head.getValue(); }
            if (head.getKey() == "header") { header = head.getValue(); }
            if (head.getKey() == "category") { category = head.getValue(); }
            // Skip to next iteration if row is not a data row
            else {
                continue;
            }

            for (int j = 1; j < BUDGET_COLS - 1; j++) {
                Map.Entry<String, String> cell = new AbstractMap.SimpleEntry<String, String>("data", String.format("%.2f", budget.findValue(type, header, category, j)));
                table.get(i).set(j, cell);
            }
        }



    }

    public static void fillBudgetTableTotals(){}

    public static void fillCellByRow(List<List<Map.Entry<String, String>>> table, Index row, AbstractMap.SimpleEntry<String, String> cell) {
        table.get(row.value()).set(0, cell);
        row.increment();
    }
}
