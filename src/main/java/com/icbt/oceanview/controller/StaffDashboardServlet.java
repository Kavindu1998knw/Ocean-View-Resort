package com.icbt.oceanview.controller;

import com.icbt.oceanview.model.User;
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
    Object userObj = session == null ? null : session.getAttribute("authUser");
    if (!(userObj instanceof User)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    User user = (User) userObj;
    String role = user.getRole();
    if (role == null || !"STAFF".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/admin/dashboard");
      return;
    }

    request.getRequestDispatcher("/WEB-INF/views/staff-dashboard.jsp").forward(request, response);
  }
}
