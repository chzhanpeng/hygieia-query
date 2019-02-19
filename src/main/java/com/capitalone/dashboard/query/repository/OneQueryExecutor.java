/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.repository;

import com.capitalone.dashboard.query.builder.QueryBuilder;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.parser.PageParser;
import com.google.common.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;


@NoRepositoryBean
public interface OneQueryExecutor<T, V extends Serializable> extends PagingAndSortingRepository<T, V>, QuerydslPredicateExecutor<T> {

    default long count(String query) throws QueryException {
        TypeToken<T> genericType = new TypeToken<T>(this.getClass()) {};
        BooleanBuilder booleanBuilder = QueryBuilder.buildQuery(query, genericType.getRawType());
        return count(booleanBuilder.getValue());
    }

    default Iterable<T> find(String query) throws QueryException {
        TypeToken<T> genericType = new TypeToken<T>(this.getClass()) {};
        BooleanBuilder booleanBuilder = QueryBuilder.buildQuery(query, genericType.getRawType());
        Sort sort = QueryBuilder.buildSort(query);
        PageRequest page = QueryBuilder.buildPage(query);

        if (booleanBuilder == null || booleanBuilder.getValue() == null) {
            return IterableUtils.emptyIterable();
        }

        // sort and page are null
        if (sort == null && page == null) {
            return findAll(booleanBuilder.getValue());
        }

        // sort null and page not null
        if (sort == null) {
            return findAll(booleanBuilder.getValue(), page);
        } else if (page == null) {
            // sort not null and page is null
            return findAll(booleanBuilder.getValue(), sort);
        }

        // booleanBuilder, sort, page all are not null
        PageRequest newPageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

        return findAll(booleanBuilder.getValue(), newPageRequest);
    }


    default Page<T> findWithDefaultPaging(String query) throws QueryException {
        return (QueryBuilder.buildPage(query) == null) ? (Page<T>) find(page(query, 1, PageParser.DEFAULT_PAGESIZE)) : (Page<T>) find(query);
    }

    /**
     * Wrapper to mimic older version of findOne from querydsl.
     * @param query
     * @return
     * @throws QueryException
     */
    default T findOne(String query) throws QueryException {
        Iterable<T> items = find(query);
        if (IterableUtils.isEmpty(items)) {
            return null;
        } else {
            return IterableUtils.toList(items).get(0);
        }
    }

    /**
     * Helper method to 'and' parts of queries
     * @param query
     * @return modified query string
     */
    default String and(String query) {
        return StringUtils.isEmpty(query) ? "" : query + " and ";
    }

    /**
     * Helper method to 'or' parts of queries
     * @param query
     * @return modified query string
     */
    default String or(String query) {
        return StringUtils.isEmpty(query) ? "" : query + " or ";
    }

    /**
     * Helper method to quote a field in the query
     * @param s
     * @return
     */
    default String quote(String s) {
        return '\'' + s + '\'';
    }

    /**
     * Helper method to quote a field in the query
     * @param o
     * @return
     */
    default String quote(Object o) {
        return '\'' + o.toString() + '\'';
    }

    /**
     * Helper method to format 'in' fields in the query
     * @param list
     * @return Modified query string
     */
    default String in(List<String> list) {
        return String.format("('%s')", String.join("','", list));
    }

    /**
     * Helper method to add paging to the query
     * @param query
     * @param pageable
     * @return modified query
     */
    default String page(String query, Pageable pageable) {
        return page(query, pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * Helper method to add paging to the query
     * @param query
     * @param number
     * @param size
     * @return
     */
    default String page(String query, int number, int size) {
        return query + ';' + "page=" + number + ", pageSize=" + size;
    }
}
