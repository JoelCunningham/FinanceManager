package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;
import java.util.List;

import com.financemanager.type.Budget;
import com.financemanager.type.BudgetItem;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

import com.financemanager.item.DropdownYear;

import com.financemanager.Helper;
import com.financemanager.JDBC;


import io.javalin.http.Context;

public class BudgetPage extends Page {

    private final int TABLE_COLS = 16; // Number of columns in the table
    private final String PAGE_NAME = "budget"; // Name of the page

    private int selected_year; // The year to display data for

    /**
     * Construct a BudgetPage object with the given context, model and jdbc
     *
     * @param context the context of the page
     * @param model the model of the page
     * @param jdbc the jdbc of the page
     */
    public BudgetPage(Context context, Map<String, Object> model, JDBC jdbc) {
        super(context, model, jdbc);
        selected_year = Calendar.getInstance().get(Calendar.YEAR);
    }
    
    /**
     * Load the page; including selectors and tables
     */
    public void load() {

        // Code for selectors
        DropdownYear year_selector = new DropdownYear(context, model, jdbc, PAGE_NAME, selected_year);
        selected_year = year_selector.load();

        // Code for tables
        loadTables();

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        if (budget_list.size() != 0) {
            saveBudget(budget_list);
        }
    }

    private void loadTables() {

        // Create a budget object
        Budget budget = new Budget(selected_year);

        String[][][] incomes_table = createBudgetTable(budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(budget, "Expenses");
        String[][][] balance_table = createBalance(budget, "Balance", incomes_table, expenses_table);

        model.put("incomes_table", incomes_table);
        model.put("expenses_table", expenses_table);
        model.put("balance_table", balance_table);   
    }

    private String[][][] createBudgetTable(Budget budget, String type) {

        Header[] headers = jdbc.getHeaderCategories(selected_year, type);

        // Create table
        String[][][] table = new String[headers.length][][];
        float[] header_total = new float[TABLE_COLS - 3];

        // For each header in the table
        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length + 1][];
            float[] column_total = new float[TABLE_COLS - 3];

            // For each category in the header 
            for (int j = 0; j < table[i].length - 1; j++) {
               fillCellsInRow(budget, table, headers[i].categories[j], column_total, header_total, i, j);
            }
            Category total_category = new Category(-1, "Total", headers[i].name, headers[i].type);
            fillCellsInRow(budget, table, total_category, column_total, header_total, i, table[i].length - 1);
        }

        return table;
    }

    private void fillCellsInRow(Budget budget, String[][][] table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        table[i][j] = new String[TABLE_COLS];

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
        table[i][j][TABLE_COLS - 1] = String.format("$%.02f", category_total);
    }

    private String[][][] createBalance(Budget budget, String type, String[][][] incomes_table, String[][][] expenses_table) {

        String[][][] table = new String[1][3][TABLE_COLS];
        float[] incomes_total = new float[TABLE_COLS - 3];
        float[] expenses_total = new float[TABLE_COLS - 3];

        // Get incomes type totals
        table[0][0][0] = type;
        table[0][0][1] = "Total";
        table[0][0][2] = "Incomes Total";
        for (int i = 0; i < incomes_table.length; i++) {
            for (int j = 3; j < TABLE_COLS; j++) {
                incomes_total[j - 3] += Helper.currencyToFloat(incomes_table[i][incomes_table[i].length - 1][j]);
            }
        }

        // Get expenses type totals
        table[0][1][0] = type;
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";
        for (int i = 0; i < expenses_table.length; i++) {
            for (int j = 3; j < TABLE_COLS; j++) {
                expenses_total[j - 3] += Helper.currencyToFloat(expenses_table[i][expenses_table[i].length - 1][j]);
            }
        }

        // Fill table and calculate balance
        table[0][2][0] = type;
        table[0][2][1] = "Total";
        table[0][2][2] = type;
        for (int i = 3; i < TABLE_COLS; i++) {
            table[0][0][i] = String.format("$%.02f", incomes_total[i - 3]);
            table[0][1][i] = String.format("$%.02f", expenses_total[i - 3]);
            table[0][2][i] = String.format("$%.02f", Helper.currencyToFloat(table[0][0][i]) - Helper.currencyToFloat(table[0][1][i]));
        }

        return table;
    }

    private void saveBudget(List<String> budget_list) {

        Budget budget = new Budget(selected_year);

        String[][][] incomes_table = createBudgetTable(budget, "Incomes");
        String[][][] expenses_table = createBudgetTable(budget, "Expenses");
        
        int incomes_length = Helper.countElementsIn3dArray(incomes_table);
        int expenses_length = Helper.countElementsIn3dArray(expenses_table);

        List<String> incomes_list = budget_list.subList(0, incomes_length);
        List<String> expenses_list = budget_list.subList(incomes_length, expenses_length + incomes_length);

        saveType(jdbc, incomes_list, incomes_table, "Incomes");
        saveType(jdbc, expenses_list, expenses_table, "Expenses");

        loadTables();
    }

    private void saveType(JDBC jdbc, List<String> changes_list, String[][][] reference_table, String type) {

        int curr_header = 0;
        int curr_category = 0;
        int curr_month = 0;
        int past_categories = 0;

        for (int i = 0; i < changes_list.size(); i++) {

            int column_index = i % TABLE_COLS;

            if (column_index == 1 && i > TABLE_COLS && !changes_list.get(i).equals(changes_list.get(i - TABLE_COLS))) { 
                past_categories += reference_table[curr_header].length;
                curr_header++; 
            }
            else if (column_index > 2 && column_index < 15) {
                
                curr_category = i / (TABLE_COLS) - past_categories;
                curr_month    = i % (TABLE_COLS);

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