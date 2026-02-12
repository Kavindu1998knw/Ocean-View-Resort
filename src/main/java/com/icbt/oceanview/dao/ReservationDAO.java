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
      "INSERT INTO reservations (reservation_no, guest_full_name, guest_email, contact_number, room_type, "
          + "number_of_guests, check_in_date, check_out_date, special_requests, status, created_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String RESERVATION_NO_EXISTS_SQL =
      "SELECT 1 FROM reservations WHERE reservation_no = ? LIMIT 1";
  private static final String FIND_ALL_SQL =
      "SELECT * FROM reservations ORDER BY created_at DESC";
  private static final String FIND_BY_ID_SQL =
      "SELECT * FROM reservations WHERE id = ?";
  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM reservations WHERE id = ?";
  private static final String UPDATE_SQL =
      "UPDATE reservations SET guest_full_name=?, guest_email=?, contact_number=?, room_type=?, "
          + "number_of_guests=?, check_in_date=?, check_out_date=?, special_requests=?, status=? "
          + "WHERE id=?";
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
      statement.setTimestamp(11, Timestamp.valueOf(reservation.getCreatedAt()));
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

  public List<Reservation> findAll() {
    List<Reservation> reservations = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
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
      statement.setInt(5, reservation.getNumberOfGuests());
      statement.setDate(6, Date.valueOf(reservation.getCheckInDate()));
      statement.setDate(7, Date.valueOf(reservation.getCheckOutDate()));

      String specialRequests = reservation.getSpecialRequests();
      if (specialRequests == null || specialRequests.trim().isEmpty()) {
        statement.setNull(8, java.sql.Types.VARCHAR);
      } else {
        statement.setString(8, specialRequests);
      }

      statement.setString(9, reservation.getStatus());
      statement.setInt(10, reservation.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private Reservation mapReservation(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id");
    String reservationNo = resultSet.getString("reservation_no");
    String guestFullName = resultSet.getString("guest_full_name");
    String guestEmail = resultSet.getString("guest_email");
    String contactNumber = resultSet.getString("contact_number");
    String roomType = resultSet.getString("room_type");
    int numberOfGuests = resultSet.getInt("number_of_guests");
    Date checkInDate = resultSet.getDate("check_in_date");
    Date checkOutDate = resultSet.getDate("check_out_date");
    String specialRequests = resultSet.getString("special_requests");
    String status = resultSet.getString("status");
    Timestamp createdAt = resultSet.getTimestamp("created_at");

    return new Reservation(
        id,
        reservationNo,
        guestFullName,
        guestEmail,
        contactNumber,
        roomType,
        numberOfGuests,
        checkInDate != null ? checkInDate.toLocalDate() : null,
        checkOutDate != null ? checkOutDate.toLocalDate() : null,
        specialRequests,
        status,
        createdAt != null ? createdAt.toLocalDateTime() : null);
  }
}
