package com.financemanager.pages;

import java.util.List;
import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public class TopnavForms {

    public static void loadCreateCategory(Context context, Map<String, Object> model, JDBC jdbc) {
        
        // Code for type selector
        List<String> type_select = jdbc.getTypes();  
        model.put("c_types", type_select);

        // Code for header selector
        List<String> income_headers = jdbc.getHeaders(jdbc.getTypeID("Incomes"));  
        model.put("c_income_headers", income_headers);
        List<String> expense_headers = jdbc.getHeaders(jdbc.getTypeID("Expenses"));  
        model.put("c_expense_headers", expense_headers); 

        model.put("category_exists", new String[]{"hidden", "", "", ""});

        String name = context.formParam("c_name_input");
        String type_name = context.formParam("c_type_select");
        String header_name = ""; // Determined by selected type

        // Check if add is valid
        if (name != null && !name.isEmpty() && type_name != null) {
            if (type_name.equals("Incomes")){ 
                header_name = context.formParam("c_income_header_select");
            }
            else {
                header_name = context.formParam("c_expense_header_select");
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
            } 
        }    
    }   

    public static void loadCreateHeader(Context context, Map<String, Object> model, JDBC jdbc) {
    
        // Code for type selector
        List<String> type_select = jdbc.getTypes();  
        model.put("h_types", type_select);

        String name = context.formParam("h_name_input");
        String type_name = context.formParam("h_type_select");

        model.put("header_exists", new String[]{"hidden", "", ""});

        // Check if add is valid
        if (name != null && !name.isEmpty() && type_name != null) {
        
            int type_id = jdbc.getTypeID(type_name);

            // Check if header exists
            List<String> categories = jdbc.getHeaders(type_id);
            if (categories.contains(name)){
                model.put("header_exists", new String[]{"visible", name, type_name.toLowerCase()});
            }
            // Add category to database
            else {
                jdbc.addHeader(name, type_id);
            } 
        }    
    
    }
}
