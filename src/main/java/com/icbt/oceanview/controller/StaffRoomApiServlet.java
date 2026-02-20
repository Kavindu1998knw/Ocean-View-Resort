package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.RoomInfo;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/staff/api/room-price", "/staff/api/rooms"})
public class StaffRoomApiServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!isStaff(request)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    String servletPath = request.getServletPath();
    if ("/staff/api/room-price".equals(servletPath)) {
      handlePriceApi(request, response);
      return;
    }
    if ("/staff/api/rooms".equals(servletPath)) {
      handleRoomsApi(request, response);
      return;
    }

    response.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  private void handlePriceApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");

    String roomType = request.getParameter("roomType");
    if (roomType == null || roomType.trim().isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\":\"ROOM_TYPE_REQUIRED\"}");
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    BigDecimal price = dao.findPriceByRoomType(roomType.trim());
    if (price == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("{\"error\":\"NOT_FOUND\"}");
      return;
    }

    String normalizedPrice = price.setScale(2, RoundingMode.HALF_UP).toPlainString();
    response
        .getWriter()
        .write(
            "{\"roomType\":\""
                + escapeJson(roomType.trim())
                + "\",\"pricePerNight\":"
                + normalizedPrice
                + "}");
  }

  private void handleRoomsApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");

    String roomType = request.getParameter("roomType");
    if (roomType == null || roomType.trim().isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\":\"ROOM_TYPE_REQUIRED\"}");
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    List<RoomInfo> rooms = dao.findActiveRoomsByType(roomType.trim());
    if (rooms.isEmpty()) {
      response.getWriter().write("[]");
      return;
    }

    StringBuilder json = new StringBuilder();
    json.append('[');
    for (int i = 0; i < rooms.size(); i++) {
      RoomInfo room = rooms.get(i);
      if (i > 0) {
        json.append(',');
      }
      String price =
          room.getPricePerNight() == null
              ? "0.00"
              : room.getPricePerNight().setScale(2, RoundingMode.HALF_UP).toPlainString();
      json.append("{\"id\":")
          .append(room.getId())
          .append(",\"roomNo\":\"")
          .append(escapeJson(room.getRoomNo()))
          .append("\",\"pricePerNight\":")
          .append(price)
          .append('}');
    }
    json.append(']');
    response.getWriter().write(json.toString());
  }

  private boolean isStaff(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    Object roleObj = session.getAttribute("authRole");
    if (roleObj == null) {
      return false;
    }
    String role = String.valueOf(roleObj);
    return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
  }

  private String escapeJson(String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
