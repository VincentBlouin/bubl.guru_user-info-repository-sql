package org.triple_brain.module.repository_sql;

import org.triple_brain.module.repository.SQLConnectionInfo;

/*
* Copyright Mozilla Public License 1.1
*/
public class PropertyBasedConnectionInfo implements SQLConnectionInfo {

    @Override
    public String driverClassPath() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String databasePath() {
        return "jdbc:mysql://127.0.0.1/";
    }

    @Override
    public String databaseName() {
        return "triple_brain";
    }

    @Override
    public String username() {
        return "triple_brain";
    }

    @Override
    public String password() {
        return "boraptop34";
    }

    @Override
    public int databaseType() {
        return MYSQL_DATABASE_TYPE;
    }
}
