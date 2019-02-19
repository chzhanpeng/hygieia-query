/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;


import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

public class StringLookupPath extends AbstractLookupPath {

    @Override
    public StringPath getPath(PathBuilder path, String field, Class<?> type) {
        return path.getString(field);
    }


}
