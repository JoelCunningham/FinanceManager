package com.financemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.financemanager.type.BudgetItem;
import com.financemanager.type.Header;
import com.financemanager.type.StatementItem;
import com.financemanager.type.Category;
/**
* Class for managing the JDBC Connection to a SQLLite Database.
* Allows SQL queries to be used with the SQLLite Database in Java.
*
* @author Joel Cunningham 2023
*/
public class JDBC {

    // "jdbc:sqlite::resource:database/FinanceManagerDatabase.db";
    private static final String DATABASE = "jdbc:sqlite:FinanceManagerDatabase.db";

    // Create a JDBC Object to communicate with the database
    public JDBC() {}

    public void addYear(int year) {
        
        String query = "INSERT INTO year(year) VALUES(?)";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            PreparedStatement statement = connection.prepareStatement(query);  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);

            statement.setInt(1, year);
            statement.executeUpdate();
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
    }
    
    public void addHeader(String name, int type_id) {
        
        String query = "INSERT INTO header (name, type_id) VALUES (\"" + name + "\", " + type_id + ");";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);

            statement.executeQuery(query);
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
    }

    public void addCategory(String name, int header_id) {
        
        String query = "INSERT INTO category (name, header_id) VALUES (\"" + name + "\", " + header_id + ");";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);

            statement.executeQuery(query);
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
    }

    public List<Integer> getYears() {
    
        List<Integer> years = new ArrayList<>();

        String query = "SELECT year FROM year";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) { 
                years.add(results.getInt(1)); 
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return years;
    }

    public List<String> getTypes() {
    
        List<String> types = new ArrayList<>();

        String query = "SELECT name FROM type;";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {
                types.add(results.getString("name")); 
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return types;
    }

    public List<String> getHeaders(int type_id) {
    
        List<String> headers = new ArrayList<>();

        String query = "SELECT name FROM header WHERE type_id = " + type_id;
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {
                headers.add(results.getString("name")); 
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return headers;
    }
    
    public List<String> getCategories(int header_id) {
    
        List<String> categories = new ArrayList<>();

        String query = "SELECT name FROM category WHERE header_id = " + header_id;
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {
                categories.add(results.getString("name")); 
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return categories;
    }

    public int getTypeID(String name) {
    
        int id = -1;

        String query = "SELECT id FROM type WHERE name = \"" + name + "\"";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            if (results.next()) { id =  results.getInt(1); }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return id;
    }

    public int getHeaderID(String name, int type_id) {
    
        int id = -1;

        String query = "SELECT id FROM header WHERE type_id = " + type_id + " AND name = \"" + name + "\"";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            if (results.next()) { id =  results.getInt(1); }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return id;
    }

    public int getCategoryID(String name, int header_id) {
    
        int id = -1;

        String query = "SELECT id FROM category WHERE header_id = " + header_id + " AND name = \"" + name + "\"";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            if (results.next()) { id =  results.getInt(1); }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return id;
    }
    
    public int getMaxYear() {
    
        int year = -1;

        String query = "SELECT MAX(year) FROM year";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            if (results.next()) { year =  results.getInt(1); }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return year;
    }

    public List<String[]> getUnusedCategories(int not_year) {
    
        List<String[]> categories = new ArrayList<>();

        String query = """
            SELECT category.id AS category_id, category.name AS category_name, header.name AS header_name, type.name AS type_name, MAX(category_year.year) AS max_year 
            FROM category 
            JOIN header ON category.header_id = header.id 
            JOIN type ON header.type_id = type.id 
            LEFT JOIN category_year ON category.id = category_year.category_id 
            WHERE NOT EXISTS (
                SELECT 1 
                FROM category_year 
                WHERE category_year.category_id = category.id AND category_year.year = 
            """      
            + not_year + 
            """
            )
            GROUP BY category.id;
            """;
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {

                String[] category = new String[5];

                category[0] = Integer.toString(results.getInt("category_id"));

                category[1] = results.getString("category_name"); 
                category[2] = results.getString("header_name"); 
                category[3] = results.getString("type_name"); 

                String year = Integer.toString(results.getInt("max_year"));
                if (year.equals("0")) { year = "Never";}
                category[4] = year;

                categories.add(category);
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
        return categories;
    }

    public Header[] getHeaderCategories(int year, String type) {
    
        int type_id = getTypeID(type);

        List<Header> headers = new ArrayList<>();

        String query = """
            SELECT category.id AS category_id, category.name AS category_name, type.name AS type_name, header.name AS header_name 
            FROM category_year 
            JOIN category ON category_year.category_id = category.id 
            JOIN header ON category.header_id = header.id 
            JOIN type ON header.type_id = type.id 
            WHERE type.id = '""" + type_id + "'";

            if (year != -1) {
                query += " AND category_year.year = '" + year + "'";
            }
            else {
                query += " GROUP BY category.id, category.name, type.name, header.name";
            }
            query += " ORDER BY type.name, header.name, category.name;";

        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            String curr_header = "";
            List<Category> categories = new ArrayList<>();

            while (results.next()) {

                int category_id = results.getInt("category_id");
                String category_name = results.getString("category_name");
                String header_name = results.getString("header_name");
                String type_name = results.getString("type_name");

                if (curr_header == "") { curr_header = header_name; }

                if (!curr_header.equals(header_name)) {

                    Category[] categories_array = new Category[categories.size()];
                    categories_array = categories.toArray(categories_array);

                    headers.add(new Header(curr_header, type_name, categories_array));
                    
                    categories.clear();
                    curr_header = header_name;
                }
                
                categories.add(new Category(category_id, category_name, header_name, type_name));
            }

            if (!categories.isEmpty()) {

                Category[] categories_array = new Category[categories.size()];
                categories_array = categories.toArray(categories_array);

                headers.add(new Header(curr_header, type, categories_array));
            }

        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }

        Header[] headers_array = new Header[headers.size()];
        headers_array = headers.toArray(headers_array);

        return headers_array;
    }
 
    public BudgetItem[] getBudgetItems(int year, int month) {
    
        List<BudgetItem> budget_items = new ArrayList<>();

        String query = """
            SELECT category_id, month, amount
            FROM budget
            """;
         
        if (year != -1 && month != -1) {
            query += " WHERE year = '" + Integer.toString(year) + "'" + " AND month = '" + String.format("%02d", month + 1) + "'";
        }
        else if (year != -1) { 
            query += " WHERE year = '" + Integer.toString(year) + "'";
        }

        else if (month != -1) { 
            query += " WHERE month = '" + String.format("%02d", month + 1) + "'";
        }
        
        query += ";";

        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {

                int category_id = results.getInt("category_id");
                int month_no = results.getInt("month");
                float amount = results.getFloat("amount");

                budget_items.add(new BudgetItem(category_id, month_no, amount));
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }

        BudgetItem[] budget_items_array = new BudgetItem[budget_items.size()];
        budget_items_array = budget_items.toArray(budget_items_array);

        return budget_items_array;
    }

    public StatementItem[] getStatementItems(int year, int month) {
        
        List<StatementItem> statement_items = new ArrayList<>();

        String query = """
            SELECT id, category_id, amount, date, details
            FROM cashflow 
            """;

        if (year != -1 && month != -1) {
            query += " WHERE strftime('%Y', date) = '" + Integer.toString(year) + "'" + " AND strftime('%m', date) = '" + String.format("%02d", month + 1) + "'";
        }
        else if (year != -1) { 
            query += " WHERE strftime('%Y', date) = '" + Integer.toString(year) + "'";
        }
        
        else if (month != -1) { 
            query += " WHERE strftime('%m', date) = '" + String.format("%02d", month + 1) + "'";
        }
        
        query += ";";

        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            while (results.next()) {

                int id = results.getInt("id");
                int category_id = results.getInt("category_id");
                float amount = results.getFloat("amount");
                String details = results.getString("details");
                String date = results.getString("date");

                statement_items.add(new StatementItem(id, category_id, amount, details, date));
            }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }

        StatementItem[] statement_items_array = new StatementItem[statement_items.size()];
        statement_items_array = statement_items.toArray(statement_items_array);

        return statement_items_array;
    }

    public void addBudgetItem(BudgetItem item, int year) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
    
            String checkQuery = "SELECT * FROM budget WHERE year = " + year + " AND month = " + item.month + " AND category_id = " + item.category_id + ";";
            ResultSet results = statement.executeQuery(checkQuery);

            if (results.next()) {
                String updateQuery = "UPDATE budget SET amount = " + item.amount + " WHERE year = " + year + " AND month = " + item.month + " AND category_id = " + item.category_id + ";";
                statement.executeUpdate(updateQuery);
            } 
            else {
                String insertQuery = "INSERT INTO budget (year, month, amount, category_id) VALUES (" + year + ", " + item.month + ", " + item.amount + ", " + item.category_id + ");";
                statement.executeUpdate(insertQuery);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try { if (connection != null) { connection.close(); } } 
            catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    public void addStatementItem(StatementItem item) {
        
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            if (item.id != -1) {
                String updateQuery = "UPDATE cashflow SET amount = " + item.amount + ", date = '" + item.date + "', details = '" + item.details + "', category_id = " + item.category_id + " WHERE id = " + item.id + ";";
                statement.executeUpdate(updateQuery);
            }
            else {
                String insertQuery = "INSERT INTO cashflow (amount, date, details, category_id) VALUES (" + item.amount + ", '" + item.date + "', '" + item.details + "', " + item.category_id + ");";            
                statement.executeUpdate(insertQuery);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try { if (connection != null) { connection.close(); } } 
            catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    public void addCategoryYear(int category_id, int year) {
        
        String query = "INSERT INTO category_year (category_id, year) VALUES (" + category_id + ", " + year + ");";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);

            statement.executeQuery(query);
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }
    }

    public void importBudget(int year, boolean override) {
        
        int current_year = Calendar.getInstance().get(Calendar.YEAR);

        String query = override ? "REPLACE " : "INSERT OR IGNORE ";
        query += """
            INTO budget (year, month, amount, category_id)
            SELECT """ + " " + current_year + """ 
            , month, amount, category_id
            FROM budget
            WHERE year = """ + " " + year + ";";

        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);

            statement.executeQuery(query);
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage()); //If there is an error, spring it
        } 
        finally {
            try { if (connection != null) { connection.close(); } }  //Code cleanup
            catch (SQLException e) { System.err.println(e.getMessage()); }//Connection close failed
        }

    }

}
