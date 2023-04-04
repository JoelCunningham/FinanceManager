package com.financemanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Calendar;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
* Homepage java class using Javalin, Thymeleaf & JDBC.
*
* @author Joel Cunningham 2023
*/
public class PageMain implements Handler{

    public  static final String     URL         = "/";
    private static final String     TEMPLATE    = "html/main.html";

    private static final int BUDGET_COLS = 14;
    private static       int BUDGET_ROWS =  0;

    private static boolean initial_post = true;
    private static String selected_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

    @Override
    public void handle(Context context) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        JDBC jdbc = new JDBC();

        budgetPage(context, model, jdbc);
      
        model.put("version", Helper.getGitVersion());

        context.render(TEMPLATE, model); //Make Javalin render the webpage
        
        initial_post = false;
    }

    public void budgetPage(Context context, Map<String, Object> model, JDBC jdbc) {
       
        // Code for year selector
        Map<String, String> year_select = new HashMap<>();
        initializeYearSelect(context, year_select);      

        // Create a budget object
        Budget budget = new Budget();
        budget.load(Integer.parseInt(selected_year));

        // Create a table resprestation of the budget object
        String[][] budget_table = createBudgetTable(budget);

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        String[][] new_budget_table = Helper.convertListToArray(budget_list, BUDGET_ROWS, BUDGET_COLS);
        
        Helper.print2DArray(new_budget_table);

        model.put("budget_table", budget_table);
        model.put("years", year_select);
    }

    public void initializeYearSelect(Context context, Map<String, String> year_select) {
        // String[] years = jdbc.getYears(); //TODO
        String[] years = {"2023", "2022", "2021"}; 
        // Fill dictionary of years
        for (String year : years) { year_select.put(year, "False"); }
        // If the user has selected a value, override the default
        if (!initial_post) { selected_year = context.formParam("year_selector"); }
        year_select.put(selected_year, "True");   
    }

    public static String[][] createBudgetTable(Budget budget) {

        //Header[] headers = jdbc.getHeaders(selected_year);
        Header[] headers = new Header[2];
        headers[0] = new Header("Head1", "Income", 3);
        headers[1] = new Header("Head2", "Expense", 3);
        for (Header header : headers) {
            header.addCategory(new Category(1, header.getName() + "Cat1"));
            header.addCategory(new Category(2, header.getName() + "Cat2"));
            header.addCategory(new Category(3, header.getName() + "Cat3"));
        }

        return fillBudgetTableCategories(budget, headers);
    }

    public static String[][] fillBudgetTableCategories(Budget budget, Header[] headers) {
          
        BUDGET_ROWS = getTableHeight(headers);
        String[][] budget_table = new String[BUDGET_ROWS][BUDGET_COLS];

        Index current_row = new Index();

        fillHeaderType(budget_table, headers, current_row, "Income");
        fillHeaderType(budget_table, headers, current_row, "Expense");

        return budget_table;
    }

    public static int getTableHeight(Header[] headers) {
        int header_count = headers.length;
        int category_count = 0;
        for (Header header : headers) {
            category_count += header.getCategories().length;
        }
        return 2 * header_count + category_count + 2 * 2;
    }

    public static void fillCellByRow(String[][] table, Index row, String value) {
        table[row.value][0] = value;
        row.value++;
    }

    public static void fillHeaderType(String[][] table, Header[] headers, Index row, String type) {
        fillCellByRow(table, row, type);
        for (Header header : headers) {
            if (header.getType() == type) { 
                fillHeader(table, header, row); 
            }     
        }
        fillCellByRow(table, row, type + " Total");
    }

    public static void fillHeader(String[][] table, Header header, Index row) {

        fillCellByRow(table, row, header.getName());
        for (Category category : header.getCategories()) {
            fillCellByRow(table, row, category.getName());
        }
        fillCellByRow(table, row, header.getName() + " Total");
    }

}
