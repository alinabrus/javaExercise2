package com.abrus.util.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jcourse";
    private static final String DRIVER_URL = "com.mysql.cj.jdbc.Driver";
    private static final Properties DB_PROPERTIES = new Properties();

    static {
        DB_PROPERTIES.put("user", "ab");
        DB_PROPERTIES.put("password", "123");

        try {
            Class.forName(DRIVER_URL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load JDBC driver", e);
        }
    }

    public static java.sql.Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_PROPERTIES);
        } catch (SQLException e) {
            throw new RuntimeException("Unable get connection to DB", e);
        }
    }
}
