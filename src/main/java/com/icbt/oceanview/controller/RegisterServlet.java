package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
  private static final String DEFAULT_ROLE = "STAFF";

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");

    ValidationResult vr = new ValidationResult();
    RequestUtil.collectOldValues(request, vr, "fullName", "username", "email", "contact");

    String fullName = ValidationUtil.trimToNull(request.getParameter("fullName"));
    String username = ValidationUtil.trimToNull(request.getParameter("username"));
    String email = ValidationUtil.trimToNull(request.getParameter("email"));
    String contact = ValidationUtil.trimToNull(request.getParameter("contact"));
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");

    if (ValidationUtil.isBlank(fullName)) {
      vr.addError("fullName", "Full name is required.");
    } else if (fullName.length() < 2 || fullName.length() > 100) {
      vr.addError("fullName", "Full name must be between 2 and 100 characters.");
    }

    if (ValidationUtil.isBlank(username)) {
      vr.addError("username", "Username is required.");
    } else if (!ValidationUtil.isValidUsername(username)) {
      vr.addError("username", "Username must be 3-20 characters (letters, digits, underscore).");
    }

    if (ValidationUtil.isBlank(email)) {
      vr.addError("email", "Email is required.");
    } else if (!ValidationUtil.isValidEmail(email)) {
      vr.addError("email", "Enter a valid email address.");
    }

    if (ValidationUtil.isBlank(contact)) {
      vr.addError("contact", "Contact number is required.");
    } else if (!ValidationUtil.isValidSriLankaMobile(contact)
        && !ValidationUtil.isValidPhone10(contact.replaceAll("[^0-9]", ""))) {
      vr.addError("contact", "Enter a valid contact number.");
    }

    if (ValidationUtil.isBlank(password)) {
      vr.addError("password", "Password is required.");
    } else if (!ValidationUtil.isStrongPassword(password)) {
      vr.addError("password", "Password must be at least 8 characters.");
    }

    if (ValidationUtil.isBlank(confirmPassword)) {
      vr.addError("confirmPassword", "Confirm password is required.");
    } else if (password != null && !password.equals(confirmPassword)) {
      vr.addError("confirmPassword", "Passwords do not match.");
    }

    UserDAO userDAO = new UserDAO();
    if (vr.error("email") == null && email != null && userDAO.emailExists(email)) {
      vr.addError("email", "This email is already registered.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields and try again.");
      RequestUtil.forwardWithErrors(request, response, "/register.jsp", vr);
      return;
    }

    String hashedPassword;
    try {
      hashedPassword = hashPassword(password);
    } catch (SQLException e) {
      vr.addError("global", "Registration failed. Please try again.");
      RequestUtil.forwardWithErrors(request, response, "/register.jsp", vr);
      return;
    }

    User user =
        User.newForRegistration(fullName, email, hashedPassword, DEFAULT_ROLE, true, LocalDateTime.now());
    boolean created = userDAO.insert(user);

    if (created) {
      response.sendRedirect(request.getContextPath() + "/login?registered=1");
      return;
    }

    vr.addError("global", "Registration failed. Please try again.");
    RequestUtil.forwardWithErrors(request, response, "/register.jsp", vr);
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
