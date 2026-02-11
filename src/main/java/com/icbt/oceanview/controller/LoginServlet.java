package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
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
    User user = userDAO.findByEmail(email);
    if (user == null) {
      request.setAttribute("error", "Invalid email or password.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    if (!user.isActive()) {
      request.setAttribute("error", "Your account is inactive. Please contact support.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    String inputHash;
    try {
      inputHash = hashPassword(password);
    } catch (SQLException e) {
      request.setAttribute("error", "Login failed. Please try again.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    if (!inputHash.equals(user.getPassword())) {
      request.setAttribute("error", "Invalid email or password.");
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("loggedUser", user.getName());
    session.setAttribute("role", user.getRole());
    session.setAttribute("userId", user.getId());

    String role = user.getRole() == null ? "" : user.getRole();
    if ("ADMIN".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/admin-dashboard.jsp");
    } else {
      response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
  }

  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private String hashPassword(String password) throws SQLException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new SQLException("Unable to hash password.", e);
    }
  }
}
