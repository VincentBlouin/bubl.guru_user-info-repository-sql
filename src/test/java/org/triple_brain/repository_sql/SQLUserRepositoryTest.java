package org.triple_brain.repository_sql;

import com.mycila.jdbc.query.Results;
import org.junit.Test;
import org.triple_brain.model.User;
import org.triple_brain.module.repository.user.user.ExistingUserException;

import javax.inject.Inject;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

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

    @Test
    public void try_to_save_twice_a_user_with_same_email_is_not_possible() {
        User user_1 = User.withEmail("roger.lamothe@me.com");
        User user_2 = User.withEmail("roger.lamothe@me.com");

        assertThat(users().size(), is(0));

        repository.save(user_1);

        try {
            repository.save(user_2);
            fail();
        } catch (ExistingUserException e) {
            assertThat(e.getMessage(), is("A user already exist with email: roger.lamothe@me.com"));
        }
    }

    final List<User> users() {
        return sql.query("SELECT * FROM por_user")
                .list(User.class);
    }
    
}
