/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.utils;

import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.paths.AbstractLookupPath;
import com.capitalone.dashboard.query.paths.BooleanLookupPath;
import com.capitalone.dashboard.query.paths.CollectionLookupPath;
import com.capitalone.dashboard.query.paths.MapLookupPath;
import com.capitalone.dashboard.query.paths.NumberLookupPath;
import com.capitalone.dashboard.query.paths.ObjectLookupPath;
import com.capitalone.dashboard.query.paths.StringLookupPath;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ReflectionUtils {

    /**
     * Get the QueryDSL Path based on a class type
     *
     * @param clazz
     * @return @AbstractLookupPath
     * @throws QueryException
     */
    public static AbstractLookupPath getPathLookup(Class<?> clazz) throws QueryException {
        Class<?> newClazz = getClass(clazz);
        if (Objects.equals(newClazz, String.class)) {
            return new StringLookupPath();
        } else if (Objects.equals(newClazz, Number.class) || (Number.class.isAssignableFrom(newClazz))) {
            return new NumberLookupPath();
        } else if (Objects.equals(newClazz, Map.class) || (Map.class.isAssignableFrom(newClazz))) {
            return new MapLookupPath();
        } else if (Objects.equals(newClazz, Collection.class) || (Collection.class.isAssignableFrom(newClazz))) {
            return new CollectionLookupPath();
        } else if (Objects.equals(newClazz, Boolean.class) || (Boolean.class.isAssignableFrom(newClazz))) {
            return new BooleanLookupPath();
        } else {
            return new ObjectLookupPath();
        }
    }

//    public static AbstractLookupPath getPathLookup(Class<?> clazz) throws QueryException {
//        Class<?> newClazz = getClass(clazz);
//        if (Objects.equals(newClazz, String.class)) {
//            return new StringLookupPath();
//        } else if (Objects.equals(newClazz, Number.class) || (Number.class.isAssignableFrom(newClazz))) {
//            return new NumberLookupPath();
//        } else if (Objects.equals(newClazz, Boolean.class) || (Boolean.class.isAssignableFrom(newClazz))) {
//            return new BooleanLookupPath();
//        } else {
//            return new ObjectLookupPath();
//        }
//    }

    /**
     * Normalizes primitives and Number class
     *
     * @param clazz
     * @return Class
     */


    public static Class<?> getClass(Class<?> clazz) {
        Class<?> tempClass = clazz.isPrimitive() ? Primitives.wrap(clazz) : clazz;
        return (tempClass.getSuperclass() == Number.class) ? Number.class : tempClass;
    }

    /**
     * Get Field from a class using field name.
     *
     * @param clazz
     * @param name
     * @return Field
     */
    public static Field getField(Class<?> clazz, String name) throws QueryException {
        Field field = null;
        Class<?> loop = clazz;
        while (loop != null && field == null) {
            try {
                field = loop.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                loop = loop.getSuperclass();
            }
        }
        if (field == null) {
            throw new QueryException("Field '" + name + "' not found in class '" + clazz.getName() + '\'',QueryException.INVALID_FIELD);
        }
        return field;
    }

    /**
     * Get type of field in the class
     *
     * @param clazz
     * @param fieldName
     * @return Class
     * @throws QueryException
     */

    public static Class<?> getFieldType(Class<?> clazz, String fieldName) throws QueryException {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            throw new QueryException("Field '" + fieldName + "' does not exist in class '" + clazz.getName() + '\'', QueryException.INVALID_FIELD);
        }
        return getClass(field.getType());
    }

    /**
     * Determines the type of the Map value given the Map key field.
     *
     * @param field
     * @return Class
     */
    public static Class<?> getContainerClass(Field field, int depth) {
        Class<?> type = field.getType();
        // this may be a duplicate check but want to protect this method
        if (!Map.class.isAssignableFrom(type) && !Collection.class.isAssignableFrom(type)) {
            return type;
        }
        try {
            return getContainerElementClass(field.getGenericType(), depth);
        } catch (ClassNotFoundException cne) {
            //nothing
        }
        return Object.class;
    }

    private static Class getContainerElementClass(Type type, int depth) throws ClassNotFoundException {
        int i = 0;
        Type loopType = type;
        while (i < depth) {
            if (loopType instanceof ParameterizedType) {
                int index = ((ParameterizedType) loopType).getActualTypeArguments().length - 1;
                loopType = (((ParameterizedType) loopType).getActualTypeArguments()[index]);
            } else {
                return Class.forName(loopType.getTypeName());
            }
            i++;
        }
        return (loopType instanceof ParameterizedType) ? Class.forName(((ParameterizedType) loopType).getRawType().getTypeName()) : Class.forName(loopType.getTypeName());
    }

    /**
     * Determines the type of the value
     *
     * @param value
     * @return Class
     */
    public static Class<?> getFieldTypeFromFieldValue(Object value) {
        if (value == null) {
            return Object.class;
        }
//        if (value instanceof Collection) {
//            return Collection.class;
//        }
        else if (value instanceof Number) {
            return Number.class;
        } else if (value instanceof Boolean) {
            return Boolean.class;
        } else if (value instanceof String) {
            return String.class;
        }
        return Object.class;
    }

    /**
     * Mostly downcasts Number fields
     *
     * @param lastFieldType
     * @return
     */
    public static Class<?> normalizeFieldType(Class<?> lastFieldType) {
        if (Number.class.isAssignableFrom(lastFieldType)) {
            return Number.class;
        }
        else if (Boolean.class.isAssignableFrom(lastFieldType)) {
            return Boolean.class;
        } else {
            return lastFieldType;
        }
    }


    public static Object getTypedValue(Object value, Class<?> lastFieldType)  {
        try {
            Class<?> clazz = Class.forName(lastFieldType.getName());
            if (value instanceof List) {
                LinkedList newList = new LinkedList();
                for (Object v : (List) value) {
                    Constructor constructor = clazz.getConstructor(v.getClass());
                    newList.add(constructor.newInstance(v));
                }
                return newList;
            } else {
                Constructor constructor = clazz.getConstructor(value.getClass());
                return constructor.newInstance(value);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // nothing.
        }
        return value;

    }
}
