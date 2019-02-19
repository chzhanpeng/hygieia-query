/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;


import com.querydsl.core.types.dsl.PathBuilder;

public class ObjectLookupPath extends AbstractLookupPath {

    @Override
    public PathBuilder getPath(PathBuilder path, String field, Class<?> type){
        return path.get(field, type);
    }
}
