package com.financemanager.pages;

import java.util.List;
import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public class TopnavForms {
    
    public static void loadCreateCategory(Context context, Map<String, Object> model, JDBC jdbc) {
        
        // Code for type selector
        List<String> type_select = jdbc.getTypes();  
        model.put("types", type_select);

        // Code for header selector
        List<String> income_headers = jdbc.getHeaders(jdbc.getTypeID("Incomes"));  
        model.put("income_headers", income_headers);
        List<String> expense_headers = jdbc.getHeaders(jdbc.getTypeID("Expenses"));  
        model.put("expense_headers", expense_headers); 

        model.put("category_exists", new String[]{"hidden", "", "", ""});

        String name = context.formParam("name_input");
        String type_name = context.formParam("type_select");
        String header_name = "";

        // Check if add is valid
        if (name != null && !name.isEmpty() && type_name != null) {
            if (type_name.equals("Incomes")){ 
                header_name = context.formParam("income_header_select");
            }
            else {
                header_name = context.formParam("expense_header_select");
            }

            int type_id = jdbc.getTypeID(type_name);
            int header_id = jdbc.getHeaderID(type_id, header_name);

            // Check if category exists
            List<String> categories = jdbc.getCategories(header_id);
            if (categories.contains(name)){
                model.put("category_exists", new String[]{"visible", name, header_name, type_name.toLowerCase()});
            }
            // Add category to database
            else {
                jdbc.addCategory(name, header_id);
                System.out.println("add!");
            } 
        }    
    }   
}
