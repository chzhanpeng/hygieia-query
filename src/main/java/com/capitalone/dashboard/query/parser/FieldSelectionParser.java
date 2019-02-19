package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.FieldSelection;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.utils.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class FieldSelectionParser {

    public static final String INCLUDE = "include";
    public static final String EXCLUDE = "exclude";
    private static final Pattern PATTERN_INCLUDE = Pattern.compile("(?i)include");
    private static final Pattern PATTERN_EXCLUDE = Pattern.compile("(?i)exclude");

    public static FieldSelection parse(String include, String exclude, Class<?> rootClass) throws QueryException {
        FieldSelection fieldSelection = new FieldSelection();
        if (!StringUtils.isEmpty(include)) {
            Set<Field> selections = getSelections(PATTERN_INCLUDE.matcher(include).replaceFirst(""), rootClass);
            fieldSelection.setType(INCLUDE);
            fieldSelection.setSelections(selections);
        } else if (!StringUtils.isEmpty(exclude)) {
            Set<Field> selections = getSelections(PATTERN_EXCLUDE.matcher(exclude).replaceFirst(""), rootClass);
            fieldSelection.setType(EXCLUDE);
            fieldSelection.setSelections(selections);
        }
        return fieldSelection;
    }

    private static Set<Field> getSelections(String fields, Class<?> rootClass) throws QueryException {
        Set<Field> selection = new HashSet<>();
        String[] parts = fields.split(",");
        for (int i = 0; i < parts.length; i++) {
            String[] path = parts[i].split("\\.");
            Field field = null;
            Class<?> loopClass = rootClass;
            for (int j = 0; j < path.length; j++) {
                field = ReflectionUtils.getField(loopClass, path[j].trim());
                if  (Map.class.isAssignableFrom(field.getType()) || Collection.class.isAssignableFrom(field.getType())) {
                    loopClass = ReflectionUtils.getContainerClass(field, 2);
                    selection.add(field);
                }
            }
            if (field != null) {
                selection.add(field);
            }
        }
        return selection;
    }


}
