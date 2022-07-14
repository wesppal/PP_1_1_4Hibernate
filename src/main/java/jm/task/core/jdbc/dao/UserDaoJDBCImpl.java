package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection connection = Util.getConnection();
    private final static String CREATE_USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS user(id BIGINT primary key" +
            " auto_increment, name varchar(100),  lastName varchar(100), age int)";
    private final static String DROP_USER_TABLE_QUERY = "DROP TABLE IF EXISTS user";
    private final static String ADD_NEW_USER_QUERY = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";
    private final static String CLEAN_TABLE_USER_QUERY = "TRUNCATE user";
    private final static String DELETE_USER_USER_BY_ID_QUERY = "DELETE FROM user WHERE id=(?)";
    private final static String GET_ALL_USERS_QUERY = "SELECT * FROM user";


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        actionWithTable(CREATE_USER_TABLE_QUERY);
    }

    public void dropUsersTable() {
        actionWithTable(DROP_USER_TABLE_QUERY);
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection
                .prepareStatement(ADD_NEW_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            User user = new User();
            resultSet.next();

            user.setId(resultSet.getLong(1));
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            connection.commit();
            System.out.println(user + " was added to the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_USER_BY_ID_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            long id;
            String name;
            String lastName;
            byte age;

            while (resultSet.next()) {
                User user = new User();

                id = resultSet.getLong("id");
                name = resultSet.getString("name");
                lastName = resultSet.getString("lastName");
                age = (byte) resultSet.getInt("age");
                user.setId(id);
                user.setName(name);
                user.setLastName(lastName);
                user.setAge(age);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        actionWithTable(CLEAN_TABLE_USER_QUERY);
    }

    private void actionWithTable(String sqlQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
