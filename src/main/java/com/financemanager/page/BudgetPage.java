package com.financemanager.page;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.financemanager.type.Budget;
import com.financemanager.type.BudgetItem;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import io.javalin.http.Context;

public class BudgetPage {

    private static final int BUDGET_COLS = 16;

    private static int selected_year = Calendar.getInstance().get(Calendar.YEAR);
    private static int previous_year = Calendar.getInstance().get(Calendar.YEAR);
    
    public static void loadBudgetPage(Context context, Map<String, Object> model, JDBC jdbc) {

        // Code for year selector
        loadYearSelector(context, model, jdbc);      

        // Code for budget tables
        loadBudgetTables(context, model, jdbc);

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        if (budget_list.size() != 0) {
            saveBudget(context, model, jdbc, budget_list);
        }
    }

    public static void loadYearSelector(Context context, Map<String, Object> model, JDBC jdbc) {
        
        Map<Integer, String> year_select = new HashMap<>();

        // Fill dictionary of years
        List<Integer> years = jdbc.getYears();
        for (int year : years) { year_select.put(year, "False"); }

        // Get selected year
        previous_year = selected_year;
        String year_selector = context.formParam("year_selector");

        if (year_selector == null) {
            selected_year = previous_year;
        }
        else {
            selected_year = Integer.parseInt(year_selector);
        }
        
        year_select.put(selected_year, "True");   
        model.put("years", year_select);
    }

    public static void loadBudgetTables(Context context, Map<String, Object> model, JDBC jdbc) {

        // Create a budget object
        Budget budget = new Budget();
        budget.load(selected_year);

        String[][][] incomes_table = createBudgetTable(jdbc, budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(jdbc, budget, "Expenses");
        String[][][] balance_table = createBalance(budget, "Balance", incomes_table, expenses_table);

        model.put("incomes_table", incomes_table);
        model.put("expenses_table", expenses_table);
        model.put("balance_table", balance_table);   
    }

    public static String[][][] createBudgetTable(JDBC jdbc, Budget budget, String type) {

        Header[] headers = jdbc.getHeaderCategories(selected_year, type);

        // Create table
        String[][][] table = new String[headers.length][][];
        float[] header_total = new float[BUDGET_COLS - 3];

        // For each header in the table
        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length + 1][];
            float[] column_total = new float[BUDGET_COLS - 3];

            // For each category in the header 
            for (int j = 0; j < table[i].length - 1; j++) {
               fillCellsInRow(budget, table, headers[i].categories[j], column_total, header_total, i, j);
            }
            Category total_category = new Category(-1, "Total", headers[i].name, headers[i].type);
            fillCellsInRow(budget, table, total_category, column_total, header_total, i, table[i].length - 1);
        }

        return table;
    }

    public static void fillCellsInRow(Budget budget, String[][][] table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        table[i][j] = new String[BUDGET_COLS];

        // Set row head values
        table[i][j][0] = category.type;
        table[i][j][1] = category.header_name;
        table[i][j][2] = category.name;
        
        float category_total = 0;

        // Loop through each month and add value
        for (int k = 3; k < table[i][j].length - 1; k++) {
            
            float value = 0;

            // Normal operation
            if (j != table[i].length - 1){
                value = budget.findValue(category.id, k - 2);
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
                incomes_total[j - 3] += Helper.currencyToFloat(incomes_table[i][incomes_table[i].length - 1][j]);
            }
        }

        // Get expenses type totals
        table[0][1][0] = type;
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";
        for (int i = 0; i < expenses_table.length; i++) {
            for (int j = 3; j < BUDGET_COLS; j++) {
                expenses_total[j - 3] += Helper.currencyToFloat(expenses_table[i][expenses_table[i].length - 1][j]);
            }
        }

        // Fill table and calculate balance
        table[0][2][0] = type;
        table[0][2][1] = "Total";
        table[0][2][2] = type;
        for (int i = 3; i < BUDGET_COLS; i++) {
            table[0][0][i] = String.format("$%.02f", incomes_total[i - 3]);
            table[0][1][i] = String.format("$%.02f", expenses_total[i - 3]);
            table[0][2][i] = String.format("$%.02f", Helper.currencyToFloat(table[0][0][i]) - Helper.currencyToFloat(table[0][1][i]));
        }

        return table;
    }

    public static void saveBudget(Context context, Map<String, Object> model, JDBC jdbc, List<String> budget_list) {

        Budget budget = new Budget();
        budget.load(selected_year);

        String[][][] incomes_table = createBudgetTable(jdbc, budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(jdbc, budget, "Expenses");
        
        int incomes_length = Helper.countElementsIn3dArray(incomes_table);
        int expenses_length = Helper.countElementsIn3dArray(expenses_table);

        List<String> incomes_list = budget_list.subList(0, incomes_length);
        List<String> expenses_list = budget_list.subList(incomes_length, expenses_length + incomes_length);

        saveType(jdbc, incomes_list, incomes_table, "Incomes");
        saveType(jdbc, expenses_list, expenses_table, "Expenses");

        loadBudgetTables(context, model, jdbc);
    }

    public static void saveType(JDBC jdbc, List<String> changes_list, String[][][] reference_table, String type) {

        int curr_header = 0;
        int curr_category = 0;
        int curr_month = 0;
        int past_categories = 0;

        for (int i = 0; i < changes_list.size(); i++) {

            int column_index = i % BUDGET_COLS;

            if (column_index == 1 && i > BUDGET_COLS && !changes_list.get(i).equals(changes_list.get(i - BUDGET_COLS))) { 
                past_categories += reference_table[curr_header].length;
                curr_header++; 
            }
            else if (column_index > 2 && column_index < 15) {
                
                curr_category = i / (BUDGET_COLS) - past_categories;
                curr_month    = i % (BUDGET_COLS);

                if (Helper.currencyToFloat(changes_list.get(i)) != Helper.currencyToFloat(reference_table[curr_header][curr_category][curr_month])) {

                    int type_id = jdbc.getTypeID(type);
                    int header_id = jdbc.getHeaderID(reference_table[curr_header][curr_category][1], type_id);
                    int category_id = jdbc.getCategoryID(reference_table[curr_header][curr_category][2], header_id);

                    BudgetItem item = new BudgetItem(category_id, curr_month - 2, Helper.currencyToFloat(changes_list.get(i)));

                    jdbc.addBudgetItem(item, selected_year);               
                }
            }
        }




    }

}