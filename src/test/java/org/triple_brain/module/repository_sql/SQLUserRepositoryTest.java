package org.triple_brain.module.repository_sql;

import org.junit.Test;
import org.triple_brain.module.model.User;
import org.triple_brain.module.repository.user.user.ExistingUserException;
import org.triple_brain.module.repository.user.user.NonExistingUserException;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.triple_brain.module.repository_sql.SQLConnection.preparedStatement;

/**
 * @author Vincent Blouin
 */
public class SQLUserRepositoryTest extends AbstractSqlTest{

    @Inject
    SQLUserRepository userRepository;

    @Test
    public void can_save_user() throws Exception{
        ResultSet resultSet = preparedStatement("SELECT id, uuid FROM por_user").executeQuery();
        assertFalse(resultSet.next());

        User user = User.withEmail("roger.lamothe@me.com").password("patate");
        userRepository.save(user);

        resultSet = preparedStatement("SELECT id, uuid FROM por_user").executeQuery();
        assertTrue(resultSet.next());
    }

    @Test
    public void try_to_save_twice_a_user_with_same_email_is_not_possible() throws Exception{
        User user_1 = User.withEmail("roger.lamothe@me.com");
        User user_2 = User.withEmail("roger.lamothe@me.com");

        assertThat(users().size(), is(0));

        userRepository.save(user_1);

        try {
            userRepository.save(user_2);
            fail();
        } catch (ExistingUserException e) {
            assertThat(e.getMessage(), is("A user already exist with email: roger.lamothe@me.com"));
        }
    }

    @Test
    public void user_fields_are_well_saved() {
        User user = User.withEmail("roger@me.com")
                .firstName("Roger").lastName("Lamothe")
                .password("secret");
        userRepository.save(user);

        User loadedUser = userRepository.findByEmail("roger@me.com");

        assertThat(loadedUser.id(), is(user.id()));
        assertThat(loadedUser.email(), is(user.email()));
        assertThat(userRepository.getInternalId(loadedUser), is(userRepository.getInternalId(user)));

        assertThat(loadedUser.firstName(), is(user.firstName()));
        assertThat(loadedUser.lastName(), is(user.lastName()));
        assertTrue(loadedUser.hasPassword("secret"));
    }

    @Test
    public void can_find_user_by_email() {
        User user = createAUser();
        userRepository.save(user);
        assertThat(userRepository.findByEmail(user.email()), is(user));
    }

    @Test
    public void try_to_find_none_existing_user_by_email_throw_and_Exception() {
        try {
            userRepository.findByEmail("non_existing@example.org");
            fail();
        } catch (NonExistingUserException e) {
            assertThat(e.getMessage(), is("User not found: non_existing@example.org"));
        }

        try {
            userRepository.findByEmail("");
            fail();
        } catch (NonExistingUserException e) {
            assertThat(e.getMessage(), is("User not found: "));
        }
    }

    private User createAUser(){
        User user = User.withEmail("a_user@triple_brain.org");
        return user;
    }
    
    final List<User> users() throws Exception{
        ResultSet resultSet = preparedStatement("SELECT * FROM por_user").executeQuery();
        List<User> users = new ArrayList<User>();
        while(resultSet.next()){
            users.add(userRepository.userFromResultSet(resultSet));
        }
        return users;
    }

}
