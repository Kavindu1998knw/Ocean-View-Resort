package com.icbt.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {
  private DBUtil() {}

  private static final String DEFAULT_URL =
      "jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
  private static final String DEFAULT_USER = "root";
  private static final String DEFAULT_PASSWORD = "123456";

  public static Connection getConnection() throws SQLException {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new SQLException("MySQL JDBC Driver not found in classpath.", e);
    }

    String url = getEnvOrDefault("DB_URL", DEFAULT_URL);
    String user = getEnvOrDefault("DB_USER", DEFAULT_USER);
    String password = getEnvOrDefault("DB_PASS", DEFAULT_PASSWORD);

    return DriverManager.getConnection(url, user, password);
  }

  private static String getEnvOrDefault(String name, String defaultValue) {
    String value = System.getenv(name);
    return (value == null || value.trim().isEmpty()) ? defaultValue : value;
  }
}
