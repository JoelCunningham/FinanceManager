package com.financemanager.type;

import java.awt.Color;

import com.financemanager.Helper;

public class Cell {
    public String value;
    public String tooltip;
    public Color colour;
    public Type type;

    public enum Type {
        Income,
        Expense
    }

    public Cell(String value) {
        this.value = value;
    }

    public Cell(String value, String tooltip) {
        this.value = value;
        this.tooltip = tooltip;
    }

    public void colour(Type type) {      
        float value_f = Helper.currencyToFloat(value);
        float tooltip_f = Helper.currencyToFloat(tooltip);
        
        if (value_f < tooltip_f && type == Type.Income) {
            colour = Color.red;
        }
        if (value_f > tooltip_f && type == Type.Income) {
            colour = Color.green;
        }
        if (value_f < tooltip_f && type == Type.Expense) {
            colour = Color.green;
        }
        if (value_f > tooltip_f && type == Type.Expense) {
            colour = Color.red;
        }
    }
}
