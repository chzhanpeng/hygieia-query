package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.model.SomeTestClass;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OneExpressionTest {

    @Test
    public void evaluate1() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "stringListClassOne.key1.mapOne.key2.stringTwo", "eq", "test");
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.get("stringListClassOne", Map.class).get("key1", List.class).get("mapOne", Map.class)
                .get("key2", SomeTestClass.ClassTwo.class).getString("stringTwo").eq("test");


        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);

    }

    @Test
    public void evaluate2() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "field1", "eq", "test");
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.getString("field1").eq("test");


        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);

    }

    @Test
    public void evaluate3() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "field1", "equalsIgnoreCase", "test");
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.getString("field1").equalsIgnoreCase("test");


        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);

    }

    @Test
    public void evaluate4() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "field1", "in", Lists.newArrayList("test1", "test2"));
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.getString("field1").in(Lists.newArrayList("test1", "test2"));


        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);

    }

    @Test
    public void evaluate5() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "stringObjectMap.key1", "eq", 10);
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.get("stringObjectMap", Map.class).get("key1", Object.class).eq(10);

        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);
    }


    @Test
    public void evaluate6() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "classOneList.stringOne", "startsWith", "test");
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.get("classOneList", List.class).getString("stringOne").startsWith("test");

        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);

    }


    @Test
    public void evaluate7() throws QueryException {
        OneExpression expression = new OneExpression(SomeTestClass.class, "classOneList.mapOne.key1.stringTwo", "startsWith", "test");
        BooleanExpression actual = expression.evaluate();

        PathBuilder<SomeTestClass> pathBuilder = new PathBuilder<>(SomeTestClass.class, SomeTestClass.class.getName());
        BooleanExpression expected = pathBuilder.get("classOneList", SomeTestClass.ClassOne.class).get("mapOne", Map.class).get("key1", SomeTestClass.ClassTwo.class).getString("stringTwo").startsWith("test");

        assertThat(actual).usingDefaultComparator().isEqualToComparingFieldByField(expected);
    }


}