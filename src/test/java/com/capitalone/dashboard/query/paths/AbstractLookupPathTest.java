package com.capitalone.dashboard.query.paths;


import com.capitalone.dashboard.query.model.SomeTestClass;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class AbstractLookupPathTest {

    @Test
    public void getMethod() throws NoSuchMethodException {
        Method method = AbstractLookupPath.getMethod(NumberPath.class, "gt", 20, Number.class, SomeTestClass.class);
        assertEquals(NumberExpression.class.getMethod("gt", Number.class), method);
    }

    @Test
    public void getMethodStringGt() throws NoSuchMethodException {
        Method method = AbstractLookupPath.getMethod(StringPath.class, "gt", "test", String.class, SomeTestClass.class);
        assertEquals(StringExpression.class.getMethod("gt", Comparable.class), method);
    }
}