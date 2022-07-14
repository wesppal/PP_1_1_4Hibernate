package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private Transaction transaction = null;

    private final static String CREATE_USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS user(id BIGINT primary key" +
            " auto_increment, name varchar(100),  lastName varchar(100), age int)";
    private final static String DROP_USER_TABLE_QUERY = "DROP TABLE IF EXISTS user";
    private final static String ADD_NEW_USER_QUERY = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";
    private final static String CLEAN_TABLE_USER_QUERY = "TRUNCATE user";
    private final static String DELETE_USER_USER_BY_ID_QUERY = "DELETE FROM user WHERE id=(?)";
    private final static String GET_ALL_USERS_QUERY = "SELECT * FROM user";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(CREATE_USER_TABLE_QUERY).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(DROP_USER_TABLE_QUERY).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.load(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            users = session.createSQLQuery(GET_ALL_USERS_QUERY).addEntity(User.class).getResultList();
            transaction.commit();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(CLEAN_TABLE_USER_QUERY).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
