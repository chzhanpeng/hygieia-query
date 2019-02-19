/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;

import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.utils.ReflectionUtils;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.core.types.dsl.PathBuilder;

public class MapLookupPath extends AbstractLookupPath {

    @Override
    public MapPath getPath(PathBuilder path, String field, Class<?> type) throws QueryException {
        Class<?> keyType = ReflectionUtils.getFieldType(type, field);
        return path.getMap(field,keyType, type);
    }
}
