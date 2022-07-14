package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static SessionFactory sessionFactory;
    private static final String hibernateDialect = "org.hibernate.dialect.MySQL5Dialect";
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String connectionString = "jdbc:mysql://127.0.0.1:3306/katapp";
    private static final String login = "root";
    private static final String password = "root";
    private static Connection connection = null;
    private static final String open = "Connection is open.";
    private static final String close = "Connection closed.";


    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(connectionString, login, password);
            System.out.println(open);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println(close);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, driverName);
                settings.put(Environment.URL, connectionString);
                settings.put(Environment.USER, login);
                settings.put(Environment.PASS, password);
//                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//                settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                settings.put(Environment.DIALECT, hibernateDialect);

                settings.put(Environment.SHOW_SQL, "true");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                System.out.println(open);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void closeSession() {
        getSessionFactory().close();
        System.out.println(close);
    }
}
