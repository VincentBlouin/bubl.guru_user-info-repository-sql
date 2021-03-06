/*
 * Copyright Vincent Blouin under the Mozilla Public License 1.1
 */

package org.triple_brain.module.repository_sql;

import com.google.inject.AbstractModule;
import org.triple_brain.module.repository.user.UserRepository;

public class SQLModule extends AbstractModule {

    @Override
    protected void configure() {
        requestStaticInjection(SQLConnection.class);
        bind(UserRepository.class).to(SqlUserRepository.class);
    }
}
