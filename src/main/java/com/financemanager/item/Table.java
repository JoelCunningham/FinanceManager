package com.financemanager.item;

import java.awt.FileDialog;
import java.awt.Frame;

import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

import com.financemanager.JDBC;

import com.financemanager.type.CashCollection;

import io.javalin.http.Context;

public abstract class Table<S, R> {

    protected String name;
    protected int size;
    protected CashCollection<S> source;
    protected CashCollection<R> reference;
    protected Map<String, Object> model;
    protected JDBC jdbc;

    /**
     * Constructor for the Table class 
     * 
     * @param name The name of the table
     * @param size The number of columns in the table
     * @param source The data source of the table
     * @param reference The data reference of the table
     * @param model The model to sumbit the table to
     * @param jdbc The database connection for the table
     */
    public Table(String name, int size, CashCollection<S> source, CashCollection<R> reference, Map<String, Object> model, JDBC jdbc) {
        this.name = name;
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
     * Export the table to csv
     */
    public abstract void export();
    
    /**
     * Refresh the table and its source
     */
    public void refresh() {
        source.load();
        this.load();
    }

    /**
     * General code for exporting a table
     * 
     * @param data The data to export
     * @param name The name of the table
     * @param offset Which columns to ignore
     */
    public void exportData(String[][] data, String name, int offset) {

        FileDialog file_dialog = new FileDialog((Frame) null, "Save " + name + " CSV File", FileDialog.SAVE);
        file_dialog.setFile(name + ".csv");
        file_dialog.setVisible(true);
        String file_name = file_dialog.getDirectory() + file_dialog.getFile();
        if (file_name != null) {
            if (!file_name.endsWith(".csv")) {
                file_name += ".csv";
            }
            try (FileWriter writer = new FileWriter(file_name)) {
                for (int i = 0; i < data.length; i++) {
                    for (int j = 1 + offset; j < data[i].length + offset; j++) {
                        writer.append(data[i][j]);
                        if (j < data[i].length + offset) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 
    
}
