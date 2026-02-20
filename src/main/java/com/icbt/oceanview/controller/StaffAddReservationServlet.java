package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.model.RoomInfo;
import com.icbt.oceanview.util.RoomTypes;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/reservation/add")
public class StaffAddReservationServlet extends HttpServlet {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!isStaff(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    setRoomTypes(request);
    request.getRequestDispatcher("/WEB-INF/views/staff-add-reservation.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    if (!isStaff(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String guestFullName = trimParam(request, "guestFullName");
    String guestEmail = trimParam(request, "guestEmail");
    String contactNumber = trimParam(request, "contactNumber");
    String roomType = trimParam(request, "roomType");
    String roomIdRaw = trimParam(request, "roomId");
    String numberOfGuestsRaw = trimParam(request, "numberOfGuests");
    String checkInDateRaw = trimParam(request, "checkInDate");
    String checkOutDateRaw = trimParam(request, "checkOutDate");
    String specialRequests = trimParam(request, "specialRequests");
    String status = trimParam(request, "status");
    if (status.isEmpty()) {
      status = trimParam(request, "reservationStatus");
    }

    preserveFormValues(
        request,
        guestFullName,
        guestEmail,
        contactNumber,
        roomType,
        roomIdRaw,
        numberOfGuestsRaw,
        checkInDateRaw,
        checkOutDateRaw,
        specialRequests,
        status);

    if (isBlank(guestFullName)
        || isBlank(guestEmail)
        || isBlank(contactNumber)
        || isBlank(roomType)
        || isBlank(roomIdRaw)
        || isBlank(numberOfGuestsRaw)
        || isBlank(checkInDateRaw)
        || isBlank(checkOutDateRaw)) {
      forwardWithError(request, response, "Please fill all required fields.");
      return;
    }

    if (!EMAIL_PATTERN.matcher(guestEmail).matches()) {
      forwardWithError(request, response, "Please enter a valid email address.");
      return;
    }

    int numberOfGuests;
    try {
      numberOfGuests = Integer.parseInt(numberOfGuestsRaw);
    } catch (NumberFormatException e) {
      forwardWithError(request, response, "Number of guests must be a valid number.");
      return;
    }
    if (numberOfGuests < 1) {
      forwardWithError(request, response, "Number of guests must be at least 1.");
      return;
    }

    int roomId;
    try {
      roomId = Integer.parseInt(roomIdRaw);
    } catch (NumberFormatException e) {
      forwardWithError(request, response, "Please select a valid room.");
      return;
    }
    if (roomId <= 0) {
      forwardWithError(request, response, "Please select a valid room.");
      return;
    }

    LocalDate checkInDate;
    LocalDate checkOutDate;
    try {
      checkInDate = LocalDate.parse(checkInDateRaw);
      checkOutDate = LocalDate.parse(checkOutDateRaw);
    } catch (DateTimeParseException e) {
      forwardWithError(request, response, "Please enter valid check-in and check-out dates.");
      return;
    }

    if (!checkInDate.isBefore(checkOutDate)) {
      forwardWithError(request, response, "Check-in date must be earlier than check-out date.");
      return;
    }

    if (!isBlank(status)) {
      String normalizedStatus = status.toUpperCase(Locale.ROOT);
      if (!"PENDING".equals(normalizedStatus)
          && !"CONFIRMED".equals(normalizedStatus)
          && !"CANCELLED".equals(normalizedStatus)) {
        forwardWithError(request, response, "Invalid reservation status.");
        return;
      }
      status = normalizedStatus;
    }

    RoomManagementDAO roomDAO = new RoomManagementDAO();
    RoomInfo selectedRoom = roomDAO.findById(roomId);
    if (selectedRoom == null
        || !selectedRoom.isActive()
        || !roomType.equals(selectedRoom.getRoomType())) {
      forwardWithError(request, response, "Please select a valid room.");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    if (reservationDAO.hasOverlappingReservationForRoom(roomId, checkInDate, checkOutDate)) {
      forwardWithError(
          request,
          response,
          "This room is already reserved for the selected dates.");
      return;
    }
    String reservationNo = reservationDAO.generateReservationNo();
    if (reservationNo == null) {
      forwardWithError(request, response, "Unable to generate reservation number. Please try again.");
      return;
    }

    Reservation reservation =
        Reservation.newForCreate(
            reservationNo,
            guestFullName,
            guestEmail,
            contactNumber,
            roomType,
            roomId,
            numberOfGuests,
            checkInDate,
            checkOutDate,
            specialRequests,
            status,
            LocalDateTime.now());

    boolean saved = reservationDAO.insert(reservation);
    if (saved) {
      request.setAttribute("success", "Reservation created successfully.");
      request.setAttribute("reservationNo", reservationNo);
    } else {
      request.setAttribute("error", "Failed to create reservation. Please try again.");
    }

    setRoomTypes(request);
    request.getRequestDispatcher("/WEB-INF/views/staff-add-reservation.jsp").forward(request, response);
  }

  private boolean isStaff(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    Object roleObj = session.getAttribute("authRole");
    if (roleObj == null) {
      return false;
    }
    String role = String.valueOf(roleObj);
    return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
  }

  private void preserveFormValues(
      HttpServletRequest request,
      String guestFullName,
      String guestEmail,
      String contactNumber,
      String roomType,
      String roomId,
      String numberOfGuests,
      String checkInDate,
      String checkOutDate,
      String specialRequests,
      String status) {
    request.setAttribute("guestFullName", guestFullName);
    request.setAttribute("guestEmail", guestEmail);
    request.setAttribute("contactNumber", contactNumber);
    request.setAttribute("roomType", roomType);
    request.setAttribute("roomId", roomId);
    request.setAttribute("numberOfGuests", numberOfGuests);
    request.setAttribute("checkInDate", checkInDate);
    request.setAttribute("checkOutDate", checkOutDate);
    request.setAttribute("specialRequests", specialRequests);
    request.setAttribute("reservationStatus", status);
  }

  private void forwardWithError(
      HttpServletRequest request, HttpServletResponse response, String message)
      throws ServletException, IOException {
    request.setAttribute("error", message);
    setRoomTypes(request);
    request.getRequestDispatcher("/WEB-INF/views/staff-add-reservation.jsp").forward(request, response);
  }

  private void setRoomTypes(HttpServletRequest request) {
    request.setAttribute("roomTypes", RoomTypes.ROOM_TYPES);
  }

  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
