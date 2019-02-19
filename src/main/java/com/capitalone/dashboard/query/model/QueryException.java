/*
 * Developed by Topo Pal  (c) 2019.
 *
 */

package com.capitalone.dashboard.query.model;

public class QueryException extends Exception {

    public static final int INVALID_FIELD = -11;
    public static final int INVALID_OPERATOR = -12;
    public static final int INVALID_QUERY_START = -13;
    public static final int INVALID_VALUE = -14;
    public static final int INCLUDE_EXCLUDE_NOT_ALLOWED = -15 ;


    private int errorCode = 0;

    public QueryException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public QueryException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
