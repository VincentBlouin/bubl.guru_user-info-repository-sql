package org.triple_brain.repository_sql;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.jdbc.UnitOfWork;
import com.mycila.jdbc.query.Sql;
import com.ovea.tadjin.sql.testing.DriverManagerDataSource;
import com.ovea.tadjin.sql.testing.guice.H2TestModule;
import com.ovea.tadjin.sql.testing.guice.RuntimeTestModule;
import org.junit.After;
import org.junit.Before;
import org.triple_brain.module.repository.user.user.UserRepository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Vincent Blouin
 */
public class AbstractSqlTest implements Module {

    @Inject
    DataSource dataSource;

    @Inject
    UnitOfWork unitOfWork;

    Sql sql;

    @Before
    public final void before() throws SQLException {
        Jsr250.createInjector(Stage.PRODUCTION, Modules.override(new SQLModule(), new RuntimeTestModule()).with(this)).injectMembers(this);
        sql = new Sql(dataSource.getConnection());
        sql.getConnection().setAutoCommit(true);
        unitOfWork.begin();
        cleanTables();
    }

    protected void cleanTables() {
        sql.update("delete from por_user").noreturn().execute();
    }

    @After
    public final void after() throws SQLException {
        sql.getConnection().close();
        unitOfWork.end();
        Map<Connection, Throwable> opened = ((DataSourceProxyfier.Marker) dataSource).getOpenedConnections();
        if (!opened.isEmpty()) {
            for (Throwable throwable : opened.values())
                System.out.println(DataSourceProxyfier.asString(throwable));
            throw new AssertionError("Connection opened !");
        }
    }

    @Override
    public final void configure(Binder binder) {
        binder.bind(UserRepository.class).to(SQLUserRepository.class);

        binder.install(new H2TestModule());
//        binder.install(new MySqlTestModule());


        final DataSource dataSource = DriverManagerDataSource
                .use(org.h2.Driver.class)
                .withUsername("patate")
                .withPassword("")
                .withUrl("jdbc:h2:mem:triple_brain;DB_CLOSE_DELAY=-1")
                .build();

        /*       final DataSource dataSource = DriverManagerDataSource
        .use(oracle.jdbc.OracleDriver.class)
        .withUsername("potatoe")
        .withPassword("potatoe")
        .withUrl("jdbc:oracle:thin:@10.1.200.31:1521:DEVDB02")
        .build();*/

//        final DataSource dataSource = DriverManagerDataSource
//                .use(com.mysql.jdbc.Driver.class)
//                .withUsername("potatoe")
//                .withPassword("potatoe")
//                .withUrl("jdbc:mysql://127.0.0.1:3306/triple_brain")
//                .build();

        binder.bind(DataSource.class).toInstance(DataSourceProxyfier.proxify(dataSource));

    }
}
