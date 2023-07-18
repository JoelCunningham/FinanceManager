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

public class BudgetPage extends Page {

    private final int TABLE_COLS = 16; // Number of columns in the table
    private final String PAGE_NAME = "budget"; // Name of the page

    private int selected_year; // The year to display data for

    private TablePanel<BudgetItem, StatementItem> table;

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

        // Code for selector
        DropdownYear year_selector = new DropdownYear(context, model, jdbc, PAGE_NAME, selected_year);
        selected_year = year_selector.load();

        // Code for tables
        Budget budget = new Budget(selected_year);
        Statement reference = new Statement(selected_year);
        table = new TablePanel<BudgetItem, StatementItem>(PAGE_NAME, TABLE_COLS, budget, reference, model, jdbc);
        table.load();

        // Code for saving changes
        table.save(context);

        // Code for importing and exporting budgets
        loadImport();
        loadExport();
    }

    private void loadImport() {
        Integer current_year = Calendar.getInstance().get(Calendar.YEAR);
        String name = "import_" + PAGE_NAME;

        // Code for selector
        DropdownYear year_select = new DropdownYear(context, model, jdbc, name, -1);
        year_select.remove(Integer.toString(current_year));
        year_select.remove("All");
        year_select.load();

        // Code for importing the budget
        String selected_year = context.formParam(name + "_year_select");
        String override = context.formParam(name + "checkbox");
        if (override == null) {
            override = "off";
        }
        if (selected_year != null && selected_year != "") {
           jdbc.importBudget(Integer.parseInt(selected_year), override.equals("on"));
           table.refresh();
        }        

    }

    private void loadExport() {
        String name = "export_" + PAGE_NAME;

        // Code for selectors
        DropdownYear year_select = new DropdownYear(context, model, jdbc, name, selected_year);
        year_select.load();

        // Code for exporting a summary
        String selected_year = context.formParam(name + "_year_select");

        if (selected_year != null && selected_year != "") {

            if (selected_year.equals("All")) { selected_year = "-1"; }
            int year = Integer.parseInt(selected_year);

            // Create table to export
            Budget budget = new Budget(year);
            Statement reference = new Statement(year);
            TablePanel<BudgetItem, StatementItem> export_table = new TablePanel<BudgetItem, StatementItem>(PAGE_NAME, TABLE_COLS, budget, reference, model, jdbc);
            export_table.load();

            // Export table
            export_table.export();
        }

        table.refresh();
    }

}