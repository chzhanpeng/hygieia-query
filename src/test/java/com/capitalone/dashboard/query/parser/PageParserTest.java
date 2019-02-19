/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class PageParserTest {

    @Test
    public void buildPageValid() throws QueryException {
        String s = "page=1, pageSize=50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        PageRequest actual = pageParser.build();
        PageRequest expected =  PageRequest.of(1, 50);
        assertEquals(actual, expected);
    }

    @Test (expected = QueryException.class)
    public void buildPageInValid() throws QueryException {
        String s = "mypage=1, pageSize=50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        pageParser.build();
    }

    @Test
    public void buildPageValidNoEqual() throws QueryException {
        String s = "page 1, pageSize 50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        PageRequest actual = pageParser.build();
        PageRequest expected =  PageRequest.of(1, 50);
        assertEquals(actual, expected);
    }

    @Test
    public void buildPageValidNoComma() throws QueryException {
        String s = "page 1 pageSize 50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        PageRequest actual = pageParser.build();
        PageRequest expected =  PageRequest.of(1, 50) ;
        assertEquals(actual, expected);
    }

    @Test
    public void buildPageEmpty() throws QueryException {
        String s = "";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        PageRequest actual = pageParser.build();
        PageRequest expected =  PageRequest.of(0, 25) ;
        assertEquals(actual, expected);
    }

    @Test
    public void buildPageDefaultPageSize() throws QueryException {
        String s = "page=1";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        PageRequest actual = pageParser.build();
        PageRequest expected =  PageRequest.of(1, 25) ;
        assertEquals(actual, expected);
    }


    @Test(expected = QueryException.class)
    public void buildPageBadPageNumber() throws QueryException {
        String s = "page A, pageSize 50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        try {
            PageRequest actual = pageParser.build();
        } catch (QueryException e) {
            assertEquals("Query expression malformed at 2. Must be a number", e.getMessage());
            throw  e;
        }
    }

    @Test(expected = QueryException.class)
    public void buildPageBadStart() throws QueryException {
        String s = ".. A, pageSize 50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        try {
            PageRequest actual = pageParser.build();
        } catch (QueryException e) {
            assertEquals("Query expression malformed at 1. Must start with PAGE", e.getMessage());
            throw  e;
        }
    }

    @Test(expected = QueryException.class)
    public void buildPageSizeBadStart() throws QueryException {
        String s = "page=12, sort a";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        try {
            PageRequest actual = pageParser.build();
        } catch (QueryException e) {
            assertEquals("Query expression malformed at 5. Must start with PAGESIZE", e.getMessage());
            throw  e;
        }
    }

    @Test(expected = QueryException.class)
    public void buildPageNonIntegerNumber() throws QueryException {
        String s = "page=5.75, pageSize 50";
        Lexer lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
        PageParser pageParser = new PageParser(lexer);
        try {
            PageRequest actual = pageParser.build();
        } catch (QueryException e) {
            assertEquals("Query expression malformed at 3. Page number must be an integer value", e.getMessage());
            throw  e;
        }
    }

}