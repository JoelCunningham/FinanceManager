package com.financemanager.page;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.financemanager.type.Budget;
import com.financemanager.item.DropdownYear;
import com.financemanager.item.TablePanel;

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
        Budget budget = new Budget(selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, budget, model, jdbc);
        table_panel.load();

        // Code for saving changes
        List<String> budget_list = context.formParams("budget_table");
        if (budget_list.size() != 0) {
            table_panel.save(budget_list);
        } 
    }

}