package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

    String toggleId = ValidationUtil.trimToNull(request.getParameter("toggleId"));
    if (toggleId != null) {
      handleToggle(toggleId, userDAO, request, response);
      return;
    }

    String deleteId = ValidationUtil.trimToNull(request.getParameter("deleteId"));
    if (deleteId != null) {
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

    ValidationResult vr = new ValidationResult();
    vr.getOldValues().put("name", request.getParameter("name") == null ? "" : request.getParameter("name"));
    vr.getOldValues().put("email", request.getParameter("email") == null ? "" : request.getParameter("email"));
    vr.getOldValues().put("role", request.getParameter("role") == null ? "" : request.getParameter("role"));
    vr.getOldValues().put("active", String.valueOf(request.getParameter("active") != null));

    String name = ValidationUtil.trimToNull(request.getParameter("name"));
    String email = ValidationUtil.trimToNull(request.getParameter("email"));
    String password = request.getParameter("password");
    String role = ValidationUtil.trimToNull(request.getParameter("role"));
    boolean active = request.getParameter("active") != null;

    if (ValidationUtil.isBlank(name)) {
      vr.addError("name", "Full name is required.");
    } else if (name.length() < 2 || name.length() > 100) {
      vr.addError("name", "Full name must be between 2 and 100 characters.");
    }

    if (ValidationUtil.isBlank(email)) {
      vr.addError("email", "Email is required.");
    } else if (!ValidationUtil.isValidEmail(email)) {
      vr.addError("email", "Please enter a valid email address.");
    }

    if (ValidationUtil.isBlank(password)) {
      vr.addError("password", "Password is required.");
    } else if (!ValidationUtil.isStrongPassword(password)) {
      vr.addError("password", "Password must be at least 8 characters.");
    }

    if (role == null) {
      vr.addError("role", "Role is required.");
    } else {
      role = role.toUpperCase(Locale.ROOT);
      vr.getOldValues().put("role", role);
      Set<String> allowedRoles = new LinkedHashSet<String>() {
        {
          add("ADMIN");
          add("STAFF");
        }
      };
      if (!allowedRoles.contains(role)) {
        vr.addError("role", "Invalid role selected.");
      }
    }

    if (vr.error("email") == null && email != null && userDAO.emailExists(email)) {
      vr.addError("email", "This email is already registered.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      request.setAttribute("errors", vr.getErrors());
      request.setAttribute("oldValues", vr.getOldValues());
      loadAndForwardList(request, response, userDAO);
      return;
    }

    String hashedPassword;
    try {
      hashedPassword = hashPassword(password);
    } catch (SQLException e) {
      vr.addError("global", "Failed to create user.");
      request.setAttribute("errors", vr.getErrors());
      request.setAttribute("oldValues", vr.getOldValues());
      loadAndForwardList(request, response, userDAO);
      return;
    }

    User user = User.newForRegistration(name, email, hashedPassword, role, active, LocalDateTime.now());
    boolean inserted = userDAO.insert(user);
    if (inserted) {
      response.sendRedirect(request.getContextPath() + "/admin/staff-users?success=" + encode("User created"));
      return;
    }

    vr.addError("global", "Failed to create user.");
    request.setAttribute("errors", vr.getErrors());
    request.setAttribute("oldValues", vr.getOldValues());
    loadAndForwardList(request, response, userDAO);
  }

  private void handleToggle(
      String toggleId, UserDAO userDAO, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Integer id = ValidationUtil.parseIntSafe(toggleId);
    if (!ValidationUtil.isPositiveInt(id)) {
      response.sendRedirect(request.getContextPath() + "/admin/staff-users?error=" + encode("Invalid user id"));
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
    Integer id = ValidationUtil.parseIntSafe(deleteId);
    if (!ValidationUtil.isPositiveInt(id)) {
      response.sendRedirect(request.getContextPath() + "/admin/staff-users?error=" + encode("Invalid user id"));
      return;
    }

    boolean deleted = userDAO.deleteById(id);
    if (deleted) {
      response.sendRedirect(request.getContextPath() + "/admin/staff-users?success=" + encode("User deleted"));
    } else {
      response.sendRedirect(request.getContextPath() + "/admin/staff-users?error=" + encode("Failed to delete user"));
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
    Object userObj = session.getAttribute("authUser");
    if (!(userObj instanceof User)) {
      return false;
    }
    User user = (User) userObj;
    String role = user.getRole();
    return role != null && "ADMIN".equalsIgnoreCase(role);
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
