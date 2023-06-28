package com.financemanager.type;

import com.financemanager.Helper;
import com.financemanager.Helper.Type;

public class Cell {
    public String value;
    public String tooltip;
    public String colour;
    public Type type;

    public Cell(String value) {
        this.value = value;
    }

    public Cell(Type type, String value, String tooltip) {
        this.type = type;
        this.value = value;
        this.tooltip = tooltip;
    }

    public void setColour() {      
        float value_f = Helper.currencyToFloat(value);
        float tooltip_f = Helper.currencyToFloat(tooltip);
        
        if (value_f < tooltip_f && type == Type.Income) {
            this.colour = "var(--red-colour)";
        }
        if (value_f > tooltip_f && type == Type.Income) {
            this.colour = "var(--green-colour)";
        }
        if (value_f < tooltip_f && type == Type.Expense) {
            this.colour = "var(--green-colour)";
        }
        if (value_f > tooltip_f && type == Type.Expense) {
            this.colour = "var(--red-colour)";
        }
    }
}
