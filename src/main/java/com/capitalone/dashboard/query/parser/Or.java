/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;
import com.querydsl.core.types.dsl.BooleanExpression;


public class Or extends NonTerminal<BooleanExpression> {
    public String toString() {
        return String.format("(%s | %s)", left, right);
    }


    @Override
    public BooleanExpression evaluate() throws QueryException {
        return left.evaluate().or(right.evaluate());
    }
}
