package com.financemanager.item;

import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.Helper.Type;
import com.financemanager.JDBC;

import com.financemanager.type.BudgetItem;
import com.financemanager.type.CashCollection;
import com.financemanager.type.Category;
import com.financemanager.type.Cell;
import com.financemanager.type.Header;

import io.javalin.http.Context;

public class TablePanel<S, R> extends Table<S, R> {

    // The number of descriptor columns (columns not used in calculations)
    private static final int NUM_DESC = 3; 
    // The tables of data
    private Cell[][][] incomes;
    private Cell[][][] expenses;
    private Cell[][][] balance;

    /**
     * Constructor for the Table class 
     * 
     * @param name The name of the table
     * @param year The year the table's data will represent
     * @param size The number of columns in the table
     * @param source The data source of the table
     * @param reference The data reference of the table
     * @param model The model to sumbit the table to
     * @param jdbc The database connection for the table
     */
    public TablePanel(String name, int year, int size, CashCollection<S> source, CashCollection<R> reference, Map<String, Object> model, JDBC jdbc) {
        super(name, year, size, source, reference, model, jdbc);
    }

    /**
     * Load each table, and submit it to the model
     */
    public void load() {
        incomes = createFlow(Type.Income);
        expenses = createFlow(Type.Expense);
        balance = createBalance(incomes, expenses);

        model.put(name + "_incomes_table", incomes);
        model.put(name + "_expenses_table", expenses);
        model.put(name + "_balance_table", balance);   
    }

    /**
     * Create sub tables for a flow (type)
     * 
     * @param type The type of the flow. Either "Incomes" or "Expenses"
     * @return The sub tables of the flow
     */
    private Cell[][][] createFlow(Type type) {
        // Get the header for the flow type
        Header[] headers = jdbc.getHeaderCategories(year, type.toString() + "s");
        // 3D array represents a list of sub tables
        Cell[][][] flow_table = new Cell[headers.length][][];

        // Create a sub table for every header
        for (int i = 0; i < headers.length; i++) {
            flow_table[i] = createSubTable(headers[i], type);
        }
        return flow_table;
    }

    /**
     * Create a sub table for a header
     * 
     * @param header The header to create the table for
     * @return The header sub table
     */
    private Cell[][] createSubTable(Header header, Type type) {
        // 2D array represents a sub table
        Cell[][] header_table = new Cell[header.categories.length + 1][];
        // Total value of each column
        float[] column_total = new float[size - NUM_DESC];
        float[] column_tooltip_total = new float[size - NUM_DESC];

        // Fill each row for each category of the header
        for (int i = 0; i < header_table.length - 1; i++) {
            fillRow(header_table, header.categories[i], type, column_total, column_tooltip_total, i);
        }
        // Create a total row for the header
        Category total_category = new Category(-1, "Total", header.name, header.type);
        fillRow(header_table, total_category, type, column_total, column_tooltip_total, header_table.length - 1);

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
    private void fillRow(Cell[][] sub_table, Category category, Type type, float[] column_total, float[] column_tooltip_total, int row_index) {
        // 1D array represents a category in the sub table
        sub_table[row_index] = new Cell[size];
        // Set the values for the type, header, and category name cells
        sub_table[row_index][0] = new Cell(category.type);
        sub_table[row_index][1] = new Cell(category.header_name);
        sub_table[row_index][2] = new Cell(category.name);

        float category_total = 0;
        float category_tooltip_total = 0;
        // For each value cell in the category
        for (int i = NUM_DESC; i < sub_table[row_index].length - 1; i++) {
            // Determine if the current row is the total row
            boolean is_total_row = row_index == sub_table.length - 1;
            // Set the values based on whether the current row is the total row        
            float value = is_total_row ? column_total[i - NUM_DESC] : source.findValue(category.id, i - NUM_DESC + 1);
            float tooltip = is_total_row ? column_tooltip_total[i - NUM_DESC] : reference.findValue(category.id, i - NUM_DESC + 1);
            // Update the total trackers
            column_total[i - NUM_DESC] += value;
            category_total += value;
            column_tooltip_total[i - NUM_DESC] += tooltip;
            category_tooltip_total += tooltip;
            // Set the value in the table
            sub_table[row_index][i] = new Cell(type, Helper.floatToCurrency(value), Helper.floatToCurrency(tooltip));
        }
        // Set the category total cell
        sub_table[row_index][size - 1] = new Cell(type, Helper.floatToCurrency(category_total), Helper.floatToCurrency(category_tooltip_total));
    }

    /**
     * Create the balance sub table for the data in flow tables
     * 
     * @param income_flows The table of income flows
     * @param expense_flows The table of expense flows
     * @return The balance sub table
     */
    private Cell[][][] createBalance(Cell[][][] income_flows, Cell[][][] expense_flows) {
        // 3D array represents the totals for incomes, expenses and the balance
        Cell[][][] balance_table = new Cell[1][3][size];
        // Get the totals for each flow
        Cell[] incomes_total = getTypeTotal(income_flows, Type.Income);
        Cell[] expenses_total = getTypeTotal(expense_flows, Type.Expense);

        // Set the values for the type, header, and category name cells
        balance_table[0][0][0] = new Cell("Balance");
        balance_table[0][0][1] = new Cell("Total");
        balance_table[0][0][2] = new Cell("Incomes Total");

        balance_table[0][1][0] = new Cell("Balance");
        balance_table[0][1][1] = new Cell("Total");
        balance_table[0][1][2] = new Cell("Expenses Total");

        balance_table[0][2][0] = new Cell("Balance");
        balance_table[0][2][1] = new Cell("Total");
        balance_table[0][2][2] = new Cell("Balance");

        // For each value cell
        for (int i = NUM_DESC; i < size; i++) {
            // Set the value of the income and expense total
            balance_table[0][0][i] = incomes_total[i - NUM_DESC];
            balance_table[0][1][i] = expenses_total[i - NUM_DESC];
            // Calculate and set the value of the balance total (incomes - expenses)
            float value = Helper.currencyToFloat(incomes_total[i - NUM_DESC].value) - Helper.currencyToFloat(expenses_total[i - NUM_DESC].value);
            float tooltip = Helper.currencyToFloat(incomes_total[i - NUM_DESC].tooltip) - Helper.currencyToFloat(expenses_total[i - NUM_DESC].tooltip);
            balance_table[0][2][i] = new Cell(Type.Income, Helper.floatToCurrency(value), Helper.floatToCurrency(tooltip));
        }
        return balance_table;
    }

    /**
     * Determine the totals of each column for a type 
     * 
     * @param flows All of the flows of a type
     * @return An array of total values for the flow
     */
    private Cell[] getTypeTotal(Cell[][][] flows, Type type) {
        Cell[] total = new Cell[size - NUM_DESC];
        Float[] value = new Float[size - NUM_DESC];
        Float[] tooltip = new Float[size - NUM_DESC];
        for (int i = 0; i < size - NUM_DESC; i++) {
            value[i] = Float.valueOf(0);
            tooltip[i] = Float.valueOf(0);
        }
        for (int i = 0; i < flows.length; i++) {
            for (int j = NUM_DESC; j < size; j++) {
                value[j - NUM_DESC] += Helper.currencyToFloat(flows[i][flows[i].length - 1][j].value);
                tooltip[j - NUM_DESC] += Helper.currencyToFloat(flows[i][flows[i].length - 1][j].tooltip);
            }
        }
        for (int i = 0; i < size - NUM_DESC; i++) {
            total[i] = new Cell(type, Helper.floatToCurrency(value[i]), Helper.floatToCurrency(tooltip[i]));
        }
        return total;
    }

    public void colour() {
        incomes = colourType(incomes);
        expenses = colourType(expenses);
        balance = colourType(balance);

        model.put(name + "_incomes_table", incomes);
        model.put(name + "_expenses_table", expenses);
        model.put(name + "_balance_table", balance);   
    }

    private Cell[][][] colourType(Cell[][][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                for (int k = NUM_DESC; k < table[i][j].length; k++) {
                    table[i][j][k].setColour();
                }
            }
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
            Cell[][][] incomes_table = createFlow(Type.Income);
            Cell[][][] expenses_table = createFlow(Type.Expense);
            
            // Separate new data into two flows
            int incomes_length = Helper.countElementsIn3dArray(incomes_table);
            int expenses_length = Helper.countElementsIn3dArray(expenses_table);

            List<String> incomes_list = changes_list.subList(0, incomes_length);
            List<String> expenses_list = changes_list.subList(incomes_length, expenses_length + incomes_length);

            // Save each flow
            saveType(incomes_list, incomes_table, Type.Income);
            saveType(expenses_list, expenses_table, Type.Expense);

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
    private void saveType(List<String> changes_list, Cell[][][] reference_table, Type type) {

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
                if (Helper.currencyToFloat(changes_list.get(i)) != Helper.currencyToFloat(reference_table[curr_header][curr_category][curr_month].value)) {

                    int type_id = jdbc.getTypeID(type.toString() + "s");
                    int header_id = jdbc.getHeaderID(reference_table[curr_header][curr_category][1].value, type_id);
                    int category_id = jdbc.getCategoryID(reference_table[curr_header][curr_category][2].value, header_id);
                    
                    // Save the item to the database
                    BudgetItem item = new BudgetItem(category_id, curr_month - 2, Helper.currencyToFloat(changes_list.get(i)));
                    jdbc.addBudgetItem(item, year);               
                }
            }
        }
    }

}