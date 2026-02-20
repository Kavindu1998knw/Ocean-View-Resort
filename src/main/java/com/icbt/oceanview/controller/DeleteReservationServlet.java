package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import com.icbt.oceanview.model.User;
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
      response.sendRedirect(request.getContextPath() + "/login");
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
    if (session == null) {
      return false;
    }
    Object userObj = session.getAttribute("authUser");
    if (!(userObj instanceof User)) {
      return false;
    }
    User user = (User) userObj;
    String role = user.getRole();
    return role != null && "ADMIN".equalsIgnoreCase(role);
  }
}
