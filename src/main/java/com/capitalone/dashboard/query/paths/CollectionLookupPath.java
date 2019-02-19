/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;


import com.querydsl.core.types.dsl.CollectionPath;
import com.querydsl.core.types.dsl.PathBuilder;

public class CollectionLookupPath extends AbstractLookupPath {

    @Override
    public CollectionPath getPath(PathBuilder path, String field, Class<?> type) {

        return path.getCollection(field, type);
    }

}
