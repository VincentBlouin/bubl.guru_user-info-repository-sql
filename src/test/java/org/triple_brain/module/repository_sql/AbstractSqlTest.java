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

import static org.triple_brain.module.repository_sql.SQLConnection.*;

/**
 * @author Vincent Blouin
 */
public class AbstractSqlTest implements Module {

    @Before
    public final void before() throws SQLException {
        Jsr250.createInjector(Stage.PRODUCTION, Modules.override(new SQLModule()).with(this)).injectMembers(this);
        clearDatabases();
        createTables();
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
