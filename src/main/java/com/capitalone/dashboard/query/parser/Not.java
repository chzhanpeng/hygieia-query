/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import com.querydsl.core.types.dsl.BooleanExpression;


public class Not extends NonTerminal<BooleanExpression> {
    void setChild(AST child) {
        setLeft(child);
    }

    public void setRight(AST right) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BooleanExpression evaluate() throws QueryException {
        return left.evaluate().not();
    }
}
