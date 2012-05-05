package org.triple_brain.module.repository_sql;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/*
* Copyright Mozilla Public License 1.1
*/
public class JenaH2DataSource extends JenaFriendlyDataSource {

    public JenaH2DataSource(){
        super();
        setDriverClassName("org.h2.Driver");
        setUrl("jdbc:h2:mem:jena_database");
        setUsername("sa");
        setDatabaseTypeName("HSQLDB");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
