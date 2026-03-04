package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.UserDAO;
import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.validation.RequestUtil;
import com.icbt.oceanview.util.validation.ValidationResult;
import com.icbt.oceanview.util.validation.ValidationUtil;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
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
    String errorParam = ValidationUtil.trimToNull(request.getParameter("error"));
    if (errorParam != null) {
      Map<String, String> errors = new LinkedHashMap<>();
      errors.put("global", errorParam);
      request.setAttribute("errors", errors);
    }
    request.getRequestDispatcher("login.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");

    ValidationResult vr = new ValidationResult();
    RequestUtil.collectOldValues(request, vr, "email");

    String email = ValidationUtil.trimToNull(request.getParameter("email"));
    String password = request.getParameter("password");

    if (ValidationUtil.isBlank(email)) {
      vr.addError("email", "Email is required.");
    } else if (!ValidationUtil.isValidEmail(email)) {
      vr.addError("email", "Enter a valid email address.");
    }

    if (ValidationUtil.isBlank(password)) {
      vr.addError("password", "Password is required.");
    }

    if (vr.hasErrors()) {
      vr.addError("global", "Please fix the highlighted fields.");
      RequestUtil.forwardWithErrors(request, response, "/login.jsp", vr);
      return;
    }

    UserDAO userDAO = new UserDAO();
    User user = userDAO.authenticate(email, password);
    if (user == null) {
      vr.addError("global", "Invalid email or password.");
      RequestUtil.forwardWithErrors(request, response, "/login.jsp", vr);
      return;
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("authUser", user);
    session.setAttribute("authName", user.getName());
    session.setAttribute("authRole", user.getRole());

    String role = user.getRole() == null ? "" : user.getRole().trim();
    if ("ADMIN".equalsIgnoreCase(role) || "STAFF".equalsIgnoreCase(role)) {
      response.sendRedirect(request.getContextPath() + "/dashboard");
      return;
    }

    response.sendRedirect(request.getContextPath() + "/login?error=Invalid+role");
  }
}
