package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.JDBC;
import com.financemanager.type.Budget;
import com.financemanager.type.BudgetItem;
import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;
import com.financemanager.item.DropdownYear;
import com.financemanager.item.TablePanel;

import io.javalin.http.Context;

public class SummaryPage extends Page {

    private final int TABLE_COLS = 16; // Number of columns in the table
    private final String PAGE_NAME = "summary"; // Name of the page

    private int selected_year; // The year to display data for

    /**
     * Construct a BudgetPage object with the given context, model and jdbc
     *
     * @param context the context of the page
     * @param model the model of the page
     * @param jdbc the jdbc of the page
     */
    public SummaryPage(Context context, Map<String, Object> model, JDBC jdbc) {
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
        Statement statement = new Statement(selected_year);
        Budget reference = new Budget(selected_year);
        TablePanel<StatementItem, BudgetItem> panel_table = new TablePanel<StatementItem, BudgetItem>(PAGE_NAME, selected_year, TABLE_COLS, statement, reference, model, jdbc); 
        panel_table.load();
        panel_table.colour();
    }

}