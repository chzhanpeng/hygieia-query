/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.model;

import java.util.Objects;

public class KeyOpVal {
    private String key;
    private String op;
    private Object value;

    public KeyOpVal(String key, String op, Object value) {
        this.key = key;
        this.op = op;
        this.value = value;
    }


    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getOp() {
        return op;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyOpVal)) return false;
        KeyOpVal keyOpVal = (KeyOpVal) o;
        return Objects.equals(key, keyOpVal.key) &&
                Objects.equals(op, keyOpVal.op) &&
                Objects.equals(value, keyOpVal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, op, value);
    }
}
