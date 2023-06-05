package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.JDBC;

import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

import io.javalin.http.Context;

public class StatementPage {

    private static final int STATEMENT_COLS = 9;

    private static int selected_year = Calendar.getInstance().get(Calendar.YEAR);
    private static int selected_month = Calendar.getInstance().get(Calendar.MONTH);
    
    public static void loadStatementPage(Context context, Map<String, Object> model, JDBC jdbc) {

        // Code for year selector
        selected_year = Generic.loadYearSelector(context, model, jdbc, "statement", selected_year);      

        // Code for month selector
        selected_month = Generic.loadMonthSelector(context, model, jdbc, "statement", selected_month);      

        // Code for statement tables
        loadStatementTables(context, model, jdbc);
    }

    public static void loadStatementTables(Context context, Map<String, Object> model, JDBC jdbc) {

        // Create a statement object
        Statement statement = new Statement(selected_month, selected_year);
        statement.load();

        String[][][] incomes_table = createStatementTable(jdbc, statement, "Incomes");
        String[][][] expenses_table = createStatementTable(jdbc, statement, "Expenses");
        //String[][][] balance_table = createBalance(statement, "Balance", incomes_table, expenses_table);

        model.put("statement_incomes_table", incomes_table);
        model.put("statement_expenses_table", expenses_table);
        //model.put("statement_balance_table", balance_table);   
    }

    public static String[][][] createStatementTable(JDBC jdbc, Statement statement, String type) {

        Header[] headers = jdbc.getHeaderCategories(selected_year, type);

        // Create table
        String[][][] table = new String[headers.length][][];
        float[] header_total = new float[STATEMENT_COLS - 3];

        // For each header in the table
        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length + 1][];
            float[] column_total = new float[STATEMENT_COLS - 3];

            // For each category in the header 
            for (int j = 0; j < table[i].length - 1; j++) {
               fillCellsInRow(statement, table, headers[i].categories[j], column_total, header_total, i, j);
            }
            Category total_category = new Category(-1, "Total", headers[i].name, headers[i].type);
            fillCellsInRow(statement, table, total_category, column_total, header_total, i, table[i].length - 1);
        }
        return table;
    }

    public static void fillCellsInRow(Statement statement, String[][][] table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        table[i][j] = new String[STATEMENT_COLS];

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
        table[i][j][STATEMENT_COLS - 1] = String.format("$%.02f", category_total);
    }

}