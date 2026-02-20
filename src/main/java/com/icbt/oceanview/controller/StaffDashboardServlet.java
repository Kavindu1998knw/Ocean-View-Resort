package com.icbt.oceanview.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("role") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String role = String.valueOf(session.getAttribute("role"));
    if (!"STAFF".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/admin/dashboard");
      return;
    }

    request.setAttribute("loggedUserName", session.getAttribute("loggedUserName"));
    request.getRequestDispatcher("/WEB-INF/views/staff-dashboard.jsp").forward(request, response);
  }
}
