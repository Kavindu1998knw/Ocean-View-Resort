package com.icbt.oceanview.dao;

import com.icbt.oceanview.model.RoomInfo;
import com.icbt.oceanview.util.DBUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RoomManagementDAO {
  private static final String FIND_ALL_WITH_PRICES_SQL =
      "SELECT id, room_no, room_type, price_per_night, active, created_at "
          + "FROM rooms ORDER BY created_at DESC";
  private static final String FIND_BY_ID_SQL =
      "SELECT id, room_no, room_type, price_per_night, active, created_at "
          + "FROM rooms WHERE id = ?";
  private static final String INSERT_ROOM_SQL =
      "INSERT INTO rooms (room_no, room_type, price_per_night, active) VALUES (?, ?, ?, ?)";
  private static final String UPDATE_ROOM_SQL =
      "UPDATE rooms SET room_no = ?, room_type = ?, price_per_night = ?, active = ? WHERE id = ?";
  private static final String TOGGLE_ACTIVE_SQL =
      "UPDATE rooms SET active = IF(active = 1, 0, 1) WHERE id = ?";
  private static final String DELETE_BY_ID_SQL = "DELETE FROM rooms WHERE id = ?";
  private static final String ROOM_NO_EXISTS_SQL =
      "SELECT 1 FROM rooms WHERE room_no = ? LIMIT 1";
  private static final String ROOM_NO_EXISTS_EXCLUDING_ID_SQL =
      "SELECT 1 FROM rooms WHERE room_no = ? AND id <> ? LIMIT 1";
  private static final String FIND_PRICE_BY_ROOM_TYPE_SQL =
      "SELECT price_per_night FROM rooms WHERE room_type = ? AND active = 1 ORDER BY id DESC LIMIT 1";
  private static final String FIND_ACTIVE_BY_TYPE_SQL =
      "SELECT id, room_no, room_type, price_per_night, active, created_at "
          + "FROM rooms WHERE active = 1 AND room_type = ? ORDER BY room_no";
  private static final String COUNT_AVAILABLE_ROOMS_SQL =
      "SELECT COUNT(*) AS cnt "
          + "FROM rooms r "
          + "WHERE r.active = 1 "
          + "AND r.id NOT IN ("
          + "SELECT res.room_id "
          + "FROM reservations res "
          + "WHERE res.status IN ('PENDING','CONFIRMED','CHECKED_IN') "
          + "AND ? >= res.check_in_date "
          + "AND ? < res.check_out_date "
          + "AND res.room_id IS NOT NULL"
          + ")";

  public List<RoomInfo> findAllRoomsWithPrices() {
    List<RoomInfo> rooms = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_WITH_PRICES_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        rooms.add(mapRoomInfo(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rooms;
  }

  public RoomInfo findById(int id) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return mapRoomInfo(resultSet);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean roomNoExists(String roomNo) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(ROOM_NO_EXISTS_SQL)) {
      statement.setString(1, roomNo);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean roomNoExistsExcludingId(int id, String roomNo) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(ROOM_NO_EXISTS_EXCLUDING_ID_SQL)) {
      statement.setString(1, roomNo);
      statement.setInt(2, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean createRoomWithPrice(RoomInfo info) {
    Connection connection = null;
    try {
      connection = DBUtil.getConnection();
      connection.setAutoCommit(false);
      try (PreparedStatement roomStatement = connection.prepareStatement(INSERT_ROOM_SQL)) {
        roomStatement.setString(1, info.getRoomNo());
        roomStatement.setString(2, info.getRoomType());
        roomStatement.setBigDecimal(3, info.getPricePerNight());
        roomStatement.setBoolean(4, info.isActive());
        int roomInserted = roomStatement.executeUpdate();

        if (roomInserted != 1) {
          connection.rollback();
          return false;
        }
        connection.commit();
        return true;
      }
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
      return false;
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public boolean updateRoomWithPrice(RoomInfo info) {
    Connection connection = null;
    try {
      connection = DBUtil.getConnection();
      connection.setAutoCommit(false);
      try (PreparedStatement roomStatement = connection.prepareStatement(UPDATE_ROOM_SQL)) {
        roomStatement.setString(1, info.getRoomNo());
        roomStatement.setString(2, info.getRoomType());
        roomStatement.setBigDecimal(3, info.getPricePerNight());
        roomStatement.setBoolean(4, info.isActive());
        roomStatement.setInt(5, info.getId());
        int roomUpdated = roomStatement.executeUpdate();

        if (roomUpdated != 1) {
          connection.rollback();
          return false;
        }
        connection.commit();
        return true;
      }
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
      return false;
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
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

  public BigDecimal findPriceByRoomType(String roomType) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_PRICE_BY_ROOM_TYPE_SQL)) {
      statement.setString(1, roomType);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getBigDecimal("price_per_night");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<RoomInfo> findActiveRoomsByType(String roomType) {
    List<RoomInfo> rooms = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE_BY_TYPE_SQL)) {
      statement.setString(1, roomType);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          rooms.add(mapRoomInfo(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rooms;
  }

  public int countAvailableRooms(java.time.LocalDate date) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(COUNT_AVAILABLE_ROOMS_SQL)) {
      java.sql.Date sqlDate = java.sql.Date.valueOf(date);
      statement.setDate(1, sqlDate);
      statement.setDate(2, sqlDate);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next() ? resultSet.getInt("cnt") : 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  private RoomInfo mapRoomInfo(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id");
    String roomNo = resultSet.getString("room_no");
    String roomType = resultSet.getString("room_type");
    boolean active = resultSet.getBoolean("active");
    Timestamp createdAtTs = resultSet.getTimestamp("created_at");
    BigDecimal pricePerNight = resultSet.getBigDecimal("price_per_night");
    return new RoomInfo(
        id,
        roomNo,
        roomType,
        active,
        pricePerNight,
        createdAtTs != null ? createdAtTs.toLocalDateTime() : null,
        null);
  }
}
