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
@WebServlet("/reservations")
public class ViewReservationsServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
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
    if (AuthHelper.ROLE_ADMIN.equals(role)) {
      request.getRequestDispatcher("/WEB-INF/views/view-reservations.jsp").forward(request, response);
      return;
    }
    request.getRequestDispatcher("/WEB-INF/views/staff-view-reservations.jsp")
        .forward(request, response);
  }
}
