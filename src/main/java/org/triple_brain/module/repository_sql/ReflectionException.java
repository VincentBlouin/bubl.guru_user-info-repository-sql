package org.triple_brain.module.repository_sql;

/**
 * Copyright Mozilla Public License 1.1
 */
public class ReflectionException extends RuntimeException{
    public ReflectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
