/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;


import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;

public class NumberLookupPath extends AbstractLookupPath {

    @Override
    public NumberPath getPath(PathBuilder path, String field, Class<?> type) {
        return path.getNumber(field, Number.class);
    }
}
