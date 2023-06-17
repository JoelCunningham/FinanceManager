package com.financemanager.page;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import com.financemanager.item.DropdownYear;
import com.financemanager.item.TablePanel;
import com.financemanager.item.DropdownMonth;
import com.financemanager.type.Header;
import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;

import io.javalin.http.Context;

public class StatementPage extends Page {

    private final int TABLE_COLS = 9; // Number of columns in the table
    private final int CASHFLOW_COLS = 5; // Number of columns in the flow table
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

        // Code for adding records
        addNew();

        // Code for saving changes
        // List<String> budget_list = context.formParams("budget_table");
        // if (budget_list.size() != 0) {
        //     table_panel.save(budget_list);
        // } 
    }

    private void loadCashFlowTable(Statement statement) {

        StatementItem[] items = statement.items;
        String[][] table = new String[items.length][CASHFLOW_COLS];
        
        Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        Arrays.sort(items);
        for (int i = 0; i < items.length; i++) {
            table[i][0] = Helper.idToHeader(items[i].category_id, headers);
            table[i][1] = Helper.idToName(items[i].category_id, headers);
            table[i][2] = Float.toString(items[i].amount);
            table[i][3] = items[i].details;
            table[i][4] = items[i].date;
        }
        model.put("cashflow_table", table);
    }

    private void loadSelectors() {

        Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        String[] header_names = new String[headers.length];
        String[][] category_names = new String[headers.length][];

        for (int i = 0; i < headers.length; i++) {
            header_names[i] = headers[i].name;
            String[] categories = new String [headers[i].categories.length];
            for (int j = 0; j < headers[i].categories.length; j ++) {
                categories[j] = headers[i].categories[j].name;
            }
            category_names[i] = categories;
        }

        model.put("cashflow_headers", header_names);
        model.put("cashflow_categories", category_names);
    }

    private void addNew() {
        String header = context.formParam("cashflow_header");
        String category = context.formParam("cashflow_category");
        String value = context.formParam("cashflow_value");
        String date = context.formParam("cashflow_date");
        String details = context.formParam("cashflow_details");

        if (header != "" && header != null && category != "" && category != null && value != "" && value != null && date != "" && date != null) {
            //TODO: Get to work with Income and Expense
            Header[] income_headers = jdbc.getHeaderCategories(selected_year, "Incomes");
            Header[] expense_headers = jdbc.getHeaderCategories(selected_year, "Expenses");
            Header[] headers = Helper.combineArrays(income_headers, expense_headers);
            
            int id = Helper.getCategoryId(header, category, headers);
            int amount = Integer.parseInt(value);

            StatementItem item = new StatementItem(id, amount, details, date);
            jdbc.addCashFlowItem(item);
        }

        Statement statement = new Statement(selected_month, selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, statement, model, jdbc);
        table_panel.load();
        loadCashFlowTable(statement);
    }

}