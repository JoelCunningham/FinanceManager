package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.JDBC;
import com.financemanager.type.Budget;
import com.financemanager.type.BudgetItem;
import com.financemanager.type.Statement;
import com.financemanager.type.StatementItem;

import com.financemanager.item.DropdownYear;
import com.financemanager.item.TableDetailed;
import com.financemanager.item.TablePanel;
import com.financemanager.item.DropdownField;
import com.financemanager.item.DropdownMonth;

import io.javalin.http.Context;

public class StatementPage extends Page {

    private final int TABLE_COLS = 9; // Number of columns in the table
    private final int CASHFLOW_COLS = 7; // Number of columns in the flow table
    private final String PAGE_NAME = "statement"; // Name of the page

    private int selected_year; // The year to display data for
    private int selected_month; // The month to display data for

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
        selected_year = year_selector.load();   

        DropdownMonth month_selector = new DropdownMonth(context, model, jdbc, PAGE_NAME, selected_month);  
        selected_month = month_selector.load();      

        DropdownField field_selectors = new DropdownField(context, model, jdbc, PAGE_NAME, selected_year, selected_month);         
        field_selectors.load();

        // Code for tables
        Statement statement = new Statement(selected_month, selected_year);
        Budget reference = new Budget(selected_year);
        
        TablePanel<StatementItem, BudgetItem> panel_table = new TablePanel<StatementItem, BudgetItem>(PAGE_NAME, selected_year, TABLE_COLS, statement, reference, model, jdbc);
        panel_table.load();
        
        TableDetailed detailed_table = new TableDetailed(PAGE_NAME, selected_year, CASHFLOW_COLS, statement, reference, model, jdbc);
        detailed_table.load();
        
        // Code for adding and saving changes
        detailed_table.add(context);
        detailed_table.save(context);
        panel_table.refresh();
    }

}