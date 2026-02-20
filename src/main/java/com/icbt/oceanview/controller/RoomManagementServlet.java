package com.icbt.oceanview.controller;

import com.icbt.oceanview.dao.RoomManagementDAO;
import com.icbt.oceanview.model.RoomInfo;
import com.icbt.oceanview.model.User;
import com.icbt.oceanview.util.RoomTypes;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/admin/rooms", "/admin/api/room-price", "/admin/api/rooms"})
public class RoomManagementServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String servletPath = request.getServletPath();
    if ("/admin/api/room-price".equals(servletPath)) {
      handlePriceApi(request, response);
      return;
    }
    if ("/admin/api/rooms".equals(servletPath)) {
      handleRoomsApi(request, response);
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();

    String toggleIdParam = trimParam(request, "toggleId");
    if (!toggleIdParam.isEmpty()) {
      int toggleId = parseId(toggleIdParam);
      if (toggleId <= 0) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Invalid room ID."));
        return;
      }
      boolean toggled = dao.toggleActive(toggleId);
      if (toggled) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room status updated."));
      } else {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Failed to update room status."));
      }
      return;
    }

    String deleteIdParam = trimParam(request, "deleteId");
    if (!deleteIdParam.isEmpty()) {
      int deleteId = parseId(deleteIdParam);
      if (deleteId <= 0) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Invalid room ID."));
        return;
      }
      boolean deleted = dao.deleteById(deleteId);
      if (deleted) {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room deleted successfully."));
      } else {
        response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode("Failed to delete room."));
      }
      return;
    }

    String editIdParam = trimParam(request, "editId");
    if (!editIdParam.isEmpty()) {
      int editId = parseId(editIdParam);
      if (editId > 0) {
        RoomInfo editRoom = dao.findById(editId);
        if (editRoom != null) {
          request.setAttribute("editRoom", editRoom);
        } else {
          request.setAttribute("error", "Room not found.");
        }
      } else {
        request.setAttribute("error", "Invalid room ID.");
      }
    }

    List<RoomInfo> rooms = dao.findAllRoomsWithPrices();
    request.setAttribute("rooms", rooms);
    request.setAttribute("roomTypes", RoomTypes.ROOM_TYPES);
    request.getRequestDispatcher("/WEB-INF/views/manage-rooms.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    if (!isAdmin(request)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String servletPath = request.getServletPath();
    if (!"/admin/rooms".equals(servletPath)) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return;
    }

    String idParam = trimParam(request, "id");
    String roomNo = trimParam(request, "roomNo");
    String roomType = trimParam(request, "roomType");
    String priceRaw = trimParam(request, "pricePerNight");
    boolean active = request.getParameter("active") != null;

    if (roomNo.isEmpty() || roomType.isEmpty() || priceRaw.isEmpty()) {
      forwardWithError(
          request, response, "Please fill all required fields.", idParam, roomNo, roomType, priceRaw, active);
      return;
    }

    if (!RoomTypes.ROOM_TYPES.contains(roomType)) {
      forwardWithError(
          request, response, "Invalid room type selected.", idParam, roomNo, roomType, priceRaw, active);
      return;
    }

    BigDecimal price;
    try {
      price = new BigDecimal(priceRaw);
    } catch (NumberFormatException e) {
      forwardWithError(
          request, response, "Price must be a valid numeric value.", idParam, roomNo, roomType, priceRaw, active);
      return;
    }

    if (price.compareTo(BigDecimal.ZERO) <= 0) {
      forwardWithError(
          request, response, "Price must be greater than 0.", idParam, roomNo, roomType, priceRaw, active);
      return;
    }

    RoomManagementDAO dao = new RoomManagementDAO();
    boolean saved;

    if (idParam.isEmpty()) {
      if (dao.roomNoExists(roomNo)) {
        forwardWithError(
            request, response, "Room number already exists.", "", roomNo, roomType, priceRaw, active);
        return;
      }
      RoomInfo info = new RoomInfo(roomNo, roomType, active, price);
      saved = dao.createRoomWithPrice(info);
    } else {
      int id = parseId(idParam);
      if (id <= 0) {
        forwardWithError(
            request, response, "Invalid room ID.", idParam, roomNo, roomType, priceRaw, active);
        return;
      }
      if (dao.roomNoExistsExcludingId(id, roomNo)) {
        forwardWithError(
            request, response, "Room number already exists.", idParam, roomNo, roomType, priceRaw, active);
        return;
      }
      RoomInfo info = new RoomInfo(id, roomNo, roomType, active, price, null, null);
      saved = dao.updateRoomWithPrice(info);
    }

    if (saved) {
      response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode("Room saved successfully."));
    } else {
      forwardWithError(
          request, response, "Failed to save room. Please try again.", idParam, roomNo, roomType, priceRaw, active);
    }
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
      String price = room.getPricePerNight() == null
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

  private void forwardWithError(
      HttpServletRequest request,
      HttpServletResponse response,
      String message,
      String idParam,
      String roomNo,
      String roomType,
      String priceRaw,
      boolean active)
      throws ServletException, IOException {
    request.setAttribute("error", message);

    if (!idParam.isEmpty()) {
      int id = parseId(idParam);
      RoomInfo editRoom = new RoomInfo();
      editRoom.setId(id);
      editRoom.setRoomNo(roomNo);
      editRoom.setRoomType(roomType);
      editRoom.setActive(active);
      try {
        editRoom.setPricePerNight(new BigDecimal(priceRaw));
      } catch (NumberFormatException e) {
        editRoom.setPricePerNight(null);
      }
      request.setAttribute("editRoom", editRoom);
    }

    request.setAttribute("roomNo", roomNo);
    request.setAttribute("roomType", roomType);
    request.setAttribute("pricePerNight", priceRaw);
    request.setAttribute("active", active);
    request.setAttribute("rooms", new RoomManagementDAO().findAllRoomsWithPrices());
    request.setAttribute("roomTypes", RoomTypes.ROOM_TYPES);
    request.getRequestDispatcher("/WEB-INF/views/manage-rooms.jsp").forward(request, response);
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

  private int parseId(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private String encode(String value) {
    if (value == null) {
      return "";
    }
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (java.io.UnsupportedEncodingException e) {
      return "";
    }
  }

  private String escapeJson(String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}
