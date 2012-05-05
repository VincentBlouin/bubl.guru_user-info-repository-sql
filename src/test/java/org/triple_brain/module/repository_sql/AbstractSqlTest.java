package org.triple_brain.module.repository_sql;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.triple_brain.module.repository.user.UserRepository;

import java.sql.SQLException;

import static org.triple_brain.module.repository_sql.SQLConnection.*;

/**
 * Copyright Mozilla Public License 1.1
 */
public class AbstractSqlTest implements Module {

    @BeforeClass
    public static void beforeClass(){

    }

    @Before
    public final void before() throws SQLException {
        Injector injector = Guice.createInjector(new SQLTestModule());
        injector.injectMembers(this);
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
