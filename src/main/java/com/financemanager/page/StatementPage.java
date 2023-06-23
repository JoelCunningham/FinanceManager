package com.financemanager.page;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import com.financemanager.item.DropdownYear;
import com.financemanager.item.TablePanel;
import com.financemanager.item.DropdownMonth;
import com.financemanager.type.Category;
import com.financemanager.type.Header;
import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;

import io.javalin.http.Context;

public class StatementPage extends Page {

    private final int TABLE_COLS = 9; // Number of columns in the table
    private final int CASHFLOW_COLS = 7; // Number of columns in the flow table

    private final int NUM_TYPES = 2; // Number of types (income & expense)

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
        Statement statement = new Statement(selected_month, selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, statement, model, jdbc);
        table_panel.load();

        loadCashFlowTable(statement);   
        loadSelectors();

        model.put("date_min", Helper.getMinDate(selected_year, selected_month));
        model.put("date_max", Helper.getMaxDate(selected_year, selected_month));

        // Code for adding records
        addNew();

        // Code for saving changes
        List<String> cashflow_list= context.formParams("cashflow_table");
        if (cashflow_list.size() != 0) {
            saveCashFlowTable(statement, cashflow_list);
        } 
    }

    /**
     * Load the table, and submit it to the model
     */
    private void loadCashFlowTable(Statement statement) {
        String[][] cashflows = createFlow(statement);
    
        model.put("cashflow_table", cashflows);
    }

    /**
     * Create a table for a flow
     */
    private String[][] createFlow(Statement statement) {
        StatementItem[] items = statement.items;
        String[][] table = new String[items.length][CASHFLOW_COLS];
        
        Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        Arrays.sort(items);

        for (int i = 0; i < items.length; i++) {

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

    private void loadSelectors() {

        Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        // Track position in name arrays
        int income_index = 0, expense_index = 0;
       
        // Construct name arrays 
        String[][] header_names = new String[NUM_TYPES][];
        String[][][] category_names = new String[NUM_TYPES][][];

        header_names[0] = new String[income_headers.length];
        header_names[1] = new String[expense_headers.length];

        category_names[0] = new String[header_names[0].length][];
        category_names[1] = new String[header_names[1].length][];

        // Add names to arrays based on type and header
        for (Header header : headers) {
            int type_index = header.type.equals("Incomes") ? 0 : 1;
            int index = type_index == 0 ? income_index++ : expense_index++;

            header_names[type_index][index] = header.name;

            category_names[type_index][index] = new String[header.categories.length];
            for (int j = 0; j < header.categories.length; j++) {
                category_names[type_index][index][j] = header.categories[j].name;
            }
        }

        model.put("cashflow_headers", header_names);
        model.put("cashflow_categories", category_names);
    }

    private void addNew() {

        // Get fields from form elements  
        String type = context.formParam("cashflow_type");
        String header = context.formParam("cashflow_header");
        String category = context.formParam("cashflow_category");
        String value = context.formParam("cashflow_value");
        String date = context.formParam("cashflow_date");
        String details = context.formParam("cashflow_details");

        // If they have been correctly filled 
        if (type != null && header != null && category != null && value != null && date != null) {

            Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
            Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
            Header[] headers = Helper.combineArrays(income_headers, expense_headers);
            
            int category_id = Helper.getCategoryId(type, header, category, headers);
            int amount = Integer.parseInt(value);

            StatementItem item = new StatementItem(category_id, amount, details, date);
            jdbc.addStatementItem(item);
        }

        Statement statement = new Statement(selected_month, selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, statement, model, jdbc);
        
        table_panel.load();
        loadCashFlowTable(statement);
    }

    private void saveCashFlowTable(Statement statement, List<String> changes_list) {
        // Get current stored data
        String[][] cashflow_table = createFlow(statement);

        int size = CASHFLOW_COLS; // Make member

        Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
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
                int category_id = Helper.getCategoryId(type_name, header_name, category_name, headers);
                
                float amount = Helper.currencyToFloat(changes_list.get(i + 4));
                String details = changes_list.get(i + 5);
                String date = changes_list.get(i + 6);
                
                StatementItem item = new StatementItem(id, category_id, amount, details, date); 

                jdbc.addStatementItem(item);
            }
        }

        statement = new Statement(selected_month, selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, statement, model, jdbc);
        
        table_panel.load();
        loadCashFlowTable(statement);
    }

}