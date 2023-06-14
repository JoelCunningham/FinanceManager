package com.financemanager.item;

import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public abstract class Dropdown {

    protected Context context;
    protected Map<String, Object> model;
    protected JDBC jdbc;
    protected String name;

    /**
     * Constructor for the Dropdown class.
     *
     * @param context The context in which the Dropdown is being used.
     * @param model   The model to which the Dropdown will add data.
     * @param jdbc    The JDBC object used to retrieve data from the database.
     * @param name    The name of the Dropdown.
     */
    public Dropdown(Context context, Map<String, Object> model, JDBC jdbc, String name) {
        this.context = context;
        this.model = model;
        this.jdbc = jdbc;
        this.name = name;
    }

    /**
     * Loads data into the Dropdown.
     */
    public abstract int load();
}