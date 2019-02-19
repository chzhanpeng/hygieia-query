/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;


import com.capitalone.dashboard.query.model.QueryException;
import com.querydsl.core.types.dsl.BooleanExpression;


public class And extends NonTerminal<BooleanExpression> {
  @Override
    public BooleanExpression evaluate() throws QueryException {
        return left.evaluate().and(right.evaluate());
    }
}
