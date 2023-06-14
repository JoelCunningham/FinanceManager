package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.JDBC;

import com.financemanager.item.DropdownYear;
import com.financemanager.item.TablePanel;
import com.financemanager.item.DropdownMonth;

import com.financemanager.type.Statement;

import io.javalin.http.Context;

public class StatementPage extends Page {

    private final int TABLE_COLS = 9; // Number of columns in the table
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

        DropdownYear year_selector = new DropdownYear(context, model, jdbc, PAGE_NAME, selected_year);
        DropdownMonth month_selector = new DropdownMonth(context, model, jdbc, PAGE_NAME, selected_month);  
        selected_year = year_selector.load();   
        selected_month = month_selector.load(); 

        Statement statement = new Statement(selected_month, selected_year);
        TablePanel table_panel = new TablePanel(PAGE_NAME, selected_year, TABLE_COLS, statement, model, jdbc);
        table_panel.load();

    }

}