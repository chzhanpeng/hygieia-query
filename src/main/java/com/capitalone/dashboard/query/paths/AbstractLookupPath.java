/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.paths;

import com.capitalone.dashboard.query.model.QueryException;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;


public abstract class AbstractLookupPath {

    public  BooleanExpression getBooleanExpression(PathBuilder pathBuilder, String field, String op, Object value, Class<?> fieldType) throws QueryException {
        Path path = getPath(pathBuilder, field, fieldType);
        try {
            Method method = (value instanceof Collection) ? getMethod(path.getClass(), op, value, Collection.class, path.getClass()) : getMethod(path.getClass(), op, value, fieldType, path.getClass());
            if (method.getParameterCount() > 0) {
                return (BooleanExpression) method.invoke(path, value);
            } else {
                return (BooleanExpression) method.invoke(path);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new QueryException("Invalid operation '" + op + '\'' + " on field '" + field + '\'', e, QueryException.INVALID_OPERATOR);
        }
    }


    protected static Method getMethod(Class<?> clazz, String name, Object value, Class<?> fieldType, Class<?> originalClass) throws NoSuchMethodException {
        if (value == null) {
            return clazz.getMethod(name);
        }
        try {
            // Try getting method with the field type
            return clazz.getMethod(name, fieldType);
        } catch (NoSuchMethodException e) {
            try {
                //If failed, try with Comparable
                return clazz.getMethod(name, Comparable.class);
            } catch (NoSuchMethodException ne) {
                //Last resort - with Object type
                return clazz.getMethod(name, Object.class);
            }
        }
    }


    public abstract Path getPath(PathBuilder path, String field, Class<?> type) throws QueryException;
}
