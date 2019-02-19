/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.builder;

import com.capitalone.dashboard.query.model.FieldSelection;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.model.QueryResponse;
import com.capitalone.dashboard.query.parser.AST;
import com.capitalone.dashboard.query.parser.FieldSelectionParser;
import com.capitalone.dashboard.query.parser.Lexer;
import com.capitalone.dashboard.query.parser.PageParser;
import com.capitalone.dashboard.query.parser.QueryParser;
import com.capitalone.dashboard.query.parser.SortParser;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

public class QueryBuilder<T> {
    private static final String SORT = "sort";
    private static final String PAGE = "page";


    public static BooleanBuilder buildQuery(String query, Class<?> rootClass) throws QueryException {
        BooleanBuilder builder = new BooleanBuilder();

        String[] parts = query.split(";");
        if (StringUtils.isEmpty(parts[0])) {
            return builder;
        }
        Lexer lexer = new Lexer(new ByteArrayInputStream(parts[0].getBytes()));
        QueryParser queryParser = new QueryParser(lexer, rootClass);
        AST<BooleanExpression> ast = queryParser.build();
        BooleanExpression expression = ast.evaluate();
        return builder.and(expression);
    }

    public static Sort buildSort(String query) throws QueryException {
        String[] parts = query.split(";");
        int bound = parts.length;
        for (int i = 1; i < bound; i++) {
            if (parts[i].trim().toLowerCase().startsWith(SORT)) {
                Lexer lexer = new Lexer(new ByteArrayInputStream(parts[i].getBytes()));
                SortParser sortParser = new SortParser(lexer);
                return sortParser.build();
            }
        }
        return null;
    }

    public static PageRequest buildPage(String query) throws QueryException {
        String[] parts = query.split(";");
        int bound = parts.length;
        for (int i = 1; i < bound; i++) {
            if (parts[i].trim().toLowerCase().startsWith(PAGE)) {
                Lexer lexer = new Lexer(new ByteArrayInputStream(parts[i].getBytes()));
                PageParser pageParser = new PageParser(lexer);
                return pageParser.build();
            }
        }
        return null;
    }


    public static FieldSelection buildFieldSelection(String query, Class<?> rootClass) throws QueryException {
        String include = null;
        String exclude = null;
        String[] parts = query.split(";");
        int bound = parts.length;
        for (int i = 1; i < bound; i++) {
            if (parts[i].trim().toLowerCase().startsWith(FieldSelectionParser.INCLUDE)) {
                include = parts[i];
            } else if (parts[i].trim().toLowerCase().startsWith(FieldSelectionParser.EXCLUDE)) {
                exclude = parts[i];
            }
        }
        if (!StringUtils.isEmpty(include) && (!StringUtils.isEmpty(exclude))) {
            throw new QueryException("Can't have both included and excluded list of fields", QueryException.INCLUDE_EXCLUDE_NOT_ALLOWED);
        }
        return FieldSelectionParser.parse(include, exclude, rootClass);
    }

    public QueryResponse<T> buildResponse(Page<T> items) {
        QueryResponse<T> response = new QueryResponse<>();
        if (!IterableUtils.isEmpty(items)) {
            List<T> values = Lists.newArrayList(items);
            response.setTotal(items.getTotalElements());
            response.setCount(items.getNumberOfElements());
            response.setTotalPages(items.getTotalPages());
            response.setValues(values);
        }
        return response;
    }


}
