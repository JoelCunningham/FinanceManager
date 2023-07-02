package com.financemanager.item;

import java.util.Map;

import com.financemanager.Helper;
import com.financemanager.JDBC;

import com.financemanager.type.Header;

import io.javalin.http.Context;

public class DropdownField extends Dropdown<String> {
   
    private int year;
    private int month;

    /**
     * Constructor for the DropdownField class
     *
     * @param context       The context in which the DropdownField is being used
     * @param model         The model to which the DropdownField will add data
     * @param jdbc          The JDBC object used to retrieve data from the database
     * @param name          The name of the DropdownField
     * @param headers       The initially selected header in the DropdownField
     */
    public DropdownField(Context context, Map<String, Object> model, JDBC jdbc, String name, int year, int month) {
        super(context, model, jdbc, name);
        this.year = year;
        this.month = month;
    }

    /**
     * Loads data into the year selector and returns the selected year
     *
     * @return The selected year in the year selector
     */
    public int load() {

        Header[] income_headers = jdbc.getHeaderCategories(year, "Incomes");
        Header[] expense_headers = jdbc.getHeaderCategories(year, "Expenses");
        Header[] headers = Helper.combineArrays(income_headers, expense_headers);

        // Track position in name arrays
        int income_index = 0, expense_index = 0;
       
        // Construct name arrays 
        String[][] header_names = new String[2][];
        String[][][] category_names = new String[2][][];

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

        // Date Picker
        model.put("date_min", Helper.getMinDate(year, month));
        model.put("date_max", Helper.getMaxDate(year, month));

        return 1;
    }

}