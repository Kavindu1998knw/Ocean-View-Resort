package com.icbt.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {
  private DBUtil() {}

  private static final String URL =
      "jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&serverTimezone=UTC";
  private static final String USER = "root";
  private static final String PASSWORD = "123456";

  public static Connection getConnection() throws SQLException {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new SQLException("MySQL JDBC Driver not found in classpath.", e);
    }
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}
