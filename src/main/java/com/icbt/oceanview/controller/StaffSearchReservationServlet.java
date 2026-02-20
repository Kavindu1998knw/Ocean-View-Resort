package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/search-reservation")
public class StaffSearchReservationServlet extends HttpServlet {

  private boolean isAllowedRole(Object roleObj) {
    if (roleObj == null) {
      return false;
    }
    String role = String.valueOf(roleObj);
    return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    Object roleObj = session.getAttribute("authRole");
    if (!isAllowedRole(roleObj)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      request.getRequestDispatcher("/WEB-INF/views/staff-search-reservation.jsp").forward(request, response);
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findByReservationNo(reservationNo.trim());
    if (reservation != null) {
      request.setAttribute("reservation", reservation);
    } else {
      request.setAttribute("notFound", true);
    }

    request.getRequestDispatcher("/WEB-INF/views/staff-search-reservation.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || !isAllowedRole(session.getAttribute("authRole"))) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/staff/search-reservation");
      return;
    }

    String encoded = URLEncoder.encode(reservationNo.trim(), StandardCharsets.UTF_8.name());
    response.sendRedirect(
        request.getContextPath() + "/staff/search-reservation?reservationNo=" + encoded);
  }
}
