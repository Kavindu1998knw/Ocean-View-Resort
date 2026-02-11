package com.icbt.oceanview.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/reservation/new")
public class AddReservationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("HIT: /admin/reservation/new");

    HttpSession session = request.getSession(false);
    Object role = session == null ? null : session.getAttribute("role");

    if (role == null || !"ADMIN".equalsIgnoreCase(String.valueOf(role))) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
    }

    request.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(request, response);
  }
}
