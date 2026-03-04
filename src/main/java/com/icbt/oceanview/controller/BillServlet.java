package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {
  private static final BigDecimal SERVICE_CHARGE_RATE = new BigDecimal("0.10");

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    setRecentReservations(request, reservationDAO);

    String reservationNo = ValidationUtil.trimToNull(request.getParameter("reservationNo"));
    if (reservationNo == null) {
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    Reservation reservation = reservationDAO.findByReservationNo(reservationNo);
    if (reservation == null) {
      setFieldError(request, "reservationNo", "Reservation not found.", reservationNo);
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    LocalDate checkInDate = reservation.getCheckInDate();
    LocalDate checkOutDate = reservation.getCheckOutDate();
    if (checkInDate == null || checkOutDate == null) {
      setGlobalError(request, "Reservation dates are invalid for billing.", reservationNo);
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    long dayDiff = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    int nights = (int) Math.max(1L, dayDiff);

    RoomManagementDAO roomManagementDAO = new RoomManagementDAO();
    BigDecimal rate = roomManagementDAO.findPriceByRoomType(reservation.getRoomType());
    if (rate == null) {
      setGlobalError(request, "Room price is not configured for this room type.", reservationNo);
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
    BigDecimal serviceCharge =
        subtotal.multiply(SERVICE_CHARGE_RATE).setScale(2, RoundingMode.HALF_UP);
    BigDecimal total = subtotal.add(serviceCharge).setScale(2, RoundingMode.HALF_UP);

    request.setAttribute("reservation", reservation);
    request.setAttribute("nights", nights);
    request.setAttribute("rate", rate.setScale(2, RoundingMode.HALF_UP));
    request.setAttribute("subtotal", subtotal);
    request.setAttribute("serviceCharge", serviceCharge);
    request.setAttribute("total", total);
    request.getRequestDispatcher(resolveView(role)).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    ValidationResult vr = new ValidationResult();
    RequestUtil.collectOldValues(request, vr, "reservationNo");

    String reservationNo = ValidationUtil.trimToNull(request.getParameter("reservationNo"));
    if (reservationNo == null) {
      vr.addError("reservationNo", "Reservation number is required.");
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    setRecentReservations(request, reservationDAO);

    if (vr.error("reservationNo") == null && reservationDAO.findByReservationNo(reservationNo) == null) {
      vr.addError("reservationNo", "Reservation not found.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      RequestUtil.forwardWithErrors(request, response, resolveView(role), vr);
      return;
    }

    response.sendRedirect(
        request.getContextPath() + "/bill?reservationNo=" + encode(reservationNo));
  }

  private void setRecentReservations(HttpServletRequest request, ReservationDAO reservationDAO) {
    request.setAttribute("recentReservations", reservationDAO.findRecentReservationsWithRoomNo(20));
  }

  private void setFieldError(
      HttpServletRequest request, String field, String message, String reservationNo) {
    Map<String, String> errors = new LinkedHashMap<>();
    errors.put(field, message);
    Map<String, String> oldValues = new LinkedHashMap<>();
    oldValues.put("reservationNo", reservationNo);
    request.setAttribute("errors", errors);
    request.setAttribute("oldValues", oldValues);
  }

  private void setGlobalError(HttpServletRequest request, String message, String reservationNo) {
    Map<String, String> errors = new LinkedHashMap<>();
    errors.put("global", message);
    Map<String, String> oldValues = new LinkedHashMap<>();
    oldValues.put("reservationNo", reservationNo);
    request.setAttribute("errors", errors);
    request.setAttribute("oldValues", oldValues);
  }

  private String resolveView(String role) {
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      return "/WEB-INF/views/bill.jsp";
    }
    return "/WEB-INF/views/staff-bill.jsp";
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
