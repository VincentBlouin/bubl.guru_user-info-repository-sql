package org.triple_brain.module.repository_sql;

import java.sql.*;

/**
 * @author Vincent Blouin
 */
public class SQLConnection {
    static Connection connection;

    public static PreparedStatement preparedStatement(String query){
        try{
            return connection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        }catch (SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    public static void closeConnection() throws SQLException{
        connection().close();
    }

    private static Connection connection(){
        try{
            if(connection == null || connection.isClosed()){
                connection = createConnection();
            }
            return connection;
        }catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }
    
    private static Connection createConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1/triple_brain",
                    "triple_brain", "boraptop34");
            return connection;
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }
  
}
