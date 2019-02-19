package com.capitalone.dashboard.query.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class KeyOpValTest {

    @Test
    public void equals() {
        KeyOpVal val1 = new KeyOpVal("field1", "eq", "test");
        KeyOpVal val2 = new KeyOpVal("field1", "eq", "test");
        assertEquals(val1,val2);
    }

    @Test
    public void equalsNot() {
        KeyOpVal val1 = new KeyOpVal("field1", "equalsIgnoreCase", "test");
        KeyOpVal val2 = new KeyOpVal("field1", "eq", "test");
        assertNotEquals(val1,val2);
    }
}