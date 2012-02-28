package org.triple_brain.module.repository_sql;

/**
 * @author Vincent Blouin
 */
public class SQLConnectionException extends RuntimeException{
    public SQLConnectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
