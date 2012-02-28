package org.triple_brain.module.repository_sql;

/**
 * @author Vincent Blouin
 */
public class ReflectionException extends RuntimeException{
    public ReflectionException(Exception ex) {
        super(ex.getMessage());
        ex.printStackTrace();
    }
}
