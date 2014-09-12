/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.triple_brain.module.repository.user.UserRepository;

import javax.sql.DataSource;

public class SqlTestModule extends AbstractModule{

    @Override
    protected void configure() {
        install(new SQLModule());
        bind(UserRepository.class).to(SqlUserRepository.class);
        bind(DataSource.class)
                .annotatedWith(Names.named("nonRdfDb"))
                .toInstance(new NonRdfH2DataSource());
    }


}
