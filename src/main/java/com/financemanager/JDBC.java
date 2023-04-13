package com.financemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
* Class for managing the JDBC Connection to a SQLLite Database.
* Allows SQL queries to be used with the SQLLite Database in Java.
*
* @author Joel Cunningham 2023
*/
public class JDBC {

    private static final String DATABASE = "jdbc:sqlite:database/FinanceManagerDatabase.db";

    // Create a JDBC Object to communicate with the database
    public JDBC() {}

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
        System.out.println(query);
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

}
