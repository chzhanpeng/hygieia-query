/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.KeyOpVal;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.paths.AbstractLookupPath;
import com.capitalone.dashboard.query.utils.ReflectionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;


public class OneExpression extends Terminal<BooleanExpression> {
    private AtomicReference<KeyOpVal> keyOpVal = new AtomicReference<>();
    private AtomicReference<Class<?>> rootClass = new AtomicReference<>();
    private AtomicReference<PathBuilder> pathBuilder = new AtomicReference<>();


    OneExpression(Class<?> rootClass, String key, String op, Object value) {
        keyOpVal.set(new KeyOpVal(key, op, value));
        pathBuilder.set(new PathBuilder<>(rootClass, rootClass.getTypeName()));
        this.rootClass.set(rootClass);
    }

    // Build pathbuilder and return the last but one type
    private Class<?> buildPath(Queue<String> queue, Class<?> refClass) throws QueryException {
        Class<?> loopClass = refClass;
        Field field = null;
        while (!StringUtils.isEmpty(queue.peek()) && (queue.size() > 1)) {
            String part = queue.remove();
            field = ReflectionUtils.getField(loopClass, part);
            Class<?> type = field.getType();
            //for collection types, set the type to generic type
            if (Collection.class.isAssignableFrom(type)) {
                type = ReflectionUtils.getContainerClass(field, 2);
            }
            pathBuilder.set(pathBuilder.get().get(part, type));

            if (queue.size() > 1 && (Map.class.isAssignableFrom(type) || (Collection.class.isAssignableFrom(type)))) {
                String mapKey = queue.remove();
                type = ReflectionUtils.getContainerClass(field, 1);

                if (Collection.class.isAssignableFrom(type) || (Map.class.isAssignableFrom(type))) {
                    type = ReflectionUtils.getContainerClass(field, 2);
                    pathBuilder.set(pathBuilder.get().get(mapKey, type));
                } else {
                    pathBuilder.set(pathBuilder.get().get(mapKey, type));
                }
            }
            loopClass = type;
        }
        String lastFieldName = queue.peek();

        if ((field != null) &&  (Map.class.isAssignableFrom(loopClass) || Collection.class.isAssignableFrom(loopClass))) {
            return ReflectionUtils.getContainerClass(field, 2);
        } else {
            Field lastField = ReflectionUtils.getField(loopClass, lastFieldName);
            return lastField.getType();
        }
    }

    @Override
    public BooleanExpression evaluate() throws QueryException {
        String f = keyOpVal.get().getKey();
        String[] parts = f.split(Pattern.quote("."));
        Queue<String> queue = new LinkedList<>(Arrays.asList(parts));

        Class<?> lastFieldType = buildPath(queue,rootClass.get());

        String lastFieldName = queue.remove();

        if (lastFieldType == Object.class) {
            lastFieldType = ReflectionUtils.getFieldTypeFromFieldValue(keyOpVal.get().getValue());
        }
        lastFieldType = ReflectionUtils.normalizeFieldType(lastFieldType);
        AbstractLookupPath path = ReflectionUtils.getPathLookup(lastFieldType);
        Object modifiedValue = keyOpVal.get().getValue() != null ? ReflectionUtils.getTypedValue(keyOpVal.get().getValue(), lastFieldType) : null;
        return path.getBooleanExpression(pathBuilder.get(), lastFieldName, keyOpVal.get().getOp(), modifiedValue, lastFieldType);
    }


}
