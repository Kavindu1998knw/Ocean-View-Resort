package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");

    String fullName = trimParam(request, "fullName");
    String username = trimParam(request, "username");
    String email = trimParam(request, "email");
    String contact = trimParam(request, "contact");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String role = trimParam(request, "role");

    Map<String, String> fieldErrors = new HashMap<>();
    List<String> errors = new ArrayList<>();

    if (isBlank(fullName)) {
      fieldErrors.put("fullName", "Full name is required.");
    }
    if (isBlank(username)) {
      fieldErrors.put("username", "Username is required.");
    }
    if (isBlank(email)) {
      fieldErrors.put("email", "Email is required.");
    }
    if (isBlank(contact)) {
      fieldErrors.put("contact", "Contact number is required.");
    }
    if (isBlank(password)) {
      fieldErrors.put("password", "Password is required.");
    }
    if (isBlank(confirmPassword)) {
      fieldErrors.put("confirmPassword", "Confirm password is required.");
    }
    if (isBlank(role)) {
      fieldErrors.put("role", "Role is required.");
    }

    if (!isBlank(email) && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      fieldErrors.put("email", "Enter a valid email address.");
    }

    if (!isBlank(contact) && !contact.matches("^\\d{9,12}$")) {
      fieldErrors.put("contact", "Contact number must be 9 to 12 digits.");
    }

    if (!isBlank(password) && password.length() < 8) {
      fieldErrors.put("password", "Password must be at least 8 characters.");
    }

    if (!isBlank(password) && !isBlank(confirmPassword) && !password.equals(confirmPassword)) {
      fieldErrors.put("confirmPassword", "Passwords do not match.");
    }

    if (!fieldErrors.isEmpty()) {
      errors.add("Please fix the highlighted fields and try again.");
      forwardWithErrors(
          request,
          response,
          errors,
          fieldErrors,
          fullName,
          username,
          email,
          contact,
          role);
      return;
    }

    UserDAO userDAO = new UserDAO();
    if (userDAO.emailExists(email)) {
      fieldErrors.put("email", "An account with this email already exists.");
      errors.add("This email is already registered. Please use a different email.");
      forwardWithErrors(
          request,
          response,
          errors,
          fieldErrors,
          fullName,
          username,
          email,
          contact,
          role);
      return;
    }

    String hashedPassword;
    try {
      hashedPassword = hashPassword(password);
    } catch (SQLException e) {
      forwardWithErrors(
          request,
          response,
          Collections.singletonList("Registration failed. Please try again."),
          new HashMap<>(),
          fullName,
          username,
          email,
          contact,
          role);
      return;
    }

    User user = new User(fullName, email, hashedPassword, role, true, LocalDateTime.now());
    boolean created = userDAO.insert(user);

    if (created) {
      response.sendRedirect("login.jsp?registered=1");
      return;
    }

    forwardWithErrors(
        request,
        response,
        Collections.singletonList("Registration failed. Please try again."),
        new HashMap<>(),
        fullName,
        username,
        email,
        contact,
        role);
  }

  private String trimParam(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    return value == null ? "" : value.trim();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private void forwardWithErrors(
      HttpServletRequest request,
      HttpServletResponse response,
      List<String> messages,
      Map<String, String> fieldErrors,
      String fullName,
      String username,
      String email,
      String contact,
      String role)
      throws ServletException, IOException {
    request.setAttribute("errors", messages);
    request.setAttribute("fieldErrors", fieldErrors);
    request.setAttribute("fullName", fullName);
    request.setAttribute("username", username);
    request.setAttribute("email", email);
    request.setAttribute("contact", contact);
    request.setAttribute("role", role);
    request.getRequestDispatcher("register.jsp").forward(request, response);
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
