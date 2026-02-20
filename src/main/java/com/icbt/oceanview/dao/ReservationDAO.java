package com.icbt.oceanview.dao;

import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.DBUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservationDAO {
  private static final String INSERT_SQL =
      "INSERT INTO reservations (reservation_no, guest_full_name, guest_email, contact_number, room_type, room_id, "
          + "number_of_guests, check_in_date, check_out_date, special_requests, status, created_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String RESERVATION_NO_EXISTS_SQL =
      "SELECT 1 FROM reservations WHERE reservation_no = ? LIMIT 1";
  private static final String FIND_ALL_WITH_ROOM_NO_SQL =
      "SELECT res.*, r.room_no FROM reservations res LEFT JOIN rooms r ON res.room_id = r.id "
          + "ORDER BY res.created_at DESC";
  private static final String FIND_BY_ID_SQL =
      "SELECT * FROM reservations WHERE id = ?";
  private static final String FIND_BY_RESERVATION_NO_SQL =
      "SELECT res.*, r.room_no FROM reservations res LEFT JOIN rooms r ON res.room_id = r.id "
          + "WHERE res.reservation_no = ? LIMIT 1";
  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM reservations WHERE id = ?";
  private static final String UPDATE_SQL =
      "UPDATE reservations SET guest_full_name=?, guest_email=?, contact_number=?, room_type=?, room_id=?, "
          + "number_of_guests=?, check_in_date=?, check_out_date=?, special_requests=?, status=? "
          + "WHERE id=?";
  private static final String COUNT_ALL_SQL = "SELECT COUNT(*) FROM reservations";
  private static final String COUNT_TODAY_CHECKINS_SQL =
      "SELECT COUNT(*) FROM reservations WHERE check_in_date = CURDATE()";
  private static final String COUNT_TOTAL_GUESTS_SQL =
      "SELECT COALESCE(SUM(number_of_guests), 0) FROM reservations";
  private static final String FIND_RECENT_SQL =
      "SELECT * FROM reservations ORDER BY created_at DESC LIMIT ?";
  private static final String FIND_RECENT_WITH_ROOM_NO_SQL =
      "SELECT res.*, r.room_no FROM reservations res LEFT JOIN rooms r ON res.room_id = r.id "
          + "ORDER BY res.created_at DESC LIMIT ?";
  private static final String OVERLAP_EXISTS_BY_ROOM_SQL =
      "SELECT 1 FROM reservations WHERE room_id = ? AND status <> 'CANCELLED' "
          + "AND check_in_date < ? AND check_out_date > ? LIMIT 1";
  private static final String OVERLAP_EXISTS_BY_ROOM_EXCLUDING_ID_SQL =
      "SELECT 1 FROM reservations WHERE room_id = ? AND status <> 'CANCELLED' "
          + "AND check_in_date < ? AND check_out_date > ? AND id <> ? LIMIT 1";
  private static final int MAX_GENERATION_ATTEMPTS = 10;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
  private final Random random = new Random();

  public boolean insert(Reservation reservation) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
      statement.setString(1, reservation.getReservationNo());
      statement.setString(2, reservation.getGuestFullName());
      statement.setString(3, reservation.getGuestEmail());
      statement.setString(4, reservation.getContactNumber());
      statement.setString(5, reservation.getRoomType());
      if (reservation.getRoomId() == null) {
        statement.setNull(6, java.sql.Types.INTEGER);
      } else {
        statement.setInt(6, reservation.getRoomId());
      }
      statement.setInt(7, reservation.getNumberOfGuests());
      statement.setDate(8, Date.valueOf(reservation.getCheckInDate()));
      statement.setDate(9, Date.valueOf(reservation.getCheckOutDate()));

      String specialRequests = reservation.getSpecialRequests();
      if (specialRequests == null || specialRequests.trim().isEmpty()) {
        statement.setNull(10, java.sql.Types.VARCHAR);
      } else {
        statement.setString(10, specialRequests);
      }

      statement.setString(11, reservation.getStatus());
      statement.setTimestamp(12, Timestamp.valueOf(reservation.getCreatedAt()));
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean reservationNoExists(String reservationNo) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(RESERVATION_NO_EXISTS_SQL)) {
      statement.setString(1, reservationNo);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  public String generateReservationNo() {
    String datePart = LocalDate.now().format(DATE_FORMAT);
    for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
      int randomPart = 1000 + random.nextInt(9000);
      String candidate = "RES-" + datePart + "-" + randomPart;
      if (!reservationNoExists(candidate)) {
        return candidate;
      }
    }
    return null;
  }

  public List<Reservation> findAllWithRoomNo() {
    List<Reservation> reservations = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_WITH_ROOM_NO_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        reservations.add(mapReservation(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return reservations;
  }

  public Reservation findById(int id) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return mapReservation(resultSet);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Reservation findByReservationNo(String reservationNo) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_RESERVATION_NO_SQL)) {
      statement.setString(1, reservationNo);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return mapReservation(resultSet);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
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

  public boolean update(Reservation reservation) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
      statement.setString(1, reservation.getGuestFullName());
      statement.setString(2, reservation.getGuestEmail());
      statement.setString(3, reservation.getContactNumber());
      statement.setString(4, reservation.getRoomType());
      if (reservation.getRoomId() == null) {
        statement.setNull(5, java.sql.Types.INTEGER);
      } else {
        statement.setInt(5, reservation.getRoomId());
      }
      statement.setInt(6, reservation.getNumberOfGuests());
      statement.setDate(7, Date.valueOf(reservation.getCheckInDate()));
      statement.setDate(8, Date.valueOf(reservation.getCheckOutDate()));

      String specialRequests = reservation.getSpecialRequests();
      if (specialRequests == null || specialRequests.trim().isEmpty()) {
        statement.setNull(9, java.sql.Types.VARCHAR);
      } else {
        statement.setString(9, specialRequests);
      }

      statement.setString(10, reservation.getStatus());
      statement.setInt(11, reservation.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public int countAllReservations() {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(COUNT_ALL_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      return resultSet.next() ? resultSet.getInt(1) : 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public int countTodayCheckIns() {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(COUNT_TODAY_CHECKINS_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      return resultSet.next() ? resultSet.getInt(1) : 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public int countTotalGuests() {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(COUNT_TOTAL_GUESTS_SQL);
        ResultSet resultSet = statement.executeQuery()) {
      return resultSet.next() ? resultSet.getInt(1) : 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public List<Reservation> findRecentReservations(int limit) {
    List<Reservation> reservations = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_RECENT_SQL)) {
      statement.setInt(1, limit);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          reservations.add(mapReservation(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return reservations;
  }

  public List<Reservation> findRecentReservationsWithRoomNo(int limit) {
    List<Reservation> reservations = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_RECENT_WITH_ROOM_NO_SQL)) {
      statement.setInt(1, limit);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          reservations.add(mapReservation(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return reservations;
  }

  public boolean hasOverlappingReservationForRoom(
      int roomId, LocalDate checkIn, LocalDate checkOut) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(OVERLAP_EXISTS_BY_ROOM_SQL)) {
      statement.setInt(1, roomId);
      statement.setDate(2, Date.valueOf(checkOut));
      statement.setDate(3, Date.valueOf(checkIn));
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean hasOverlappingReservationForRoomExcludingId(
      int id, int roomId, LocalDate checkIn, LocalDate checkOut) {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(OVERLAP_EXISTS_BY_ROOM_EXCLUDING_ID_SQL)) {
      statement.setInt(1, roomId);
      statement.setDate(2, Date.valueOf(checkOut));
      statement.setDate(3, Date.valueOf(checkIn));
      statement.setInt(4, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  private Reservation mapReservation(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id");
    String reservationNo = resultSet.getString("reservation_no");
    String guestFullName = resultSet.getString("guest_full_name");
    String guestEmail = resultSet.getString("guest_email");
    String contactNumber = resultSet.getString("contact_number");
    String roomType = resultSet.getString("room_type");
    Integer roomId = null;
    Object roomIdObj = resultSet.getObject("room_id");
    if (roomIdObj != null) {
      roomId = ((Number) roomIdObj).intValue();
    }
    int numberOfGuests = resultSet.getInt("number_of_guests");
    Date checkInDate = resultSet.getDate("check_in_date");
    Date checkOutDate = resultSet.getDate("check_out_date");
    String specialRequests = resultSet.getString("special_requests");
    String status = resultSet.getString("status");
    Timestamp createdAt = resultSet.getTimestamp("created_at");

    Reservation reservation =
        new Reservation(
        id,
        reservationNo,
        guestFullName,
        guestEmail,
        contactNumber,
        roomType,
        roomId,
        numberOfGuests,
        checkInDate != null ? checkInDate.toLocalDate() : null,
        checkOutDate != null ? checkOutDate.toLocalDate() : null,
        specialRequests,
        status,
        createdAt != null ? createdAt.toLocalDateTime() : null);
    if (hasColumn(resultSet, "room_no")) {
      reservation.setRoomNo(resultSet.getString("room_no"));
    }
    return reservation;
  }

  private boolean hasColumn(ResultSet resultSet, String name) throws SQLException {
    int columnCount = resultSet.getMetaData().getColumnCount();
    for (int i = 1; i <= columnCount; i++) {
      if (name.equalsIgnoreCase(resultSet.getMetaData().getColumnLabel(i))) {
        return true;
      }
    }
    return false;
  }
}
