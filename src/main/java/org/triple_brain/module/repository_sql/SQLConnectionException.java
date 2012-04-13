package org.triple_brain.module.repository_sql;

/**
 * Copyright Mozilla Public License 1.1
 */
public class SQLConnectionException extends RuntimeException{
    public SQLConnectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
