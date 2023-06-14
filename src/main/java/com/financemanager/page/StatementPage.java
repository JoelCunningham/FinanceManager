package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.JDBC;

import com.financemanager.item.DropdownYear;
import com.financemanager.item.DropdownMonth;

import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

import io.javalin.http.Context;

public class StatementPage extends Page {

    private final int TABLE_COLS = 9; // Number of columns in the table
    private final String PAGE_NAME = "statement"; // Name of the page

    private static int selected_year; // The year to display data for
    private static int selected_month; // The month to display data for

    /**
     * Construct a StatementPage object with the given context, model and jdbc
     *
     * @param context the context of the page
     * @param model the model of the page
     * @param jdbc the jdbc of the page
     */
    public StatementPage(Context context, Map<String, Object> model, JDBC jdbc) {
        super(context, model, jdbc);
        selected_year = Calendar.getInstance().get(Calendar.YEAR);
        selected_month = Calendar.getInstance().get(Calendar.MONTH);
    }

     /**
     * Load the page; including selectors and tables
     */
    public void load() {

        // Code for selectors
        DropdownYear year_selector = new DropdownYear(context, model, jdbc, PAGE_NAME, selected_year);
        DropdownMonth month_selector = new DropdownMonth(context, model, jdbc, PAGE_NAME, selected_month);  

        selected_year = year_selector.load();   
        selected_month = month_selector.load(); 

        // Code for tables
        loadTables();
    }

    private void loadTables() {

        // Create a statement object
        Statement statement = new Statement(selected_month, selected_year);
        statement.load();

        String[][][] incomes_table = createStatementTable(statement, "Incomes");
        String[][][] expenses_table = createStatementTable(statement, "Expenses");
        //String[][][] balance_table = createBalance(statement, "Balance", incomes_table, expenses_table);

        model.put("statement_incomes_table", incomes_table);
        model.put("statement_expenses_table", expenses_table);
        //model.put("statement_balance_table", balance_table);   
    }

    private String[][][] createStatementTable(Statement statement, String type) {

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
               fillCellsInRow(statement, table, headers[i].categories[j], column_total, header_total, i, j);
            }
            Category total_category = new Category(-1, "Total", headers[i].name, headers[i].type);
            fillCellsInRow(statement, table, total_category, column_total, header_total, i, table[i].length - 1);
        }
        return table;
    }

    private void fillCellsInRow(Statement statement, String[][][] table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        table[i][j] = new String[TABLE_COLS];

        // Set row head values
        table[i][j][0] = category.type;
        table[i][j][1] = category.header_name;
        table[i][j][2] = category.name;
        
        float category_total = 0;

        // Loop through each week and add value
        for (int k = 3; k < table[i][j].length - 1; k++) {
            
            float value = 0;

            // Normal operation
            if (j != table[i].length - 1){
                value = statement.getWeekValue(category.id, k - 2);
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

}