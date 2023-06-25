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

    // The number of descriptor columns
    // These are not used in calculations
    private static final int NUM_DESC = 3; 

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
     * Create sub tables for a flow type
     * 
     * @param type The type of the flow. Either "Incomes" or "Expenses"
     * @return The sub tables of the flow
     */
    private String[][][] createFlow(String type) {
        // Get the header for the flow type
        Header[] headers = jdbc.getHeaderCategories(year, type);
        // 3D array represents a list of sub tables
        String[][][] table = new String[headers.length][][];

        // Create a sub table for every header
        for (int i = 0; i < headers.length; i++) {
            table[i] = createSubTable(headers[i]);
        }
        return table;
    }

    /**
     * Create a sub table for a header
     * 
     * @param header The header to create the table for
     * @return The header sub table
     */
    private String[][] createSubTable(Header header) {
        // 2D array represents a sub table
        String[][] header_table = new String[header.categories.length + 1][];
        // Total value of each column
        float[] column_total = new float[size - NUM_DESC];

        // Fill each row for each category of the header
        for (int i = 0; i < header_table.length - 1; i++) {
            fillRow(header_table, header.categories[i], column_total, i);
        }
        // Create a total row for the header
        Category total_category = new Category(-1, "Total", header.name, header.type);
        fillRow(header_table, total_category, column_total, header_table.length - 1);

        return header_table;
    }

    /**
     * Fill a row (category) of data in a sub table
     * 
     * @param sub_table The sub table the row belongs to
     * @param category The category of the row
     * @param column_total The current total of the current column
     * @param row_index The current row index
     */
    private void fillRow(String[][] sub_table, Category category, float[] column_total, int row_index) {
        // 1D array represents a category in the sub table
        sub_table[row_index] = new String[size];
        // Set the values for the type, header, and category name cells
        sub_table[row_index][0] = category.type;
        sub_table[row_index][1] = category.header_name;
        sub_table[row_index][2] = category.name;

        float category_total = 0;
        // For each value cell in the category
        for (int i = NUM_DESC; i < sub_table[row_index].length - 1; i++) {
            // Determine the value of the cell depending on weather it is a total row or not
            boolean is_total_row = row_index == sub_table.length - 1;
            float value = is_total_row ? column_total[i - NUM_DESC] : source.findValue(category.id, i - NUM_DESC + 1, size - NUM_DESC - 1) ;
            // Update the total trackers
            column_total[i - NUM_DESC] += value;
            category_total += value;
            // Set the value in the table
            sub_table[row_index][i] = Helper.floatToCurrency(value);
        }
        // Set the category total cell
        sub_table[row_index][size - 1] = Helper.floatToCurrency(category_total);
    }

    /**
     * Create the balance sub table for the data in flow tables
     * 
     * @param income_flows The table of income flows
     * @param expense_flows The table of expense flows
     * @return The balance sub table
     */
    private String[][][] createBalance(String[][][] income_flows, String[][][] expense_flows) {
        // 3D array represents the totals for incomes, expenses and the balance
        String[][][] table = new String[1][3][size];
        // Get the totals for each flow
        float[] incomes_total = getTypeTotal(income_flows);
        float[] expenses_total = getTypeTotal(expense_flows);

        // Set the values for the type, header, and category name cells
        table[0][0][0] = "Balance";
        table[0][0][1] = "Total";
        table[0][0][2] = "Incomes Total";

        table[0][1][0] = "Balance";
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";

        table[0][2][0] = "Balance";
        table[0][2][1] = "Total";
        table[0][2][2] = "Balance";

        // For each value cell
        for (int i = NUM_DESC; i < size; i++) {
            // Set the value of the income and expense total
            table[0][0][i] = Helper.floatToCurrency(incomes_total[i - NUM_DESC]);
            table[0][1][i] = Helper.floatToCurrency(expenses_total[i - NUM_DESC]);
            // Calculate and set the value of the balance total (incomes - expenses)
            table[0][2][i] = Helper.floatToCurrency(incomes_total[i - NUM_DESC] - expenses_total[i - NUM_DESC]);
        }
        return table;
    }

    /**
     * Determine the totals of each column for a type 
     * 
     * @param flows All of the flows of a type
     * @return An array of total values for the flow
     */
    private float[] getTypeTotal(String[][][] flows) {
        float[] total = new float[size - NUM_DESC];
        for (int i = 0; i < flows.length; i++) {
            for (int j = NUM_DESC; j < size; j++) {
                total[j - NUM_DESC] += Helper.currencyToFloat(flows[i][flows[i].length - 1][j]);
            }
        }
        return total;
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
            else if (column_index >= NUM_DESC && column_index < size - 1) {
                
                // Determine current position
                curr_category = i / (size) - past_categories;
                curr_month    = i % (size);

                // If the current value is different from the original value, update the database
                if (Helper.currencyToFloat(changes_list.get(i)) != Helper.currencyToFloat(reference_table[curr_header][curr_category][curr_month])) {

                    int type_id = jdbc.getTypeID(type);
                    int header_id = jdbc.getHeaderID(reference_table[curr_header][curr_category][1], type_id);
                    int category_id = jdbc.getCategoryID(reference_table[curr_header][curr_category][2], header_id);
                    
                    // Save the item to the database
                    BudgetItem item = new BudgetItem(category_id, curr_month - 2, Helper.currencyToFloat(changes_list.get(i)));
                    jdbc.addBudgetItem(item, year);               
                }
            }
        }
    }

}