package com.icbt.oceanview.controller;

import com.icbt.oceanview.model.User;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class AuthHelper {
  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_STAFF = "STAFF";

  private AuthHelper() {}

  public static String requireRole(
      HttpServletRequest request,
      HttpServletResponse response,
      boolean allowAdmin,
      boolean allowStaff)
      throws IOException {
    HttpSession session = request.getSession(false);
    if (session == null) {
      redirectToLogin(request, response);
      return null;
    }

    String role = resolveRole(session);
    if (role == null) {
      redirectToLogin(request, response);
      return null;
    }

    String normalized = role.trim().toUpperCase(Locale.ROOT);
    if (!ROLE_ADMIN.equals(normalized) && !ROLE_STAFF.equals(normalized)) {
      redirectToLogin(request, response);
      return null;
    }

    if ((ROLE_ADMIN.equals(normalized) && !allowAdmin)
        || (ROLE_STAFF.equals(normalized) && !allowStaff)) {
      redirectToLogin(request, response);
      return null;
    }

    return normalized;
  }

  public static String resolveDisplayName(HttpSession session) {
    if (session == null) {
      return "";
    }
    Object nameObj = session.getAttribute("authName");
    if (nameObj != null) {
      return String.valueOf(nameObj);
    }
    Object userObj = session.getAttribute("authUser");
    if (userObj instanceof User) {
      String name = ((User) userObj).getName();
      return name == null ? "" : name;
    }
    return "";
  }

  private static String resolveRole(HttpSession session) {
    Object roleObj = session.getAttribute("authRole");
    if (roleObj != null) {
      String role = String.valueOf(roleObj);
      if (!role.trim().isEmpty()) {
        return role;
      }
    }
    Object userObj = session.getAttribute("authUser");
    if (userObj instanceof User) {
      return ((User) userObj).getRole();
    }
    return null;
  }

  private static void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect(request.getContextPath() + "/login");
  }
}
