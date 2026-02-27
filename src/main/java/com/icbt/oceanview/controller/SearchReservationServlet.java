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

@WebServlet("/reservations/search")
public class SearchReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return;
    }

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      request.getRequestDispatcher(resolveView(role)).forward(request, response);
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findByReservationNo(reservationNo.trim());
    if (reservation != null) {
      request.setAttribute("reservation", reservation);
    } else {
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

    String reservationNo = request.getParameter("reservationNo");
    if (reservationNo == null || reservationNo.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/reservations/search");
      return;
    }

    String encoded = URLEncoder.encode(reservationNo.trim(), StandardCharsets.UTF_8.name());
    response.sendRedirect(
        request.getContextPath() + "/reservations/search?reservationNo=" + encoded);
  }

  private String resolveView(String role) {
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      return "/WEB-INF/views/search-reservation.jsp";
    }
    return "/WEB-INF/views/staff-search-reservation.jsp";
  }
}
