/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

import com.capitalone.dashboard.query.model.QueryException;

public interface AST<R> {
     R evaluate() throws QueryException;
}
