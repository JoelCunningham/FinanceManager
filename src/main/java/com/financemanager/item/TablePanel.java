package com.financemanager.item;

import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;
import com.financemanager.type.BudgetItem;
import com.financemanager.type.CashCollection;
import com.financemanager.type.Category;
import com.financemanager.type.Header;

public class TablePanel {

    private String name;
    private int year;
    private int size;
    private CashCollection source;
    private Map<String, Object> model;
    private JDBC jdbc;

    public TablePanel(String name, int year, int size, CashCollection source, Map<String, Object> model, JDBC jdbc) {
        this.name = name;
        this.year = year;
        this.size = size;
        this.source = source;
        this.model = model;
        this.jdbc = jdbc;
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
     * @param table The table the row belongs to
     * @param category The category of the row
     * @param column_total The current total of the current column
     * @param header_total The current total of the current header
     * @param i The current header index
     * @param j The current category index
     */
    private void fillCellsInRow(String[][][] table, Category category, float[] column_total, float[] header_total, int i, int j) {
        
        table[i][j] = new String[size];

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
                value = source.findValue(category.id, k - 2);
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
        table[i][j][size - 1] = String.format("$%.02f", category_total);
    }

    /**
     * Compute the balance of the data in flow tables
     * 
     * @param incomes_table The table of income flows
     * @param expenses_table The table of expense flows
     */
    private String[][][] createBalance(String[][][] incomes_table, String[][][] expenses_table) {

        String[][][] table = new String[1][3][size];
        float[] incomes_total = new float[size - 3];
        float[] expenses_total = new float[size - 3];

        // Get incomes type totals
        table[0][0][0] = "Balance";
        table[0][0][1] = "Total";
        table[0][0][2] = "Incomes Total";
        for (int i = 0; i < incomes_table.length; i++) {
            for (int j = 3; j < size; j++) {
                incomes_total[j - 3] += Helper.currencyToFloat(incomes_table[i][incomes_table[i].length - 1][j]);
            }
        }

        // Get expenses type totals
        table[0][1][0] = "Balance";
        table[0][1][1] = "Total";
        table[0][1][2] = "Expenses Total";
        for (int i = 0; i < expenses_table.length; i++) {
            for (int j = 3; j < size; j++) {
                expenses_total[j - 3] += Helper.currencyToFloat(expenses_table[i][expenses_table[i].length - 1][j]);
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
     * @param flow_list The income and expense flows in the table
     */
     public void save(List<String> flow_list) {

        // Get current stored data
        String[][][] incomes_table = createFlow("Incomes");
        String[][][] expenses_table = createFlow("Expenses");
        
        int incomes_length = Helper.countElementsIn3dArray(incomes_table);
        int expenses_length = Helper.countElementsIn3dArray(expenses_table);

        // Separate new data
        List<String> incomes_list = flow_list.subList(0, incomes_length);
        List<String> expenses_list = flow_list.subList(incomes_length, expenses_length + incomes_length);

        saveType(incomes_list, incomes_table, "Incomes");
        saveType(expenses_list, expenses_table, "Expenses");

        source.load();
        this.load();
    }

    /**
     * Save changes to a flow.
     * Only works with TablePanels created from Budget sources
     * 
     * @param changes_list The alterted flow data in the table
     * @param reference_table The unaltered data
     * @param type The type of flow to save
     */
    private void saveType(List<String> changes_list, String[][][] reference_table, String type) {

        int curr_header = 0;
        int curr_category = 0;
        int curr_month = 0;

        int past_categories = 0;

        for (int i = 0; i < changes_list.size(); i++) {

            int column_index = i % size;

            if (column_index == 1 && i > size && !changes_list.get(i).equals(changes_list.get(i - size))) { 
                past_categories += reference_table[curr_header].length;
                curr_header++; 
            }
            else if (column_index > 2 && column_index < 15) {
                
                curr_category = i / (size) - past_categories;
                curr_month    = i % (size);

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