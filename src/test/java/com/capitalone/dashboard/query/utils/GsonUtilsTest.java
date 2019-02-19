package com.capitalone.dashboard.query.utils;

import com.capitalone.dashboard.query.model.FieldSelection;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.model.SomeTestClass;
import com.capitalone.dashboard.query.parser.FieldSelectionParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GsonUtilsTest {

    JsonParser parser = new JsonParser();
    @Test
    public void getGsonInclude1() throws QueryException {
        SomeTestClass someTestClass = new SomeTestClass();
        someTestClass.setField1("Field1");
        someTestClass.setField2(123);
        someTestClass.setField3(123.4);
        FieldSelection fieldSelection = new FieldSelection();
        Field field1 = ReflectionUtils.getField(SomeTestClass.class, "field1");
        Field field2 = ReflectionUtils.getField(SomeTestClass.class, "field2");
        fieldSelection.setType(FieldSelectionParser.INCLUDE);
        fieldSelection.setSelections(Sets.newLinkedHashSet(field1, field2));

        GsonUtils gsonUtils = new GsonUtils();
        Gson gson= GsonUtils.getGson(fieldSelection);
        String json = gson.toJson(someTestClass);
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        assertEquals("Field1", jsonObject.get("field1").getAsString());
        assertEquals(123, jsonObject.get("field2").getAsInt());
        assertEquals(null, jsonObject.get("field3"));
        assertEquals(2, jsonObject.size());
    }


    @Test
    public void getGsonExclude1() throws QueryException {
        SomeTestClass someTestClass = new SomeTestClass();
        someTestClass.setField1("Field1");
        someTestClass.setField2(123);
        someTestClass.setField3(123.4);
        FieldSelection fieldSelection = new FieldSelection();
        Field field1 = ReflectionUtils.getField(SomeTestClass.class, "field1");
        Field field2 = ReflectionUtils.getField(SomeTestClass.class, "field2");
        fieldSelection.setType(FieldSelectionParser.EXCLUDE);
        fieldSelection.setSelections(Sets.newLinkedHashSet(field1, field2));

        GsonUtils gsonUtils = new GsonUtils();
        Gson gson= GsonUtils.getGson(fieldSelection);
        String json = gson.toJson(someTestClass);
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        assertNull(jsonObject.get("field1"));
        assertNull(jsonObject.get("field2"));
        assertEquals(123.4, jsonObject.get("field3").getAsDouble(), 0.0);
        assertEquals(1, jsonObject.size());
    }


    @Test
    public void getGsonInclude2() throws QueryException {
        SomeTestClass someTestClass = new SomeTestClass();
        someTestClass.setField1("Field1");
        someTestClass.setField2(123);
        someTestClass.setField3(123.4);
        Map<String, Object> map = new HashMap<>();
        map.put("string1", "value1");
        map.put("string2",10.0);
        someTestClass.setStringObjectMap(map);

        FieldSelection fieldSelection = new FieldSelection();
        Field field1 = ReflectionUtils.getField(SomeTestClass.class, "field1");
        Field field2 = ReflectionUtils.getField(SomeTestClass.class, "stringObjectMap");

        fieldSelection.setType(FieldSelectionParser.INCLUDE);
        fieldSelection.setSelections(Sets.newLinkedHashSet(field1, field2));

        GsonUtils gsonUtils = new GsonUtils();
        Gson gson= GsonUtils.getGson(fieldSelection);
        String json = gson.toJson(someTestClass);
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        assertNotNull(jsonObject.get("field1"));
        assertNotNull(jsonObject.get("stringObjectMap"));
        assertEquals("value1", jsonObject.get("stringObjectMap").getAsJsonObject().get("string1").getAsString());

        assertNull(jsonObject.get("field2"));
        assertNull(jsonObject.get("field3"));

        assertEquals(2, jsonObject.size());
    }

    @Test
    public void getGsonInclude3() throws QueryException {
        SomeTestClass someTestClass = new SomeTestClass();
        someTestClass.setField1("Field1");
        someTestClass.setField2(123);
        someTestClass.setField3(123.4);
        SomeTestClass.ClassOne classOne1 = new SomeTestClass.ClassOne();
        classOne1.setStringOne("classOneString1");
        SomeTestClass.ClassOne classOne2 = new SomeTestClass.ClassOne();
        classOne2.setStringOne("classOneString2");
        someTestClass.setClassOneList(Lists.newArrayList(classOne1, classOne2));

        FieldSelection fieldSelection = new FieldSelection();
        Field field1 = ReflectionUtils.getField(SomeTestClass.class, "field1");
        Field field2 = ReflectionUtils.getField(SomeTestClass.class, "classOneList");
        Field field3 = ReflectionUtils.getField(SomeTestClass.ClassOne.class, "stringOne");

        fieldSelection.setType(FieldSelectionParser.INCLUDE);
        fieldSelection.setSelections(Sets.newLinkedHashSet(field1, field2, field3));

        GsonUtils gsonUtils = new GsonUtils();
        Gson gson= GsonUtils.getGson(fieldSelection);
        String json = gson.toJson(someTestClass);
        JsonObject jsonObject = (JsonObject) parser.parse(json);
        assertNotNull(jsonObject.get("field1"));
        assertNull(jsonObject.get("field2"));
        assertNull(jsonObject.get("field3"));

        assertEquals(2, jsonObject.size());
    }
}