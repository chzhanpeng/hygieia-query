/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import org.springframework.data.domain.Sort;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class SortParser {
    private AtomicReference<Lexer> lexer = new AtomicReference<>();
    private AtomicReference<Sort> parsed = new AtomicReference<>();
    private AtomicInteger symbol = new AtomicInteger();
    private AtomicReference<String> word = new AtomicReference<>();
    private Sort.Direction direction = Sort.Direction.ASC;
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";


    public SortParser(Lexer lexer) {
        this.lexer.set(lexer);
    }

    public Sort build() throws QueryException {

        if (lexer.get().nextSymbol() != Lexer.SORT) {
            throw new QueryException("Query expression malformed at " + lexer.get().getInput().lineno() + ". Must start with SORT", QueryException.INVALID_FIELD);
        }
        symbol.set(lexer.get().nextSymbol());
        if (symbol.get() != Lexer.CONDITION_WORD) {
            throw new QueryException("Query expression malformed at " + lexer.get().getInput().lineno() + ". Must be a alpha-numeric string", QueryException.INVALID_FIELD);
        }
        word.set(lexer.get().getInput().sval);
        if ("by".equalsIgnoreCase(word.get())) {
            symbol.set(lexer.get().nextSymbol());  // basically ignore the word 'by'
        }

        while (symbol.get() == Lexer.CONDITION_WORD) {
            parseSort();
        }

        return parsed.get();
    }


    private void parseSort() throws QueryException {
        word.set(lexer.get().getInput().sval);
        String prop = word.get();
        symbol.set(lexer.get().nextSymbol());
        if (symbol.get() == Lexer.COMMA) {
            fillSort(prop, direction);
            symbol.set(lexer.get().nextSymbol());
            return;
        }
        if (IntStream.of(Lexer.NONE, Lexer.EOF, Lexer.EOL).anyMatch(i -> symbol.get() == i)) {
            fillSort(prop, direction);
            return;
        }
        if (symbol.get() != Lexer.CONDITION_WORD) {
            throw new QueryException("Query expression malformed at " + lexer.get().getInput().lineno() + ". Must be an alpha-numeric string", QueryException.INVALID_VALUE);
        }
        word.set(lexer.get().getInput().sval);
        if (ASC.equalsIgnoreCase(word.get()) || (DESC.equalsIgnoreCase(word.get()))) {
            direction = Sort.Direction.fromString(word.get());
        } else {
            throw new QueryException("Query expression malformed at " + lexer.get().getInput().lineno() + ". Invalid sort direction. Must be ASC or DESC. Found '" + word + '\'', QueryException.INVALID_FIELD);
        }
        fillSort(prop, direction);
        symbol.set(lexer.get().nextSymbol());
        if (symbol.get() == Lexer.COMMA) {
            symbol.set(lexer.get().nextSymbol());
        }
    }

    private void fillSort (String prop, Sort.Direction direction) {
        if (parsed.get() == null) {
            parsed.set(new Sort(direction, prop));
        } else {
            parsed.set(parsed.get().and(new Sort(direction, prop)));
        }
    }
}