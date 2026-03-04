package com.icbt.oceanview.util.validation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RequestUtil {
  private RequestUtil() {}

  public static void collectOldValues(HttpServletRequest req, ValidationResult vr, String... fields) {
    if (req == null || vr == null || fields == null) {
      return;
    }

    for (String field : fields) {
      if (field == null || field.trim().isEmpty()) {
        continue;
      }
      String value = req.getParameter(field);
      if (value != null) {
        vr.getOldValues().put(field, value);
      }
    }
  }

  public static void forwardWithErrors(
      HttpServletRequest req, HttpServletResponse resp, String jspPath, ValidationResult vr)
      throws ServletException, IOException {
    req.setAttribute("errors", vr.getErrors());
    req.setAttribute("oldValues", vr.getOldValues());
    req.getRequestDispatcher(jspPath).forward(req, resp);
  }
}
