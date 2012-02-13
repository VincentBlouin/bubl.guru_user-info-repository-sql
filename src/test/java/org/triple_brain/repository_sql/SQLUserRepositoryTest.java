package org.triple_brain.repository_sql;

import com.mycila.jdbc.query.Results;
import org.junit.Test;
import org.triple_brain.model.User;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Vincent Blouin
 */
public class SQLUserRepositoryTest extends AbstractSqlTest{
    @Inject
    SQLUserRepository repository;

    @Test
    public void can_save_user() {
        Results result = sql.query("SELECT id, uuid FROM por_user").execute();
        assertThat(result.rows.size(), is(0));

        User user = User.withEmail("roger.lamothe@me.com").password("patate");
        repository.save(user);

        result = sql.query("SELECT id, uuid FROM por_user").execute();
        assertThat(result.rows.size(), is(1));
    }
    
}
