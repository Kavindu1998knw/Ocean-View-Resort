package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/reservation/update")
public class UpdateReservationServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
    }

    String idParam = trimParam(request, "id");
    if (idParam.isEmpty()) {
      redirectWithError(response, request, "Invalid reservation ID.");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      redirectWithError(response, request, "Invalid reservation ID.");
      return;
    }

    String guestFullName = trimParam(request, "guestFullName");
    String guestEmail = trimParam(request, "guestEmail");
    String contactNumber = trimParam(request, "contactNumber");
    String roomType = trimParam(request, "roomType");
    String numberOfGuestsRaw = trimParam(request, "numberOfGuests");
    String checkInDateRaw = trimParam(request, "checkInDate");
    String checkOutDateRaw = trimParam(request, "checkOutDate");
    String specialRequests = trimParam(request, "specialRequests");
    String status = trimParam(request, "status");

    if (isBlank(guestFullName)
        || isBlank(guestEmail)
        || isBlank(contactNumber)
        || isBlank(roomType)
        || isBlank(numberOfGuestsRaw)
        || isBlank(checkInDateRaw)
        || isBlank(checkOutDateRaw)
        || isBlank(status)) {
      redirectWithError(response, request, "Please fill all required fields.");
      return;
    }

    int numberOfGuests;
    try {
      numberOfGuests = Integer.parseInt(numberOfGuestsRaw);
    } catch (NumberFormatException e) {
      redirectWithError(response, request, "Number of guests must be a valid number.");
      return;
    }
    if (numberOfGuests < 1) {
      redirectWithError(response, request, "Number of guests must be at least 1.");
      return;
    }

    LocalDate checkInDate;
    LocalDate checkOutDate;
    try {
      checkInDate = LocalDate.parse(checkInDateRaw);
      checkOutDate = LocalDate.parse(checkOutDateRaw);
    } catch (DateTimeParseException e) {
      redirectWithError(response, request, "Please enter valid check-in and check-out dates.");
      return;
    }

    if (!checkInDate.isBefore(checkOutDate)) {
      redirectWithError(response, request, "Check-in date must be earlier than check-out date.");
      return;
    }

    String normalizedStatus = status.toUpperCase(Locale.ROOT);
    if (!"PENDING".equals(normalizedStatus)
        && !"CONFIRMED".equals(normalizedStatus)
        && !"CANCELLED".equals(normalizedStatus)) {
      redirectWithError(response, request, "Invalid reservation status.");
      return;
    }

    Reservation reservation = new Reservation();
    reservation.setId(id);
    reservation.setGuestFullName(guestFullName);
    reservation.setGuestEmail(guestEmail);
    reservation.setContactNumber(contactNumber);
    reservation.setRoomType(roomType);
    reservation.setNumberOfGuests(numberOfGuests);
    reservation.setCheckInDate(checkInDate);
    reservation.setCheckOutDate(checkOutDate);
    reservation.setSpecialRequests(specialRequests);
    reservation.setStatus(normalizedStatus);

    ReservationDAO reservationDAO = new ReservationDAO();
    boolean updated = reservationDAO.update(reservation);
    if (updated) {
      response.sendRedirect(
          request.getContextPath()
              + "/admin/reservations?success=Reservation+updated+successfully.");
    } else {
      redirectWithError(response, request, "Failed to update reservation. Please try again.");
    }
  }

  private void redirectWithError(HttpServletResponse response, HttpServletRequest request, String message)
      throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/reservations?error=" + encode(message));
  }

  private String encode(String value) {
    if (value == null) {
      return "";
    }
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (java.io.UnsupportedEncodingException e) {
      return "";
    }
  }

  private boolean isAdmin(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    Object role = session == null ? null : session.getAttribute("role");
    return role != null && "ADMIN".equalsIgnoreCase(String.valueOf(role));
  }

  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
