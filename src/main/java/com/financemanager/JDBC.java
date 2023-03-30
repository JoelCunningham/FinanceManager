package com.financemanager;

/**
* Class for managing the JDBC Connection to a SQLLite Database.
* Allows SQL queries to be used with the SQLLite Database in Java.
*
* @author Joel Cunningham 2023
*/
public class JDBC {
 
    private static final String DATABASE = "jdbc:sqlite:database/Library Database.db";

    // Create a JDBC Object to communicate with the database
    public JDBC() { System.out.println("Created JDBC Connection Object"); }

}
