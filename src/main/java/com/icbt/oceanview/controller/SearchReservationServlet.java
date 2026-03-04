package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservations/search")
public class SearchReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    String reservationNo = ValidationUtil.trimToNull(request.getParameter("reservationNo"));
    if (reservationNo == null) {
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findByReservationNo(reservationNo);

    Map<String, String> oldValues = new LinkedHashMap<>();
    oldValues.put("reservationNo", reservationNo);
    request.setAttribute("oldValues", oldValues);

    if (reservation != null) {
      request.setAttribute("reservation", reservation);
    } else {
      Map<String, String> errors = new LinkedHashMap<>();
      errors.put("reservationNo", "Reservation not found.");
      request.setAttribute("errors", errors);
      request.setAttribute("notFound", true);
    }

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
    if (vr.error("reservationNo") == null && reservationDAO.findByReservationNo(reservationNo) == null) {
      vr.addError("reservationNo", "Reservation not found.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      request.setAttribute("notFound", "Reservation not found.".equals(vr.error("reservationNo")));
      RequestUtil.forwardWithErrors(request, response, resolveView(role), vr);
      return;
    }

    String encoded = URLEncoder.encode(reservationNo, StandardCharsets.UTF_8.name());
    response.sendRedirect(request.getContextPath() + "/reservations/search?reservationNo=" + encoded);
  }

  private String resolveView(String role) {
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      return "/WEB-INF/views/search-reservation.jsp";
    }
    return "/WEB-INF/views/staff-search-reservation.jsp";
  }
}
