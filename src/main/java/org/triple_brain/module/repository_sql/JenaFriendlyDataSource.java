/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class JenaFriendlyDataSource extends BasicDataSource {

    private String databaseTypeName = "";

    public JenaFriendlyDataSource(){
        super();
        setDriverClassName("org.h2.Driver");
        setUrl("jdbc:h2:mem:jena_database");
        setUsername("sa");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setDatabaseTypeName(String databaseTypeName){
        this.databaseTypeName = databaseTypeName;
    }

    public String getDatabaseTypeName(){
        return databaseTypeName;
    }
}
