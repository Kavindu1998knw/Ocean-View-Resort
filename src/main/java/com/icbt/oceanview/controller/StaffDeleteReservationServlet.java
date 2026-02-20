package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/reservation/delete")
public class StaffDeleteReservationServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!isStaff(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/staff/reservations");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(request.getContextPath() + "/staff/reservations");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    reservationDAO.deleteById(id);
    response.sendRedirect(request.getContextPath() + "/staff/reservations");
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
}
