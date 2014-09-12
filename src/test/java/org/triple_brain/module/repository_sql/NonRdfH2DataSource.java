/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class NonRdfH2DataSource extends BasicDataSource {

    public NonRdfH2DataSource(){
        super();
        setDriverClassName("org.h2.Driver");
        setUrl("jdbc:h2:mem:triple_brain");
        setUsername("sa");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
