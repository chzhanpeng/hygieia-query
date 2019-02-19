/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.model;

import java.util.List;

public class QueryRequest {
    private String collection;
    private List<KeyOpVal> criteria;
    private List<String> fields;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<KeyOpVal> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<KeyOpVal> criteria) {
        this.criteria = criteria;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
