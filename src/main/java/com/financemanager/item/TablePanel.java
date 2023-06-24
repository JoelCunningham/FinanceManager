package com.financemanager.item;

import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import com.financemanager.type.BudgetItem;
import com.financemanager.type.CashCollection;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

import io.javalin.http.Context;

public class TablePanel<T> extends Table<T> {

    /**
     * Constructor for the Table class 
     * 
     * @param name The name of the table
     * @param year The year the table's data will represent
     * @param size The number of columns in the table
     * @param source The data source of the table
     * @param model The model to sumbit the table to
     * @param jdbc The database connection for the table
     */
    public TablePanel(String name, int year, int size, CashCollection<T> source, Map<String, Object> model, JDBC jdbc) {
        super(name, year, size, source, model, jdbc);
    }

    /**
     * Load each table, and submit it to the model
     */
    public void load() {
        String[][][] incomes = createFlow("Incomes");
        String[][][] expenses = createFlow("Expenses");
        String[][][] balance = createBalance(incomes, expenses);

        model.put(name + "_incomes_table", incomes);
        model.put(name + "_expenses_table", expenses);
        model.put(name + "_balance_table", balance);   
    }

    /**
     * Create a table for a flow
     * 
     * @param type The type of the flow. Either "Incomes" or "Expenses"
     */
    private String[][][] createFlow(String type) {
        Header[] headers = jdbc.getHeaderCategories(year, type);

        // Create table
        String[][][] table = new String[headers.length][][];
        float[] header_total = new float[size - 3];

        // For each header in the table
        for (int i = 0; i < table.length; i++) {

            table[i] = new String[headers[i].categories.length + 1][];
            float[] column_total = new float[size - 3];

            // For each category in the header 
            for (int j = 0; j < table[i].length - 1; j++) {
               fillCellsInRow(table, headers[i].categories[j], column_total, header_total, i, j);
            }
            Category total_category = new Category(-1, "Total", headers[i].name, headers[i].type);
            fillCellsInRow(table, total_category, column_total, header_total, i, table[i].length - 1);
        }
        return table;
    }

    /**
     * Fill a row (category) of data in a table
     * 
     * @param sub_table The sub table the row belongs to
     * @param category The category of the row
     * @param column_total The current total of the current column
     * @param header_total The current total of the current header
     * @param i The current header index
     * @param j The current category index
     */
    private void fillCellsInRow(String[][][] sub_table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        sub_table[i][j] = new String[size];

        // Set row head values
        sub_table[i][j][0] = category.type;
        sub_table[i][j][1] = category.header_name;
        sub_table[i][j][2] = category.name;
        
        float category_total = 0;

        // Loop through each week and add value
        for (int k = 3; k < sub_table[i][j].length - 1; k++) {
            
            float value = 0;

            // Normal operation
            if (j != sub_table[i].length - 1){
                value = source.findValue(category.id, k - 2);
                column_total[k - 3] += value;
            }
            // Total row
            else {
                value = column_total[k - 3]; 
                header_total[k - 3] += value;
            }

            sub_table[i][j][k] = String.format("$%.02f", value);
            category_total += value;
        }
        // Add category total
        sub_table[i][j][size - 1] = String.format("$%.02f", category_total);
    }

    /**
     * Compute the balance of the data in flow tables
     * 
     * @param income_flows The table of income flows
     * @param expense_flows The table of expense flows
     */
    private String[][][] createBalance(String[][][] income_flows, String[][][] expense_flows) {

        String[][][] table = new String[1][3][size];
        float[] incomes_total = new float[size - 3];
        float[] expenses_total = new float[size - 3];

        // Get incomes type totals
        table[0][0][0] = "Balance";
        table[0][0][1] = "Total";
        table[0][0][2] = "Incomes Total";
        for (int i = 0; i < income_flows.length; i++) {
            for (int j = 3; j < size; j++) {
                incomes_total[j - 3] += Helper.currencyToFloat(income_flows[i][income_flows[i].length - 1][j]);
            }
        }

        // Get expenses type totals
        table[0][1][0] = "Balance";
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";
        for (int i = 0; i < expense_flows.length; i++) {
            for (int j = 3; j < size; j++) {
                expenses_total[j - 3] += Helper.currencyToFloat(expense_flows[i][expense_flows[i].length - 1][j]);
            }
        }

        // Fill table and calculate balance
        table[0][2][0] = "Balance";
        table[0][2][1] = "Total";
        table[0][2][2] = "Balance";
        for (int i = 3; i < size; i++) {
            table[0][0][i] = String.format("$%.02f", incomes_total[i - 3]);
            table[0][1][i] = String.format("$%.02f", expenses_total[i - 3]);
            table[0][2][i] = String.format("$%.02f", Helper.currencyToFloat(table[0][0][i]) - Helper.currencyToFloat(table[0][1][i]));
        }
        return table;
    }

    /**
     * Save changes to tables.
     * Only works with TablePanels created from Budget sources
     * 
     * @param context The context to retrive the new data from
     */
     public void save(Context context) {

        List<String> changes_list = context.formParams("budget_table");
        
        if (changes_list.size() != 0) {
            // Get current stored data for reference
            String[][][] incomes_table = createFlow("Incomes");
            String[][][] expenses_table = createFlow("Expenses");
            
            // Separate new data into two flows
            int incomes_length = Helper.countElementsIn3dArray(incomes_table);
            int expenses_length = Helper.countElementsIn3dArray(expenses_table);

            List<String> incomes_list = changes_list.subList(0, incomes_length);
            List<String> expenses_list = changes_list.subList(incomes_length, expenses_length + incomes_length);

            // Save each flow
            saveType(incomes_list, incomes_table, "Incomes");
            saveType(expenses_list, expenses_table, "Expenses");

            refresh();
        }
    }

    /**
     * Save changes to a flow.
     * Only works with TablePanels created from Budget sources
     * 
     * @param changes_list The data to be saved in the table
     * @param reference_table The unaltered data
     * @param type The type of flow to save
     */
    private void saveType(List<String> changes_list, String[][][] reference_table, String type) {

        int curr_header = 0;
        int curr_category = 0;
        int curr_month = 0;

        int past_categories = 0;

        // Loop through the changes list
        for (int i = 0; i < changes_list.size(); i++) {

            // Tack column in 1D representation of 2D array
            int column_index = i % size;

            // If the cell is in the header column and it's not the first row and the header is different from the previous row's header
            if (column_index == 1 && i > size && !changes_list.get(i).equals(changes_list.get(i - size))) { 
                // Increment position trackers
                past_categories += reference_table[curr_header].length;
                curr_header++; 
            }
            // If the cell is in a data column 
            else if (column_index > 2 && column_index < 15) {
                
                // Determine current position
                curr_category = i / (size) - past_categories;
                curr_month    = i % (size);

                // If the current value is different from the original value, update the database
                if (Helper.currencyToFloat(changes_list.get(i)) != Helper.currencyToFloat(reference_table[curr_header][curr_category][curr_month])) {

                    int type_id = jdbc.getTypeID(type);
                    int header_id = jdbc.getHeaderID(reference_table[curr_header][curr_category][1], type_id);
                    int category_id = jdbc.getCategoryID(reference_table[curr_header][curr_category][2], header_id);

                    BudgetItem item = new BudgetItem(category_id, curr_month - 2, Helper.currencyToFloat(changes_list.get(i)));

                    jdbc.addBudgetItem(item, year);               
                }
            }
        }
    }

}