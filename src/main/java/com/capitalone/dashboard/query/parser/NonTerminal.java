/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.parser;

public abstract class NonTerminal<R> implements AST<R> {

    protected AST<R> left, right;
    public void setLeft(AST<R> left) {
        this.left = left;
    }
    public void setRight(AST<R> right) {
        this.right = right;
    }
}