package com.icbt.oceanview.dao;

import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.DBUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDAO {
  private static final String INSERT_SQL =
      "INSERT INTO users (name, email, password, role, active) VALUES (?, ?, ?, ?, ?)";
  private static final String FIND_BY_EMAIL_SQL =
      "SELECT id, name, email, password, role, active, created_at FROM users WHERE email = ?";
  private static final String EMAIL_EXISTS_SQL =
      "SELECT 1 FROM users WHERE email = ? LIMIT 1";
  private static final String FIND_ALL_STAFF_USERS_SQL =
      "SELECT id, name, email, password, role, active, created_at FROM users ORDER BY created_at DESC";
  private static final String TOGGLE_ACTIVE_SQL =
      "UPDATE users SET active = NOT active WHERE id = ?";
  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM users WHERE id = ?";

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
            User.fromDb(
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

  public User authenticate(String email, String password) {
    if (email == null || email.trim().isEmpty() || password == null) {
      return null;
    }

    User user = findByEmail(email.trim());
    if (user == null || !user.isActive()) {
      return null;
    }

    try {
      String inputHash = hashPassword(password);
      return inputHash.equals(user.getPassword()) ? user : null;
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

  public List<User> findAllStaffUsers() {
    List<User> users = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_STAFF_USERS_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        User user =
            User.fromDb(
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
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public boolean toggleActive(int id) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(TOGGLE_ACTIVE_SQL)) {
      statement.setInt(1, id);
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteById(int id) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
      statement.setInt(1, id);
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private String hashPassword(String password) throws SQLException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new SQLException("Unable to hash password.", e);
    }
  }
}
