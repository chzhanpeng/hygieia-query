package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.FieldSelection;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.model.SomeTestClass;
import com.capitalone.dashboard.query.utils.ReflectionUtils;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FieldSelectionParserTest {

    @Test
    public void parse1() throws QueryException {
        FieldSelection actual = FieldSelectionParser.parse("field1, classOneList.stringOne", "", SomeTestClass.class);
        assertNotNull(actual);
        assertEquals(3, actual.getSelections().size());

        FieldSelection expected = new FieldSelection();
        Field field = ReflectionUtils.getField(SomeTestClass.class, "field1");
        expected.setSelections(Sets.newLinkedHashSet(field));
        field = ReflectionUtils.getField(SomeTestClass.class, "classOneList");
        expected.getSelections().add(field);
        field = ReflectionUtils.getField(SomeTestClass.ClassOne.class, "stringOne");
        expected.getSelections().add(field);
        expected.setType(FieldSelectionParser.INCLUDE);

        assertThat(expected).usingDefaultComparator().isEqualToComparingFieldByField(actual);
    }
}