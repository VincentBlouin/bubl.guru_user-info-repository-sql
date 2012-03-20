package org.triple_brain.module.repository_sql;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.mycila.inject.jsr250.Jsr250;
import org.junit.After;
import org.junit.Before;
import org.triple_brain.module.repository.user.user.UserRepository;

import java.sql.SQLException;

import static org.triple_brain.module.repository_sql.SQLConnection.closeConnection;
import static org.triple_brain.module.repository_sql.SQLConnection.preparedStatement;

/**
 * @author Vincent Blouin
 */
public class AbstractSqlTest implements Module {

    @Before
    public final void before() throws SQLException {
        Jsr250.createInjector(Stage.PRODUCTION, Modules.override(new SQLModule()).with(this)).injectMembers(this);
        cleanTables();
    }

    protected void cleanTables()throws SQLException{
        String query = "DROP TABLE IF EXISTS por_user;";
        preparedStatement(query).executeUpdate();
        createTables();
    }

    protected void createTables() throws SQLException{
        String query = "CREATE TABLE por_user (\n" +
                "    id           BIGINT    PRIMARY KEY AUTO_INCREMENT,\n" +
                "    creationTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    updateTime   TIMESTAMP NOT NULL,\n" +
                "\n" +
                "    uuid   VARCHAR(36)   UNIQUE NOT NULL,\n" +
                "    username  VARCHAR(50)   UNIQUE NOT NULL,\n" +
                "    email  VARCHAR(50)   UNIQUE NOT NULL,\n" +
                "\n" +
                "    salt                 VARCHAR(36),\n" +
                "    passwordHash         VARCHAR(100)\n" +
                ");";
        preparedStatement(query).executeUpdate();
    }

    @After
    public final void after() throws SQLException {
        closeConnection();
    }

    @Override
    public final void configure(Binder binder) {
        binder.bind(UserRepository.class).to(SQLUserRepository.class);
    }
}
