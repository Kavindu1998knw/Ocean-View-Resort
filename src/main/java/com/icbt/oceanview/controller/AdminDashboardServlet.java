package com.icbt.oceanview.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    Object role = session == null ? null : session.getAttribute("role");

    if (role == null || !"ADMIN".equalsIgnoreCase(String.valueOf(role))) {
      response.sendRedirect(request.getContextPath() + "/login.jsp");
      return;
    }

    request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
  }
}
