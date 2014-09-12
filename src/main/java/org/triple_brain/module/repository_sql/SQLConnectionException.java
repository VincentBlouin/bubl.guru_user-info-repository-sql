/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

public class SQLConnectionException extends RuntimeException{
    public SQLConnectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
