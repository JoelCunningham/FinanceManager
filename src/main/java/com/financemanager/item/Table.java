package com.financemanager.item;

import java.util.Map;

import com.financemanager.JDBC;

import com.financemanager.type.CashCollection;

import io.javalin.http.Context;

public abstract class Table<S, R> {

    protected String name;
    protected int year;
    protected int size;
    protected CashCollection<S> source;
    protected CashCollection<R> reference;
    protected Map<String, Object> model;
    protected JDBC jdbc;

    /**
     * Constructor for the Table class 
     * 
     * @param name The name of the table
     * @param year The year the table's data will represent
     * @param size The number of columns in the table
     * @param source The data source of the table
     * @param reference The data reference of the table
     * @param model The model to sumbit the table to
     * @param jdbc The database connection for the table
     */
    public Table(String name, int year, int size, CashCollection<S> source, CashCollection<R> reference, Map<String, Object> model, JDBC jdbc) {
        this.name = name;
        this.year = year;
        this.size = size;
        this.source = source;
        this.reference = reference;
        this.model = model;
        this.jdbc = jdbc;
    }

    /**
     * Load table, and submit to the model
     */
    public abstract void load();

    /**
     * Save changes to table
     * 
     * @param context The context to retrive the new data from
     */
    public abstract void save(Context context);
    
    /**
     * Refresh the table and its source
     */
    public void refresh() {
        source.load();
        this.load();
    }

    
}
