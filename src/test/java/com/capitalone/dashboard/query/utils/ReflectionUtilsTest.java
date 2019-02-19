package com.capitalone.dashboard.query.utils;

import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.model.SomeTestClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTest {

    @Test
    public void getMapValueClass_stringListStringMap_Depth2() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringListStringMap");
        Class type = ReflectionUtils.getContainerClass(field, 2);

        assertEquals(type, String.class);
    }

    @Test
    public void getMapValueClass_stringListStringMap_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringListStringMap");
        Class type = ReflectionUtils.getContainerClass(field, 1);

        assertEquals(type, List.class);
    }


    @Test
    public void getMapValueClass_nonType() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "noTypeMap");
        Class type = ReflectionUtils.getContainerClass(field, 2);
        assertEquals(type, Map.class);
    }

    @Test
    public void getMapValueClass_stringStringMap_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringStringMap");
        Class type = ReflectionUtils.getContainerClass(field, 1);

        assertEquals(type, String.class);
    }

    @Test
    public void getMapValueClass_stringStringMap_Depth2() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringStringMap");
        Class type = ReflectionUtils.getContainerClass(field, 2);

        assertEquals(type, String.class);
    }


    @Test
    public void getMapValueClass_stringObjectMap_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringObjectMap");
        Class type = ReflectionUtils.getContainerClass(field, 1);

        assertEquals(type, Object.class);
    }


    @Test
    public void getMapValueClass_stringObjectMap_Depth2() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringObjectMap");
        Class type = ReflectionUtils.getContainerClass(field, 2);

        assertEquals(type, Object.class);
    }


    @Test
    public void getMapValueClass_stringMapMapStringString_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringString");
        Class type = ReflectionUtils.getContainerClass(field, 1);

        assertEquals(type, Map.class);
    }

    @Test
    public void getMapValueClass_stringMapMapStringString_Depth2() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringString");
        Class type = ReflectionUtils.getContainerClass(field, 2);

        assertEquals(type, Object.class);
    }

    @Test
    public void getMapValueClass_stringMapMapStringListString_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringListString");
        Class type = ReflectionUtils.getContainerClass(field, 1);

        assertEquals(type, Map.class);
    }

    @Test
    public void getMapValueClass_stringMapMapStringListString_Depth2() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringListString");
        Class type = ReflectionUtils.getContainerClass(field, 2);

        assertEquals(type, List.class);
    }


    @Test
    public void getMapValueClass_stringMapMapStringListString_Depth3() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringListString");
        Class type = ReflectionUtils.getContainerClass(field, 3);
        assertEquals(type, String.class);
    }


    @Test
    public void getMapValueClass_stringMapMapStringListString_Depth99() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringMapMapStringListString");
        Class type = ReflectionUtils.getContainerClass(field, 99);
        assertEquals(type, String.class);
    }


    //stringIntegerMap

    @Test
    public void getMapValueClass_stringIntegerMap_Depth1() throws QueryException {
        Field field = ReflectionUtils.getField(SomeTestClass.class, "stringIntegerMap");
        Class type = ReflectionUtils.getContainerClass(field, 1);
        assertEquals(type, Integer.class);
    }
}