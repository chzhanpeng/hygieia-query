/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicReference;

public class PageParser {
    private AtomicReference<Lexer> lexer = new AtomicReference<>();
    public static final int DEFAULT_PAGESIZE = 25;

    public PageParser(Lexer lexer) {
        this.lexer.set(lexer);
    }

    @SuppressWarnings("PMD.NPathComplexity")
    public PageRequest build() throws QueryException {

        int symbol = lexer.get().nextSymbol();

        if (symbol == Lexer.EOF || symbol == Lexer.EOL || symbol == Lexer.NONE) {
            return PageRequest.of(0, DEFAULT_PAGESIZE);
        }
        if (symbol != Lexer.CONDITION_WORD) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Must start with PAGE", QueryException.INVALID_FIELD);
        }

        if (StringUtils.isEmpty(lexer.get().getInput().sval) || !"page".equalsIgnoreCase(lexer.get().getInput().sval)) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Must start with PAGE=", QueryException.INVALID_FIELD);
        }

        int pageNumber = getNumber();

        symbol = lexer.get().nextSymbol();
        if (symbol == Lexer.COMMA) {
            symbol = lexer.get().nextSymbol(); //ignore comma
        }

        if (symbol == Lexer.EOF || symbol == Lexer.EOL || symbol == Lexer.NONE) {
            return PageRequest.of(pageNumber, DEFAULT_PAGESIZE);
        }

        if (symbol != Lexer.CONDITION_WORD) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Must start with PAGESIZE", QueryException.INVALID_FIELD);
        }

        if (StringUtils.isEmpty(lexer.get().getInput().sval) || !"pagesize".equalsIgnoreCase(lexer.get().getInput().sval)) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Must start with PAGESIZE=", QueryException.INVALID_FIELD);
        }

        return  PageRequest.of(pageNumber, getNumber());
    }


    private int getNumber() throws QueryException {
        int symbol = lexer.get().nextSymbol();
        if (symbol == Lexer.EQUAL) {
            symbol = lexer.get().nextSymbol();
        }
        if (symbol != Lexer.CONDITION_NUMBER) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Must be a number", QueryException.INVALID_VALUE);
        }

        Double val = lexer.get().getInput().nval;

        if (val % 1 != 0) {
            throw new QueryException("Query expression malformed at " + lexer.get().getTokenNumber() + ". Page number must be an integer value", QueryException.INVALID_VALUE);
        }
        return val.intValue();

    }
}