package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.ReservationDAO;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservations/delete")
public class DeleteReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!handleDelete(request, response)) {
      return;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!handleDelete(request, response)) {
      return;
    }
  }

  private boolean handleDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String role = AuthHelper.requireRole(request, response, true, true);
    if (role == null) {
      return false;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
      response.sendRedirect(request.getContextPath() + "/reservations");
      return false;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      response.sendRedirect(request.getContextPath() + "/reservations");
      return false;
    }

    ReservationDAO reservationDAO = new ReservationDAO();
    reservationDAO.deleteById(id);
    response.sendRedirect(request.getContextPath() + "/reservations");
    return true;
  }
}
