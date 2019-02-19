package com.capitalone.dashboard.query.model;

import java.lang.reflect.Field;
import java.util.Set;

public class FieldSelection {

    private String type;
    private Set<Field> selections;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Field> getSelections() {
        return selections;
    }

    public void setSelections(Set<Field> selections) {
        this.selections = selections;
    }
}
