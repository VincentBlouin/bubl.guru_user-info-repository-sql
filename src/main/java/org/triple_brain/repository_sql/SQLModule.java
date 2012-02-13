package org.triple_brain.repository_sql;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mycila.jdbc.ConnectionFactory;
import com.mycila.jdbc.UnitOfWork;
import com.mycila.jdbc.query.Sql;
import com.mycila.jdbc.tx.*;
import com.mycila.jdbc.tx.sql.JdbcConnectionFactory;
import com.mycila.jdbc.tx.sql.JdbcTransactionManager;
import com.mycila.jdbc.tx.sql.JdbcUnitOfWork;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.google.inject.jndi.JndiIntegration.fromJndi;
import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * @author Vincent Blouin
 */
public class SQLModule extends AbstractModule {

    @Override
    protected void configure() {
        // Datasource
        bind(DataSource.class).toProvider(fromJndi(DataSource.class, "jdbc/triple_brainDB")).in(Singleton.class);

        // Manage datasource connections per thread
        bind(ConnectionFactory.class).to(JdbcConnectionFactory.class).in(Singleton.class);
        bind(UnitOfWork.class).to(JdbcUnitOfWork.class).in(Singleton.class);

        // Manage transactions
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        requestInjection(transactionInterceptor);
        bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);
        bind(TransactionManager.class).to(JdbcTransactionManager.class).in(Singleton.class);
        bind(TransactionDefinitionBuilder.class).to(AnnotatedTransactionDefinitionBuilder.class);
    }

    @Provides
    Connection connection(ConnectionFactory factory) throws SQLException {
        return factory.getCurrentConnection();
    }

    @Provides
    Sql sql(ConnectionFactory factory) throws SQLException {
        return new Sql(factory.getCurrentConnection());
    }
}
