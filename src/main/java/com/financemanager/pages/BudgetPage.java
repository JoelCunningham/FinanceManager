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

        // Create a budget object
        Budget budget = new Budget();
        budget.load(Integer.parseInt(selected_year));

        String[][][] incomes_table = createBudgetTable(budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(budget, "Expenses");
        String[][][] balance_table = createBalance(budget, "Balance", incomes_table, expenses_table);
        model.put("budget_table", new String[][][][]{incomes_table, expenses_table, balance_table});

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        System.out.println(budget_list);

        // Code for year selector
        Map<String, String> year_select = new HashMap<>();
        initializeYearSelect(context, year_select);      
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
        float[] header_total = new float[BUDGET_COLS - 3];

        // For each header in the table
        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length + 1][];
            float[] column_total = new float[BUDGET_COLS - 3];

            // For each category in the header 
            for (int j = 0; j < table[i].length - 1; j++) {
               fillCellsInRow(budget, table, headers, column_total, header_total, headers[i].categories[j].name, type, i, j);
            }
            fillCellsInRow(budget, table, headers, column_total, header_total, "Total", type, i, table[i].length - 1);
        }

        return table;
    }

    public static void fillCellsInRow(Budget budget, String[][][] table, Header[] headers, float[] column_total, float[] header_total, String category_name, String type, int i, int j) {
        
        table[i][j] = new String[BUDGET_COLS];  
        String header_name = headers[i].name;

        // Set row head values
        table[i][j][0] = type;
        table[i][j][1] = header_name;
        table[i][j][2] = category_name;
        
        float category_total = 0;

        // Loop through each month and add value
        for (int k = 3; k < table[i][j].length - 1; k++) {
            
            float value;

            // Normal operation
            if (j != table[i].length - 1){
                value = budget.findValue(type, header_name, category_name, k - 2);
                column_total[k - 3] += value;
            }
            // Total row
            else {
                value = column_total[k - 3]; 
                header_total[k - 3] += value;
            }

            table[i][j][k] = String.format("$%.02f", value);
            category_total += value;
        }
        // Add category total
        table[i][j][BUDGET_COLS - 1] = String.format("$%.02f", category_total);
    }

    public static String[][][] createBalance(Budget budget, String type, String[][][] incomes_table, String[][][] expenses_table) {

        String[][][] table = new String[1][3][BUDGET_COLS];
        float[] incomes_total = new float[BUDGET_COLS - 3];
        float[] expenses_total = new float[BUDGET_COLS - 3];

        // Get incomes type totals
        table[0][0][0] = type;
        table[0][0][1] = "Total";
        table[0][0][2] = "Incomes Total";
        for (int i = 0; i < incomes_table.length; i++) {
            for (int j = 3; j < BUDGET_COLS; j++) {
                incomes_total[j - 3] += currencyToFloat(incomes_table[i][incomes_table[i].length - 1][j]);
            }
        }

        // Get expenses type totals
        table[0][1][0] = type;
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";
        for (int i = 0; i < expenses_table.length; i++) {
            for (int j = 3; j < BUDGET_COLS; j++) {
                expenses_total[j - 3] += currencyToFloat(expenses_table[i][expenses_table[i].length - 1][j]);
            }
        }

        // Fill table and calculate balance
        table[0][2][0] = type;
        table[0][2][1] = "Total";
        table[0][2][2] = type;
        for (int i = 3; i < BUDGET_COLS; i++) {
            table[0][0][i] = String.format("$%.02f", incomes_total[i - 3]);
            table[0][1][i] = String.format("$%.02f", expenses_total[i - 3]);
            table[0][2][i] = String.format("$%.02f", currencyToFloat(table[0][0][i]) - currencyToFloat(table[0][1][i]));
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

    public static float currencyToFloat(String s) {
        if (s.charAt(0) == '$') {
            s = s.substring(1);
          }
         
          return Float.parseFloat(s);
    }

}
