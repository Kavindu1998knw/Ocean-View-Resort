package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/reservation/delete")
public class DeleteReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/admin/reservations");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(request.getContextPath() + "/admin/reservations");
      return;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    reservationDAO.deleteById(id);
    response.sendRedirect(request.getContextPath() + "/admin/reservations");
  }

  private boolean isAdmin(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    Object role = session == null ? null : session.getAttribute("role");
    return role != null && "ADMIN".equalsIgnoreCase(String.valueOf(role));
  }
}
