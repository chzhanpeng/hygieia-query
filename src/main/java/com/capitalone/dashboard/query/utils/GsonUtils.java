package com.capitalone.dashboard.query.utils;

import com.capitalone.dashboard.query.model.FieldSelection;
import com.capitalone.dashboard.query.parser.FieldSelectionParser;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

public class GsonUtils {

    public static Gson getGson(FieldSelection selection) {

         ExclusionStrategy strategy = new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                Set<Field> selectionSet = selection.getSelections();
                boolean isInSet = false;
                for (Field f : selectionSet) {
                    Type type = field.getDeclaredType() instanceof ParameterizedType ? ((ParameterizedType) field.getDeclaredType()).getRawType() : field.getDeclaredType();
                    if (Objects.equals(f.getType(), type) && f.getName().equals(field.getName()) && f.getDeclaringClass().getName().equals(field.getDeclaringClass().getName())) {
                        isInSet = true;
                        break;
                    }
                }
                return selection.getType().equalsIgnoreCase(FieldSelectionParser.INCLUDE) != isInSet;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        return new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .create();
    }
}

