package org.triple_brain.module.repository_sql;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.triple_brain.module.repository.user.user.UserRepository;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.google.inject.jndi.JndiIntegration.fromJndi;
import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * Copyright Mozilla Public License 1.1
 */
public class SQLModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).to(SQLUserRepository.class);
        // Datasource
        //bind(DataSource.class).toProvider(fromJndi(DataSource.class, "jdbc/triple_brainDB")).in(Singleton.class);

        // Manage datasource connections per thread
//        bind(ConnectionFactory.class).to(JdbcConnectionFactory.class).in(Singleton.class);
//        bind(UnitOfWork.class).to(JdbcUnitOfWork.class).in(Singleton.class);

        // Manage transactions
//        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
//        requestInjection(transactionInterceptor);
//        bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);
//        bind(TransactionManager.class).to(JdbcTransactionManager.class).in(Singleton.class);
//        bind(TransactionDefinitionBuilder.class).to(AnnotatedTransactionDefinitionBuilder.class);
    }
}
