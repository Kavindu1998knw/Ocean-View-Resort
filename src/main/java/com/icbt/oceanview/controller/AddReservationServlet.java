package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.model.RoomInfo;
import com.icbt.oceanview.util.RoomTypes;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservations/add")
public class AddReservationServlet extends HttpServlet {
  private static final Set<String> ALLOWED_STATUSES =
      new LinkedHashSet<String>() {
        {
          add("PENDING");
          add("CONFIRMED");
          add("CANCELLED");
          add("CHECKED_IN");
        }
      };

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    String success = ValidationUtil.trimToNull(request.getParameter("success"));
    String reservationNo = ValidationUtil.trimToNull(request.getParameter("reservationNo"));
    if (success != null) {
      request.setAttribute("success", success);
    }
    if (reservationNo != null) {
      request.setAttribute("reservationNo", reservationNo);
    }

    setRoomTypes(request);
    request.getRequestDispatcher(resolveView(role)).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    ValidationResult vr = new ValidationResult();
    RequestUtil.collectOldValues(
        request,
        vr,
        "guestFullName",
        "guestEmail",
        "contactNumber",
        "roomType",
        "roomId",
        "numberOfGuests",
        "checkInDate",
        "checkOutDate",
        "specialRequests",
        "reservationStatus");

    String guestFullName = ValidationUtil.trimToNull(request.getParameter("guestFullName"));
    String guestEmail = ValidationUtil.trimToNull(request.getParameter("guestEmail"));
    String contactNumber = ValidationUtil.trimToNull(request.getParameter("contactNumber"));
    String roomType = ValidationUtil.trimToNull(request.getParameter("roomType"));
    Integer roomId = ValidationUtil.parseIntSafe(request.getParameter("roomId"));
    Integer numberOfGuests = ValidationUtil.parseIntSafe(request.getParameter("numberOfGuests"));
    LocalDate checkInDate = ValidationUtil.parseDateSafe(request.getParameter("checkInDate"));
    LocalDate checkOutDate = ValidationUtil.parseDateSafe(request.getParameter("checkOutDate"));
    String specialRequests = ValidationUtil.trimToNull(request.getParameter("specialRequests"));

    String status = ValidationUtil.trimToNull(request.getParameter("status"));
    if (status == null) {
      status = ValidationUtil.trimToNull(request.getParameter("reservationStatus"));
    }
    if (status != null) {
      status = status.toUpperCase(Locale.ROOT);
      vr.getOldValues().put("reservationStatus", status);
    }

    if (ValidationUtil.isBlank(guestFullName)) {
      vr.addError("guestFullName", "Guest full name is required.");
    } else if (guestFullName.length() < 2 || guestFullName.length() > 120) {
      vr.addError("guestFullName", "Guest full name must be between 2 and 120 characters.");
    }

    if (ValidationUtil.isBlank(guestEmail)) {
      vr.addError("guestEmail", "Guest email is required.");
    } else if (!ValidationUtil.isValidEmail(guestEmail)) {
      vr.addError("guestEmail", "Enter a valid guest email address.");
    }

    if (ValidationUtil.isBlank(contactNumber)) {
      vr.addError("contactNumber", "Contact number is required.");
    } else if (!ValidationUtil.isValidSriLankaMobile(contactNumber)
        && !ValidationUtil.isValidPhone10(contactNumber.replaceAll("[^0-9]", ""))) {
      vr.addError("contactNumber", "Enter a valid contact number.");
    }

    Set<String> allowedRoomTypes = new LinkedHashSet<>(RoomTypes.getRoomTypes());
    if (ValidationUtil.isBlank(roomType)) {
      vr.addError("roomType", "Room type is required.");
    } else if (!ValidationUtil.isInAllowedSet(roomType, allowedRoomTypes)) {
      vr.addError("roomType", "Invalid room type selected.");
    }

    if (!ValidationUtil.isPositiveInt(roomId)) {
      vr.addError("roomId", "Please select a valid room.");
    }

    if (!ValidationUtil.isPositiveInt(numberOfGuests)) {
      vr.addError("numberOfGuests", "Number of guests must be at least 1.");
    }

    if (checkInDate == null) {
      vr.addError("checkInDate", "Check-in date is required.");
    }

    if (checkOutDate == null) {
      vr.addError("checkOutDate", "Check-out date is required.");
    }

    if (checkInDate != null
        && checkOutDate != null
        && !ValidationUtil.isDateOrderValid(checkInDate, checkOutDate)) {
      vr.addError("checkOutDate", "Check-out date must be later than check-in date.");
    }

    if (ValidationUtil.isBlank(status)) {
      vr.addError("reservationStatus", "Reservation status is required.");
    } else if (!ValidationUtil.isInAllowedSet(status, ALLOWED_STATUSES)) {
      vr.addError("reservationStatus", "Invalid reservation status.");
    }

    RoomManagementDAO roomDAO = new RoomManagementDAO();
    if (vr.error("roomId") == null && vr.error("roomType") == null) {
      RoomInfo selectedRoom = roomDAO.findById(roomId);
      if (selectedRoom == null
          || !selectedRoom.isActive()
          || !roomType.equals(selectedRoom.getRoomType())) {
        vr.addError("roomId", "Selected room is invalid for the chosen room type.");
      }
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    if (vr.error("roomId") == null
        && vr.error("checkInDate") == null
        && vr.error("checkOutDate") == null
        && reservationDAO.hasOverlappingReservationForRoom(roomId, checkInDate, checkOutDate)) {
      vr.addError("roomId", "Room is not available for selected dates.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      setRoomTypes(request);
      RequestUtil.forwardWithErrors(request, response, resolveView(role), vr);
      return;
    }

    String reservationNo = reservationDAO.generateReservationNo();
    if (reservationNo == null) {
      vr.addError("global", "Unable to generate reservation number. Please try again.");
      setRoomTypes(request);
      RequestUtil.forwardWithErrors(request, response, resolveView(role), vr);
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
      response.sendRedirect(
          request.getContextPath()
              + "/reservations/add?success="
              + encode("Reservation created successfully.")
              + "&reservationNo="
              + encode(reservationNo));
      return;
    }

    vr.addError("global", "Failed to create reservation. Please try again.");
    setRoomTypes(request);
    RequestUtil.forwardWithErrors(request, response, resolveView(role), vr);
  }

  private String resolveView(String role) {
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      return "/WEB-INF/views/add-reservation.jsp";
    }
    return "/WEB-INF/views/staff-add-reservation.jsp";
  }

  private void setRoomTypes(HttpServletRequest request) {
    request.setAttribute("roomTypes", RoomTypes.getRoomTypes());
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
}
