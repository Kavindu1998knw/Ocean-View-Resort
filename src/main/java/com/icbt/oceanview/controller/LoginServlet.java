package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String errorParam = request.getParameter("error");
    if (errorParam != null && !errorParam.trim().isEmpty()) {
      request.setAttribute("error", errorParam);
    }
    request.getRequestDispatcher("login.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");

    String email = trimParam(request, "email");
    String password = request.getParameter("password");

    if (isBlank(email) || isBlank(password)) {
      request.setAttribute("error", "Email and password are required.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    UserDAO userDAO = new UserDAO();
    User user = userDAO.authenticate(email, password);
    if (user == null) {
      request.setAttribute("error", "Invalid email or password.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("user", user);

    String role = user.getRole() == null ? "" : user.getRole().trim();
    if ("ADMIN".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/admin/dashboard");
      return;
    }
    if ("STAFF".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/staff/dashboard");
      return;
    }

    response.sendRedirect(request.getContextPath() + "/login?error=Invalid+role");
  }

  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
