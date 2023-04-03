package com.financemanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.api.Request.HeadersListener;

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
    private static String selected_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    @Override
    public void handle(Context context) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        //JDBC jdbc = new JDBC();

        // Get dictionary of years and their selection status
        Map<String, String> year_select = initializeYearSelect(context);      

        // Budget page
        Budget budget = loadBudget();
        String[][] budget_table = createBudgetTable(budget);

        // Save changes to table
        List<String> budget_list = context.formParams("budget_table");
        String[][] new_budget_table = convertListToArray(budget_list);
        
        print2DArray(new_budget_table);


        model.put("budget_table", budget_table);

        model.put("years", year_select);

        model.put("version", Helper.getGitVersion());

        context.render(TEMPLATE, model); //Make Javalin render the webpage
        
        initial_post = false;
    }

    public Map<String, String> initializeYearSelect(Context context) {
        
        // String[] years = jdbc.getYears(); //TODO
        String[] years = {"2023", "2022", "2021"}; 
                
        // Create dictionary of years
        Map<String, String> year_select = new HashMap<>();
        for (String year : years) {
            year_select.put(year, "False");
        }
        
        // If the user has selected a value, override the default
        if (!initial_post) {
            selected_year = context.formParam("year_selector");
        }
        year_select.put(selected_year, "True");   

        return year_select;
    }


    public static Budget loadBudget() {
        
        //Budget budget = jdbc.getBudget('selected_year')
        BudgetItem[] budget_items = new BudgetItem[3];
        budget_items[0] = new BudgetItem(1, 1, 20);
        budget_items[1] = new BudgetItem(2, 7, 33);
        budget_items[2] = new BudgetItem(3, 4, 21);
        Budget budget = new Budget(2023, budget_items);

        return budget;    
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

        // Create budget table
        BUDGET_ROWS = getTableHeight(headers);
        String[][] budget_table = new String[BUDGET_ROWS][BUDGET_COLS];

        int current_row = 0;

        budget_table[current_row][0] = "Incomes";
        current_row++;

        for (Header header : headers) {
            if (header.getType() == "Income") { 
                current_row = fillHeader(budget_table, header, current_row); 
            }     
        }

        budget_table[current_row][0] = "Income Total";
        current_row += 1;
        budget_table[current_row][0] = "Expenses";
        current_row += 1;

        for (Header header : headers) {
            if (header.getType() == "Expense") {
                current_row = fillHeader(budget_table, header, current_row); 
            }
        }
        budget_table[current_row][0] = "Expense Total";
        current_row += 1;

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

    public static int fillHeader(String[][] table, Header header, int current_row) {

        table[current_row][0] = header.getName();
        current_row++;
        for (Category category : header.getCategories()) {
            table[current_row][0] = category.getName(); 
            current_row++;
        }
        table[current_row][0] = header.getName() + " Total";
        current_row++;

        return current_row;
    }

    public static String[][] convertListToArray(List<String> list) {

        String[][] array = new String[BUDGET_ROWS][BUDGET_COLS];
        
        for (int i = 0; i < BUDGET_ROWS; i++) {
            
            for (int j = 0; j < BUDGET_COLS; j++) {
                
                int index = i * BUDGET_COLS + j;
                if (index < list.size() && list.get(index) != "") {
                    array[i][j] = list.get(index);
                } 
                else {
                    array[i][j] = null;
                }
            }
        }
        return array;
    }

    //TEMP
    public static void print2DArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

}
