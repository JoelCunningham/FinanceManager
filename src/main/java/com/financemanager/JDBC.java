package com.financemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
* Class for managing the JDBC Connection to a SQLLite Database.
* Allows SQL queries to be used with the SQLLite Database in Java.
*
* @author Joel Cunningham 2023
*/
public class JDBC {

    private static final String DATABASE = "jdbc:sqlite:database/FinanceManagerDatabase.db";

    // Create a JDBC Object to communicate with the database
    public JDBC() { System.out.println("Created JDBC Connection Object"); }

    public int getMaxYear() {
    
        int year = -1;

        String query = "SELECT MAX(year) FROM year";
        Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DATABASE); //Connect to JDBC data base         
            Statement statement = connection.createStatement();  //Prepare a new SQL Query 
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery(query); //Get Result

            statement.close(); //Close the statement 

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
}
