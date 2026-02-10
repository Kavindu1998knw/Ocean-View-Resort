package com.icbt.oceanview.dao;

import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDAO {
  private static final String INSERT_SQL =
      "INSERT INTO users (name, email, password, role, active) VALUES (?, ?, ?, ?, ?)";
  private static final String FIND_BY_EMAIL_SQL =
      "SELECT id, name, email, password, role, active, created_at FROM users WHERE email = ?";
  private static final String EMAIL_EXISTS_SQL =
      "SELECT 1 FROM users WHERE email = ? LIMIT 1";

  public boolean insert(User user) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getRole());
      statement.setBoolean(5, user.isActive());
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public User findByEmail(String email) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
      statement.setString(1, email);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }

        User user =
            new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role"),
                resultSet.getBoolean("active"),
                null);

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
          user.setCreatedAt(createdAt.toLocalDateTime());
        }

        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean emailExists(String email) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(EMAIL_EXISTS_SQL)) {
      statement.setString(1, email);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
