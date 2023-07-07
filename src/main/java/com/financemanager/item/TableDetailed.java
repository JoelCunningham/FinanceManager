package com.financemanager.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.Helper.Type;
import com.financemanager.JDBC;

import com.financemanager.type.BudgetItem;
import com.financemanager.type.CashCollection;
import com.financemanager.type.Category;
import com.financemanager.type.Header;
import com.financemanager.type.StatementItem;

import io.javalin.http.Context;

public class TableDetailed extends Table<StatementItem, BudgetItem> {

    private String[][] cashflows;

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
    public TableDetailed(String name, int year, int size, CashCollection<StatementItem> source, CashCollection<BudgetItem> reference, Map<String, Object> model, JDBC jdbc) {
        super(name, year, size, source, reference, model, jdbc);
    }

    /**
     * Load the table, and submit it to the model
     */
    public void load() {
        cashflows = createFlow();
    
        model.put("cashflow_table", cashflows);
    }

    /**
     * Create table for the cash flow
     * 
     * @return The table of the flow
     */
    private String[][] createFlow() {
        // Get the items to be added to the 
        StatementItem[] items = source.items;
        // 2D array represents the list of cashflow items
        String[][] table = new String[items.length][size];
        
        // Determine headers used in flow
        Header[] income_headers = jdbc.getHeaderCategories(year, Type.Income.toString() + "s");
        Header[] expense_headers = jdbc.getHeaderCategories(year, Type.Expense.toString() + "s");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        Arrays.sort(items);

        for (int i = 0; i < items.length; i++) {
            // Infer category infomation from provided id
            Category category = Helper.idToCategory(items[i].category_id, headers);

            table[i][0] = Integer.toString(items[i].id);
            table[i][1] = category.type;
            table[i][2] = category.header_name;
            table[i][3] = category.name;
            table[i][4] = String.format("%.2f", items[i].amount);
            table[i][5] = items[i].details;
            table[i][6] = items[i].date;
        }
        return table;
    }

    /**
     * Add a new record to the table
     * 
     * @param context The context to retrive the new data from
     */
    public void add(Context context) {

        // Get fields from form elements  
        String type = context.formParam("cashflow_type");
        String header = context.formParam("cashflow_header");
        String category = context.formParam("cashflow_category");
        String value = context.formParam("cashflow_value");
        String date = context.formParam("cashflow_date");
        String details = context.formParam("cashflow_details");

        // If they have been correctly filled 
        if (type != null && header != null && category != null && value != null && date != null) {
            // Determine the id of the category
            Header[] income_headers = jdbc.getHeaderCategories(year, "Incomes");
            Header[] expense_headers = jdbc.getHeaderCategories(year, "Expenses");
            Header[] headers = Helper.combineArrays(income_headers, expense_headers);
            int category_id = Helper.getCategoryId(type, header, category, headers);
            // Parse the amount
            float amount = Float.parseFloat(value);

            // Add the new item to the database
            StatementItem item = new StatementItem(category_id, amount, details, date);
            jdbc.addStatementItem(item);
        }
       refresh();
    }

    /**
     * Save changes the tables
     * 
     * @param context The context to retrive the new data from
     */
     public void save(Context context) {
        
        List<String> changes_list = context.formParams("cashflow_table");
        
        if (changes_list.size() != 0) {
            // Get current stored data
            String[][] cashflow_table = createFlow();

            Header[] income_headers = jdbc.getHeaderCategories(year, "Incomes");
            Header[] expense_headers = jdbc.getHeaderCategories(year, "Expenses");
            Header[] headers = Helper.combineArrays(income_headers, expense_headers);
            
            // Loop through the changes list
            for (int i = 0; i < changes_list.size() - size + 1; i += size) {

                boolean is_changed = !(
                    changes_list.get(i + 0).equals(cashflow_table[i / size][0]) &&
                    changes_list.get(i + 1).equals(cashflow_table[i / size][1]) &&
                    changes_list.get(i + 2).equals(cashflow_table[i / size][2]) && 
                    changes_list.get(i + 3).equals(cashflow_table[i / size][3]) &&
                    changes_list.get(i + 4).equals(cashflow_table[i / size][4]) &&
                    changes_list.get(i + 5).equals(cashflow_table[i / size][5]) &&
                    changes_list.get(i + 6).equals(cashflow_table[i / size][6])
                );

                boolean is_valid = !(
                    changes_list.get(i + 1).equals("Type") ||
                    changes_list.get(i + 2).equals("Header") || 
                    changes_list.get(i + 3).equals("Category")
                );

                // Check if any felids in the record are changed
                if (is_changed && is_valid) {

                    int id = Integer.parseInt(changes_list.get(i + 0));
                    String type_name = changes_list.get(i + 1);
                    String header_name = changes_list.get(i + 2);
                    String category_name = changes_list.get(i + 3);
                    float amount = Helper.currencyToFloat(changes_list.get(i + 4));
                    String details = changes_list.get(i + 5);
                    String date = changes_list.get(i + 6);

                    int category_id = Helper.getCategoryId(type_name, header_name, category_name, headers);
                    
                    // Save the item to the database
                    StatementItem item = new StatementItem(id, category_id, amount, details, date); 
                    jdbc.addStatementItem(item);
                }
            }
            refresh();
        }
    }
    
}
