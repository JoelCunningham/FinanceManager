package com.financemanager.page;

import java.util.Calendar;
import java.util.Map;

import com.financemanager.Helper;
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

    private TablePanel<StatementItem, BudgetItem> panel_table;
    private TableDetailed detailed_table;

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
        Budget reference = new Budget(selected_month, selected_year);
        
        panel_table = new TablePanel<StatementItem, BudgetItem>(PAGE_NAME, TABLE_COLS, statement, reference, model, jdbc);
        panel_table.load();
        
        detailed_table = new TableDetailed(PAGE_NAME, CASHFLOW_COLS, statement, reference, model, jdbc);
        detailed_table.load();
        
        // Code for adding and saving changes
        detailed_table.add(context);
        detailed_table.save(context);
        panel_table.refresh();

        loadExport();
    }

    private void loadExport() {

        String name = "export_" + PAGE_NAME;

        // Code for selectors
        DropdownYear year_select = new DropdownYear(context, model, jdbc, name, selected_year);
        year_select.add("All");
        year_select.load();

        DropdownMonth month_select = new DropdownMonth(context, model, jdbc, name, selected_month);
        month_select.add("All");
        month_select.load();

        // Code for exporting a statement
        String selected_year = context.formParam(name + "_year_select");
        String selected_month = context.formParam(name + "_month_select");

        if (selected_year != null && selected_year != "" && selected_month != null && selected_month != "") {

            if (selected_year.equals("All")) { selected_year = "-1"; }
            int year = Integer.parseInt(selected_year);
            int month = Helper.monthToInt(selected_month);

            // Create table to export
            Statement statement = new Statement(month, year);
            Budget reference = new Budget(month, year);
            TableDetailed export_table = new TableDetailed(PAGE_NAME, CASHFLOW_COLS, statement, reference, model, jdbc);
            export_table.load();

            // Export table
            export_table.export();
        }

        detailed_table.refresh();
        panel_table.refresh();
    }

}