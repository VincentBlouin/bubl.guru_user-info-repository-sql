package org.triple_brain.module.repository_sql;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.triple_brain.module.model.User;
import org.triple_brain.module.repository.user.user.ExistingUserException;
import org.triple_brain.module.repository.user.user.NonExistingUserException;
import org.triple_brain.module.repository.user.user.UserRepository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.triple_brain.module.model.json.UserJSONFields.*;
import static org.triple_brain.module.repository_sql.SQLConnection.preparedStatement;

/**
 * @author Vincent Blouin
 */
public class SQLUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        Long id = getInternalId(user);
        if (id == null) {
            if(emailExists(user.email())){
                throw new ExistingUserException(user.email());
            }
            // If no user found, this is clearly a new user
            String query = "insert into por_user(uuid, salt, email, passwordHash, firstname, lastname, creationTime, updateTime) values(?, ?, ?, ?, ?, ?, ?, ?);";
            Timestamp now = new Timestamp(System.currentTimeMillis());
            PreparedStatement stm = preparedStatement(query);
            try{
                stm.setString(1, user.id());
                stm.setString(2, user.salt());
                stm.setString(3, user.email());
                stm.setString(4, user.passwordHash());
                stm.setString(5, user.firstName());
                stm.setString(6, user.lastName());
                stm.setTimestamp(7, now);
                stm.setTimestamp(8, now);
                stm.executeUpdate();
                ResultSet resultSet = stm.getGeneratedKeys();
                resultSet.next();
                long generatedId = resultSet.getLong(1);
                setUserInternalId(user, generatedId);
            }catch(SQLException ex){
                throw new SQLConnectionException(ex);
            }
        } else {
            // If a user is found, and if it comes from DB, we can update all its fields
            String query = "UPDATE por_user SET salt = ?, passwordHash = ?, locale = ?, firstname = ?, lastname = ?, updateTime = ? WHERE uuid = ?";
            PreparedStatement stm = preparedStatement(query);
            try{
                stm.setString(1, user.salt());
                stm.setString(2, user.passwordHash());
                stm.setString(4, user.firstName());
                stm.setString(5, user.lastName());
                stm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                stm.setString(7, user.id());
                stm.executeUpdate();
            }
            catch(SQLException ex){
                throw new SQLConnectionException(ex);
            }
        }
    }

    @Override
    public User findById(String id) throws NonExistingUserException {
        String query = "SELECT id as internalID, email, uuid as id, salt, passwordHash, firstname, lastname FROM por_user WHERE uuid = ?";
        try {
            PreparedStatement stm = preparedStatement(query);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return userFromResultSet(rs);
            }else{
                throw new NonExistingUserException(id);
            }
        } catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    @Override
    public User findByEmail(String email) throws NonExistingUserException {
        String query = "SELECT id as internalId, email, uuid as id, salt, passwordHash, firstname, lastname FROM por_user WHERE email = ?";
        try {
            PreparedStatement stm = preparedStatement(query);
            stm.setString(1, email.trim().toLowerCase());
            ResultSet rs = stm.executeQuery();
            if(!rs.next()){
                throw new NonExistingUserException(email);
            }
            return userFromResultSet(rs);
        } catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    @Override
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(email) FROM por_user WHERE email = ?";
        try{
            PreparedStatement stm = preparedStatement(query);
            stm.setString(1, email.trim().toLowerCase());
            ResultSet resultSet = stm.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) >= 1;

        }catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    protected User userFromResultSet(ResultSet rs){
        try{
            User user = User.withEmail(rs.getString("email"))
                    .firstName(rs.getString("firstname"))
                    .lastName(rs.getString("lastname"));

            setUserInternalId(user, rs.getLong("internalId"));
            setUUId(user, rs.getString("id"));
            setSalt(user, rs.getString("salt"));
            setPasswordHash(user, rs.getString("passwordHash"));
            return user;
        }catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    @Override
    public JSONObject findByIdAsJson(String id) throws NonExistingUserException, JSONException {
        User user = findById(id);
        return toJson(user);
    }

    private JSONObject toJson(User user) throws JSONException {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put(ID, user.id());
        jsonUser.put(EMAIL, user.email());
        jsonUser.put(FIRST_NAME, user.firstName());
        jsonUser.put(LAST_NAME, user.lastName());
        return jsonUser;
    }

    protected void setUserInternalId(User user, long internalId){
        try{
            Field field = User.class.getDeclaredField("internalId");
            field.setAccessible(true);
            field.set(user, internalId);
            field.setAccessible(false);
        }catch(NoSuchFieldException | IllegalAccessException ex){
            throw new ReflectionException(ex);
        }
    }

    protected void setUUId(User user, String id){
        try{
            Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
            field.setAccessible(false);
        }catch(NoSuchFieldException | IllegalAccessException ex){
            throw new ReflectionException(ex);
        }
    }

    protected void setSalt(User user, String salt){
        try{
            Field field = User.class.getDeclaredField("salt");
            field.setAccessible(true);
            field.set(user, salt);
            field.setAccessible(false);
        }catch(NoSuchFieldException | IllegalAccessException ex){
            throw new ReflectionException(ex);
        }
    }

    protected void setPasswordHash(User user, String passwordHash){
        try{
            Field field = User.class.getDeclaredField("passwordHash");
            field.setAccessible(true);
            field.set(user, passwordHash);
            field.setAccessible(false);
        }catch(NoSuchFieldException | IllegalAccessException ex){
            throw new ReflectionException(ex);
        }
    }


    protected Long getInternalId(User user){
        try{
            Field field = User.class.getDeclaredField("internalId");
            field.setAccessible(true);
            return (Long) field.get(user);
        }catch(NoSuchFieldException | IllegalAccessException ex){
            throw new ReflectionException(ex);
        }
    }
}
