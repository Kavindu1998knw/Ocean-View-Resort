package com.icbt.oceanview.util;

import java.sql.Connection;
import java.sql.SQLException;

public final class DBUtil {
  private DBUtil() {}

  public static Connection getConnection() throws SQLException {
    return DBConnection.getConnection();
  }
}
