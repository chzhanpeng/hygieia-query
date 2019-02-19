/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.model;

import java.util.ArrayList;
import java.util.List;

public class QueryResponse<T> {
    private long total = 0;
    private long count = 0;
    private long totalPages=0;
    private List<T> values = new ArrayList<>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getValues() {
        return values;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
