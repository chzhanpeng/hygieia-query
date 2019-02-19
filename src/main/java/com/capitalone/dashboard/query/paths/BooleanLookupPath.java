/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;


import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.PathBuilder;

public class BooleanLookupPath extends AbstractLookupPath {

    @Override
    public BooleanPath getPath(PathBuilder path, String field, Class<?> type) {
        return path.getBoolean(field);
    }
}
