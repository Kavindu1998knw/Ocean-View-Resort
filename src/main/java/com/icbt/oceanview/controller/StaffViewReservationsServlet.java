package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.Reservation;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/reservations")
public class StaffViewReservationsServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    Object roleObj = session.getAttribute("authRole");
    if (roleObj == null || !"STAFF".equalsIgnoreCase(String.valueOf(roleObj))) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    List<Reservation> reservations = reservationDAO.findAllWithRoomNo();
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    if (success != null && !success.trim().isEmpty()) {
      request.setAttribute("success", success);
    }
    if (error != null && !error.trim().isEmpty()) {
      request.setAttribute("error", error);
    }
    request.setAttribute("reservations", reservations);
    request.getRequestDispatcher("/WEB-INF/views/staff-view-reservations.jsp").forward(request, response);
  }
}
