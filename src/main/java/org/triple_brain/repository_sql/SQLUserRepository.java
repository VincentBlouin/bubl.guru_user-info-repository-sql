package org.triple_brain.repository_sql;

import com.mycila.jdbc.query.Insert;
import com.mycila.jdbc.query.Reflect;
import com.mycila.jdbc.query.Results;
import com.mycila.jdbc.query.Sql;
import com.mycila.jdbc.tx.Isolation;
import com.mycila.jdbc.tx.Propagation;
import com.mycila.jdbc.tx.Transactional;
import org.triple_brain.model.User;
import org.triple_brain.module.repository.user.user.UserRepository;
import org.triple_brain.module.repository.user.user.ExistingUserException;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Timestamp;

/**
 * @author Vincent Blouin
 */
public class SQLUserRepository implements UserRepository {
    private static final String INTERNAL_ID = "internalID";
    private final Reflect<User> userAccess = Reflect.access(User.class);

    @Inject
    Provider<Sql> sql;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void save(User user) {
        Long id = userAccess.getBest(INTERNAL_ID, user);
        if (id == null) {
            Results results = sql.get().query("SELECT id, uuid FROM POR_USER WHERE email = ?")
                    .setString(1, user.email())
                    .execute();
            if (!results.isEmpty()) {
                throw new ExistingUserException(user.email());
            }

            // If no user found, this is clearly a new user
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Insert insert = sql.get().insert("por_user")
                    .setString("uuid", user.id())
                    .setString("salt", user.salt())
                    .setString("email", user.email())
                    .setString("passwordHash", user.passwordHash())
                    .setString("firstname", user.firstName())
                    .setString("lastname", user.lastName())
                    .setTimestamp("creationTime", now)
                    .setTimestamp("updateTime", now);
            results = insert.executeAndReturns("id");
            long generatedId = results.uniqueRow().uniqueCell().asLong();
            userAccess.setBest(INTERNAL_ID, user, generatedId);
        } else {
            // If a user is found, and if it comes from DB, we can update all its fields
            sql.get().update("UPDATE por_user SET salt = ?, passwordHash = ?, locale = ?, firstname = ?, lastname = ?, updateTime = ? WHERE uuid = ?")
                    .noreturn()
                    .setString(1, user.salt())
                    .setString(2, user.passwordHash())
                    .setString(4, user.firstName())
                    .setString(5, user.lastName())
                    .setTimestamp(6, new Timestamp(System.currentTimeMillis()))
                    .setString(7, user.id())
                    .execute();
        }
    }
}
