package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/staff-users")
public class ManageStaffUsersServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    UserDAO userDAO = new UserDAO();

    String toggleId = trimParam(request, "toggleId");
    if (!toggleId.isEmpty()) {
      handleToggle(toggleId, userDAO, request, response);
      return;
    }

    String deleteId = trimParam(request, "deleteId");
    if (!deleteId.isEmpty()) {
      handleDelete(deleteId, userDAO, request, response);
      return;
    }

    loadAndForwardList(request, response, userDAO);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    UserDAO userDAO = new UserDAO();

    String name = trimParam(request, "name");
    String email = trimParam(request, "email");
    String password = request.getParameter("password");
    String role = trimParam(request, "role").toUpperCase(Locale.ROOT);
    boolean active = request.getParameter("active") != null;

    request.setAttribute("formName", name);
    request.setAttribute("formEmail", email);
    request.setAttribute("formRole", role);
    request.setAttribute("formActive", active);

    if (name.isEmpty() || email.isEmpty() || password == null || password.trim().isEmpty()) {
      request.setAttribute("error", "Name, email, and password are required.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      request.setAttribute("error", "Please enter a valid email address.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    if (password.length() < 8) {
      request.setAttribute("error", "Password must be at least 8 characters.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
      request.setAttribute("error", "Invalid role selected.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    if (userDAO.emailExists(email)) {
      request.setAttribute("error", "Email already exists.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    String hashedPassword;
    try {
      hashedPassword = hashPassword(password);
    } catch (SQLException e) {
      request.setAttribute("error", "Failed to create user.");
      loadAndForwardList(request, response, userDAO);
      return;
    }

    User user = User.newForRegistration(name, email, hashedPassword, role, active, LocalDateTime.now());
    boolean inserted = userDAO.insert(user);
    if (inserted) {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?success=" + encode("User created"));
      return;
    }

    request.setAttribute("error", "Failed to create user.");
    loadAndForwardList(request, response, userDAO);
  }

  private void handleToggle(
      String toggleId, UserDAO userDAO, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    int id;
    try {
      id = Integer.parseInt(toggleId);
    } catch (NumberFormatException e) {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?error=" + encode("Invalid user id"));
      return;
    }

    boolean updated = userDAO.toggleActive(id);
    if (updated) {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?success=" + encode("User status updated"));
    } else {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?error=" + encode("Failed to update user"));
    }
  }

  private void handleDelete(
      String deleteId, UserDAO userDAO, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    int id;
    try {
      id = Integer.parseInt(deleteId);
    } catch (NumberFormatException e) {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?error=" + encode("Invalid user id"));
      return;
    }

    boolean deleted = userDAO.deleteById(id);
    if (deleted) {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?success=" + encode("User deleted"));
    } else {
      response.sendRedirect(
          request.getContextPath() + "/admin/staff-users?error=" + encode("Failed to delete user"));
    }
  }

  private void loadAndForwardList(
      HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
      throws ServletException, IOException {
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    if (success != null && !success.trim().isEmpty() && request.getAttribute("success") == null) {
      request.setAttribute("success", success);
    }
    if (error != null && !error.trim().isEmpty() && request.getAttribute("error") == null) {
      request.setAttribute("error", error);
    }
    List<User> users = userDAO.findAllStaffUsers();
    request.setAttribute("users", users);
    request.getRequestDispatcher("/WEB-INF/views/manage-staff-users.jsp").forward(request, response);
  }

  private boolean isAdmin(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    Object userObj = session.getAttribute("user");
    if (!(userObj instanceof User)) {
      return false;
    }
    User user = (User) userObj;
    String role = user.getRole();
    return role != null && "ADMIN".equalsIgnoreCase(role);
  }


  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private String encode(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (java.io.UnsupportedEncodingException e) {
      return "";
    }
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
