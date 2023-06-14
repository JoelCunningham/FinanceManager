package com.financemanager.page;

import java.util.Map;

import com.financemanager.JDBC;

import io.javalin.http.Context;

public abstract class Page {

    protected Context context;
    protected Map<String, Object> model;
    protected JDBC jdbc;

    /**
     * Construct a Page object with the given context, model and jdbc
     *
     * @param context the context of the page
     * @param model the model of the page
     * @param jdbc the jdbc of the page
     */
    public Page(Context context, Map<String, Object> model, JDBC jdbc) {
        this.context = context;
        this.model = model;
        this.jdbc = jdbc;
    }

    /**
     * Load the page
     */
    public abstract void load();
}
