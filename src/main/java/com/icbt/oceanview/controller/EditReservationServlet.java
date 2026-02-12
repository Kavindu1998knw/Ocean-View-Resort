package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import com.icbt.oceanview.util.RoomTypes;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/reservation/edit")
public class EditReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(
          request.getContextPath() + "/admin/reservations?error=Invalid+reservation+ID.");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(
          request.getContextPath() + "/admin/reservations?error=Invalid+reservation+ID.");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    Reservation reservation = reservationDAO.findById(id);
    if (reservation == null) {
      response.sendRedirect(
          request.getContextPath() + "/admin/reservations?error=Reservation+not+found.");
      return;
    }

    request.setAttribute("roomTypes", RoomTypes.ROOM_TYPES);
    request.setAttribute("reservation", reservation);
    request.getRequestDispatcher("/WEB-INF/views/edit-reservation.jsp").forward(request, response);
  }

  private boolean isAdmin(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    Object role = session == null ? null : session.getAttribute("role");
    return role != null && "ADMIN".equalsIgnoreCase(String.valueOf(role));
  }
}
