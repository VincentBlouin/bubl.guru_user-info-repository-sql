/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

public class ReflectionException extends RuntimeException{
    public ReflectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
